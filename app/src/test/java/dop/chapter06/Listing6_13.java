package dop.chapter06;

import java.time.LocalDate;

public class Listing6_13 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.13
     * ───────────────────────────────────────────────────────
     * Purposefully adding friction to the wrong path
     * ───────────────────────────────────────────────────────
     */
    class FeeService {
      RatingsAPI ratingsAPI;

      static LocalDate figureOutDueDate(  // ◄── Marking the method as static
        LocalDate today,
        PaymentTerms terms
      ){
        // [NOPE] this.ratingsAPI [NOPE]
        //               ▲
        //               └──── The ratingsApi is now totally out of scope. We couldn’t use
        //                     it even if we wanted to. The function’s determinism is protected.
        // ...


        return null; // (just to compile)
      }
    }















    interface RatingsAPI {}

    enum PaymentTerms {NET_30}
}
