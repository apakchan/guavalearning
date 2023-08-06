package com.learn.guava.utilities;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableBiMap;
import org.junit.Test;

import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testSplitOnSplit() {
        List<String> stringList = Splitter.on("|").splitToList("hello|world");
        assertNotNull(stringList);
        assertEquals(Arrays.asList("hello", "world"), stringList);
    }

    @Test
    public void testSplitObSplit_OmitEmpty() {
        // omit empty str
        List<String> stringList = Splitter.on("|").omitEmptyStrings().splitToList("hello|world||||||||");
        assertNotNull(stringList);
        assertEquals(Arrays.asList("hello", "world"), stringList);
    }

    @Test
    public void testSplitObSplit_OmitEmpty_TrimResult() {
        // omit empty str
        // trim every str head or tail empty str
        List<String> stringList = Splitter.on("|").omitEmptyStrings().trimResults().splitToList("hello | world| |||||||");
        assertNotNull(stringList);
        assertEquals(Arrays.asList("hello", "world"), stringList);
    }

    @Test
    public void testSplitFixLength() {
        Iterable<String> result = Splitter.fixedLength(4).split("aaaabbbbccccdddd");
        assertNotNull(result);
        List<String> stringList = new ArrayList<>();
        result.forEach(stringList::add);
        assertEquals(Arrays.asList("aaaa", "bbbb", "cccc", "dddd"), stringList);
    }

    @Test
    public void testSplitOnSplit_Limit() {
        // limit length of list
        List<String> stringList = Splitter.on("#").limit(3).splitToList("hello#world#java#guava#google");
        assertNotNull(stringList);
        assertEquals(Arrays.asList("hello", "world", "java#guava#google"), stringList);
    }

    @Test
    public void testSplitOnPatternString() {
        List<String> stringList = Splitter.onPattern("\\|").omitEmptyStrings().trimResults().splitToList("hello | world| |||||||");
        assertEquals(Arrays.asList("hello", "world"), stringList);
    }

    @Test
    public void testSplitOnPattern() {
        List<String> stringList = Splitter.on(Pattern.compile("\\|")).omitEmptyStrings().trimResults().splitToList("hello | world| |||||||");
        assertEquals(Arrays.asList("hello", "world"), stringList);
    }

    @Test
    public void testSplitOnSplitToMap() {
        Map<String, String> splitMap = Splitter.on(Pattern.compile("\\|"))
                .omitEmptyStrings().trimResults()
                .withKeyValueSeparator("=")
                .split("hello=HELLO |world=WORLD| |||||||");
        assertEquals(ImmutableBiMap.of("hello", "HELLO", "world", "WORLD"), splitMap);
    }

}
