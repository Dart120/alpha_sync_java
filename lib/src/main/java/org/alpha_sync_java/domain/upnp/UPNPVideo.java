package org.alpha_sync_java.domain.upnp;

import org.alpha_sync_java.application.strategy.ExtractionStrategy;

import java.net.URL;
import java.util.Map;

public class UPNPVideo extends UPNPObject implements Downloadable{

    Map<VIDEO_RES, URL> res;

    public Map<VIDEO_RES, URL> getRes() {
        return res;
    }

    private UPNPVideo(Builder builder) {
        super(builder);
        dcTitle = builder.dcTitle;
        id = builder.id;
        parentID = builder.parentID;
        res = builder.res;
        upnpClass = "object.item.videoItem.movie";
    }




    public static class Builder extends SuperBuilder<Builder> {
        private String dcTitle;
        private String id;
        private String parentID;
        private Map<VIDEO_RES, URL> res;

        public Builder() {
        }

        public Builder dcTitle(String val) {
            dcTitle = val;
            return self();
        }

        public Builder id(String val) {
            id = val;
            return self();
        }

        public Builder parentID(String val) {
            parentID = val;
            return self();
        }


        @Override
        protected Builder self() {
            return this;
        }

        public Builder res(Map<VIDEO_RES, URL> val) {
            res = val;
            return self();
        }

        public UPNPVideo build() {
            return new UPNPVideo(this);
        }
    }


}