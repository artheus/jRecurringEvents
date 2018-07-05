package se.artheus.time;

import java.time.LocalDate;
import java.time.Period;

public interface IRecurrenceRule {
    Period getStepFrom(LocalDate day);
}
