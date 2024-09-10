package dop.chapter02;

import dop.chapter02.Listings.L2_2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Examples {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.1
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
     * Listing 2.1
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
        person.setHairColor("wait -- is that a grey..?");  // Am I... old...?!

        Assertions.assertTrue(person == person);
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.3
     * ───────────────────────────────────────────────────────
     * Values have no identity. They cannot be changed.
     * They just "are."
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_3() {
        //            ┌─────── Thankfully adding 1 doesn't mutate the number 4.
        //            ▼        (Thus removing our concept of 4 from the universe)
        //        4 + 1
        //        ▲
        //        └────── The number 4 just "is." It will remain
        //                no matter what operations we perform "on" it
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.4
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
     * Listing 2.5
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
     * Listing 2.6
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
            a.equals(a)    //  ◄───  They're reflexive (equal to themselves a == a)
                && a.equals(b) //  ◄───┐ Symmetric (a == b && b == a)
                && b.equals(a) //  ◄───┘
                && a.equals(c) // ◄───  and transitive! if a == b, and b == c, then a == c
                && Integer.valueOf(1234).equals(a)
                && Integer.valueOf(1234).equals(b)
                && a.equals(Integer.valueOf(1234))
                && Integer.valueOf(1234).equals(Integer.valueOf(1234)));
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.7
     * ───────────────────────────────────────────────────────
     * We can make new value objects on top of existing ones!
     * ───────────────────────────────────────────────────────
     */
    static class Vector {
        Double x;
        Double y;
        //  ▲
        //  └────── There's *technically* one more thing
        //          we'd need to add here to really make
        //          this a value, but we'll come back to it later.
        public Vector(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        public Double x(){return x;}
        public Double y(){return y;}
        //            ▲
        //            └───── Getters are A-OK on value objects
        //
        //      ┌───── But note that there are no setters!
        //      ▼                   Values don't change!
        // (No setters here!)

        @Override
        public boolean equals(Object o) {
            // The default equals method includes an object check
            // but this is irrelevant.
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            // The state is what determines the equality!
            Vector vector = (Vector) o;
            return Objects.equals(x, vector.x)
                    && Objects.equals(y, vector.y);
        }
        @Override
        public int hashCode() {return Objects.hash(x, y);}
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.8
     * ───────────────────────────────────────────────────────
     * The value objects should honor all the same rules for
     * equality as the values we build upon. Value objects *are*
     * values. They should behave like values.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_8() {
        Vector a = new Vector(2.0, 3.0);
        Vector b = new Vector(2.0, 3.0);
        Vector c = new Vector(2.0, 3.0);

        // Just as in listing 2.6, the value object we
        // made has the same reflexive, transitive, and symmetric properties!
        Assertions.assertTrue(
            a.equals(b)
            && b.equals(a)
            && a.equals(c)
            && new Vector(2.0, 3.0).equals(a)
            && new Vector(2.0, 3.0).equals(b)
            && a.equals(new Vector(2.0, 3.0))
            && new Vector(2.0, 3.0).equals(new Vector(2.0, 3.0)));
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.9
     * ───────────────────────────────────────────────────────
     * Heads up! This listing looks very different from the one
     * in the book. Unlike plain text on a page, which doesn't
     * need to compile, Java does need that text to be compilable.
     * So, we have to do a lot of interface shimming in order to
     * get the (mostly) made up API in the example to compile.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_9() {
        // This section just sets up a bunch of fake interfaces
        // for our "API". It's ignorable.
        // ──────────────────────────────────────────────────────────┐
        interface Result {}                                        //│
        class Customer {                                           //│
            Customer(Result result) {}                             //|
        };                                                         //│
        interface QueryResponse {                                  //│
            List<Result> items();                                  //│
            String nextToken();                                    //│
        }                                                          //│
        interface DefinitelyNotDynamoDb {                          //│
            QueryResponse query(String request);                   //│
            QueryResponse query(String request, String nextToken); //│
        }                                                          //│
                                                                   //│
        // Ditto here.                                               │
        // These are just placeholders for compilation               │
        String request = null;                                            //│
        DefinitelyNotDynamoDb database = null;                            //│
        // ──────────────────────────────────────────────────────────┘

        // Here's the bulk of the listing. It's exploring how we often
        // use the Java collections as Identity Objects rather than
        // value objects.
        Supplier<List<Customer>> demo = () -> {
            List<Customer> customers = new ArrayList<>(); // ◄─────  Creating the collection gives us a
                                                          //         useful identity we can carry around
                                                          //         as we do work

            QueryResponse response = database.query(request);
            for (Result result : response.items()) {
                customers.add(new Customer(result));  //     ◄─────  Which we do here
            }

            while (response.nextToken() != null) {
                response = database.query(request, response.nextToken());
                for (Result result : response.items()) {
                    customers.add(new Customer(result));  // ◄─────  And then over-and-over again down here
                }
            }
            return customers;  //      ◄───────────────────────────  Before finally returning it here
        };
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.10
     * ───────────────────────────────────────────────────────
     * We can convert Java collections from identity objects
     * into value objects using the Unmodifiable suite of wrapper
     * types. Super useful!
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_10() {
        // We start out with an identity object.
        List<String> letters = new ArrayList<>();
        letters.add("A");
        letters.add("B");  // we can freely mutate it as we go
        letters.add("...");
        letters.add("Z");
        // However here we convert it to a **Value Object** by wrapping it
        // up as an unmodifiableList.
        List<String> immutableLetters = Collections.unmodifiableList(letters);
        // Any attempts to continue treating it as an identity are
        // met with an error. The Unmodifiable wrapping classes turn off
        // every API method that might cause a mutation.
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            immutableLetters.add("1"); // If you're in an IDE like IntelliJ
                                       // it should highlight this as an invalid
                                       // method call.
        });
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.11
     * ───────────────────────────────────────────────────────
     * As of Java 10, each of the major collection interfaces
     * exposes unmodifiable constructors! This makes it super
     * easy to create collections which act like value objects.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_11() {
        // Creating value objects.
        Set<Integer> a = Set.of(1, 2, 3, 4);
        List<Integer> b = List.of(1,2,3,4);
        Map<Integer, String> c = Map.of(1, "One", 2, "Two");
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.12
     * ───────────────────────────────────────────────────────
     * Value objects are special. They can be used in places where
     * identity objects would be unsafe or potentially lead to
     * very unexpected behaviors.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_12() {
        Map<List<String>, String> map = new HashMap<>();

        map.put(List.of("Bob", "Joe"), "Bob and Joe"); // This is perfectly safe, because it’s a value
        System.out.println(map.get(List.of("Bob", "Joe"))); // ◄─┐ We can query it back out using a totally
        // [out] "Bob and Joe"                                   │ different value object, because their state
        //                                                       │ and equality is the same (i.e. they’re the same)
        //

        List<String> mutable = new ArrayList<>(); // However, with an identity-based setup...
        map.put(mutable, "TBD");
        mutable.add("Bob");                           // ◄─┐
        System.out.println(map.get(mutable) == null); //   │ We'll never see ol' Bob again...
        // [out] NULL
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.13
     * ───────────────────────────────────────────────────────
     * Identities are created when we assign values to
     * *mutable* variables!
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_13() {
        Integer xPosition = Integer.valueOf(4);
        // This does not create a value!
        // It creates an identity, xPosition, against which
        // values will be tracked over time.
        // We can do something that should be impossible:
        xPosition = Integer.valueOf(5); // The change breaks the core contract of values!
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.14 & 2.15
     * ───────────────────────────────────────────────────────
     * Code is how we communicate. If we don't make it say exactly
     * what we mean, then it's up to our readers to guess.
     *
     * Value classes look like any other identity class. It's very
     * easy for a newcomer to misunderstand that we want this class
     * to represent a value
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_14_and_2_15() {
        class Vector {
            double x;   // ◄─┐
            double y;   //   │ If we don't say that these are supposed to be values
                        //   │ it'll be super easy for the intended semantics of the
                        //   │ code to drift over time

            public void scale(double amount) {    // ◄─┐
                this.x = this.x * amount;         //   │ This is a reasonable addition for
                this.y = this.y * amount;         //   │ an Identity Class, but *not* a valid
            }                                     //   │ addition for a Value Class!
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.16
     * ───────────────────────────────────────────────────────
     * We use the final keyword to control whether we're creating
     * an identity or a value.
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_16() {
        // Now this is a value!
        final Integer xPosition = Integer.valueOf(4);
        // If we try to change it Java won't even let our code compile.
        // xPosition = Integer.valueOf(5);
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.17
     * ───────────────────────────────────────────────────────
     * The control we get with final also applies to classes.
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_17() {

        // ┌ We mark the class as final, too. Inheritance of
        // │ values can be done (with a lot of caution), but
        // │ avoiding it cuts off a lot of potential problems
        // ▼
        final class Vector {

            final double x;  // │ Marking the instance variables as final ensures
            final double y;  // │ what this class *is*, value class, is communicated
                             // │ and enforced.
            public Vector(double x, double y) {
                this.x = x;
                this.y = y;
            }
        }
        // combine all of that with a final variable, and you've
        // got yourself a true value object!
        final Vector vector = new Vector(2, 3);
    }




    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.18
     * ───────────────────────────────────────────────────────
     * Mutability is infectious. All it takes is a single mutable
     * reference on our value class to drag it back into the land
     * of identities.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_18() {

        final class Person {
            final String name;
            final Date birthday;  // ◄─┐ Mutability can be sneaking in Java.
                                  //   │ The assignment to this variable is final, but
                                  //   │ the object itself is mutable
            public Person(String name, Date birthday) {
                this.name = name;
                this.birthday = birthday;
            }

            public String name() {return name;}
            public Date birthday() {return birthday;}
        }

        // which means we can violate the value-ness of our value object
        final Person person = new Person("Bob", new Date());

        SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String before = isoFormatter.format(person.birthday());
        // anyone can punch into the mutable reference and change it
        person.birthday().setTime(new Date().getTime() + 123456789);
        String after = isoFormatter.format(person.birthday());
        System.out.println("Before: " + before);
        System.out.println("After: " + after);

        Assertions.assertNotEquals(before, after);  //  :(
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.19 & 2.20
     * ───────────────────────────────────────────────────────
     * Mutability where is doesn't belong is dangerous. It breaks
     * the semantics of our code. The ground shakes under our feet
     * when our core assumptions get violated.
     *
     * What's crummy is that the poison runs deep. Any mutable
     * reference *anywhere* ruins the value-ness of the entire
     * structure.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_19_and_2_20() {
        // This is just getting our "value" class into the
        // shape we need. (Don't worry, we'll soon explore records
        // which let us define value classes without all the
        // ceremony.
        final class Person {
            final String name;
            final Date birthday;
            public Person(String name, Date birthday) {
                this.name = name;
                this.birthday = birthday; // Like listing 2.18, we create value class that
                                          // has an accidental "back door" for mutability
            }

            public String name() {return name;}
            public Date birthday() {return birthday;}

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Person person = (Person) o;
                return Objects.equals(name, person.name) && Objects.equals(birthday, person.birthday);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, birthday);
            }
        }

        Person person = new Person("Bob", new Date());
        Set<Person> people = new HashSet<>(); // ◄─┐ Mutability can break core invariants in our code.
        people.add(person);                   //   │ What should be inviolable, like the fact that sets
        people.add(person);                   //   │ hold no duplicates, can be subtly violated.
        Assertions.assertEquals(1, people.size());

        // The set behaves like a set right up until something
        // on our "value" gets mutated.
        person.birthday().setTime(new Date().getTime() + 123456789);
        people.add(person);
        // The mutation breaks the HashSet contract, and we end up with two
        // copies of the exact same object
        Assertions.assertEquals(2, people.size());

        // │ Note: this is the example from listing 2.20.
        // │ It's combined with Listing 2.19 so that we don't
        // │ have to define the Person class again.

        // The cruel part of mutable references is that even if we
        // do everything else "right" (we use value-based
        // constructors filled with "value" objects, and mark everything
        // final) a single mutable reference can still betray all of
        // that work.
        final List<List<Person>> peoplelist = List.of(List.of(person));
        int original = peoplelist.hashCode();
        // Some random piece of code can always reach in and modify things
        // thus wreaking havoc on our expectations.
        peoplelist.get(0).get(0).birthday().setTime(12345678);
        System.out.println(original == people.hashCode());  // FALSE
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.21
     * ───────────────────────────────────────────────────────
     * Now we get to the really good stuff. What does it mean to
     * data "as data"? It means to model it as a value! Data is a
     * value that's "about" something.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_21() {
        // Notice how different this feels simply because we made
        // the number 4 "about" temperatures. All kinds of new questions
        // flood into my mind about what this 4 now *means*.
        Integer temperature = 4;
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.22
     * ───────────────────────────────────────────────────────
     * Data has a lot of familiar shapes. Stuff we might just sum
     * up as "information" about the world. Sales reports, daily weather,
     * etc. The kind of stuff we'd read out of a database.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_22() {
        interface Temperature {} // Note! Here just to enable compilation.

        // These value classes are examples of pretty standard stuff
        // that we'd all probably agree "looks like data"
        class DailyElectricityUsage {
            LocalDate date;
            Temperature high;
            Temperature low;
            Double kWh;
        }

        class Sale {
            String productName;
            LocalDate soldOn;
            int quantity;
            BigDecimal totalPrice;
        }

    }




    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.23
     * ───────────────────────────────────────────────────────
     * The really fun part of data-oriented programming is learning
     * to see the world differently. Data is everywhere! It can
     * be anything. It can even be abstract!
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_23() {
        // These two value classes model decisions that our
        // system could make (for a refresher on these types, checkout
        // the Listings from chapter 01).
        class ReattemptLater {
            LocalDateTime scheduledAt;
            // getters,final,equals, and hashcode omitted
        };
        // You might have to squint a bit to "see" these as being
        // data just like we would a sales or weather report. They
        // tick all the boxes, though!
        class RetryImmediately {
            LocalDateTime scheduledAt;
            int attempts;
            // getters,final,equals, and hashcode omitted
        };
    }




    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.24, 2.25
     * ───────────────────────────────────────────────────────
     * Object Identity gives continuity to all the changes
     * we make to the attributes inside the object throughout
     * the course of our program. Even if we change everything.
     * The object remains. It's still *that* object.
     *
     * This identity influences *how* we program. It nudges us to
     * towards a particular style of writing methods. Towards a
     * certain way of exposing behaviors. Towards a way of putting
     * our code together as a whole.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_24_and_2_25() {
        class Person {
            String name;
            int age;

            public Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setAge(int age) {
                this.age = age;
            }

            // Note the special kind of method we can write when
            // modeling with Identity classes. This method takes
            // nothing and returns nothing. it exists only to mutate
            // the object in place.
            public void haveBirthday() {
                this.age++;
            }
        }

        // Identity Objects give continuity to changes
        // we make to their internal attributes over time.
        Person person = new Person("Bob", 32);
        person.setAge(33);
        person.setName("Bobbie");
        // Even if we change everything about the object, the obejct
        // itself remains the same. It's still the same object.
        person.setName("Robert");

        // Each time we change the object, its previous state is lost
        // to time. The holder remains, but what it held disappears.
        person.haveBirthday();
        person.haveBirthday();
        person.haveBirthday();
        // Down here the person is a new age. What they were before is overwritten
        System.out.println(person);
    }




    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.26 & 2.27 & 2.28
     * ───────────────────────────────────────────────────────
     * A new programming style emerges naturally when you model
     * identity and change using data. It emerges because there's
     * a fundamental constraint: data doesn't change. We can't mutate
     * an object in place, so we've got to do something else.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing_2_26_and_2_27_and_2_28() {
        // Redesigning Person as a Value Class
        final class Person {        // ◄─┐
            final String name;      //   │ Everything is now final
            final int age;          //   │

            public Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            // public void haveBirthday() {  // ◄─┐ Note that this method *can't* live here
            //    this.age++;                //   │ anymore. Values don't change.
            // }
        }

        // The big question is: Uhh... what's next?
        // Where do we go from here? If everything is final, how
        // do we represent change...?
        final Person person = new Person("Bob", 32);

        // with more data!
        final Person older     = new Person("Bob", 33);  // ◄─┐ Nothing is mutated, but we can still see that
        final Person evenOlder = new Person("Bob", 34);  //   │ they’re all clearly “about” the same identity.
        final Person cruelTime = new Person("Bob", 35);  //   │ There's a steady progression of change.
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.29 - 2.33
     * ───────────────────────────────────────────────────────
     * Heads up! We combine quite a few of the listings here so
     * that we can reuse the object definitions and explore the
     * various styles next to each other.
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_29_through_2_33() {
        final class Person {
            final String name;
            final int age;

            public Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
            public String name() {return name;}
            public int age() {return age;}

            // This method represents one of many possible ways
            // of creating data that models change. This creates
            // a *succession* of values. The inputs directly lead
            // to the outputs.
            public Person haveBirthday() {              // ◄─┐ Note that this method *can* live here! It doesn't
                return new Person(name, age + 1);  //   │ mutate anything. It computes a *new* value.
            }
            // public void haveBirthday() {     ◄─┐ Compare the above with this one, which needs an
            //    this.age++;                     │ Identity class in order to work
            // }
        }
        // We're defining this as a lambda just for sake of keeping
        // all the definitions local to each listing. In practice, it would
        // be a normal method.
        //
        // This is another way we could create a succession of values.
        // Just like the one we defined on the Value Class, this one takes
        // input and uses it to compute the next state.
        Function<Person, Person> haveBirthday = (p) -> {
            return new Person(p.name(), p.age());
        };

        // These two approaches lead to very different programming styles.
        // When methods are on the Value class, they give us a fluent API for "free".
        // For instance, we can give our person a bunch of birthdays back to back.
        Person example01 = new Person("Bob", 30)
                .haveBirthday()  // each method returns a *new* value object
                .haveBirthday()  // which makes chaining calls a breeze.
                .haveBirthday(); // Note that nothing was mutated here!
        System.out.println(example01.age());

        // Standalone methods have their own call flavor.
        Person example02 = haveBirthday
                .andThen(haveBirthday)
                .andThen(haveBirthday)
                .apply(new Person("Bob", 30));
        // One is not better than the other.
        // Both are useful in different situations.
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.34
     * ───────────────────────────────────────────────────────
     * Heavy emphasis on “broadly speaking”, “it depends,” and
     * “just my opinion” for the following!
     *
     * Generally speaking, the more "plain value" or "math-y"
     * something is, the more it benefits from defining the methods
     * on the value class.
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_34() {
        class Vector {
            double x,y;

            public Vector(double x, double y) {
                this.x = x;
                this.y = y;
            }

            // Defining all the methods on the value class
            // makes it nice to interact with when dealing
            // with operations that need to stack together.
            public Vector scale(double amount) {
                return new Vector(x*amount, y*amount);
            }
            public Vector subtract(Vector other) {
                return new Vector(x-other.x, y-other.y);
            }
            public Double magnitude() {
                return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            }
        }

        // For example:
        double result = new Vector(2.0, 9.1)
                .scale(10.0)
                .subtract(new Vector(1.0, 1.0))
                .magnitude();
    }





    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.35
     * ───────────────────────────────────────────────────────
     * Finally, we get to Record Classes! They let us define value
     * and data classes with ease!
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_35() {
        // Here's the same Value Class from previous listings
        // defined with a record. How nice!
        record Vector(double x, double y){}
        // records give all the value semantics we'd expect
        final Vector a = new Vector(2, 3);
        final Vector b = new Vector(2, 3);
        final Vector c = new Vector(2, 3);
        // They produce true value objects.
        Assertions.assertTrue(
                a.equals(b)
                && b.equals(a)
                && a.equals(c));

    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.36
     * ───────────────────────────────────────────────────────
     * Record classes have some Quality of Life features over
     * regular classes (in addition to just involving less
     * boilerplate). One of the main ones we'll utilize throughout
     * the book is the Compact Constructor.
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_36() {
        record Rational(int num, int denom) {
            Rational {                                           // ◄─┐
                if (denom == 0) {                                //   │ Checking during construction makes sure
                    throw new IllegalArgumentException(          //   │ that only valid data is created.
                            "Hey! That’s not how math works!"
                    );
                }
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.37
     * ───────────────────────────────────────────────────────
     * We can also transform record arguments during construction!
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_37() {
        record Rational(int num, int denom) {
            Rational {
                if (denom == 0) {
                    throw new IllegalArgumentException(
                        "Hey! That’s not how math works!"
                    );
                }

                int gcd = gcd(num, denom);  // ◄─┐ While we’re inside the constructor, we can transform
                num /= gcd;                 //   │ the incoming argument as much as we want. Here we’re normalizing
                denom /= gcd;               //   │ them before assignment.
            }

            public static int gcd(int a, int b) {
                if (b==0) return a;
                return gcd(b,a%b);
            }
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.38 & 3.39
     * ───────────────────────────────────────────────────────
     * Things to watch our for: mutability is ever-present in Java!
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_38() {
        record Person(
                String name,
                // Dangerous!
                List<String> friends  // ◄─┐ The value-ness of this entire data-type depends on the people who
        ){}                           //   │ use it remembering to give an unmodifiable version of the List interface.

        // We can "undo" the value-ness of records just like
        // we could with the value classes we created by hand.
        List<String> friends = Arrays.asList("Joe", "Jane");
        Person person = new Person("Bob", friends);

        System.out.println(person);
        //[out]  Person2[name=Bob, friends=[Joe, Jane]]

        friends.set(0, "Billy");
        System.out.println(person);
        // [out] Person2[name=Bob, friends=[Billy, Jane]]     #D
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.40 through 2.42
     * ───────────────────────────────────────────────────────
     * We can side-step a ton of potential mutability problems
     * by transforming the data on the way in.
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_40_through_2_42() {
        record Person(
                String name,
                // Dangerous!
                List<String> friends



        ) {
            Person {
                friends = List.copyOf(friends);  // ◄── copyOf produces an Unmodifiable copy of the list.
            }
        }
        // now there's no more chance of someone accidentally creating
        // an identity object when they mean to create a value one
        List<String> friends = Arrays.asList("Joe", "Jane");
        Person person = new Person("Bob", friends);

        friends.set(0, "Billy");
        friends.set(1, "Michael");  // Out here, they can freely mutate their collection as much as they want

        System.out.println(person); // But we remain an unchanging, immutable value.
        // [out] Person2[name=Bob, friends=[Joe, Jane]]

        // However, there's still a problem here.
        // Nothing tells us, the devs using the code, that List is
        // supposed to be immutable. As far as we can see, it's a Java
        // collection like any other.
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            // It looks like this should totally work, but it
            // will throw a nasty runtime error
            person.friends().add("Billy");
        });
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.43 & 2.44
     * ───────────────────────────────────────────────────────
     * Once again, we revisit the idea of representation. Can we
     * make our code communicate the constraints that live in our
     * heads? Yes! With another data type!
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_43_and_2_44() {
        record ImmutableList<A>(List<A> items) {  // ◄── just like that, we introduce a new layer of
            ImmutableList {                       //     semantic precision into the code.
                items = Collections.unmodifiableList(items);
            }

            public static <A> ImmutableList<A> of(A... items) {
                return new ImmutableList<>(List.of(items));
            }
        }

        // Now we can use this new precise data type on our person model
        record Person(
                String name,
                ImmutableList<String> friends // Now there is no way to accidentally provide the wrong kind
        ){}                                   // of collection. It says exactly what it expects.


        Person person = new Person(
            "Bob",
            ImmutableList.of("Jane", "Joe") // And we HAVE to provide an ImmutableList here.
                                                   // anything else wouldn't compile. The code forces us
                                                   // to do the right thing.
        );
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.45 & 2.46
     * ───────────────────────────────────────────────────────
     * More stuff to watch out for with records: nulls :(
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_40_and_2_41() {
        record Person(String firstName, String lastName){};

        // nothing prevents nulls from being passed to our records
        Person person = new Person(null, null);
        // Records are transparent carriers of data. They'll hold
        // whatever we give them -- including junk.


        // One option is to defend against those nulls on the way in.
        record PersonV2(String firstName, String lastName){
            PersonV2 {
                Objects.requireNonNull(firstName);
                Objects.requireNonNull(lastName);
            }
        };
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.47 & 2.48
     * ───────────────────────────────────────────────────────
     * Some record niceties: you can define them wherever you
     * need them.
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_47_and_2_48() {
        record Person(String name){}
        // Note! Here just for completeness of example.
        BiFunction<Person, List<Person>, Integer> friendGraph = (person, people) -> {
            return 0;
        };

        List<Person> people = List.of();
        // (As with all the listings. We're only defining this as a lambda
        // so everything stays within its particular listing)
        Supplier<Person> mostPopular = () -> {
            //      ┌ Records can be defined directly in a method body!
            //      │ (We've actually seen this a ton so far)
            //      │ This is a huge improvement over tuples or lists.
            //      ▼
            record Popularity(Person person, Integer totalFriends){};
            return people.stream()
                    .map(person -> new Popularity(
                            person,
                            friendGraph.apply(person, people))
                    )
                    .sorted(Comparator.comparingDouble(Popularity::totalFriends))
                    .map(Popularity::person)
                    .toList()
                    .getFirst();
        };
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.49
     * ───────────────────────────────────────────────────────
     * One of the subtle but extremely powerful aspects of records
     * is that they make it possible to structure our code in ways
     * that previously wouldn't have made sense. We can define
     * related data types *right next to each other*. They don't
     * have to be tucked away in different files.
     * ───────────────────────────────────────────────────────
     */
    public void listing_2_49() {
        interface CardGame {
            enum Suit {Hearts, Diamonds, Club, Spade;}
            enum Rank {ONE, TWO, THREE, FOUR, FIVE, /*...*/;}

            record Card(Rank rank, Suit suit){};

            record GameState(List<Card> drawPile, List<Card> discards){}
        }
    }
}
