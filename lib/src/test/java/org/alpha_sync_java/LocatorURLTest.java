package org.alpha_sync_java;

import org.alpha_sync_java.domain.communication.LocatorURL;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

public class LocatorURLTest {
    @Test
    public void decompose() throws MalformedURLException {
        LocatorURL p = new LocatorURL("http://test:8080/test/");
        assertEquals("http://test:8080/test/",p.getUrl());
    }

    @Test
    public void from() throws MalformedURLException {
        LocatorURL p = new LocatorURL("http://test:8080/test/");

        LocatorURL newURL = LocatorURL.from(p,"/new/");
        assertEquals("http://test:8080/new/",newURL.getUrl());
    }

    @Test
    public void fromEmpty() throws MalformedURLException {
        LocatorURL p = new LocatorURL("http://test:8080");
        LocatorURL newURL = LocatorURL.from(p,"");
        assertEquals("http://test:8080",newURL.getUrl());
    }


}
