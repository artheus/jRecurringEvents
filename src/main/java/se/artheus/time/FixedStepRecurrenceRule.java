package se.artheus.time;

import java.time.LocalDate;
import java.time.Period;

public class FixedStepRecurrenceRule implements IRecurrenceRule {
    private final Period step;

    public FixedStepRecurrenceRule(Period step) {
        this.step = step;
    }

    @Override
    public Period getStepFrom(LocalDate day) {
        return step;
    }
}
