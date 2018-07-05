package se.artheus.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

public class DayOfWeekRecurrenceRule implements IRecurrenceRule {
    private final DayOfWeek dayOfWeek;

    public DayOfWeekRecurrenceRule(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public Period getStepFrom(LocalDate date) {
        return Period.between(date, calcNextDayOfWeek(date));
    }

    private LocalDate calcNextDayOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.next(dayOfWeek));
    }
}
