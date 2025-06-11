package org.alpha_sync_java.domain.communication;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import static java.util.Arrays.stream;

public class LocatorURL {
    private URL url;

    public LocatorURL(String url) throws MalformedURLException {
        this.url = new URL(url);
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

    public static LocatorURL from(LocatorURL url, String path) throws MalformedURLException {
        return new LocatorURL("http://" + url.getHost() +  url.getPort().map((port) -> ":" + port).orElse("") + path);
    }



    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocatorURL)) return false;
        LocatorURL url = (LocatorURL) o;
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