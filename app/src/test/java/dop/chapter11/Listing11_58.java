package dop.chapter11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Listing11_58 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.58
     * ───────────────────────────────────────────────────────
     * Asserting the mock responds with what you told it to mock
     * ───────────────────────────────────────────────────────
     */
    @Test
    void literallyJustHereForCoverage() {
        Thing thing = mock(Thing.class);        //
        when(thing.getFoo()).thenReturn("Foo"); //  Tell the mocking library to mock something

        Service service = new Service(thing);
        assertEquals("Foo", service.someMethod());
        //      ▲
        //      └──── Then assert the thing you just mocked returns the value you just
        //            told it to mock.
    }














    interface Thing {
        String getFoo();
    }
    static class Service {
        Service(Thing thing) {}
        String someMethod() { return ""; }
    }
}
