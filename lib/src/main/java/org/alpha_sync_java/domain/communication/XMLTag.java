package org.alpha_sync_java.domain.communication;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class XMLTag {
    String name;
    String textContent;
    Supplier<CompletableFuture<List<XMLTag>>>  contents;
    List<XMLTag> contentsStream;
    Map<String, String> attributes;

    private XMLTag(Builder builder) {
        setName(builder.name);
        setTextContent(builder.textContent);
        setContents(builder.contents);
        setAttributes(builder.attributes);
    }

    public XMLTag(String name, String textContent, Supplier<CompletableFuture<List<XMLTag>>>  contents) {
        this.name = name;
        this.textContent = textContent;
        this.contents = contents;
    }
    public XMLTag(String name, String textContent) {
        this.name = name;
        this.textContent = textContent;
        this.contents = () -> CompletableFuture.supplyAsync(Collections::emptyList);
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextDecodedContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public List<XMLTag> getContents() {
        return contents.get().join();
    }

    public void setContents(Supplier<CompletableFuture<List<XMLTag>>>  contents) {
        this.contents = contents;
    }


    public static final class Builder {
        private String name;
        private String textContent;
        private Supplier<CompletableFuture<List<XMLTag>>>  contents;
        private Map<String, String> attributes;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder textContent(String val) {
            textContent = val;
            return this;
        }

        public Builder contents(Supplier<CompletableFuture<List<XMLTag>>> val) {
            contents = val;
            return this;
        }

        public Builder attributes(Map<String, String> val) {
            attributes = val;
            return this;
        }

        public XMLTag build() {
            return new XMLTag(this);
        }
    }
}
