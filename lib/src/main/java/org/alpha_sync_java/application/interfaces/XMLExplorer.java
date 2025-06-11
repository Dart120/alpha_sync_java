package org.alpha_sync_java.application.interfaces;

import org.alpha_sync_java.domain.communication.XMLTag;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.stream.Stream;

public interface XMLExplorer {
    public Stream<XMLTag> getTagsByName(String name) throws ParserConfigurationException, IOException, SAXException;
    public Stream<XMLTag> getTagsSiblings(XMLTag tag);
}
