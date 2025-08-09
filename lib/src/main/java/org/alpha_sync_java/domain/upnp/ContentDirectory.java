package org.alpha_sync_java.domain.upnp;

import org.alpha_sync_java.domain.communication.LocatorURL;
import org.alpha_sync_java.domain.communication.XMLTag;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContentDirectory {
        @Override
        public boolean equals(Object o) {
                if (!(o instanceof ContentDirectory that)) return false;
            return Objects.equals(SCPDURL, that.SCPDURL) && Objects.equals(controlURL, that.controlURL) && Objects.equals(eventSubURL, that.eventSubURL);
        }

        @Override
        public int hashCode() {
                return Objects.hash( SCPDURL, controlURL, eventSubURL);
        }

        public LocatorURL SCPDURL;
        public LocatorURL controlURL;
        public LocatorURL eventSubURL;
        private ContentDirectory(Builder contentDirectoryBuilder){
                this.SCPDURL = contentDirectoryBuilder.SCPDURL;
                this.controlURL = contentDirectoryBuilder.controlURL;
                this.eventSubURL = contentDirectoryBuilder.eventSubURL;
        }
        public static class Builder {
                private LocatorURL SCPDURL;
                private LocatorURL controlURL;
                private LocatorURL eventSubURL;


                public Builder SCPDURL(LocatorURL SCPDURL) {
                        this.SCPDURL = SCPDURL;
                        return this;
                }

                public Builder controlURL(LocatorURL controlURL) {
                        this.controlURL = controlURL;
                        return this;
                }

                public Builder eventSubURL(LocatorURL eventSubURL) {
                        this.eventSubURL = eventSubURL;
                        return this;
                }
                public ContentDirectory build(){
                        return new ContentDirectory(this);
                }
        }

        public static ContentDirectory fromXMLStream(Stream<XMLTag> tagStream, LocatorURL baseURL){
                List<XMLTag> tagList = tagStream.collect(Collectors.toList());
                LocatorURL SCPDURL = tagList.stream().filter(xmlTag -> Objects.equals(xmlTag.getName(), "SCPDURL"))
                        .findFirst()
                        .map(xmlTag -> {
                            try {
                                return LocatorURL.from(baseURL, xmlTag.getTextDecodedContent());
                            } catch (MalformedURLException e) {
                                throw new RuntimeException("Could not extract SCPDURL from content directory");
                            } catch (URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .orElse(null);
                LocatorURL controlURL = tagList.stream().filter(xmlTag -> xmlTag.getName().equals("controlURL"))
                        .findFirst()
                        .map(xmlTag -> {
                            try {
                                return LocatorURL.from(baseURL, xmlTag.getTextDecodedContent());
                            } catch (MalformedURLException | URISyntaxException e) {
                                throw new RuntimeException("Could not extract controlURL from content directory");
                            }
                        })
                        .orElse(null);
                LocatorURL eventSubURL = tagList.stream().filter(xmlTag -> Objects.equals(xmlTag.getName(), "eventSubURL"))
                        .findFirst()
                        .map(xmlTag -> {
                            try {
                                return LocatorURL.from(baseURL, xmlTag.getTextDecodedContent());
                            } catch (MalformedURLException | URISyntaxException e) {
                                    throw new RuntimeException("Could not extract eventSubURL from content directory");
                            }
                        })
                        .orElse(null);
                return new Builder().eventSubURL(eventSubURL)
                        .controlURL(controlURL)
                        .SCPDURL(SCPDURL)
                        .build();
        }

    @Override
    public String toString() {
        return "ContentDirectory{" +
                "SCPDURL=" + SCPDURL +
                ", controlURL=" + controlURL +
                ", eventSubURL=" + eventSubURL +
                '}';
    }

}
