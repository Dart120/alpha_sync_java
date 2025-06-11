package org.alpha_sync_java.domain.utils;

import java.util.Optional;

public class Either<L, R> {
    private final L left;
    private final R right;

    private Either(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Either<L, R> left(L value) {
        return new Either<>(value, null);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Either<>(null, value);
    }

    public Optional<L> getLeft() { return Optional.ofNullable(left); }
    public Optional<R> getRight() { return Optional.ofNullable(right); }
}
