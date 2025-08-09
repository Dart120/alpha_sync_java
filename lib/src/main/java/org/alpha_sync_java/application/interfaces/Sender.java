package org.alpha_sync_java.application.interfaces;

public interface Sender {
    void send(String address, String port, String message) throws Exception;
}
