package org.alpha_sync_java.domain.upnp;

import java.util.Objects;

public class UPNPObject {
    String dcTitle;
    String id;
    String parentID;
    String upnpClass;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UPNPObject)) return false;
        UPNPObject that = (UPNPObject) o;
        return Objects.equals(dcTitle, that.dcTitle) && Objects.equals(id, that.id) && Objects.equals(parentID, that.parentID) && Objects.equals(upnpClass, that.upnpClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dcTitle, id, parentID, upnpClass);
    }

    public String getDcTitle() {
        return dcTitle;
    }

    public String getId() {
        return id;
    }

    public String getParentID() {
        return parentID;
    }

    public String getUpnpClass() {
        return upnpClass;
    }

    UPNPObject(SuperBuilder<?> builder) {
        dcTitle = builder.dcTitle;
        id = builder.id;
        parentID = builder.parentID;
        upnpClass = builder.upnpClass;
    }

    public abstract static class SuperBuilder<B extends SuperBuilder> {
        String dcTitle;
        String id;
        String parentID;
        String upnpClass;

        public B dcTitle(String val) {
            dcTitle = val;
            return self();
        }

        public B id(String val) {
            id = val;
            return self();
        }

        public B parentID(String val) {
            parentID = val;
            return self();
        }

        public B upnpClass(String val) {
            upnpClass = val;
            return self();
        }

        protected abstract B self();

        public UPNPObject build() {
            return new UPNPObject(this);
        }
    }

    public static class Builder extends SuperBuilder<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }
}
