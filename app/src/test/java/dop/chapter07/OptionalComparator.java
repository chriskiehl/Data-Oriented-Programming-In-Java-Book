package dop.chapter07;

import java.util.Comparator;
import java.util.Optional;

public class OptionalComparator<T> implements Comparator<Optional<T>> {
    private final Comparator<T> comparator;

    public OptionalComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(Optional<T> o1, Optional<T> o2) {
        return (o1.isPresent() && o2.isPresent())
            ? this.comparator.compare(o1.get(), o2.get())
            : o1.isPresent() ? 1 : 0;
    }
}
