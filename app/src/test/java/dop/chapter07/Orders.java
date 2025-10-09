package dop.chapter07;

//import dop.chapter07.Sketch.PartialOrder.TotalOrder.Tie;
//import dop.chapter07.Sketch.PartialOrder.TotalOrder.Winner;
import dop.chapter07.Finalizing.AuditFinding;
import dop.chapter07.Finalizing.CustomerImpact;
import dop.chapter07.Finalizing.Policy;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static dop.chapter07.Starting.findingsImpact;
import static dop.chapter07.Starting.policyImpact;
import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class Orders {

    record RawData (
            String id,
            Policy policy,
            AuditFinding findings,
            Boolean isPremium
    ){}

    static class V1 {
        static RawData add(RawData x, RawData y) {
            if (!x.id().equals(y.id())) {
                throw new IllegalArgumentException(
                        "Hey! Only conficting rows allowed!"
                );
            }
            return new RawData(
                    x.id(),
                    add(x.policy(), y.policy()),
                    add(x.findings(), y.findings()),
                    add(x.isPremium(), y.isPremium())
                );
        }

        static Policy add(Policy x, Policy y) {
            if (policyImpact(x).equals(policyImpact(y))) {
                return x.name().compareTo(y.name()) >= 0 ? x : y;
            } else {
                return policyImpact(x).equals(CustomerImpact.FAVORS) ? x : y;
            }
        }
        static AuditFinding add(AuditFinding x, AuditFinding y) {
            if (findingsImpact(x).equals(findingsImpact(y))) {
                return x.name().compareTo(y.name()) >= 0 ? x : y;
            } else {
                return findingsImpact(x).equals(CustomerImpact.FAVORS) ? x : y;
            }
        }
        static Boolean add(Boolean x, Boolean y) {
            return x || y;
        }
    }

    @Test
    void asdfasdfasdfasdf() {
        System.out.println("A".compareTo("B"));
//        System.out.p " <= "b");
    }


    static class V2 {
        static RawData add(RawData x, RawData y) {
            if (!x.id().equals(y.id())) {
                throw new IllegalArgumentException(
                        "Hey! Only conflicting rows allowed!"
                );
            }
            return new RawData(
                    x.id(),
                    add(x.policy(), y.policy()),
                    add(x.findings(), y.findings()),
                    add(x.isPremium(), y.isPremium())
            );
        }

        static Policy add(Policy x, Policy y) {
            return add(x, y, Starting::policyImpact);
        }
        static AuditFinding add(AuditFinding x, AuditFinding y) {
            return add(x, y, Starting::findingsImpact);
        }
        static Boolean add(Boolean x, Boolean y) {
            return x || y;
        }

        static <E extends Enum<E>> E add(E x, E y, Function<E, CustomerImpact> f) {
            if (f.apply(x).equals(f.apply(y))) {
                return x.name().compareTo(y.name()) >= 0 ? x : y;
            } else {
                return f.apply(x).equals(CustomerImpact.FAVORS) ? x : y;
            }
        }
    }
}
