package dop.chapter04;

/**
 * Supplement to dop/chapter04/Listings.
 */
public sealed interface SealingExample {
    record Foo() implements SealingExample {};
    record Bar() implements SealingExample {};
    record Baz() implements SealingExample {};
}
