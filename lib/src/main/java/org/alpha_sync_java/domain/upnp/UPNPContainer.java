package org.alpha_sync_java.domain.upnp;

import org.alpha_sync_java.application.service.ContentDirectoryQueryService;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UPNPContainer extends UPNPObject {
    Integer childCount;
    private List<UPNPObject> children;

    public Integer getChildCount() {
        return childCount;
    }

    private UPNPContainer(Builder builder) {
        super(builder);
        childCount = builder.childCount;
        upnpClass = "object.container";
    }

    public CompletableFuture<List<UPNPObject>> getChildren() {
        return CompletableFuture.supplyAsync(()-> {
            BrowseRequestObject bro = constructBrowseRequestObject();
            return ContentDirectoryQueryService.query(bro);
        });
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


