package org.alpha_sync_java.domain.upnp.browse_objects;


import org.alpha_sync_java.domain.upnp.UPNPContainer;

public class BrowseRequestObject {
    String objectID;
    String filter;
    Integer startingIdx;
    Integer requestedCount;

    public String getObjectID() {
        return objectID;
    }

    private BrowseRequestObject(Builder builder) {
        objectID = builder.objectID;
        filter = builder.filter;
        startingIdx = builder.startingIdx;
        requestedCount = builder.requestedCount;
    }

    public static BrowseRequestObject from(UPNPContainer upnpContainer){
        return new Builder()
                .objectID(upnpContainer.getId())
                .filter("*")
                .requestedCount(100)
                .startingIdx(0)
                .build();
    }

    public String toXML(){
        return String.format("<?xml version=\"1.0\"?>\n" +
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                "    <s:Body>\n" +
                "        <u:Browse xmlns:u=\"urn:schemas-upnp-org:service:ContentDirectory:1\">\n" +
                "            <ObjectID>%s</ObjectID>\n" +
                "            <BrowseFlag>BrowseDirectChildren</BrowseFlag>\n" +
                "            <Filter>%s</Filter>\n" +
                "            <StartingIndex>%s</StartingIndex>\n" +
                "            <RequestedCount>%s</RequestedCount>\n" +
                "            <SortCriteria/>\n" +
                "        </u:Browse>\n" +
                "    </s:Body>\n" +
                "</s:Envelope>", objectID, filter, startingIdx, requestedCount);
    }

    public static final class Builder {
        private String objectID;
        private String filter;
        private Integer startingIdx;
        private Integer requestedCount;

        public Builder() {
        }

        public Builder objectID(String val) {
            objectID = val;
            return this;
        }

        public Builder filter(String val) {
            filter = val;
            return this;
        }

        public Builder startingIdx(Integer val) {
            startingIdx = val;
            return this;
        }

        public Builder requestedCount(Integer val) {
            requestedCount = val;
            return this;
        }

        public BrowseRequestObject build() {
            return new BrowseRequestObject(this);
        }
    }
}



