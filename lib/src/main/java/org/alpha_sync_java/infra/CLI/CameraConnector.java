package org.alpha_sync_java.infra.CLI;

import org.alpha_sync_java.application.usecases.ConnectCamera;
import org.alpha_sync_java.application.usecases.DownloadDate;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class CameraConnector {
    private final TextIO textIO;
    ConnectCamera connectCamera;

    public CameraConnector() {
        this.connectCamera = new ConnectCamera();
        this.textIO = TextIoFactory.getTextIO();
    }

    public Optional<UPNPContainer> show() throws ExecutionException, InterruptedException, IOException, ParserConfigurationException, SAXException {
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("press enter to connect camera ...");
            String keyPressed = sc.nextLine();
            sc.nextLine();

            if (keyPressed.isEmpty()) break;
        }
        return connectCamera.execute();
    }
}

