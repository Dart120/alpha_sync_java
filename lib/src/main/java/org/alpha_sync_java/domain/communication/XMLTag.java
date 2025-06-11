package org.alpha_sync_java.domain.communication;

import java.util.stream.Stream;

public class XMLTag {
    String name;
    String textContent;
    Stream<XMLTag> contents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public Stream<XMLTag> getContents() {
        return contents;
    }

    public void setContents(Stream<XMLTag> contents) {
        this.contents = contents;
    }

    public XMLTag(String name, String textContent, Stream<XMLTag> contents) {
        this.name = name;
        this.textContent = textContent;
        this.contents = contents;
    }
}
