package dop.chapter10;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map.Entry;

import static org.mockito.Mockito.when;

public class Listing10_16 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.16
     * ───────────────────────────────────────────────────────
     * What happens when something fails?
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void testReadFailureDuringIngestion() {
        String accountCsv = "account_id, region, segment, ...";
        String invoiceCsv = "invoice_id, invoice_num, due_date, ...";
        String disputeCsv = "account_id, invoice_id, reason, ...";

        CloudObject cloudAccountObj = Mockito.mock(CloudObject.class);
        CloudObject cloudInvoiceObj = Mockito.mock(CloudObject.class);
        CloudObject cloudDisputeObj = Mockito.mock(CloudObject.class);

        CloudStore mockStore = Mockito.mock(CloudStore.class);
        when(mockStore.getObject("accounts.csv"))           //  ┐
            .thenReturn(cloudAccountObj);                   //  │
        when(mockStore.getObject("invoices.csv"))           //  │◄── What state will we be left in
            .thenReturn(cloudInvoiceObj);                   //  │    if some paths succeed while
        when(mockStore.getObject("disputes.csv"))           //  │    others fail?
            .thenThrow(new   RuntimeException("ERROR!"));   //  ┘

        // rest of test
    }

    public Response ingest(Request request) {
        for (Entry<String, String> entry : manifest.entrySet()) {
            String type = entry.getKey();
            // The correctness of this code depends entirely on
            // the external world never misbehaving.
            var fileObject = cloudStore.getObject(entry.getValue());
            UpdatesAndInserts records = transformer.transform(fileObject);
            bulkLoader.load(records);
        }
        return __;
    }




    static Response __;












    Manifest manifest;
    CloudStore cloudStore;
    Transformer transformer;
    BulkLoader bulkLoader;

    interface Manifest {
        Iterable<Entry<String, String>> entrySet();
    }
    interface CloudStore {
        CloudObject getObject(String key);
    }
    interface CloudObject {}
    interface Transformer {
        UpdatesAndInserts transform(CloudObject object);
    }
    interface BulkLoader {
        void load(UpdatesAndInserts records);
    }
    record UpdatesAndInserts(){}
    interface Request {}
    static class Response {}
}
