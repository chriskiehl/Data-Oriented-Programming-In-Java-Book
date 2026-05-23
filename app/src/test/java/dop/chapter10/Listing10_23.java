package dop.chapter10;

public class Listing10_23 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.23
     * ───────────────────────────────────────────────────────
     * The REST API’s view of the world
     * ───────────────────────────────────────────────────────
     */
    public class RestAPI {
        // This is a subtle shift in perspective, but it underpins
        // the entire architectural philosophy. If we put ourselves in
        // the shoes of our REST API, then "our program" is just some
        // dependency it uses. 
        private YourProgramHere program;
        // Even though we think of our application as being the center
        // of the universe, to something like a Rest API, it's no different
        // from any other dependency. 
        private SerializerLib jsonSerDe;
    }














    static class YourProgramHere {}
    static class SerializerLib {}
}
