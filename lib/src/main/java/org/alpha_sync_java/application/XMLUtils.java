package org.alpha_sync_java.application;

import org.alpha_sync_java.application.interfaces.XMLExplorer;
import org.alpha_sync_java.domain.communication.XMLTag;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

public class XMLUtils {
    public static String getSingleTextFromXML(String tagName, XMLExplorer xmlExplorer) throws ParserConfigurationException, IOException, SAXException {
        return xmlExplorer
                .getTagsByName(tagName)
                .findFirst()
                .map(XMLTag::getTextDecodedContent)
                .orElseThrow(() -> new RuntimeException("Could not find " + tagName + " in xml"));
    }

    public static Integer getSingleIntFromXML(String tagName, XMLExplorer xmlExplorer) throws ParserConfigurationException, IOException, SAXException {
        return xmlExplorer
                .getTagsByName(tagName)
                .findFirst()
                .map(XMLTag::getTextDecodedContent)
                .map(Integer::parseInt)
                .orElseThrow(() -> new RuntimeException("Could not find " + tagName + " in xml"));
    }

    public static String getSingleAttributeFromXML(String tagName, String attributeName, XMLExplorer xmlExplorer) throws ParserConfigurationException, IOException, SAXException {
        return xmlExplorer
                .getTagsByName(tagName)
                .findFirst()
                .map(XMLTag::getAttributes)
                .map(attributes -> attributes.get(attributeName))
                .orElseThrow(() -> new RuntimeException("Could not find attribute in " + tagName + " with name: " + attributeName + " in xml"));
    }

    public static XMLTag findTagInStream(String tagName, Stream<XMLTag> tags) throws ParserConfigurationException, IOException, SAXException {
        return tags
                .filter(xmlTag -> Objects.equals(xmlTag.getName(), tagName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find " + tagName + " in Stream"));
    }
    public static Stream<XMLTag> findTagsInStream(String tagName, Stream<XMLTag> tags) throws ParserConfigurationException, IOException, SAXException {
        return tags
                .filter(xmlTag -> Objects.equals(xmlTag.getName(), tagName));
    }


}
