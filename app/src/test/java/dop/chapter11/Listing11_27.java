package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static dop.chapter11.Listing11_17.ONE;
import static dop.chapter11.Listing11_27.Region.AMER;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class Listing11_27 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.27
     * ───────────────────────────────────────────────────────
     * Driving and verifying side-effects with data
     * ───────────────────────────────────────────────────────
     */
    @Test
    void noDiscountForLargeAccountsInAMER4_11_5() {
        List<USD> thresholds = List.of(
            USD.valueOf(0.0),
            USD.valueOf(0.1),
            USD.valueOf(1_500.0),
            USD.valueOf(1_500_000.0)
        );

        thresholds.forEach(threshold -> {
            Account aboveLimit = mkAcnt(AMER, threshold.plus(ONE));
            Account atLimit = mkAcnt(AMER, threshold);
            Account belowLimit = mkAcnt(AMER, threshold.minus(ONE));

            Set<Account> states = Set.of(aboveLimit, atLimit, belowLimit);
            for (Set<Account> accounts : powerSet(states)) {
                Set<Account> shouldBeProcessed = without(accounts, aboveLimit);
//              ▲
//              └──── Note that nothing changes in how we set up our test.
//                    It’s still driven by data!
//             ┌───────────────────────────────────────────────────────────────┐
                NotificationService notifier = mock(NotificationService.class);
                MyService service = new MyService(threshold, notifier);
                service.notifyEligibleAccounts(accounts);
//             └───────────────────────────────────────────────────────────────┘
//                      ▲
//                      └──── The only change is that we use that it to
//                            trigger side effects.

                verify(notifier, never()).notify(eq(aboveLimit), anyString());
                for (Account account : shouldBeProcessed) {
                    //                                  ┌───────┐
                    verify(notifier, times(1)).notify(eq(account), anyString());
                    //                                  └───────┘
                    //                                     ▲
                    //                                     └─ Despite mocks being involved, we're
                }   //                                        still asserting against *data*
//
//
//
            }
        });
    }














    enum Region {AMER}
    record Account(AccountId id){}
    record AccountId(String value){}
    static Account mkAcnt(Region region, USD spend) { return new Account(new AccountId("")); }
    static <A> Set<Set<A>> powerSet(Set<A> items) { return Set.of(items); }
    static <A> Set<A> without(Set<A> left, A item) { return left; }
    static class MyService {
        MyService(USD threshold, NotificationService notifier) {}
        void notifyEligibleAccounts(Set<Account> accounts) {}
    }
    interface NotificationService {
        void notify(Account account, String message);
    }
}
