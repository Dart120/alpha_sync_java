package org.alpha_sync_java.infra;

import org.alpha_sync_java.application.interfaces.XMLExplorer;
import org.alpha_sync_java.domain.communication.XMLTag;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
// TODO refactor the adapter out
// TODO change the parser to a stream based one or return lists from this
public class XMLParser implements XMLExplorer, Loggable {

    Document doc;
    Map<XMLTag,Node> map = new HashMap<>();
    public XMLParser(InputStream xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = builder.parse(xml,"me");
    }

    @Override
    public Stream<XMLTag> getTagsByName(String name) throws ParserConfigurationException, IOException, SAXException {
        logger().info("getting tags called: {}", name);
        NodeList nl =  doc.getElementsByTagName(name);

        List<XMLTag> tagList = new ArrayList<>();

        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            XMLTag xmlTag = nodeToTag(node);
            logger().info("getting found node with name: {}", node.getNodeName());
            tagList.add(xmlTag);
            map.put(xmlTag, node);
        }

        return tagList.stream();

    }

    @Override
    public Stream<XMLTag> getTagsSiblings(XMLTag tag) {
        XMLTag xmlTag;
        Node oriNode = map.get(tag);
        Node node = oriNode;
        List<XMLTag> tagList = new ArrayList<>();
        while (node.getPreviousSibling() != null){
            node = node.getPreviousSibling();
            logger().info("found sibling with name: {}", node.getNodeName());
            xmlTag = nodeToTag(node);
            tagList.add(xmlTag);
            map.put(xmlTag,node);
        }
        node = oriNode;
        while (node.getNextSibling() != null){
            node = node.getNextSibling();
            logger().info("found sibling with name: {}", node.getNodeName());
            xmlTag = nodeToTag(node);
            tagList.add(xmlTag);
            map.put(xmlTag,node);
        }
        return tagList.stream();
    }

    private XMLTag nodeToTag(Node node){
        logger().info("converting to tag: {}", node.getNodeName());
        return new XMLTag(node.getNodeName(), node.getTextContent(), Stream.empty());
    }
}
