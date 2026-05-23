package dop.chapter10;

public class Listing10_25 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.25
     * ───────────────────────────────────────────────────────
     * A refresher on the CloudStore’s interfaces
     * ───────────────────────────────────────────────────────
     */
    interface ObjectStore {
        CloudObject getObject(
            String key
//                 ▲
//                 └──── How the CloudStore wants our program to model its data
        );
    }














    interface CloudObject {}
}
