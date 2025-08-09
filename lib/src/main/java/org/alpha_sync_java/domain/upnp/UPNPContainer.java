package org.alpha_sync_java.domain.upnp;

import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;


import java.util.List;

public class UPNPContainer extends UPNPObject {
    Integer childCount;

    @Override
    public String toString() {
        return "UPNPContainer{" +
                "childCount=" + childCount +
                ", dcTitle='" + dcTitle + '\'' +
                ", id='" + id + '\'' +
                ", parentID='" + parentID + '\'' +
                ", upnpClass='" + upnpClass + '\'' +
                '}';
    }

    public Integer getChildCount() {
        return childCount;
    }


    private UPNPContainer(Builder builder) {
        super(builder);
        childCount = builder.childCount;
        upnpClass = "object.container";
    }

    public static class Builder extends SuperBuilder<Builder> {
        private Integer childCount;


        public Builder() {
        }

        public Builder childCount(Integer val) {
            childCount = val;
            return self();
        }

        @Override
        protected UPNPContainer.Builder self() {
            return this;
        }

        public UPNPContainer build() {
            return new UPNPContainer(this);
        }
    }

    public BrowseRequestObject constructBrowseRequestObject(){
        return new BrowseRequestObject
                .Builder()
                .objectID(id)
                .requestedCount(100)
                .filter("*")
                .startingIdx(0)
                .build();
    }
}


