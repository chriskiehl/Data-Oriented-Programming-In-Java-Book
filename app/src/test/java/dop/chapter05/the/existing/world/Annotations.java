package dop.chapter05.the.existing.world;

/**
 * These don't do anything. They're inspired by things
 * like Spring and Hibernate, but the specifics are kept purposefully vague.
 *
 * The pressures ORM-like libraries place on us are the same regardless
 * the specific technology involved.
 */
public interface Annotations {
    public @interface Entity {}

    public @interface Id {}
    public @interface OneToMany {}
    public @interface OneToOne {}
    public @interface ManyToMany {}

    public @interface Transaction {}
    public @interface Service {}
    public @interface Controller {}

    public interface Repository<EntityType, PrimaryKeyType>{}
}
