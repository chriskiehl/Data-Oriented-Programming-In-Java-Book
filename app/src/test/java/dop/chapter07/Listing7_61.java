package dop.chapter07;

import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;

public class Listing7_61 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.61
     * ───────────────────────────────────────────────────────
     * Here’s the whole thing
     * ───────────────────────────────────────────────────────
     */
    static BinaryOperator<Optional<Policy>> addPolicies =
        withOptional(maxBy(comparing(Listings::policyImpact)
            .thenComparing(Policy::name)));

    static BinaryOperator<Optional<AuditFinding>> addFindings =
        withOptional(maxBy(comparing(Listings::findingsImpact)
            .thenComparing(AuditFinding::name)));

    static BinaryOperator<Optional<Boolean>> addStatuses =
        withOptional(Boolean::logicalOr);

    static RawData merge(RawData x, RawData y) {
        if (!x.id().equals(y.id())) {
            throw new IllegalArgumentException("...");
        }
        return new RawData(
            x.id(),
            addPolicies.apply(x.policy(), y.policy()),
            addFindings.apply(x.findings(), y.findings()),
            addStatuses.apply(x.isPremium(), y.isPremium())
        );
    }

    static List<RawData> cleanDuplicates(List<RawData> rows) {
      return List.copyOf(toMap(rows, RawData::id, Chapter07::add).values());
    }














    enum CustomerImpact { HARMS, FAVORS }

    enum Policy {
        GRACE_PERIOD, FLEXIBLE, IMMEDIATE, STRICT, MANUAL_REVIEW
    }

    enum AuditFinding {
        BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
    }

    record RawData(
        String id,
        Optional<Policy> policy,
        Optional<AuditFinding> findings,
        Optional<Boolean> isPremium
    ){}

    static class Listings {
        static CustomerImpact policyImpact(Policy policy) {
            return CustomerImpact.FAVORS;
        }

        static CustomerImpact findingsImpact(AuditFinding finding) {
            return CustomerImpact.FAVORS;
        }
    }

    static class Chapter07 {
        static RawData add(RawData x, RawData y) {
            return x;
        }
    }

    static <A> BinaryOperator<Optional<A>> withOptional(BinaryOperator<A> operator) {
        return (opt1, opt2) -> Optional.empty();
    }

    static <K,V> java.util.Map<K,V> toMap(
            List<V> items,
            java.util.function.Function<V, K> classifier,
            java.util.function.BinaryOperator<V> binop
    ) {
        return java.util.Map.of();
    }
}
