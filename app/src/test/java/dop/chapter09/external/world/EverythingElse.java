package dop.chapter09.external.world;

import lombok.Data;

public class EverythingElse {

    @Data
    public static class Response {
        public static Response OK(Object o) {
            return new Response();
        }

        public static Response BAD_REQUEST(Object o) {
            return new Response();
        }
    }


}
