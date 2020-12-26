package org.restaurant.voting;

import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMatcher<T> {
    private final Class<T> clazz;
    private final BiConsumer<T, T> assertion;
    private final BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion;

    private TestMatcher(Class<T> clazz, BiConsumer<T, T> assertion, BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion) {
        this.clazz = clazz;
        this.assertion = assertion;
        this.iterableAssertion = iterableAssertion;
    }

    public static <T> TestMatcher<T> usingIgnoringFieldsComparator(Class<T> clazz, String... fieldsToIgnore) {
        return new TestMatcher<>(clazz,
                (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields(fieldsToIgnore).isEqualTo(e),
                (a, e) -> assertThat(a).usingElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(e));
    }

    public void assertMatch(T actual, T expected) {
        assertion.accept(actual, expected);
    }

    @SafeVarargs
    public final void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, List.of(expected));
    }

    private void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        iterableAssertion.accept(actual, expected);
    }
}
