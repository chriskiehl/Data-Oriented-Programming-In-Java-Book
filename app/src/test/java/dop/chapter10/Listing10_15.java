package dop.chapter10;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Listing10_15 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.15
     * ───────────────────────────────────────────────────────
     * The middle of our system knows about its outermost edges
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void testTransformer() {
        String csv = """
            account_id, region, segment,   ...
            test-id-1,  AMER,   Strategic,  ...
            test-id-2,  LATAM,  Enterprise, ...""";                       //  ┐
        CloudObject cloudObj = mock(CloudObject.class);                   //  │
        when(cloudObj.read()).thenReturn(                                 //  │
            new ByteArrayInputStream(csv.getBytes()));                    //  │
        AccountRepository accountRepo = mock(AccountRepository.class);    //  │ Almost everything is exactly the same!
        when(accountRepo.get(anyString()))                                //  │ The transformer needs the same setup because
            .thenReturn(Optional.of(new Account("test-id-1")));           //  │ it’s tightly coupled to knowledge about
                                                                          //  │ external systems and libraries.
        Transformer transformer = new AccountsTransformer(accountRepo);   //  │
                                                                          //  │
        UpdatesAndInserts output = transformer.transform(cloudObj);       //  │
        output.inserts.forEach(tableRecord -> {/*assertions here*/});     //  │
        output.updates.forEach(tableRecord -> {/*assertions here*/});     //  ┘
    }














    interface CloudObject {
        ByteArrayInputStream read();
    }
    interface AccountRepository {
        Optional<Account> get(String id);
    }
    record Account(String id){}
    record UpdatesAndInserts(java.util.List<TableRecord> inserts, java.util.List<TableRecord> updates){}
    record TableRecord(){}
    interface Transformer {
        UpdatesAndInserts transform(CloudObject object);
    }
    static class AccountsTransformer implements Transformer {
        AccountsTransformer(AccountRepository accountRepo) {}
        public UpdatesAndInserts transform(CloudObject object) {
            return new UpdatesAndInserts(java.util.List.of(), java.util.List.of());
        }
    }
}
