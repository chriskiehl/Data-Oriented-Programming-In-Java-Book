package dop.chapter02;

import dop.chapter02.Listings.L2_2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Examples {

    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.1
     * ───────────────────────────────────────────────────────
     * Demoing the relationship between variables and the values
     * the hold over time.
     * ───────────────────────────────────────────────────────
     */
    @Test
    void listing_2_1() {
        double xPosition = 4.2;

        xPosition++;                        // The values assigned to xPosition
                                            // keep changing, but the variable
        xPosition = xPosition * xPosition;  // gives them a continuous identity

        System.out.println(xPosition);
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.1
     * ───────────────────────────────────────────────────────
     * Demoing the relationship between variables and the values
     * the hold over time.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_2() {
        L2_2.Person person = new L2_2.Person(
            "Chris",
            36,
            "brown"
        );

        person.setAge(37);
        person.setHairColor("less-brown");
        person.setAge(38);
        person.setHairColor("wait -- is that a grey..?");

        Assertions.assertTrue(person == person);
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.3
     * ───────────────────────────────────────────────────────
     * Values have no identity. They cannot be changed.
     * They just "are."
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_3() {
        //            ┌─────── Adding 1 doesn't mutate the number 4
        //            ▼        number 4
        //        4 + 1
        //        ▲
        //        └────── The number 4 just "is." It will remain
        //                no matter what operations we perform "on" it
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.4
     * ───────────────────────────────────────────────────────
     * Trying to use identity on an object that's modeling a
     * value can lead to very strange results!
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_4() {
        Integer x = Integer.valueOf(128);  // We’re creating different objects, which
        Integer y = Integer.valueOf(128);  // means unique object identities, right?

        System.out.println(x == y);
        // So far it seems that way. This will show false .
        // But, hang on...
        x = Integer.valueOf(127);  // What happens if we pick a slightly different
        y = Integer.valueOf(127);  // number...?

        System.out.println(x == y);  // What the heck?! They're now different!
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.5
     * ───────────────────────────────────────────────────────
     * Value objects should be compared exclusively by their
     * state. The identities will vary, but that's OK.
     * They're irrelevant to values!
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_5() {
        Integer a = Integer.valueOf(3042);
        Integer b = Integer.valueOf(3042);
        // a and b are the same value, even though they’re
        // assigned to different objects, and different
        // variables, and have unique object identities.
        System.out.println(a.equals(b)); // true
        System.out.println(a == b);      // false

    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.6
     * ───────────────────────────────────────────────────────
     * All of these are exactly the same values. Their identities
     * don't matter. Any one of these can be used in place of any
     * other -- because they're the same values.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_6() {
        Integer a = Integer.valueOf(1234);
        Integer b = Integer.valueOf(1234);
        Integer c = Integer.valueOf(1234);

        //  We don't address it in the book, but
        //  values follow the rules for an Equivalent Relation.
        Assertions.assertTrue(  //
            a.equals(a)    //  ◄───  They're reflexive (equal to themselves)
                    && a.equals(b) //  ◄───┐ Symmetric (a == b && b == a)
                    && b.equals(a) //  ◄───┘
                    && a.equals(c) //
                    && Integer.valueOf(1234).equals(a)
                    && Integer.valueOf(1234).equals(b)
                    && a.equals(Integer.valueOf(1234))
                    && Integer.valueOf(1234).equals(Integer.valueOf(1234))
                    && a.equals(b) && a.equals(c)); // ◄───  and transitive
                                                    //       if a == b, and b == c, then a == c
    }


}
