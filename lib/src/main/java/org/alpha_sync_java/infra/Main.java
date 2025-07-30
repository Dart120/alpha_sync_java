package org.alpha_sync_java.infra;



import org.alpha_sync_java.infra.CLI.CameraConnector;
import org.alpha_sync_java.infra.CLI.DateSelector;



public class Main {
    public static void main(String[] args) throws Exception  {
        CameraConnector cameraConnector = new CameraConnector();
        DateSelector dateSelector = new DateSelector();
        dateSelector.show(cameraConnector.show().get());
    }
}
