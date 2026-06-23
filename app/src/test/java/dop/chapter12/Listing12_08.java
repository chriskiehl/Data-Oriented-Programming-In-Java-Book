package dop.chapter12;

import java.util.List;

public class Listing12_08 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.8
     * ───────────────────────────────────────────────────────
     * Encoding our business rules into the database's expression language
     * ───────────────────────────────────────────────────────
     */
    class ConditionFailedException extends RuntimeException {}
//        ^ This will be thrown if we attempt to save anything that fails
//          the ConditionExpression below

    interface SomeDistributedKeyValueStore {
        void putItem(Item item, ConditionExpression expression)
//           ^ In our imagined database, constraints are written in a custom Expression DSL
            throws ConditionFailedException;
    }

    class MyRepository {
        private SomeDistributedKeyValueStore db;

        public void save(Job job) {
            ConditionExpression condition = or(
                // Rule 1: all new writes are accepted regardless of state
                not(exists(attr("id"))),
                // However, each state is immutable. They cannot be updated
                // with new information; they can only be transitioned to
                // new states.
                and(
                    // Rule 2: The same state cannot be updated in place.
                    eq(attr("state"), job.getClass().getSimpleName()),
                    // Rule 3: they cannot transition out of a terminal state.
                    not(in(attr("state"), List.of("Completed", "Failed")))
                )
            );
            db.putItem(serialize(job), condition);
        }
    }















    record Item(){}
    record ConditionExpression(){}
    interface Job {}
    ConditionExpression or(ConditionExpression... expressions) { return null; }
    ConditionExpression and(ConditionExpression... expressions) { return null; }
    ConditionExpression not(ConditionExpression expression) { return null; }
    ConditionExpression exists(Attribute attribute) { return null; }
    ConditionExpression eq(Attribute attribute, String value) { return null; }
    ConditionExpression in(Attribute attribute, List<String> values) { return null; }
    Attribute attr(String name) { return null; }
    Item serialize(Job job) { return null; }
    record Attribute(String name){}
}

