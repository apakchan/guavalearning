package com.learn.guava.utilities;

import com.google.common.base.Objects;
import org.junit.Test;

import java.util.Calendar;

public class ObjectsTest {
    static class Guava {
        private final String manufacturer;
        private final String version;
        private final Calendar releaseDate;

        public Guava(String manufacturer, String version, Calendar releaseDate) {
            this.manufacturer = manufacturer;
            this.version = version;
            this.releaseDate = releaseDate;
        }
    }

    @Test
    public void test() {

    }
}
