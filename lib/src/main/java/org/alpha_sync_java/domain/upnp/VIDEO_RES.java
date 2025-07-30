package org.alpha_sync_java.domain.upnp;

public enum VIDEO_RES implements ResolutionSelector<VIDEO_RES> {
    ORG("*"), TN("TN");

    private final String protocolInfo;

    VIDEO_RES(String s) {
        this.protocolInfo = s;
    }

    public static VIDEO_RES find(String protocolInfo){
      if (protocolInfo.contains(TN.protocolInfo)) {
            return TN;
        } else{
            return ORG;
        }
    }

    @Override
    public VIDEO_RES default_value() {
        return null;
    }
}
