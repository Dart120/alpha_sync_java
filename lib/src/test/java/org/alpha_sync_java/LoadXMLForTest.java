package org.alpha_sync_java;

import org.alpha_sync_java.application.interfaces.RequestsXML;
import org.alpha_sync_java.application.interfaces.XMLExplorer;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LoadXMLForTest {
    public static String loadXML(String path){
        RequestsXML requestsXMLMock = Mockito.mock(RequestsXML.class);
        InputStream in = LoadXMLForTest.class.getClassLoader().getResourceAsStream(path);
        assertNotNull(in);
        return new BufferedReader(new InputStreamReader(in))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
