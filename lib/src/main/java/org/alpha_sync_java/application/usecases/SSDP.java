package org.alpha_sync_java.application.usecases;


import org.alpha_sync_java.application.interfaces.Connector;
import org.alpha_sync_java.domain.communication.LocatorURL;
import org.alpha_sync_java.domain.context.ServerContext;

import org.alpha_sync_java.infra.Loggable;
import org.alpha_sync_java.infra.MulticastConnector;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.concurrent.*;

public class SSDP implements Loggable {
    private static final String DISCOVERY_MESSAGE = "M-SEARCH * HTTP/1.1\r\n" + "HOST:239.255.255.250:1900\r\n" + "ST:upnp:rootdevice\r\n" + "MX:2\r\n" + "MAN:\"ssdp:discover\"\r\n" + "\r\n";
    private static final String DISCOVERY_ADDRESS = "239.255.255.250";
    private static final String DISCOVERY_PORT = "1900";

    ServerContext serverContext;

    private final Connector connector;

    public SSDP(ServerContext serverContext) throws IOException {
        this.connector = new MulticastConnector(DISCOVERY_ADDRESS, DISCOVERY_PORT);
        this.serverContext = serverContext;
    }
//    should send message and register listener
public CompletableFuture<String> execute(){

            CompletableFuture.runAsync(() -> {
                try {
                    connector.listen(DISCOVERY_PORT, this::handleResponse);
                } catch (Exception e) {
                    serverContext.location.completeExceptionally(e);
                }
            });

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            CompletableFuture<String> timeout = new CompletableFuture<>();
            scheduler.schedule(() -> timeout.completeExceptionally(new TimeoutException("Timed out!")), 15, TimeUnit.SECONDS);

            CompletableFuture.runAsync(() -> {
                while (!serverContext.location.isDone()) {
                    try {
                        connector.send(DISCOVERY_ADDRESS, DISCOVERY_PORT, DISCOVERY_MESSAGE);
                        Thread.sleep(100);
                    } catch (Exception e) {
                        serverContext.location.completeExceptionally(e);
                    }
                }
            });

            CompletableFuture<String> result = CompletableFuture.anyOf(serverContext.location, timeout)
                    .thenApply(obj -> (String) obj);

            result.whenComplete((res, ex) -> scheduler.shutdown());

            return result;
        }



    public boolean handleResponse(String response) {
        if (response.contains("UPnP/1.0 SonyImagingDevice/1.0")){
            logger().info("Search found {}", "UPnP/1.0 SonyImagingDevice/1.0");
            Arrays.stream(response.split("\r?\n"))
                    .filter((keyValue) -> keyValue.startsWith("LOCATION: "))
                    .findFirst()
                    .map(s -> s.substring("LOCATION: ".length()))
                    .map((location) -> {
                        try {
                            return this.serverContext.location.complete(new LocatorURL(location));
                        } catch (MalformedURLException e) {
                            throw new RuntimeException("Invalid URL discovered from SSDP");
                        }
                    })
                    .orElseGet(() -> this.serverContext.location.completeExceptionally(new RuntimeException("Couldn't find location")));
            return true;
        }
        return false;
    }

}
