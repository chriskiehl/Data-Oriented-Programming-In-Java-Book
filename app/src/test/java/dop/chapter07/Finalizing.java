package dop.chapter07;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.stream.Collectors.toMap;

public class Finalizing {
    public enum Policy {
        GRACE_PERIOD,
        FLEXIBLE,
        IMMEDIATE,
        STRICT,
        MANUAL_REVIEW
    }

    public enum AuditFinding {
        BILLING_ERROR,
        OUT_OF_COMPLIANCE,
        INACCURATE,
        NO_ISSUE
    }

    public enum SpecialAccount { Y, N }

    enum CustomerImpact {HARMS, FAVORS}

    static <A> Comparator<Optional<A>> compareOpt(Comparator<A> comparator) {
        return (o1, o2) -> {
            if (o1.isPresent() && o2.isPresent()) {
                return comparator.compare(o1.get(), o2.get());
            } else if (o1.isEmpty() && o2.isEmpty()) {
                return 0;
            } else {
                return o1.isPresent() ? 1 : -1;
            }
        };
    }


    static CustomerImpact policyImpact(Policy stage) {
        return switch (stage) {
            case GRACE_PERIOD, FLEXIBLE, MANUAL_REVIEW -> CustomerImpact.FAVORS;
            default -> CustomerImpact.HARMS;
        };
    }
    static CustomerImpact findingsImpact(AuditFinding stage) {
        return switch (stage) {
            case NO_ISSUE, INACCURATE -> CustomerImpact.FAVORS;
            default -> CustomerImpact.HARMS;
        };
    }

    record RawData (
        String id,
        Optional<Policy> policy,
        Optional<AuditFinding> findings,
        Optional<Boolean> isPremium
    ) implements Comparable<RawData> {
        public RawData merge(RawData other) {
            return new RawData(
                    this.id(),
                    combinePolicies.apply(this.policy(), other.policy()),
                    mergeFindings.apply(this.findings(), other.findings()),
                    mergeStatus.apply(this.isPremium(), other.isPremium())
            );
        }

        @Override
        public int compareTo(RawData o) {
            Comparator<Optional<Policy>> p = compareOpt(comparing(Finalizing::policyImpact).thenComparing(Policy::name));
            Comparator<Optional<AuditFinding>> a = compareOpt(comparing(Finalizing::findingsImpact).thenComparing(AuditFinding::name));
            Comparator<Optional<Boolean>> b = compareOpt(Boolean::compareTo);
            return p.compare(this.policy, o.policy) == 0
                    ? a.compare(this.findings, o.findings) == 0
                        ? b.compare(this.isPremium, o.isPremium)
                    : a.compare(this.findings, o.findings)
                    : p.compare(this.policy, o.policy);
        }
    }

    Boolean add(Boolean a, Boolean b) {
        return Boolean.compare(a, b) > 0 ? b : a;
    }


    static BinaryOperator<Optional<Policy>> combinePolicies = maxBy(compareOpt(comparing(Finalizing::policyImpact).thenComparing(Policy::name)));
    static BinaryOperator<Optional<AuditFinding>> mergeFindings = maxBy(compareOpt(comparing(Finalizing::findingsImpact).thenComparing(AuditFinding::name)));
    static BinaryOperator<Optional<Boolean>> mergeStatus = maxBy(compareOpt(Boolean::compareTo));
    static BinaryOperator<Boolean> orr = Boolean::logicalOr;

    static List<RawData> combineDuplicates(List<RawData> data) {
        return List.copyOf(data.stream()
                .collect(toMap(RawData::id, Function.identity(), RawData::merge))
                .values());
    }

}
