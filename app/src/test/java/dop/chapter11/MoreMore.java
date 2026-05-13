package dop.chapter11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dop.chapter11.MoreMore.Region.AMER;
import static dop.chapter11.MoreMore.Segment.ENTERPRISE;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MoreMore {
  public record Discount(AccountId forAccount){}
  public record AccountId(String value){}
  public enum Region {AMER, LA, EMEA;}
  record USD(BigDecimal value) implements Comparable<USD> {
    public static USD valueOf(double value) {
      return new USD(BigDecimal.valueOf(value));
    }
    public USD plus(double other) {
      return new USD(this.value().add(BigDecimal.valueOf(other)));
    }
    public USD minus(double other) {
      return new USD(this.value().subtract(BigDecimal.valueOf(other)));
    }
    public USD plus(USD other) {
      return new USD(this.value.add(other.value));
    }

    @Override
    public int compareTo(USD o) {
      return this.value.compareTo(o.value);
    }
  }
  public enum Segment {ENTERPRISE, STRATEGIC, EXISTING, /*...*/ }
  public record Sector(String value){}
  public record Account(AccountId id,
                        Region region,
                        USD spend,
                        Segment segment,
                        Sector sector,
                        Instant updatedOn
          /*...*/){}


  public interface NotificationService {
    void notify(Account id, String message);
  }
  static class MyService {
    private USD threshold;
    private NotificationService notifier;

    public MyService(USD threshold, NotificationService notifier) {
      this.threshold = threshold;
      this.notifier = notifier;
    }


    public Set<Discount> applyDiscounts(List<Account> accounts) {
      return this.applyDiscounts(accounts.stream().collect(toSet()));
    }

    public Set<Discount> applyDiscounts(Set<Account> accounts) {
      return accounts.stream()
              .filter(x -> !(x.region().equals(Region.AMER) && x.spend().compareTo(threshold) > 0))
              .map(x -> new Discount(x.id()))
              .collect(toSet());
    }

    public void notifyEligibleAccounts(Set<Account> accounts) {
      for (Account account : accounts) {
        if (!(account.region().equals(Region.AMER) && account.spend().compareTo(threshold) > 0)) {
          this.notifier.notify(
                account,
                "A discount is available!!!"
          );
        }
      }
    }
  }

  Account mkAcnt(Region region, USD spend) {
    return new Account(
            new AccountId("000000001"),
            region,
            spend,
            ENTERPRISE,
            new Sector("Education"),
            Instant.now()
      );
    }

  static <A> Set<A> without(Set<A> left, A item) {
    HashSet<A> copy = new HashSet<>(left);
    copy.remove(item);
    return copy;
  }

  public static <A> Set<Set<A>> powerSet(Set<A> items) {
    Set<Set<A>> allSubsets = new HashSet<>();
    allSubsets.add(new HashSet<>());

    for (A item : items) {
      Set<Set<A>> newSubsets = new HashSet<>();
      for (Set<A> subset : allSubsets) {
        Set<A> newSubset = new HashSet<>(subset);
        newSubset.add(item);
        newSubsets.add(newSubset);
      }
      allSubsets.addAll(newSubsets);
    }
    return allSubsets;
  }


  @Test
  void noDiscountForLargeAccountsInAMER4_11_5() {
    List<USD> thresholds = List.of(
            USD.valueOf(0.0),
            USD.valueOf(0.1),
            USD.valueOf(1_500.0),
            USD.valueOf(1_500_000.0)
    );

    thresholds.forEach(threshold -> {
      Account aboveLimit = mkAcnt(AMER, threshold.plus(1.0));
      Account atLimit = mkAcnt(AMER, threshold);
      Account belowLimit = mkAcnt(AMER, threshold.minus(1.0));

      Set<Account> states = Set.of(aboveLimit, atLimit, belowLimit);
      for (Set<Account> accounts : powerSet(states)) {
        Set<Account> shouldBeProcessed = without(accounts, aboveLimit);

        NotificationService notifier = mock(NotificationService.class);
        MyService service = new MyService(threshold, notifier);
        service.notifyEligibleAccounts(accounts);

        verify(notifier, never()).notify(eq(aboveLimit), anyString());
        for (Account account : shouldBeProcessed) {
          verify(notifier, times(1)).notify(eq(account), anyString());
        }
      }
    });
  }



//  @Test
//  void noDiscountForLargeAccountsInAMER() {
//    double threshold = 1_500_000.00;
//    double aboveLimit = threshold + 1;
//    double belowLimit = threshold + 1;
//
//    Set<Account> allRelevantStates = product(            #A
//            Region.values(),                                   #A
//            List.of(belowLimit, threshold, aboveLimit),        #A
//            Chapter11::mkAcnt);                                #A
//
//    Account amerAndAboveLimit = mkAcnt(AMER, aboveLimit);  #A
//    Set<Account> discountsNotAllowed = Set.of(amerAndAboveLimit); #A
//    Set<Account> discountsAllowed =                               #A
//    diff(allRelevantStates, discountsNotAllowed);               #A
//
//    Notifier notifier = Mockito.mock(Notifer.class);             #B
//    MyService service = new MyService(threshold, notifier);      #B
//    service.notifyEligibleAccounts(allRelevantStates);           #B
//
//    verify(notifier, never())
//            .notify(eq(amerAndAboveLimit.id()), anyString());
//
//    for (Account account : discountsAllowed) {
//      verify(storage).save(eq(account.id()), anyString());       #C
//    }
//  }


}
