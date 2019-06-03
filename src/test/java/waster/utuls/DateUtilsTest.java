package waster.utuls;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateUtilsTest {

    @Test
    public void isOverlap_whenLeft() {
        Long startA = 10L;
        Long endA = 15L;
        Long startB = 9L;
        Long endB = 14L;

        boolean result = DateUtils.isOverlap(startA, endA, startB, endB);
        assertTrue(result);
    }
    @Test
    public void isOverlap_whenRight() {
        Long startA = 10L;
        Long endA = 15L;
        Long startB = 11L;
        Long endB = 16L;

        boolean result = DateUtils.isOverlap(startA, endA, startB, endB);
        assertTrue(result);
    }
    @Test
    public void isOverlap_whenBoth() {
        Long startA = 10L;
        Long endA = 15L;
        Long startB = 11L;
        Long endB = 14L;

        boolean result = DateUtils.isOverlap(startA, endA, startB, endB);
        assertTrue(result);
    }
    @Test
    public void isOverlap_whenNot() {
        Long startA = 10L;
        Long endA = 15L;
        Long startB = 1L;
        Long endB = 9L;

        boolean result = DateUtils.isOverlap(startA, endA, startB, endB);
        assertFalse(result);
    }
}