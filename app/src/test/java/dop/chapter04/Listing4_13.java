package dop.chapter04;

import java.time.Instant;
import java.util.List;

public class Listing4_13 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 4.13
     * ───────────────────────────────────────────────────────
     * The finished data model
     * ───────────────────────────────────────────────────────
     */
    record Step(String name){}

    record Template(String name, List<Step> steps){}

    record Instance(
        String name,
        Instant date,
        Template template
    ){}

    record Status(
        Template template,
        Step step,
        boolean isCompleted
    ){}
}
