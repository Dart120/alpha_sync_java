package org.alpha_sync_java.application.usecases;

import org.alpha_sync_java.application.interfaces.Listener;
import org.alpha_sync_java.application.interfaces.Sender;

public class SSDP {
    private String serverDetails;
    public String location;

    private Sender sender;
    private Listener listener;

    public SSDP(Sender sender, Listener listener) {
        this.sender = sender;
        this.listener = listener;
    }
//    should send message and register listener
    public void execute(){

    }
    public void handleResponse(String response) {

    }
}
