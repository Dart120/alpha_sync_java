package org.alpha_sync_java.domain.upnp;

import java.util.List;

public class UPNPImage extends UPNPObject {
    String upnpClass ="object.item.ImageItem.photo";
    List<String> res;
    String ORG;
    String LRG;
    String TN;
    String SM;

    private UPNPImage(UPNPImage.Builder builder) {
        super(builder);
        dcTitle = builder.dcTitle;
        id = builder.id;
        parentID = builder.parentID;
        res = builder.res;
    }

    public static class Builder extends SuperBuilder<UPNPImage.Builder> {
        private String dcTitle;
        private String id;
        private String parentID;
        private List<String> res;

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
        protected UPNPImage.Builder self() {
            return this;
        }

        public UPNPImage.Builder res(List<String> val) {
            res = val;
            return self();
        }

        public UPNPImage build() {
            return new UPNPImage(this);
        }
    }
}