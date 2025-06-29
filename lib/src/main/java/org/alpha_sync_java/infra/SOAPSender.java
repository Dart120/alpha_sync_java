package org.alpha_sync_java.infra;

import org.alpha_sync_java.application.interfaces.SOAPConnector;
import org.alpha_sync_java.domain.communication.LocatorURL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SOAPSender implements SOAPConnector, Loggable {

    @Override
    public CompletableFuture<String> requestXML(LocatorURL url, String message, String soapAction) {
        logger().info("Requested XML from: {}", url);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url.getUrl());

        Future<Response> response = target
                .request()
                .header("SOAPAction", soapAction)
                .acceptLanguage("en-gb")
                .acceptEncoding("gzip, deflate").async()
                .post(Entity.entity(message, "text/xml"));


//        logger().info("Fetcher Status was: {}", response.getStatus());


        return CompletableFuture.supplyAsync(()-> {
            try {
                return response.get().readEntity(String.class);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
