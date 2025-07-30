package org.alpha_sync_java.domain.communication;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import static java.util.Arrays.stream;

public class LocatorURL {
    private final URL url;

    public LocatorURL(String url) throws MalformedURLException, URISyntaxException {
        this.url = new URI(url).toURL();
    }

    public String getUrl(){
        return "http://" + this.url.getHost() + (url.getPort() != -1 ? ":" + url.getPort() : "")  + this.url.getPath();
    }

    public String getHost(){
        return this.url.getHost();
    }

    public Optional<String> getPort(){
        return url.getPort() != -1 ? Optional.of(String.valueOf(url.getPort())) : Optional.empty();
    }

    public static LocatorURL from(LocatorURL url, String path) throws MalformedURLException, URISyntaxException {
        return new LocatorURL("http://" + url.getHost() +  url.getPort().map((port) -> ":" + port).orElse("") + path);
    }



    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocatorURL url)) return false;
        return Objects.equals(this.getUrl(), url.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUrl());
    }

    @Override
    public String toString() {
        return "LocatorURL{" +
                "url=" + url +
                '}';
    }
}