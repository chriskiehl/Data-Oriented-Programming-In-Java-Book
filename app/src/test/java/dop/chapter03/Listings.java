package dop.chapter03;

public class Listings {


    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.1
     * ───────────────────────────────────────────────────────
     * We begin where all good software books begin: woodworking.
     * The best part of woodworking is the last step of applying the
     * finish. One of my favorite finishes is a type of drying oil
     * called Tung oil. I geek out over this stuff and have been
     * slowly collecting data around its drying behavior.
     *
     * In real life, nobody lets me talk about this because it's too
     * boring. But you're stuck reading my book, so I'm not going to
     * waste my one chance to drone on about my data on oil curing rates.
     *
     * It's interesting. I swear.
     * ───────────────────────────────────────────────────────
     */
    void listing_3_1() {
        // Here's our data in Json form. We're going to turn it
        // into Java and see what we can learn along the way.
        // {
        //    "sampleId”: "UV-1",
        //    "day": 3,             ◄─── Tung oil cures painfully slow. It's measured in full days.
        //    "contactAngle": 17.4  ◄──┐ We can't see it curing, but we can measure it! This is
        // }                           │ the angle that a droplet of water forms on the surface of the wood
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.2
     * ───────────────────────────────────────────────────────
     * Our first stab at representing this in Java might be a
     * mechanical translation. text-like things in the json
     * becomes Strings in Java. Numbers in the Json become ints
     * or doubles in Java.
     * ───────────────────────────────────────────────────────
     */
    void listing_3_2() {
        record Measurement(
            String sampleId,      //  ◄───┐
            int daysElapsed,      //      │ A very JSON-like modeling
            double contactAngle   //      │
        ) {}
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.3
     * ───────────────────────────────────────────────────────
     * Taking a closer look at our data. Each of these fields
     * is about something. It has a meaning within its domain.
     * Have we captured that meaning in our code? Let's look
     * at the data again.
     * ───────────────────────────────────────────────────────
     */
    void listing_3_3() {
        // {
        //    "sampleId”: "UV-1",
        //    "day": 3,       //  ◄─── What the heck kind of measurement is "days"?
        //    "contactAngle": 17.4  //  ◄─── 17.4... *what*?
        // }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.4 through 3.6
     * ───────────────────────────────────────────────────────
     * This is a fun listing, because it gets to a very important
     * part of the software development process: stepping away from
     * the software. We're going to just sketch out what we know
     * about the stuff which makes up our domain.
     * ───────────────────────────────────────────────────────
     */
    void listing_3_4_through_3_6() {
        // You could do this with pen and paper, or on a whiteboard,
        // or in a comment like we do here. The important part is
        // giving yourself some space to think about what you're
        // trying to model before you let the quirks and limitations
        // of a programming language start influencing your thinking.

        /**
         * SampleID:
         *     Alpha-numeric + special characters  ◄───┐ As a first stab, this isn't bad, but it's
         *     Globally unique                         │ still a bit ambiguous. Do the characters matter at all?
         *
         * Days Elapsed:
         *     A positive integer (0 inclusive).   ◄────┐ The person taking the measurements (me) is
         *     Not strictly daily. Will be Sparse.      │ lazy. "days" is a hand-wavy unit of time, but about
         *                                              │ the highest fidelity I could muster.
         *
         * Contact Angle:                               ◄────┐
         *     degrees. Half open interval [0.0, 360)        │ Now we know what those numbers in the JSON
         *     Precision of 100th of a degree                │ are meant to be!
         *
         */
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.7
     * ───────────────────────────────────────────────────────
     * Let's take another stab at understanding Sample ID
     * ───────────────────────────────────────────────────────
     */
    void listing_3_7() {

        /**
         * SampleID:
         *     A sequence of characters satisfying the regex /[A-Z]+-\d+/
         *     Globally unique.
         *       ▲
         *       └─── This is an improved statement. Now we know the *shape* of
         *            the IDs. Can we be more precise that this?
         *
         * (other fields elided for brevity)
         *
         */
    }




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.8
     * ───────────────────────────────────────────────────────
     * If we keep digging on that Sample ID, we can eventually
     * get to the bottom of it. IDs are one of my favorite things
     * to harp on, because they so often contain hidden domain
     * information. That domain info ends up lost behind a generic
     * "anything goes here" type like "String"
     * ───────────────────────────────────────────────────────
     */
    void listing_3_7() {

        /**
         * CuringMethod:                        ◄────┐
         *     One of: (AIR, UV, HEAT)               │ Totally new domain information!
         *                                           │
         * SampleNumber:                        ◄────┘
         *     A positive integer (0 inclusve)
         *
         * SampleID:
         *     The pair: (CuringMethod, SampleNumber)  ◄─── Now *this* is a precise definition! 
         *     Globally unique.
         *
         * (other fields elided for brevity)
         *
         */
    }


}
