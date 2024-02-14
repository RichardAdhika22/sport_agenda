package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventDateTest {
    private EventDate eventDateTest;

    @BeforeEach
    void runBefore() {
        eventDateTest = new EventDate(17, 8, 2005);
    }

    @Test
    void testDateConstructor() {
        assertEquals(17, eventDateTest.getDay());
        assertEquals(8, eventDateTest.getMonth());
        assertEquals(2005, eventDateTest.getYear());
    }

    // Test when the day given < day of dateTest, month given < month of dateTest,
    // given year < year of dateYear
    @Test
    void dayDifferenceTest1() {
        int test = eventDateTest.dayDifference(5, 4, 2004);
        assertEquals(10412, test);
    }

    // Test when the day given = day of dateTest, month given = month of dateTest,
    // given year = year of dateYear
    @Test
    void dayDifferenceTest2() {
        int test = eventDateTest.dayDifference(17, 8, 2005);
        assertEquals(0, test);
    }

    // Test when the day given > day of dateTest, month given < month of dateTest,
    // given year <= year of dateYear
    @Test
    void dayDifferenceTest3() {
        // dayPerMonth = 30
        int test = eventDateTest.dayDifference(25, 6, 2005);
        assertEquals(122, test);

        //dayPerMonth = 31
        test = eventDateTest.dayDifference(25, 3, 2004);
        assertEquals(10423, test);

        //dayPerMonth = 28
        test = eventDateTest.dayDifference(25, 2, 2005);
        assertEquals(520, test);
    }

    // Test when the day given < day of dateTest, month given > month of dateTest,
    // given year < year of dateYear
    @Test
    void dayDifferenceTest4() {
        int test = eventDateTest.dayDifference(3, 11, 2002);
        assertEquals(20914, test);
    }

    // Test when the day given > day of dateTest, month given > month of dateTest,
    // given year < year of dateYear
    @Test
    void dayDifferenceTest5() {
        int test = eventDateTest.dayDifference(19, 11, 2002);
        assertEquals(20828, test);
    }

}