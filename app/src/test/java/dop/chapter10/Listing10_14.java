package dop.chapter10;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Listing10_14 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.14
     * ───────────────────────────────────────────────────────
     * Our tests have to know about the fine details of other systems
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void testIngestionHappyPath() {
        // One of the places of better architectural choices immediately
        // pays off is in tests. Right now, our boundaries are poor, and
        // so we pay for it dearly while testing. Bask ye on the below.
        // Everything knows about everything else. (Claude *loves* writing
        // this kind of code.)

        // To test our code, we have to know the details of the external CSV
        String accountCsv = """
            account_id, region, segment,   ...
            test-id-1,  AMER,   Strategic,  ...
            test-id-2,  LATAM,  Enterprise, ...""";

        CloudObject cloudObj = mock(CloudObject.class);                  //  ┐
        when(cloudObj.read()).thenReturn(                                //  │
            new ByteArrayInputStream(accountCsv.getBytes()));            //  │
                                                                         //  │◄── We also have to mock its
                                                                         //  │    low-level stream APIs
        CloudStore mockStore = mock(CloudStore.class);                   //  │
        when(mockStore.getObject("accounts.csv")).thenReturn(cloudObj);  //  │
        // invoices and disputes hidden for brevity                      //  ┘

        AccountRepository accountRepo = mock(AccountRepository.class);
        when(accountRepo.get("test-id-1")).thenReturn(
                        //        ▲
                        //        └──── To understand these mocked values, you need to understand
                        //              transformer logic that’s out of sight
                        //                │
                        //                ▼
            Optional.of(new Account("test-id-1")));
        // and it continues. We have to keep mocking the low level details
        // of our dependencies
        doNothing().when(accountRepo).load(any(UpdatesAndInserts.class));
        // invoices, disputes hidden for brevity
        IngestionFactory factory = new IngestionFactory(                           //  ┐
            Map.of("accounts", new AccountsTransformer(accountRepo) /*...*/),      //  │
            Map.of("accounts", accountRepo /*...*/)                                //  │
        );                                                                         //  │
                                                                                   //  │◄── Wiring together all the mocks
                                                                                   //  │    and classes
                                                                                   //  │
        var controller = new IngestionController(factory, mockStore);              //  ┘

//     ┌──────────────────────FINALLY THE TEST BEGINS────────────────────────┐

        // But there's even more shared knowledge
        // Here's the external JSON represenation
        Response response = controller.ingest(new Request("""
            {"accounts": "accounts.csv"}"""));

        ArgumentCaptor<UpdatesAndInserts> captor =
            ArgumentCaptor.forClass(UpdatesAndInserts.class);
        Mockito.verify(accountRepo).load(captor.capture());
        UpdatesAndInserts records = captor.getValue();
        // We're still not free. To make any assertions, we have to
        // do so in the language of the database's TableRecord
        records.inserts.forEach(tableRecord -> {/*assertions here*/});
        records.updates.forEach(tableRecord -> {/*assertions here*/});

    }














    interface CloudObject {
        ByteArrayInputStream read();
    }
    interface CloudStore {
        CloudObject getObject(String key);
    }
    interface AccountRepository {
        Optional<Account> get(String id);
        void load(UpdatesAndInserts records);
    }
    record TableRecord(){}
    record Account(String id){}
    record UpdatesAndInserts(List<Insert> inserts, List<Update> updates){}
    record Insert(String table, TableRecord record){}
    record Update(String table, TableRecord record){}
    static class AccountsTransformer {
        AccountsTransformer(AccountRepository accountRepo) {}
    }
    static class IngestionFactory {
        IngestionFactory(Map<?, ?> transformers, Map<?, ?> loaders) {}
    }
    record Request(String body){}
    static class Response {}
    static class IngestionController {
        IngestionController(IngestionFactory factory, CloudStore store) {}
        Response ingest(Request request) {
            return new Response();
        }
    }
}
