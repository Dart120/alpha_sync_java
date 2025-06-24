package org.alpha_sync_java.domain.utils;
@FunctionalInterface
public interface Supplier3<T,U,R> {
    public void apply(T arg1, U arg2, R arg3) throws Exception;
}
