package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

final class TestUtils {
    static class A {
    }

    static class B extends A {
    }

    static class C extends B {
    }

    @NotNull
    static final Function1<Boolean, Boolean> NOT = new Function1<Boolean, Boolean>() {
        @Override
        Boolean apply(@Nullable final Boolean arg) {
            return arg != null && !arg;
        }
    };

    @NotNull
    static final Function1<Object, String> TO_STRING = new Function1<Object, String>() {
        @Override
        String apply(@Nullable final Object arg) {
            return Objects.toString(arg);
        }
    };

    @NotNull
    static final Function2<Boolean, Boolean, Boolean> AND = new Function2<Boolean, Boolean, Boolean>() {
        @Nullable
        @Override
        Boolean apply(@Nullable final Boolean arg1, @Nullable final Boolean arg2) {
            return arg1 != null && arg2 != null ? arg1 && arg2 : null;
        }
    };

    @NotNull
    static final Predicate<Boolean> IS_TRUE = new Predicate<Boolean>() {
        @Override
        public Boolean apply(@Nullable final Boolean arg) {
            return arg != null && arg;
        }
    };

    @NotNull
    static final Predicate<Boolean> IS_FALSE = new Predicate<Boolean>() {
        @Override
        public Boolean apply(@Nullable final Boolean arg) {
            return arg != null && !arg;
        }
    };

    @NotNull
    static final Predicate<Boolean> ALWAYS_THROW = new Predicate<Boolean>() {
        @Override
        public Boolean apply(@Nullable final Boolean arg) {
            throw new IllegalStateException();
        }
    };

    @NotNull
    static final Function1<Integer, Integer> DOUBLE_NUM = new Function1<Integer, Integer>() {
        @Nullable
        @Override
        Integer apply(@Nullable final Integer arg) {
            return arg != null ? 2 * arg : null;
        }
    };

    @NotNull
    static final Predicate<Integer> IS_EVEN = new Predicate<Integer>() {
        @Nullable
        @Override
        Boolean apply(@Nullable final Integer arg) {
            return arg != null ? arg % 2 == 0 : null;
        }
    };

    @NotNull
    static final Function2<Integer, Integer, Integer> SUBTRACTION = new Function2<Integer, Integer, Integer>() {
        @Nullable
        @Override
        Integer apply(@Nullable final Integer arg1, @Nullable final Integer arg2) {
            return arg1 != null && arg2 != null ? arg1 - arg2 : null;
        }
    };

    private TestUtils() {
    }
}
