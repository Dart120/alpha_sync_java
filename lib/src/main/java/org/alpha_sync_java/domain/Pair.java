package org.alpha_sync_java.domain;

public class Pair<T,U> {
    T first;
    U second;

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
}
