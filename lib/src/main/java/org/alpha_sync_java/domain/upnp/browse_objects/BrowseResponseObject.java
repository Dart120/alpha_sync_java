package org.alpha_sync_java.domain.upnp.browse_objects;

import org.alpha_sync_java.application.UPNPObjectFactory;
import org.alpha_sync_java.application.XMLUtils;
import org.alpha_sync_java.application.interfaces.XMLExplorer;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.application.service.ContentDirectoryQueryService;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.infra.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class BrowseResponseObject implements Loggable {
    String result;
    String updateID;
    Integer numberReturned;
    Integer totalMatches;
    private String xmlResult;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BrowseResponseObject)) return false;
        BrowseResponseObject that = (BrowseResponseObject) o;
        return Objects.equals(updateID, that.updateID) && Objects.equals(numberReturned, that.numberReturned) && Objects.equals(totalMatches, that.totalMatches);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, updateID, numberReturned, totalMatches, xmlResult);
    }

    public List<UPNPObject> getResultToUPNPObjects(XMLExplorerFactory xmlExplorerFactory, ContentDirectoryQueryService contentDirectoryQueryService, ContentDirectory contentDirectory) throws ParserConfigurationException, IOException, SAXException {
        XMLExplorer xmlExplorer = xmlExplorerFactory.create(xmlResult);

        return UPNPObjectFactory.create(xmlExplorer, contentDirectoryQueryService, contentDirectory);
    }
    public static BrowseResponseObject fromXML(XMLExplorerFactory xmlExplorerFactory, String xml) throws ParserConfigurationException, IOException, SAXException {
        System.out.println(xml);
        XMLExplorer xmlExplorer = xmlExplorerFactory.create(xml);
        return new Builder()
                .result(XMLUtils.getSingleTextFromXML("Result", xmlExplorer))
                .updateID(XMLUtils.getSingleTextFromXML("UpdateID", xmlExplorer))
                .numberReturned(XMLUtils.getSingleIntFromXML("NumberReturned", xmlExplorer))
                .totalMatches(XMLUtils.getSingleIntFromXML("TotalMatches", xmlExplorer))
                .build();
    }

    private BrowseResponseObject(Builder builder) {
        result = builder.result;
        updateID = builder.updateID;
        numberReturned = builder.numberReturned;
        totalMatches = builder.totalMatches;
        if (xmlResult == null){
            result = result.replace("&lt;", "<");
            result = result.replace("&gt;", ">");
            xmlResult = result;
        }
    }


    public static final class Builder {
        private String result;
        private String updateID;
        private Integer numberReturned;
        private Integer totalMatches;

        public Builder() {
        }

        public Builder result(String val) {
            result = val;
            return this;
        }

        public Builder updateID(String val) {
            updateID = val;
            return this;
        }

        public Builder numberReturned(Integer val) {
            numberReturned = val;
            return this;
        }

        public Builder totalMatches(Integer val) {
            totalMatches = val;
            return this;
        }

        public BrowseResponseObject build() {
            return new BrowseResponseObject(this);
        }
    }
}