package io.github.brittLiban.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VideoProcessorAppTest {
    @Test // parsing a generic valid number string
    void parsesValidNumber() {
        assertEquals(123, Integer.parseInt("123"));
    }

    @Test // parsing a positive integer
    void parsesPositiveInteger() {
        assertEquals(42, Integer.parseInt("42"));
    }

    @Test // parsing zero
    void parsesZero() {
        assertEquals(0, Integer.parseInt("0"));
    }

    @Test // parsing a negative integer
    void parsesNegativeInteger() {
        assertEquals(-17, Integer.parseInt("-17"));
    }

    @Test // parsing with a leading plus sign
    void parsesWithPlusSign() {
        assertEquals(123, Integer.parseInt("+123"));
    }

    @Test // parsing with leading zeros
    void parsesLeadingZeros() {
        assertEquals(7, Integer.parseInt("007"));
    }

    @Test // parsing the maximum int value
    void parsesMaxInteger() {
        assertEquals(Integer.MAX_VALUE, Integer.parseInt("2147483647"));
    }

    @Test // parsing the minimum int value
    void parsesMinInteger() {
        assertEquals(Integer.MIN_VALUE, Integer.parseInt("-2147483648"));
    }

    @Test // non-numeric input throws NumberFormatException
    void nonNumericThrowsException() {
        assertThrows(NumberFormatException.class, () -> Integer.parseInt("abc123"));
    }

    @Test // decimal string throws NumberFormatException
    void invalidNumberThrowsException() {
        assertThrows(NumberFormatException.class, () -> Integer.parseInt("12.3"));
    }

    @Test // empty string throws NumberFormatException
    void emptyStringThrowsException() {
        assertThrows(NumberFormatException.class, () -> Integer.parseInt(""));
    }

    @Test // null input throws NumberFormatException
    void nullInputThrowsException() {
        assertThrows(NumberFormatException.class, () -> Integer.parseInt(null));
    }
}
