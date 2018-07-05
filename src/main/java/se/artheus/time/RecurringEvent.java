package se.artheus.time;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RecurringEvent {

    private final LocalDate startDate;
    private final List<LocalDate> exceptions;
    private final IRecurrenceRule recurrenceRule;

    public RecurringEvent(LocalDate startDate, List<LocalDate> exceptions, IRecurrenceRule recurrenceRule) {
        this.startDate = startDate;
        this.exceptions = exceptions;
        this.recurrenceRule = recurrenceRule;
    }

    public List<LocalDate> getExceptions() {
        return exceptions;
    }

    public RecurringEvent addException(LocalDate date) {
        exceptions.add(date);
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public IRecurrenceRule getRecurrenceRule() {
        return recurrenceRule;
    }

    public Boolean occursOn(LocalDate day) {
        LocalDate startDateCopy = LocalDate.ofEpochDay(startDate.toEpochDay());

        while(startDateCopy.compareTo(day) < 0) {
            startDateCopy = startDateCopy.plus(recurrenceRule.getStepFrom(startDateCopy));
        }

        return (startDateCopy.compareTo(day) == 0);
    }

    public List<LocalDate> occurrancesBetween(LocalDate start, LocalDate end) {
        LocalDate startDateCopy = LocalDate.ofEpochDay(startDate.toEpochDay());
        List<LocalDate> results = new ArrayList<>();

        while(startDateCopy.compareTo(end) < 0) {
            if(startDateCopy.compareTo(start) > 0) {
                results.add(LocalDate.ofEpochDay(startDateCopy.toEpochDay()));
            }

            startDateCopy = startDateCopy.plus(recurrenceRule.getStepFrom(startDateCopy));
        }

        return results;
    }

    public List<LocalDate> occursOnAny(LocalDate... dates) {

        List<LocalDate> results = new ArrayList<>();

        for (LocalDate d :
                dates) {
            if(occursOn(d)) results.add(d);
        }

        return results;
    }

    public List<LocalDate> occursOnAny(List<LocalDate> dates) {
        return occursOnAny((LocalDate[]) dates.toArray());
    }

    public Stream<LocalDate> occurrenceStream() {
        return occurrenceStream(startDate);
    }

    public LocalDate firstOccuranceOnOrAfter(LocalDate date) {
        LocalDate startDateCopy = LocalDate.ofEpochDay(startDate.toEpochDay());

        while(startDateCopy.compareTo(date) < 0) {
            startDateCopy = startDateCopy.plus(recurrenceRule.getStepFrom(startDateCopy));
        }

        return startDateCopy;
    }

    public Stream<LocalDate> occurrenceStream(LocalDate from) {
        from = firstOccuranceOnOrAfter(from);
        return StreamSupport.stream(new OccurrenceSpliterator(from, this), false);
    }

    private class OccurrenceSpliterator implements Spliterator<LocalDate> {

        final LocalDate fromDate;
        final RecurringEvent recurringEvent;

        LocalDate lastDate;

        public OccurrenceSpliterator(LocalDate fromDate, RecurringEvent recurringEvent) {
            this.lastDate = this.fromDate = fromDate;
            this.recurringEvent = recurringEvent;
        }

        @Override
        public boolean tryAdvance(Consumer<? super LocalDate> consumer) {
            Boolean hasException = exceptions.contains(lastDate);
            if(!hasException) consumer.accept(lastDate);
            incrementDate();
            return !hasException;
        }

        private void incrementDate() {
            lastDate.plus(
                recurringEvent.getRecurrenceRule()
                    .getStepFrom(lastDate)
            );
        }

        @Override
        public Spliterator<LocalDate> trySplit() {
            return new OccurrenceSpliterator(lastDate, recurringEvent);
        }

        @Override
        public long estimateSize() {
            return 0; // infinite
        }

        @Override
        public int characteristics() {
            return DISTINCT | NONNULL | IMMUTABLE | ORDERED | SORTED;
        }

        @Override
        public Comparator<? super LocalDate> getComparator() {
            return new LocalDateComparator();
        }
    }

    public static class LocalDateComparator implements Comparator<LocalDate> {
        @Override
        public int compare(LocalDate a, LocalDate b) {
            int test = a.compareTo(b);
            if (test < 0) test = 1;
            if (test > 0) test = -1;
            return test;
        }
    }
}
