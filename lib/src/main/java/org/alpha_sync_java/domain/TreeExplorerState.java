package org.alpha_sync_java.domain;

import java.util.Set;

public class TreeExplorerState {
    Set<String> ids;
    String id;
    String date;

    boolean foundContainer = false;
    boolean foundItem = false;

    public void foundContainer() {
        this.foundContainer = true;
    }

    public void foundItem(){
        this.foundItem = true;
    }

    public boolean haveFoundContainer() {
        return foundContainer;
    }

    public boolean haveFoundItem() {
        return foundContainer;
    }

    private TreeExplorerState(Builder builder) {
        ids = builder.ids;
        id = builder.id;
        date = builder.date;
    }

    public Set<String> getIds() {
        return ids;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public static final class Builder {
        private Set<String> ids;
        private String id;
        private String date;

        public Builder() {
        }

        public Builder ids(Set<String> val) {
            ids = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder date(String val) {
            date = val;
            return this;
        }

        public TreeExplorerState build() {
            return new TreeExplorerState(this);
        }
    }
}
