package se.artheus.time;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecurringEventTest {

    @Test
    public void testOccursOn() {
        LocalDate startDate = LocalDate.of(2018, 7, 4);
        IRecurrenceRule recurrenceRule = new FixedStepRecurrenceRule(Period.ofDays(7));

        RecurringEvent evt = new RecurringEvent(startDate, new ArrayList<>(), recurrenceRule);

        Boolean occurringOn = evt.occursOn(LocalDate.of(2022, 7, 27));

        System.out.println(occurringOn ? "yes" : "no");
    }

    @Test
    public void testOccurrancesBetween() {
        LocalDate startDate = LocalDate.of(2018, 7, 4);
        IRecurrenceRule recurrenceRule = new FixedStepRecurrenceRule(Period.ofDays(7));

        RecurringEvent evt = new RecurringEvent(startDate, new ArrayList<>(), recurrenceRule);

        List<LocalDate> results = evt.occurrancesBetween(
                LocalDate.of(2018, 8, 15),
                LocalDate.of(2019, 1, 1)
        );

        results.forEach(r -> System.out.println(r.toString()));
    }

    @Test
    public void testOccursOnAny() {
        LocalDate startDate = LocalDate.of(2018, 7, 4);
        IRecurrenceRule recurrenceRule = new FixedStepRecurrenceRule(Period.ofDays(7));

        RecurringEvent evt = new RecurringEvent(startDate, new ArrayList<>(), recurrenceRule);

        List<LocalDate> results = evt.occursOnAny(
                LocalDate.of(2018, 9, 12),
                LocalDate.of(2018, 9, 27),
                LocalDate.of(2018, 10, 3),
                LocalDate.of(2018, 11, 7)
        );

        results.forEach(r -> System.out.println(r.toString()));
    }

    @Test
    public void testOccurrenceStream() {
        LocalDate startDate = LocalDate.of(2018, 7, 4);
        IRecurrenceRule recurrenceRule = new FixedStepRecurrenceRule(Period.ofDays(7));

        RecurringEvent evt = new RecurringEvent(startDate, new ArrayList<>(), recurrenceRule);

        evt.occurrenceStream().limit(300).forEach(o -> System.out.println(o.toString()));
    }

    @Test
    public void testFirstOccurrenceOnOrAfter() {
        LocalDate startDate = LocalDate.of(2018, 7, 4);
        IRecurrenceRule recurrenceRule = new FixedStepRecurrenceRule(Period.ofDays(7));

        RecurringEvent evt = new RecurringEvent(startDate, new ArrayList<>(), recurrenceRule);

        System.out.println(evt.firstOccuranceOnOrAfter(LocalDate.of(2019, 8, 19)));
        System.out.println(evt.firstOccuranceOnOrAfter(LocalDate.of(2019, 8, 21)));
        System.out.println(evt.firstOccuranceOnOrAfter(LocalDate.of(2019, 8, 22)));
    }
}