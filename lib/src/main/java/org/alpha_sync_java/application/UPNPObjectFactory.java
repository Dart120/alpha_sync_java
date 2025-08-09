package org.alpha_sync_java.application;

import org.alpha_sync_java.application.interfaces.XMLExplorer;
import org.alpha_sync_java.application.service.ContentDirectoryQueryService;
import org.alpha_sync_java.domain.upnp.VIDEO_RES;
import org.alpha_sync_java.domain.communication.XMLTag;
import org.alpha_sync_java.domain.upnp.*;
import org.alpha_sync_java.domain.utils.Pair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UPNPObjectFactory {



    public static List<UPNPObject> create(XMLExplorer xmlExplorer, ContentDirectoryQueryService contentDirectoryQueryService, ContentDirectory contentDirectory) throws ParserConfigurationException, IOException, SAXException {
        Stream<XMLTag> tagObjects = Stream.concat(xmlExplorer.getTagsByName("container"), xmlExplorer.getTagsByName("item"));
        return tagObjects
                .map(tagObject -> {
                            try {
                                return upnpObjectMapper(tagObject, contentDirectoryQueryService, contentDirectory);
                            } catch (ParserConfigurationException | IOException | SAXException e) {
                                throw new RuntimeException("Error mapping UPNPObject", e);
                            }
                        }
                )
                .collect(Collectors.toList());
    }
    private static UPNPObject upnpObjectMapper(XMLTag tag, ContentDirectoryQueryService contentDirectoryQueryService, ContentDirectory contentDirectory) throws ParserConfigurationException, IOException, SAXException {

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
                        .res(XMLUtils
                                .findTagsInStream("res", tag.getContents().stream())
                                .map((a)-> new Pair<VIDEO_RES, String>(VIDEO_RES.find(a.getTextDecodedContent()), a.getTextDecodedContent()))
                                .map((videoResStringPair -> {
                                    try {
                                        return new Pair<>(videoResStringPair.first(),new URI(videoResStringPair.second()).toURL());
                                    } catch (URISyntaxException | MalformedURLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }))
                                .collect(Collectors.toMap(Pair::first, Pair::second)))
                        .build();
            case "object.item.imageItem.photo":
                return new UPNPImage
                        .Builder()
                        .id(tag.getAttributes().get("id"))
                        .dcTitle(XMLUtils
                                .findTagInStream("dc:title", tag.getContents().stream())
                                .getTextDecodedContent())
                        .parentID(tag.getAttributes().get("parentID"))
                        .res(XMLUtils
                                .findTagsInStream("res", tag.getContents().stream())
                                .map((a)-> new Pair<>(PHOTO_RES.find(a.getTextDecodedContent()), a.getTextDecodedContent()))
                                .map((videoResStringPair -> {
                                    try {
                                        return new Pair<>(videoResStringPair.first(),new URI(videoResStringPair.second()).toURL());
                                    } catch (URISyntaxException | MalformedURLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }))
                                .collect(Collectors.toMap(Pair::first, Pair::second)))
                        .build();
            default:
                throw new RuntimeException("Could not create UPNPObject from xml. Unknown upnp class: " + upnpClass);
        }
    }
}
