package dop.chapter11_tmp;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Stream.generate;
import static org.mockito.Mockito.*;

public class Tmp {



    @Test
    void theSameTestButMoreConvoluted() {
        generate(this::randomInPrimary).limit(10).forEach(inPrimary -> {
            generate(this::randomInMirror).limit(10).forEach(inMirror-> {
                if (compareTo(inMirror, inPrimary) < 1) {
                    verify(mirror, times(1)).save(inPrimary);
                } else {
                    verify(mirror, never()).save(inPrimary);
                }
            });
        });
    }




    record Export(Instant createdOn){}
    enum MirrorIs {EMPTY, STALE, UP_TO_DATE;}
    record State(MirrorIs state,
                 Export primaryHas,
                 Optional<Export> mirrorHas) {
    }


    Random rand = new Random();

    Optional<Export> randomInMirror() {
        return rand.nextBoolean()
                ? Optional.empty()
                : Optional.of(new Export(Instant.ofEpochMilli((long) Math.pow(10, 3))));
    }

    Export randomInPrimary() {
        return new Export(Instant.ofEpochMilli((long) Math.pow(10, 3)));
    }


    int compareTo(Optional<Export> a, Export b) {
        return a.map(export -> export.createdOn().compareTo(b.createdOn)).orElse(-1);
    }

    interface Primary {}
    interface Mirror {
        void save(Export export);
    }

    Mirror mirror;
    Primary primary;




}
