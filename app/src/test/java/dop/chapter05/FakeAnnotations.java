package dop.chapter05;

/**
 * These don't do anything. They're vaguely modelled
 * off Spring, but they're kept purposefully vague.
 *
 * The pressures ORM-like libraries place on us are
 * the same regardless the specific technology involved.
 */
public interface FakeAnnotations {
    public @interface Entity {}

    public @interface Id {}
    public @interface OneToMany {}
    public @interface OneToOne {}
    public @interface ManyToMany {}

    public @interface Transaction {}
    public @interface Service {}
    public @interface Controller {}
    public @interface Nullable {}

    public interface Repository<EntityType, PrimaryKeyType>{}
}
