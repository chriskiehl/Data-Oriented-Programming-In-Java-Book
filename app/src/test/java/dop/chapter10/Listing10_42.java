package dop.chapter10;

import static java.lang.String.format;

public class Listing10_42 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.42
     * ───────────────────────────────────────────────────────
     * A wrapper around an external web service
     * ───────────────────────────────────────────────────────
     */
    class ApprovalService {
        private InternalHttpClient client;

        public Approval getApproval(ApprovalId id) {
//                                        ▲
//                                        └──── We give it an ID; it gives back an approval
            String url = format("internal-site.com/approvals/%s", id);
            return Approval.fromResponse(client.get(url));
        }
    }














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
