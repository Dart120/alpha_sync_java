package org.alpha_sync_java.infra;

import org.alpha_sync_java.application.interfaces.RequestsXML;
import org.alpha_sync_java.domain.communication.LocatorURL;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public class FetchXML implements RequestsXML, Loggable {
    // TODO response close when done pls
    @Override
    public String getXML(LocatorURL url) {
        logger().info("Requested XML from: {}", url);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url.getUrl());
        Response response = target.request().get();
        String in = response.readEntity(String.class);
        client.close();
        response.close();
        return in;
    }
}
