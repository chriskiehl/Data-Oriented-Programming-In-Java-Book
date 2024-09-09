package dop.chapter02;

/**
 * We're going to explore many, many different permutations
 * and flavors of the Person object. To keep things straight
 * across all the listings, each instance will be namespaced
 * by their Listing number. L2_1
 */
public class Listings {

    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.2
     * ───────────────────────────────────────────────────────
     * Demoing the relationship between variables and the values
     * the hold over time.
     * ───────────────────────────────────────────────────────
     */
    static class L2_2 {
        static class Person {
            String name;
            int age;
            String hairColor;

            public Person(String name, int age, String hairColor) {
                this.name = name;
                this.age = age;
                this.hairColor = hairColor;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public void setHairColor(String hairColor) {
                this.hairColor = hairColor;
            }
        }
    }
}
