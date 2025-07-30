package org.alpha_sync_java.application.service;

import org.alpha_sync_java.application.strategy.ExtractedDownloadable;
import org.alpha_sync_java.domain.upnp.UPNPObject;

import java.util.*;
import java.util.function.Function;

public class UPNPObjectCachingService extends LinkedHashMap<String, List<UPNPObject>> {
    int cacheSize;
    Map<String, UPNPObject> coCache = new HashMap<>();

    public UPNPObjectCachingService(int cacheSize) {
        super(cacheSize, 1f, true);
        this.cacheSize = cacheSize;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    @Override
    public List<UPNPObject> put(String key, List<UPNPObject> value) {
        value.forEach((obj) -> coCache.put(obj.getId(), obj));
        return super.put(key, value);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, List<UPNPObject>> eldest) {
        return size() > cacheSize;
    }

    public UPNPObject coCacheGet(String id){
        return coCache.get(id);
    }

    public UPNPObject coCacheComputeIfAbsent(String id, Function<? super String, ? extends UPNPObject> mappingFunction){
        return coCache.computeIfAbsent(id, mappingFunction);
    }
    public List<UPNPObject> coCacheComputeIfAbsent(Set<String> ids, Function<Set<String>, Map<String, ExtractedDownloadable>> mappingFunction){
        List<UPNPObject> res = new ArrayList<>(ids
                .stream()
                .map(coCache::get)
                .filter(Objects::nonNull)
                .toList());
        res.stream().map(UPNPObject::getId).toList().forEach(ids::remove);
        mappingFunction.apply(ids).forEach((k,v) -> coCache.put(k, switch (v.value()) {
            case UPNPObject obj -> {
                res.add(obj);
                yield obj;
            }
            default -> throw new IllegalStateException("Unexpected value: " + v.value());
        }));
        return res;
    }
}
