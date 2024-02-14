package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeTest {
    private Time time;
    private Time time1;
    private Time time2;
    private Time time3;
    private Time time4;
    private Time time5;
    private Time time6;
    private Time time7;
    private Time time8;
    private Time time9;
    private Time time10;
    private Time time11;
    private Time time12;
    private Time time13;
    private Time time14;
    private Time time15;
    private Time time16;
    private Time time17;
    private Time time18;
    private Time time19;

    @BeforeEach
    void runBefore() {
        time = new Time(11, 18, 14, 9);
        time1 = new Time(8,9,9,19);
        time2 = new Time(11,4,12,40);
        time3 = new Time(11,4,11,40);
        time4 = new Time(11,4,11,10);
        time5 = new Time(10,14,11,1);
        time6 = new Time(10,24,14,16);
        time7 = new Time(14,8,14,16);

        time8 = new Time(10,5,12,40);
        time9 = new Time(11,19,12,40);
        time10 = new Time(11,4,14,10);
        time11 = new Time(13,14,15,10);

        time12 = new Time(13,14,14,1);
        time13 = new Time(13, 14, 14, 3);
        time14 = new Time(11,24,15,1);
        time15 = new Time(13,14,13,19);
        time16 = new Time(11,4,14,8);
        time17 = new Time(11,19,14,8);
        time18 = new Time(11,10,13,8);
        time19 = new Time(12,19,14,10);
    }

    @Test
    void dateConstructorTest() {
        assertEquals(11, time.getStartHour());
        assertEquals(18, time.getStartMin());
        assertEquals(14, time.getEndHour());
        assertEquals(9, time.getEndMin());
    }

    @Test
    void getDurationTest() {
        assertEquals(171, time.countDuration());
    }

    // the time does not overlap at all
    @Test
    void overlapTimeTest1() {
        boolean test = time.isOverlapTime(time1,true);
        assertFalse(test);
        test = time.isOverlapTime(time1, false);
        assertFalse(test);
    }

    // startHour of time = startHour of time2 and endHour of time < endHour of time2
    @Test
    void overlapTimeTest2() {
        boolean test = time.isOverlapTime(time2,true);
        assertTrue(test);
    }

    // startHour of time = startHour of time3 and endMinute of time < endMinute of time3
    @Test
    void overlapTimeTest3() {
        boolean test = time.isOverlapTime(time3,true);
        assertTrue(test);
    }

    // startHour of time = startHour of time4 but endMin of time > endMin of time4
    // while endHour of time = endHour of time4
    @Test
    void overlapTimeTest4() {
        boolean test = time.isOverlapTime(time4,true);
        assertFalse(test);
    }

    // the starting hour of time is larger than starting hour of time5 but
    // endMin is less than the endMin of time5.
    @Test
    void overlapTimeTest5() {
        boolean test = time.isOverlapTime(time5,true);
        assertFalse(test);
    }

    // the starting hour of time is larger than starting hour of time6
    // and the ending hour time is equals to the ending hour of time6.
    @Test
    void overlapTimeTest6() {
        boolean test = time.isOverlapTime(time6,true);
        assertTrue(test);
    }

    // the ending hour of time is equal to the ending hour of time7
    // and the ending hour time is equals to the starting hour of time7.
    @Test
    void overlapTimeTest7() {
        boolean test = time.isOverlapTime(time7,false);
        assertTrue(test);
    }

    // the starting time overlap with the starting hour strictly lies in between the startHour and endHour of
    // the time8. (startHour < testing Hour (time variable) < endHour)
    @Test
    void overlapTimeTest8() {
        boolean test = time.isOverlapTime(time8,true);
        assertTrue(test);
        test = time.isOverlapTime(time8, false);
        assertFalse(test);
    }
    // the startHour of time variable is equal to the startHour of time9 but startMinute is less than
    // the startMinute of time9.
    @Test
    void overlapTimeTest9() {
        boolean test = time.isOverlapTime(time9,true);
        assertFalse(test);
        test = time.isOverlapTime(time9, false);
        assertFalse(test);
    }

    // both starting time and ending is overlapping with the time of time10
    @Test
    void overlapTimeTest10() {
        boolean test = time.isOverlapTime(time10,true);
        assertTrue(test);
        test = time.isOverlapTime(time10,false);
        assertTrue(test);
    }

    // the ending time overlap but the starting time doesn't
    @Test
    void overlapTimeTest11() {
        boolean test = time.isOverlapTime(time11,true);
        assertFalse(test);
        test = time.isOverlapTime(time11,false);
        assertTrue(test);
    }

    // the starting time of test is later than that of time8
    @Test
    void completelyOverlapTest1() {
        boolean test = time.isCompletelyOverlap(time8);
        assertFalse(test);
    }

    // the time completely overlap time9 with starting hour of time = starting hour of time9.
    @Test
    void completelyOverlapTest2() {
        boolean test = time.isCompletelyOverlap(time9);
        assertTrue(test);
    }

    // the starting time of test is later than that of time10
    // and the ending time of test is earlier than that of time10.
    @Test
    void completelyOverlapTest3() {
        boolean test = time.isCompletelyOverlap(time10);
        assertFalse(test);
    }

    // the ending time of test is earlier than that of time11
    @Test
    void completelyOverlapTest4() {
        boolean test = time.isCompletelyOverlap(time11);
        assertFalse(test);
    }

    // the time completely overlap time12 with starting hour of time < starting hour of time12.
    @Test
    void completelyOverlapTest5() {
        boolean test = time.isCompletelyOverlap(time12);
        assertTrue(test);
    }

    // the time completely overlap time13 with ending hour of time = ending hour of time13.
    @Test
    void completelyOverlapTest6() {
        boolean test = time.isCompletelyOverlap(time13);
        assertTrue(test);
    }

    // startHour of time = startHour of time14 but endHour of time < endHour of time14
    @Test
    void completelyOverlapTest7() {
        boolean test = time.isCompletelyOverlap(time14);
        assertFalse(test);
    }

    // the time completely overlap time15 with its hours strictly in between
    @Test
    void completelyOverlapTest8() {
        boolean test = time.isCompletelyOverlap(time15);
        assertTrue(test);
    }

    // startHour of time = startHour of time16 but startMin of time > startMin of time16
    @Test
    void completelyOverlapTest9() {
        boolean test = time.isCompletelyOverlap(time16);
        assertFalse(test);
    }

    // starting and ending hour of time equals that of time17, with the minutes satisfy the restriction.
    @Test
    void completelyOverlapTest10() {
        boolean test = time.isCompletelyOverlap(time17);
        assertTrue(test);
    }

    // startHour of time equals that of time18 but startMin of time > startMin of time18
    @Test
    void completelyOverlapTest11() {
        boolean test = time.isCompletelyOverlap(time18);
        assertFalse(test);
    }

    // endHour of time equals that of time19 but endMin of time < endMin of time19.
    @Test
    void completelyOverlapTest12() {
        boolean test = time.isCompletelyOverlap(time19);
        assertFalse(test);
    }

    @Test
    void adjustEndTimeTest() {
        time.adjustEndTime(109);
        assertEquals(11, time.getStartHour());
        assertEquals(18, time.getStartMin());
        assertEquals(13, time.getEndHour());
        assertEquals(7, time.getEndMin());
    }
}