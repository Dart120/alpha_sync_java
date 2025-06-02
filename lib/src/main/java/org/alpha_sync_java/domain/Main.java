package org.alpha_sync_java.domain;


import org.alpha_sync_java.application.usecases.SSDP;

public class Main {
    public static void main(String[] args) throws Exception {

        SSDP s = new SSDP();
        s.execute();
    }
}
