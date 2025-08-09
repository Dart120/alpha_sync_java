package org.alpha_sync_java.domain;

import org.alpha_sync_java.application.service.ContentDirectoryQueryService;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.upnp.UPNPContainer;

public class CameraConnection {
    UPNPContainer root;
    ContentDirectoryQueryService contentDirectoryQueryService;
    ContentDirectory contentDirectory;

    public UPNPContainer getRoot() {
        return root;
    }

    public ContentDirectoryQueryService getContentDirectoryQueryService() {
        return contentDirectoryQueryService;
    }

    public ContentDirectory getContentDirectory() {
        return contentDirectory;
    }

    private CameraConnection(Builder builder) {
        root = builder.root;
        contentDirectoryQueryService = builder.contentDirectoryQueryService;
        contentDirectory = builder.contentDirectory;
    }

    public static final class Builder {
        private UPNPContainer root;
        private ContentDirectoryQueryService contentDirectoryQueryService;
        private ContentDirectory contentDirectory;

        public Builder() {
        }

        public Builder root(UPNPContainer val) {
            root = val;
            return this;
        }

        public Builder contentDirectoryQueryService(ContentDirectoryQueryService val) {
            contentDirectoryQueryService = val;
            return this;
        }

        public Builder contentDirectory(ContentDirectory val) {
            contentDirectory = val;
            return this;
        }

        public CameraConnection build() {
            return new CameraConnection(this);
        }
    }
}
