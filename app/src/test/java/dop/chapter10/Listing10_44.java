package dop.chapter10;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

import static java.lang.String.format;

public class Listing10_44 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.44
     * ───────────────────────────────────────────────────────
     * Building functionality that matches the desired semantics
     * ───────────────────────────────────────────────────────
     */
    class ApprovalService {
        private ExecutorService executor;
        private InternalHttpClient client;

        public Approval getApproval(ApprovalId id) {
            String url = format("internal-site.com/approvals/%s", id);
            return Approval.fromResponse(client.get(url));
        }

        // As simple and "obvious" and *useful* as this is, it's seldom done!
        public Approval getApprovals(Collection<ApprovalId> ids) {
            // This lets us optimize how we get data so that
            // consumers can focus on what they do with it
            // fetch all the approvals in parallel

            return __;
        }

    }













    static Approval __;
    record ApprovalId(String value){}
    record Approval(String value) {
        static Approval fromResponse(String response) {
            return new Approval(response);
        }
    }
    interface InternalHttpClient {
        String get(String url);
    }
}
