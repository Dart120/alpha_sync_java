package org.alpha_sync_java.domain.upnp;

public enum PHOTO_RES implements ResolutionSelector<PHOTO_RES> {
    ORG("*"), LRG("LRG_DSC"), SM("SM_DSC"), TN("TN_DSC");

    private final String protocolInfo;

    PHOTO_RES(String s) {
        this.protocolInfo = s;
    }

    public static PHOTO_RES find(String protocolInfo){
        if (protocolInfo.contains(LRG.protocolInfo)){
            return LRG;
        } else if (protocolInfo.contains(SM.protocolInfo)) {
            return SM;
        } else if (protocolInfo.contains(TN.protocolInfo)) {
            return TN;
        } else{
            return ORG;
        }
    }


    @Override
    public PHOTO_RES default_value() {
        return TN;
    }
}
