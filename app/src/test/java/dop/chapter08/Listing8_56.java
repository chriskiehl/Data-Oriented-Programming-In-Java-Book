package dop.chapter08;

public class Listing8_56 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.56
     * ───────────────────────────────────────────────────────
     * A tree laying on its side
     * ───────────────────────────────────────────────────────
     */
    /*
    And[                                              // ◄── The root of the tree
        a=Or[
          a=Or[a=Equals[field=COUNTRY, value=US],     //  ┐
               b=Equals[field=COUNTRY, value=BE]],    //  │
          b=Equals[field=COUNTRY, value=FR]],         //  │
        b=Or[                                         //  │◄── Leaf nodes
          a=Equals[field=SEGMENT, value=public],      //  │
          b=Not[a=Equals[field=REGION, value=LATAM]]  //  ┘
      ]
    ]
    */
}
