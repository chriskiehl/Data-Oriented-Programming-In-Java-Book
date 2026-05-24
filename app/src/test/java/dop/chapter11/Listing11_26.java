package dop.chapter11;

import java.util.List;

public class Listing11_26 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.26
     * ───────────────────────────────────────────────────────
     * Assume something like…
     * ───────────────────────────────────────────────────────
     */
    class MyService {
        private USD threshold;
        private NotificationService notifier;

        public void notifyEligibleAccounts(List<Account> accounts) {
            for (Account account : accounts) {
                if (/* complicated discount logic here */ __) {
                    this.notifier.notify(
                        account.id(),
                        "A discount is available!!!"
                    );
                }
            }
        }
    }













    boolean __;
    record Account(AccountId id){}
    record AccountId(String value){}
    interface NotificationService {
        void notify(AccountId id, String message);
    }
}
