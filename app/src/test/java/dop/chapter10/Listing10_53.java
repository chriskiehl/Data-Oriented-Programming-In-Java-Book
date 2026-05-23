package dop.chapter10;

public class Listing10_53 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.53
     * ───────────────────────────────────────────────────────
     * Another object is forced into existence
     * ───────────────────────────────────────────────────────
     */
    class AsyncQueueIngestor {
        // As before, to this class, "our program" is just another dependency.
        // And likewise, the Ingestor doesn't know or care who or what is feeding
        // it data.
        private Ingestor ingestor;
        private CloudQueue queue;

        public void main(String... args) {
            while (this.shouldKeepRunning()) {
                QueueItem item = queue.poll(timeout);                    //
                ingestor.ingest(/*parse queue item into Manifest*/ __);  //  ◄── Its job remains the same as all
                                                                         //       others: vend data in the shape
                                                                         //       demanded by our program
            }
        }

        boolean shouldKeepRunning() {
            return true;
        }
    }













    Manifest __;
    Object timeout;
    interface Ingestor {
        void ingest(Manifest manifest);
    }
    interface CloudQueue {
        QueueItem poll(Object timeout);
    }
    interface QueueItem {}
    record Manifest(){}
}
