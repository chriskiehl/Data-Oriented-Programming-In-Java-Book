package dop.chapter10;

import java.util.Map;

public class Listing10_12 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.12
     * ───────────────────────────────────────────────────────
     * The Factory Pattern
     * ───────────────────────────────────────────────────────
     */
    class IngestionFactory {
        private Map<String, Transformer> transformers;
        private Map<String, BulkLoader> loaders;

        // constructor elided

        public Transformer getTransformer(String type) {
            return getOrThrow(this.transformers, type);
        }
        public BulkLoader getLoader(String type) {
            return getOrThrow(this.loaders, type);
        }

        public static <A> A getOrThrow(Map<String, A> m, String key) {
            if (!m.containsKey(key)) {
                throw new IllegalArgumentException("Unknown Strategy!");
            }
            return m.get(key);
        }
    }














    interface Transformer {}
    interface BulkLoader {}
}
