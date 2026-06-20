package dop.chapter05.the.existing.world;

/**
 * These don't do anything. They're inspired by things
 * like Spring and Hibernate, but the specifics are kept purposefully vague.
 *
 * The pressures ORM-like libraries place on us are the same regardless
 * the specific technology involved.
 */
public interface Annotations {

  @interface Entity {}

  @interface ID {}
  @interface OneToMany {}
  @interface OneToOne {}
  @interface ManyToMany {}
  @interface ManyToOne {}

  @interface Transaction {}
  @interface Service {}
  @interface Controller {}

  interface Repository<EntityType, PrimaryKeyType> {}

}
