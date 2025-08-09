package org.alpha_sync_java.domain.upnp;

import java.net.URL;
import java.util.Map;

public class UPNPImage extends UPNPObject implements Downloadable {

    Map<PHOTO_RES, URL> res;

    public Map<PHOTO_RES, URL> getRes() {
        return res;
    }

    String TN;
    String SM;

    private UPNPImage(UPNPImage.Builder builder) {
        super(builder);
        dcTitle = builder.dcTitle;
        id = builder.id;
        parentID = builder.parentID;
        res = builder.res;
        upnpClass ="object.item.ImageItem.photo";
    }






    public static class Builder extends SuperBuilder<UPNPImage.Builder> {
        private String dcTitle;
        private String id;
        private String parentID;
        private Map<PHOTO_RES, URL> res;

        public Builder() {
        }

        public UPNPImage.Builder dcTitle(String val) {
            dcTitle = val;
            return self();
        }

        public UPNPImage.Builder id(String val) {
            id = val;
            return self();
        }

        public UPNPImage.Builder parentID(String val) {
            parentID = val;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder res(Map<PHOTO_RES, URL> val) {
            res = val;
            return self();
        }

        public UPNPImage build() {
            return new UPNPImage(this);
        }
    }
}