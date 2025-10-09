package dop.chapter09;

/**
 * These don't do anything. They're meant to be Spring or
 * Jakarta-like, but the specifics are kept purposefully
 * vague in the book. They're only meant to cue the reader
 * to the layer in which we're working.
 */
public @interface Annotations {

    @interface Post {}
    @interface Path {
        String value();
    }
    @interface Valid {}
}
