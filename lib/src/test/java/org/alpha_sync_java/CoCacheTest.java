package org.alpha_sync_java;

import org.alpha_sync_java.application.service.UPNPObjectCachingService;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CoCacheTest {
    UPNPObjectCachingService upnpObjectCachingService;
    List<UPNPObject> listObj;
    @Before
    public void setUp() {
        upnpObjectCachingService = new UPNPObjectCachingService(10);
        listObj = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            UPNPObject obj = new UPNPObject.Builder()
                    .id("test"+i)
                    .dcTitle("test"+i)
                    .parentID("0")
                    .build();
            listObj.add(obj);
        }
    }

    @Test
    public void addAndCheck() {
        upnpObjectCachingService.put("test", listObj);
        assertEquals("test900",upnpObjectCachingService.coCacheGet("test900").getId());
    }
}
