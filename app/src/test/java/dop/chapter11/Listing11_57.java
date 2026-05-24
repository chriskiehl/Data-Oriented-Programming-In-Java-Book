package dop.chapter11;

import org.junit.jupiter.api.Test;

import static java.util.stream.Stream.generate;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class Listing11_57 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.57
     * ───────────────────────────────────────────────────────
     * Testing via properties and randomized data
     * ───────────────────────────────────────────────────────
     */
    @Test
    void theSameTestButMoreConvoluted() {
        // In the next chapter, we'll explore where this type of
        // formalism *does* fit. However, for this test, it's overkill.
        generate(this::randPrimary).limit(1000).forEach(inPrimary -> {  //  ┐
            generate(this::randMirror).limit(1000).forEach(inMirror-> { //  Eliding the details
                if (compareTo(inMirror, inPrimary) < 1) {
    //                  ▲
    //                  └──── We only take an action when some state is < some
    //                        other state
                    verify(mirror, times(1)).save(inPrimary);
                } else {
                    verify(mirror, never()).save(inPrimary);
                }
            });
        });
    }














    Mirror mirror;
    record Export(String id){}
    Export randPrimary() { return new Export(""); }
    Export randMirror() { return new Export(""); }
    int compareTo(Export left, Export right) { return 0; }
    interface Mirror {
        void save(Export export);
    }
}
