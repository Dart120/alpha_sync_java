package org.alpha_sync_java.application.CLI;

import org.alpha_sync_java.application.usecases.ConnectCamera;
import org.alpha_sync_java.domain.CameraConnection;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
// TODO sync
public class CameraConnector {
    ConnectCamera connectCamera;

    private CameraConnector(Builder builder) {
        connectCamera = builder.connectCamera;
    }

    public CameraConnection show() throws ExecutionException, InterruptedException, IOException, ParserConfigurationException, SAXException {
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("press enter to connect camera ...");

            String keyPressed = sc.nextLine();
            sc.nextLine();

            if (keyPressed.isEmpty()) break;
        }
        System.out.println("Connecting...");
        return connectCamera.execute();
    }

    public static final class Builder {
        private ConnectCamera connectCamera;

        public Builder() {
        }

        public Builder connectCamera(ConnectCamera val) {
            connectCamera = val;
            return this;
        }

        public CameraConnector build() {
            return new CameraConnector(this);
        }
    }
}

