package org.alpha_sync_java.application.interfaces;

import org.alpha_sync_java.domain.communication.LocatorURL;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public interface SOAPConnector {
    CompletableFuture<String> requestXML(LocatorURL url, String message, String SOAPACTION);
}
