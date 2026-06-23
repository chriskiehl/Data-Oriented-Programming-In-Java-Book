package dop.chapter06;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class Listing6_43 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.43
   * ───────────────────────────────────────────────────────
   * Calling out that we’re trying to enforce something that might not hold
   * ───────────────────────────────────────────────────────
   */
  /**
   * Attempts to convert the charges on the invoice to USD.
   *
   * In practice, we expect this never to be thrown, but
   * that assumption relies on many systems all doing the
   * right thing at all times.
   *
   * TODO: Validate invoices on the way into the system
   *       and harden data modeling.
   *       see issue: my-company-backlog.com/items/1234567
   */
  static USD unsafeGetChargesInUSD(LineItem lineItem)
//                   ▲
//                   └──── A purposefully scary name calls attention to itself
      throws IllegalArgumentException {
//                  ▲
//                  └──── We throw an unchecked primarily for the humans reading the code,
//                        and only secondarily as an absolute last-ditch defense against
//                        unexpected system states
    if (!lineItem.getCurrency().getCurrencyCode().equals("USD")) {
      throw new UnexpectedCurrencyException();
    } else {
      return new USD(lineItem.getCharges());
    }
  }


  static USD computeTotal(List<PastDue> invoices) {
    return invoices.stream().map(PastDue::invoice)
        .flatMap(x -> x.getLineItems().stream())
        .map(Core::unsafeGetChargesInUSD)
                //          ▲
                //          └──── Refactoring to use this new method.
        .reduce(USD.zero(), USD::add);
  }








  record PastDue(Invoice invoice) {}
  interface Invoice {
    List<LineItem> getLineItems();
  }
  interface LineItem {
    Currency getCurrency();
    BigDecimal getCharges();
  }
  record USD(BigDecimal value) {
    static USD zero() { return new USD(BigDecimal.ZERO); }
    static USD add(USD x, USD y) { return new USD(x.value().add(y.value())); }
  }
  static class UnexpectedCurrencyException extends RuntimeException {}
  static class Core {
    static USD unsafeGetChargesInUSD(LineItem lineItem) { return Listing6_43.unsafeGetChargesInUSD(lineItem); }
  }

}
