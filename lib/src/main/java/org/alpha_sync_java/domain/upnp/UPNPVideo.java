package org.alpha_sync_java.domain.upnp;

import java.util.List;

public class UPNPVideo extends UPNPObject {
    String upnpClass = "object.item.videoItem.movie";
    List<String> res;
    String ORG;
    String LRG;
    String TN;
    String SM;



    private UPNPVideo(Builder builder) {
        super(builder);
        dcTitle = builder.dcTitle;
        id = builder.id;
        parentID = builder.parentID;
        res = builder.res;
    }

    public static class Builder extends SuperBuilder<Builder> {
        private String dcTitle;
        private String id;
        private String parentID;
        private String upnpClass;
        private List<String> res;

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

        public Builder upnpClass(String val) {
            upnpClass = val;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder res(List<String> val) {
            res = val;
            return self();
        }

        public UPNPVideo build() {
            return new UPNPVideo(this);
        }
    }


}