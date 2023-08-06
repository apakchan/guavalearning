package com.learn.guava.utilities;

import com.google.common.base.Preconditions;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PreconditionTest {

    @Test(expected = NullPointerException.class)
    public void testCheckNotNull() {
        checkNotNull(null);
    }

    private void checkNotNull(final List<String> list) {
        Preconditions.checkNotNull(list);
    }

    @Test
    public void testCheckNotNullWithMessage() {
        try {
            checkNotNullWithMessage(null);
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
            assertEquals("args can't be null", e.getMessage());
        }
    }

    private void checkNotNullWithMessage(final List<String> list) {
        Preconditions.checkNotNull(list, "args can't be null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArguments() {
        checkArguments("B");
    }

    private void checkArguments(String type) {
        Preconditions.checkNotNull(type);
        Preconditions.checkArgument(type.equals("A"));
    }

}
