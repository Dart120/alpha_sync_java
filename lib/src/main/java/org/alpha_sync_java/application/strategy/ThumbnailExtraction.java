package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.domain.extracted.ExtractedByteArray;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.*;
import org.alpha_sync_java.infra.Loggable;

import java.util.Map;
import java.util.regex.Pattern;

public class ThumbnailExtraction implements ExtractionStrategy<ExtractedByteArray>, Loggable {
    Pattern p = Pattern.compile("\\d\\d\\d\\d-\\d+-\\d+");
    DownloadService downloadService;

    public ThumbnailExtraction(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @Override
    public <U extends UPNPObject> Boolean extract(U upnpObject, Map<String, ExtractedByteArray> map, TreeExplorerState state) {
        switch (upnpObject){
            case UPNPContainer container -> {
                logger().info("Searching for {} in {}", state.getDate(), container.getDcTitle());
                return p.matcher(container.getDcTitle()).find() && !state.getDate().equals(container.getDcTitle());
            }
            case UPNPImage image -> map.put(image.getId(),new ExtractedByteArray(() -> {
                try {
                    return downloadService.downloadToBytes(image, PHOTO_RES.TN);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
            case UPNPVideo video -> map.put(video.getId(), new ExtractedByteArray(() -> {
                try {
                    return downloadService.downloadToBytes(video, PHOTO_RES.TN);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
            default -> throw new IllegalStateException("Unexpected value in Thumbnail Extraction: " + upnpObject);
        }
        return false;
    }
}
