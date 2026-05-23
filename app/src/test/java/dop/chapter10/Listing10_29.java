package dop.chapter10;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Listing10_29 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.29
     * ───────────────────────────────────────────────────────
     * Every test is forced to start by mocking the outside world
     * ───────────────────────────────────────────────────────
     */
    @Test
    void painfulTesting() {
        String rawCSV = "(CSV here)";                          //  ┐
        CloudStore store = mock(CloudStore.class);             //  │
        CloudObject object = mock(CloudObject.class);          //  │
        when(cloudObj.read()).thenReturn(                      //  │◄── All of this preamble is needed because
            new ByteArrayInputStream(rawCSV.getBytes()));      //  │    our logic is coupled to outside concerns
        when(store.getObject(any())).thenReturn(object);       //  │
        FileRepository repo = new FileRepository(mockStore);   //  ┘

        // Only down here can we finally start our test
    }














    interface CloudStore {
        CloudObject getObject(String key);
    }
    interface CloudObject {
        ByteArrayInputStream read();
    }
    static CloudObject cloudObj;
    static CloudStore mockStore;
    static class FileRepository {
        FileRepository(CloudStore store) {}
    }
}
