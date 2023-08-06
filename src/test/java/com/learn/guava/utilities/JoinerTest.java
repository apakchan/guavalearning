package com.learn.guava.utilities;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.Files;
import com.sun.xml.internal.ws.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class JoinerTest {
    private final List<String> stringList = Arrays.asList(
            "google", "java", "scala", "Kafka"
    );

    private final List<String> stringListWithNullValue = Arrays.asList(
            "google", "java", "scala", "Kafka", null
    );

    private final String targetFileName = "E:\\IdeaProjectsBook\\guavaProgramming\\guava-joiner.txt";

    private final Map<String, String> stringMap = ImmutableBiMap.of("Hello", "Guava", "Java", "Scala");

    @Test
    public void testJoin() {
        String res = Joiner.on("#").join(stringList);
        assertEquals("google#java#scala#Kafka", res);
    }

    @Test
    public void testJoinWithNullValue() {
        // NPE because of null value
        String res = Joiner.on("#").join(stringListWithNullValue);
        assertEquals("google#java#scala#Kafka", res);
    }

    @Test
    public void testJoinWithNullValueButSkip() {
        // skipNulls ignore null value
        String res = Joiner.on("#").skipNulls().join(stringListWithNullValue);
        assertEquals("google#java#scala#Kafka", res);
    }

    @Test
    public void testJoinWith_NullValue_UseDefaultValue() {
        // null value will be replaced by default value
        String res = Joiner.on("#").useForNull("default").join(stringListWithNullValue);
        assertEquals("google#java#scala#Kafka#default", res);
    }

    @Test
    public void testJoin_On_Append_To_StringBuilder() {
        final StringBuilder builder = new StringBuilder("str#");
        // return same reference -- builder
        StringBuilder stringBuilder = Joiner.on("#").useForNull("DEFAULT").appendTo(builder, stringListWithNullValue);
        assertSame(builder, stringBuilder);
        assertEquals("str#google#java#scala#Kafka#DEFAULT", stringBuilder.toString());
    }

    @Test
    public void testJoin_On_Append_To_Writer() {
        try (FileWriter writer = new FileWriter(targetFileName)) {
            Joiner.on("#").useForNull("DEFAULT").appendTo(writer, stringListWithNullValue);
            assertTrue(Files.isFile().test(new File(targetFileName)));
        } catch (IOException e) {
            fail("append to writer occur fetal error");
        }
    }

    @Test
    public void testJoinByStreamWithNullValue() {
        String res = stringListWithNullValue
                .stream()
                .filter(item -> item != null && !item.isEmpty())
                .collect(Collectors.joining("#"));
        assertEquals("google#java#scala#Kafka", res);
    }

    @Test
    public void testJoinByStreamWithDefaultValue() {
        String res = stringListWithNullValue
                .stream()
                .map(item -> Strings.isNullOrEmpty(item) ? "default" : item)
                .collect(Collectors.joining("#"));
        assertEquals("google#java#scala#Kafka#default", res);
    }

    @Test
    public void testJoinOnWithMap() {
        String res = Joiner.on("#").withKeyValueSeparator("=").join(stringMap);
        assertEquals("Hello=Guava#Java=Scala", res);
    }
}
