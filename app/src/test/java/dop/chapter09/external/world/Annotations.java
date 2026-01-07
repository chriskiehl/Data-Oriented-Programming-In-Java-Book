package dop.chapter09.external.world;

public class Annotations {
    public @interface Entity {}

    public @interface Id {}
    public @interface OneToMany {}
    public @interface OneToOne {}
    public @interface ManyToMany {}

    public @interface Transaction {}
    public @interface Service {}
    public @interface Controller {}

    public interface Repository<EntityType, PrimaryKeyType>{}

    public @interface Path {
        String value();
    }
    public @interface Post {}
}
