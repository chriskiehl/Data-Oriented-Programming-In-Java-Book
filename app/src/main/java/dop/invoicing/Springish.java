package dop.invoicing;

public interface Springish {

    public @interface Entity {}
    public @interface Id {}

    public @interface Transaction {}
    public @interface Service {}
    public @interface Controller {}

    public @interface Async {}

    public interface Repository<EntityType, PrimaryKeyType>{}

}
