package dop.chapter11;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Set;

public class ExampleTest {

    public record Account(){}
    public record Result(){}
    public static class MyService {
        private double threshold;

        public MyService(double threshold) {
            this.threshold = threshold;
        }

        public Set<Result> doSomething() {
            return Set.of(new Result());
        }
    }



    @BeforeEach
    public void setup() {
        this.myService = new MyService(1_500_000.00);
    }

    @AfterEach
    public void teardown() {

    }

    private MyService myService;

    @Mock
    Account accountB;

    @Mock
    Account accountC;

    @Test
    void noDiscountForLargeAccounts() {
//        int expected = 2;  #A
//        Set<Thing> items = Set.of(accountA(), accountB(), accountC());
//        Set<Result> result = this.service.applyDiscounts(items);
//        assertEquals(expected, result.size());  #B
    }





}
