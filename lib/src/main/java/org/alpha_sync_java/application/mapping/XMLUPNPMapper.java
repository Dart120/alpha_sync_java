package org.alpha_sync_java.application.mapping;

import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseResponseObject;

import java.io.InputStream;

public class XMLUPNPMapper {
    XMLExplorerFactory xmlExplorerFactory;
    public BrowseResponseObject xmlToBrowseResponseObject(String xml){
        BrowseRequestObject bro = new BrowseRequestObject
                .Builder()
                .objectID("0")
                .requestedCount(100)
                .filter("*")
                .startingIdx(0)
                .build();

        return null;
    }
    public String browseRequestObjectToXML(BrowseRequestObject browseRequestObject){
        return null;
    }
    public UPNPObject xmlToUPNPContainer (String xml){
        return null;
    }
}
