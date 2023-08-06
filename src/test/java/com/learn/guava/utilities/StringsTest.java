package com.learn.guava.utilities;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class StringsTest {

    @Test
    public void testStringsEmptyToNull() {
        assertNull(Strings.emptyToNull(""));
    }

    @Test
    public void testStringsNullToEmpty() {
        assertEquals("", Strings.nullToEmpty(null));
    }

    @Test
    public void testStringsNullToDefault() {
        assertEquals("hello", Strings.nullToEmpty("hello"));
    }

    @Test
    public void testStringsCommonPrefix() {
        assertEquals("hell", Strings.commonPrefix("hello", "hell"));
        assertEquals("", Strings.commonPrefix("shut", "apple"));
        assertEquals("p", Strings.commonSuffix("app", "loop"));
    }

    @Test
    public void testStringsRepeat() {
        assertEquals("aaa", Strings.repeat("a", 3));
    }

    @Test
    public void testStringsPad() {
        assertEquals("alexaa", Strings.padEnd("alex", 6, 'a'));
        assertEquals("aaalex", Strings.padStart("alex", 6, 'a'));
    }

    @Test
    public void testCharsets() {
        assertEquals(Charset.forName("utf-8"), Charsets.UTF_8);
    }

    @Test
    public void testCharMatcherMatch() {
        assertTrue(CharMatcher.javaDigit().matches('5'));
        assertFalse(CharMatcher.javaDigit().matches('x'));
    }

    @Test
    public void testCharMatcherCountIn() {
        // find num of target char in string seq
        assertEquals(1, CharMatcher.is('A').countIn("Alex is a person"));
    }

    @Test
    public void testCharMatcherReplaceWhiteSpace() {
        // replace white space to target char
        assertEquals("*hello*guava*", CharMatcher.whitespace().collapseFrom("        hello    guava        ", '*'));
        assertEquals("helloworld", CharMatcher.javaDigit().or(CharMatcher.whitespace().or(CharMatcher.anyOf("."))).removeFrom("hello 23456.123 world"));
    }

}
