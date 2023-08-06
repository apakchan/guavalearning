package com.learn.guava.functional;

import com.google.common.base.Function;
import com.google.common.base.Strings;

import javax.annotation.Nullable;

public class FunctionalExample {
    public static void main(String[] args) {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(@Nullable String input) {
                return Strings.isNullOrEmpty(input) ? 0 : input.length();
            }

        };

    }
}
