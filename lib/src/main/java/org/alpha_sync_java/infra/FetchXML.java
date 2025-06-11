package org.alpha_sync_java.infra;

import org.alpha_sync_java.application.interfaces.RequestsXML;
import org.alpha_sync_java.domain.communication.LocatorURL;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public class FetchXML implements RequestsXML, Loggable {
    @Override
    public InputStream getXML(LocatorURL url) {
        logger().info("Requested XML from: {}", url);
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(url.getUrl());

        Response response = target.request().get();

        InputStream in = response.readEntity(InputStream.class);

//        response.close();
        client.close();

        return in;
    }
}
