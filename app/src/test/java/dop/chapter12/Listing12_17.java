package dop.chapter12;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static java.lang.String.format;

public class Listing12_17 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.17
     * ───────────────────────────────────────────────────────
     * Supporting code
     * ───────────────────────────────────────────────────────
     */
    @AllArgsConstructor
    class InfraTooling {
        private CloudSDK cloudSDK;
        private Namespace namespace;

        Queue createQueue() {
            return cloudSDK.createQueue(randomName(namespace));
        }
        Bucket createBucket() {
            return cloudSDK.createBucket(randomName(namespace));
        }

        void cleanup(Function<Namespace, String> prefix) {
            deleteQueues(prefix);
            deleteBuckets(prefix);
            deleteWhatever(prefix);
        }

        void deleteQueues(Function<Namespace, String> strat) {
            for (String name : cloudSDK.listQueues()) {
                if (name.startsWith(strat.apply(namespace))) {
                    cloudSDK.deleteQueue(name);
                }
            }
        }
        void deleteBuckets(Function<Namespace, String> strat) {}
        void deleteWhatever(Function<Namespace, String> strat) {}
    }

    @Test
    void myCoolIntegrationTest() {
        String owner = myDependencyInjector.environment();
        Namespace namespace = new Namespace(
                owner,
                "chapter12",
                "myCoolIntegrationTest");
        InfraTooling infra = new InfraTooling(cloudSDK, namespace);

        Queue inputQueue = infra.createQueue();
        Queue outputQueue = infra.createQueue();
        Bucket bucket = infra.createBucket();

        // test code here

        infra.cleanup(Scope::thisTest);
    }















    CloudSDK cloudSDK;
    MyDependencyInjector myDependencyInjector;
    record Namespace(String owner, String suite, String testName){}
    class Scope {
        static String thisTest(Namespace ns) { return null; }
    }
    String randomName(Namespace namespace) {
        return format("%s-%s", Scope.thisTest(namespace), UUID.randomUUID().toString().substring(0, 8));
    }
    interface Queue {}
    interface Bucket {}
    interface CloudSDK {
        Queue createQueue(String name);
        Bucket createBucket(String name);
        List<String> listQueues();
        void deleteQueue(String name);
    }
    interface MyDependencyInjector {
        String environment();
    }
}

