package org.alpha_sync_java.domain;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {
    public static <T> Stream<T> of(Enumeration<T> e){
        Iterator<T> iterator = new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return e.hasMoreElements();
            }

            @Override
            public T next() {
                return e.nextElement();
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator ,0), false);
    }


}
