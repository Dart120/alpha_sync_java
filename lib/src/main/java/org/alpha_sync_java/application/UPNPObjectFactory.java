package org.alpha_sync_java.application;

import org.alpha_sync_java.application.interfaces.XMLExplorer;
import org.alpha_sync_java.domain.communication.XMLTag;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPImage;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.domain.upnp.UPNPVideo;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UPNPObjectFactory {
    public static List<UPNPObject> create(XMLExplorer xmlExplorer) throws ParserConfigurationException, IOException, SAXException {
        String upnpClass = XMLUtils.getSingleTextFromXML("upnp:class", xmlExplorer);
        Stream<XMLTag> tagObjects = Stream.concat(xmlExplorer.getTagsByName("container"), xmlExplorer.getTagsByName("item"));
        return tagObjects
                .map(tagObject -> {
                            try {
                                return UPNPObjectFactory.upnpObjectMapper(tagObject);
                            } catch (ParserConfigurationException | IOException | SAXException e) {
                                throw new RuntimeException("Error mapping UPNPObject", e);
                            }
                        }
                )
                .collect(Collectors.toList());
    }
    public static UPNPObject upnpObjectMapper(XMLTag tag) throws ParserConfigurationException, IOException, SAXException {
        String upnpClass = tag
                .getContents().stream()
                .filter(xmlTag -> Objects.equals(xmlTag.getName(), "upnp:class"))
                .findFirst()
                .map((XMLTag::getTextDecodedContent))
                .orElse("");
        switch (upnpClass){
            case "object.container":
                return new UPNPContainer
                        .Builder()
                        .id(tag.getAttributes().get("id"))
                        .dcTitle(XMLUtils
                                .findTagInStream("dc:title", tag.getContents().stream())
                                .getTextDecodedContent())
                        .parentID(tag.getAttributes().get("parentID"))
                        .childCount(Integer
                                .parseInt(tag.getAttributes()
                                        .get("childCount")))
                        .build();

            case "object.item.videoItem.movie":
                return new UPNPVideo
                        .Builder()
                        .id(tag.getAttributes().get("id"))
                        .dcTitle(XMLUtils
                                .findTagInStream("dc:title", tag.getContents().stream())
                                .getTextDecodedContent())
                        .parentID(tag.getAttributes().get("parentID"))
                        .res(XMLUtils.findTagsInStream("res", tag.getContents().stream())
                                .map(XMLTag::getTextDecodedContent)
                                .collect(Collectors.toList()))
                        .build();
            case "object.item.ImageItem.photo":
                return new UPNPImage
                        .Builder()
                        .id(tag.getAttributes().get("id"))
                        .dcTitle(XMLUtils
                                .findTagInStream("dc:title", tag.getContents().stream())
                                .getTextDecodedContent())
                        .parentID(tag.getAttributes().get("parentID"))
                        .res(XMLUtils.findTagsInStream("res", tag.getContents().stream())
                                .map(XMLTag::getTextDecodedContent)
                                .collect(Collectors.toList()))
                        .build();
            default:
                throw new RuntimeException("Could not create UPNPObject from xml. Unknown upnp class: " + upnpClass);
        }
    }
}
