package dop.chapter09;

import dop.chapter05.the.existing.world.Annotations.Entity;
import dop.chapter06.the.implementation.Types.Percent;
import dop.chapter06.the.implementation.Types.USD;
import dop.chapter09.Annotations.Path;
import dop.chapter09.Annotations.Post;
import dop.chapter09.Sketch.Status.Raw;
import dop.chapter09.Status.Validated;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.jqwik.api.constraints.Size;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class Sketch {



    record ItemId(){}
    record CartItem(String id, BigDecimal price){}

    record Address(
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank @Size(min = 2, max = 2) String countryCode,
        @NotBlank @Pattern(regexp = "\\d{5}") String postalCode
    ) {}

    record Cart(
        @Size(min=1) List<Item> items,
        @Valid String couponCode
    ){}

    record Item(@Size(min=5) String sku, @Min(1) int quantity) {}

    record Affiliate(LocalDate validFrom, LocalDate validUntil, Percent discountPercent){
        public static Affiliate DEFAULT = new Affiliate(null, null, null);
    }

    interface AffiliateService {
        Affiliate findAffiliate(String code);
        Affiliate fetchDefault();
    }
    interface ProductRepo {
        Product get(String id);
    }
    record Product(USD price, Boolean hasDigitalFreebie){}


    @Entity
    static class Order {
        public static Order fromDetails(List<Product> products, Affiliate affiliate, USD subtotal, USD total) {
            return new Order();
        }


    }

    static class UnknownProductException extends RuntimeException {
        String sku;
        public UnknownProductException(String sku) {

        }
    }

    interface OrderRepo {
        void save(Order order);
    }

    static class OrderControllerBAD {
        private Validator validator;
        private AffiliateService affiliateService;
        private ProductRepo productRepo;
        private OrderRepo orderRepo;

        // maybe accept the request object, and make it maybe have a user or not
        // that determines if it's a guest checkout
        // if guest: we need a special
        public void checkout(Cart cart) {
            Set<ConstraintViolation<Cart>> errors = validator.validate(cart);
            if (!errors.isEmpty()) {
                throw new ConstraintViolationException(errors);
            }
            Affiliate affiliate = affiliateService.findAffiliate(cart.couponCode());
            Order order = this.createOrder(cart, affiliate);
            this.orderRepo.save(order);
        }

        Order createOrder(Cart cart, Affiliate affiliate) {
            USD subtotal = USD.zero();
            List<Product> products = new ArrayList<>();
            for (Item item : cart.items()) {
                Product product = this.productRepo.get(item.sku());
                if (product == null) {
                    throw new UnknownProductException(item.sku());
                }
                products.add(product);
                subtotal = USD.add(subtotal, product.price().multiply(item.quantity()));
            }
            if (affiliate == null) {
                affiliate = Affiliate.DEFAULT;
            }
            if (!(affiliate.validFrom().isBefore(LocalDate.now())
                    && affiliate.validUntil().isAfter(LocalDate.now()))) {
                throw new IllegalArgumentException("Affiliate code is not active");
            }
            USD discount = subtotal.multiply(affiliate.discountPercent());
            USD total = USD.subtract(subtotal, discount);
            return Order.fromDetails(products, affiliate, subtotal, total);
        }
    }


//    record CartDTO(){}
//
//    static class OrderController_STILL_BAD {
//        private Validator validator;
//        private CheckoutService checkoutService;
//
//        public void checkout(CartDTO cartDTO) {
//            Set<ConstraintViolation<Cart>> errors = validator.validate(cart);
//            if (!errors.isEmpty()) {
//                throw new ConstraintViolationException(errors);
//            }
//            Order order = this.checkoutService.checkout(Cart.fromDTO(cartDTO));
//            return Response.OK(order);
//        }
//    }


    static class CheckoutService {
        private AffiliateService affiliateService;
        private ProductRepo productRepo;
        private OrderRepo orderRepo;

        public void checkout(Cart cart) {
            Affiliate affiliate = affiliateService.findAffiliate(cart.couponCode());
            Order order = this.createOrder(cart, affiliate);
            this.orderRepo.save(order);
        }

        Order createOrder(Cart cart, Affiliate affiliate) {
            // [ Same Code ]
            USD subtotal = USD.zero();
            List<Product> products = new ArrayList<>();
            for (Item item : cart.items()) {
                Product product = this.productRepo.get(item.sku());
                if (product == null) {
                    throw new UnknownProductException(item.sku());
                }
                products.add(product);
                subtotal = USD.add(subtotal, product.price().multiply(item.quantity()));
            }
            if (affiliate == null || !(affiliate.validFrom().isBefore(LocalDate.now())
                    && affiliate.validUntil().isAfter(LocalDate.now()))) {
                throw new IllegalArgumentException("This coupon is not active");
            }

            USD discount = subtotal.multiply(affiliate.discountPercent());
            USD total = USD.subtract(subtotal, discount);
            return Order.fromDetails(products, affiliate, subtotal, total);
        }
    }


    static class OrderProcessorREFACTORED_1 {
        private Validator validator;
        private AffiliateService affiliateService;
        private ProductRepo productRepo;
        private OrderRepo orderRepo;

        public void checkout(Cart cart) {
            Set<ConstraintViolation<Cart>> errors = validator.validate(cart);
            if (!errors.isEmpty()) {
                throw new ConstraintViolationException(errors);
            }
            Affiliate affiliate = affiliateService.findAffiliate(cart.couponCode());
            // ------------------------------------------------------------------
            //                        BOUNDARY
            // ------------------------------------------------------------------
            Order order = this.createOrder(cart, affiliate);
            this.orderRepo.save(order);
        }

        Order createOrder(Cart cart, Affiliate affiliate) {
            USD subtotal = USD.zero();
            List<Product> products = new ArrayList<>();
            for (Item item : cart.items()) {
                // IO is mixed into our business logic
                // We're trying to do work against a cart
                // that may have dubious items
                Product product = this.productRepo.get(item.sku());
                if (product == null) {
                    throw new UnknownProductException(item.sku());
                }
                products.add(product);
                subtotal = USD.add(subtotal, product.price().multiply(item.quantity()));
            }
            // We called validate() above, and yet here is more validation!
            // waht was the point of doing that if we just do more down here
            if (affiliate == null || !(affiliate.validFrom().isBefore(LocalDate.now())
                    && affiliate.validUntil().isAfter(LocalDate.now()))) {
                throw new IllegalArgumentException("This coupon is not active");
            }

            USD discount = subtotal.multiply(affiliate.discountPercent());
            USD total = USD.subtract(subtotal, discount);
            return Order.fromDetails(products, affiliate, subtotal, total);
        }
    }



    static class OrderProcessorREFACTORED_2 {
        private Validator validator;
        private AffiliateService affiliateService;
        private ProductRepo productRepo;
        private OrderRepo orderRepo;

        public void checkout(Cart cart) {
            Affiliate affiliate = affiliateService.findAffiliate(cart.couponCode());
            validate(cart, affiliate);
            // ------------------------------------------------------------------
            //                        BOUNDARY
            // ------------------------------------------------------------------
            Order order = this.createOrder(cart, affiliate);
            this.orderRepo.save(order);
        }

        Order createOrder(Cart cart, Affiliate affiliate) {
            USD subtotal = USD.zero();
            List<Product> products = new ArrayList<>();
            for (Item item : cart.items()) {
                Product product = this.productRepo.get(item.sku());
                products.add(product);
                subtotal = USD.add(subtotal, product.price().multiply(item.quantity()));
            }
            USD discount = subtotal.multiply(affiliate.discountPercent());
            USD total = USD.subtract(subtotal, discount);
            return Order.fromDetails(products, affiliate, subtotal, total);
        }

        void validate(Cart cart, Affiliate affiliate) {
            Set<ConstraintViolation<Cart>> errors = validator.validate(cart);
            if (!errors.isEmpty()) {
                throw new ConstraintViolationException(errors);
            }
            if (!(affiliate.validFrom().isBefore(LocalDate.now())
                    && affiliate.validUntil().isAfter(LocalDate.now()))) {
                throw new IllegalArgumentException("This coupon is not active");
            }
            for (Item item : cart.items()) {
                if (this.productRepo.get(item.sku()) == null) {
                    throw new IllegalArgumentException("INVALID SKUS!");
                }
            }
        }

        Affiliate getDiscount(String couponCode) {
            Affiliate affiliate = affiliateService.findAffiliate(couponCode);
            if (Objects.isNull(affiliate)) {
                throw new IllegalArgumentException("Unknown Coupon code");
            }
            return affiliate;
        }
    }

    record ValidItem(Product product, int qty){}
    record ValidatedCart(
        Affiliate affiliate,
        List<ValidItem> items
    ){}

    record ValidationError() {}

    sealed interface Decision<A> {
        record Valid<A>(A value) implements Decision<A> {}
        record Invalid<A>(ValidationError value) implements Decision<A> {}
    }

    interface Validator2<Unvalidated, Validated> {
        Decision<Validated> validate(Unvalidated input);
    }



    static final class MyValidator implements Validator2<Person, Person> {

        @Override
        public Decision<Person> validate(Person input) {
            if (input.age > 10) {
                return new Decision.Valid<>(input);
            } else {
                for (String s : List.of("one", "two")) {
                    discount(s);
                }
                return new Decision.Invalid<>(new ValidationError());
            }
        }
    }

    static String discount(String s) {
        return s.toLowerCase();
    }



    static class OrderProcessorREFACTORED_3 {
        private Validator validator;
        private AffiliateService affiliateService;
        private ProductRepo productRepo;
        private OrderRepo orderRepo;

        @Post
        @Path("/checkout")
        public void checkout(Cart cart) {
            Affiliate affiliate = affiliateService.findAffiliate(cart.couponCode());
            ValidatedCart validCart = validate(cart, affiliate);
            // ------------------------------------------------------------------
            //                        BOUNDARY
            // ------------------------------------------------------------------
            Order order = this.createOrder(validCart);
            this.orderRepo.save(order);
        }

        Order createOrder(ValidatedCart cart) {
            USD subtotal = USD.zero();
            List<Product> products = new ArrayList<>();
            for (ValidItem item : cart.items()) {
                products.add(item.product());
                subtotal = USD.add(subtotal, item.product().price().multiply(item.qty()));
            }
            USD discount = subtotal.multiply(cart.affiliate().discountPercent());
            USD total = USD.subtract(subtotal, discount);
            return Order.fromDetails(products, cart.affiliate(), subtotal, total);
        }

        ValidatedCart validate(Cart cart, Affiliate affiliate) {
            Set<ConstraintViolation<Cart>> errors = validator.validate(cart);
            if (!errors.isEmpty()) {
                throw new ConstraintViolationException(errors);
            }
            if (affiliate == null || !(affiliate.validFrom().isBefore(LocalDate.now())
                    && affiliate.validUntil().isAfter(LocalDate.now()))) {
                throw new IllegalArgumentException("This coupon is not active");
            }
            List<ValidItem> valid = new ArrayList<>();
            for (Item item : cart.items()) {
                Product product = productRepo.get(item.sku());
                if (product == null) {
                    throw new IllegalArgumentException("INVALID SKUs");
                } else {
                    valid.add(new ValidItem(product, item.quantity()));
                }
            }
            return new ValidatedCart(affiliate, valid);
        }
    }




    static class OrderProcessorREFACTORED_4 {
        private Validator validator;
        private AffiliateService affiliateService;
        private ProductRepo productRepo;
        private OrderRepo orderRepo;

        @Post
        @Path("/checkout")
        public void checkout(Cart cart) {
            Affiliate affiliate = affiliateService.findAffiliate(cart.couponCode());
            ValidatedCart validCart = validate(cart, affiliate);
            // ------------------------------------------------------------------
            //                        BOUNDARY
            // ------------------------------------------------------------------
            Order order = this.createOrder(validCart);
            this.orderRepo.save(order);
        }

        Order createOrder(ValidatedCart cart) {
            USD subtotal = USD.zero();
            List<Product> products = new ArrayList<>();
            for (ValidItem item : cart.items()) {
                products.add(item.product());
                subtotal = USD.add(subtotal, item.product().price().multiply(item.qty()));
            }
            USD discount = subtotal.multiply(cart.affiliate().discountPercent());
            USD total = USD.subtract(subtotal, discount);
            return Order.fromDetails(products, cart.affiliate(), subtotal, total);
        }

        ValidatedCart validate(Cart cart, Affiliate affiliate) {
            Set<ConstraintViolation<Cart>> errors = validator.validate(cart);
            if (!errors.isEmpty()) {
                throw new ConstraintViolationException(errors);
            }
            if (affiliate == null || !(affiliate.validFrom().isBefore(LocalDate.now())
                    && affiliate.validUntil().isAfter(LocalDate.now()))) {
                throw new IllegalArgumentException("This coupon is not active");
            }
            List<ValidItem> valid = new ArrayList<>();
            for (Item item : cart.items()) {
                Product product = productRepo.get(item.sku());
                if (product == null) {
                    throw new IllegalArgumentException("INVALID SKUs");
                } else {
                    valid.add(new ValidItem(product, item.quantity()));
                }
            }
            // check stock availability
            //
            return new ValidatedCart(affiliate, valid);
        }
    }


    record Person(
        String name,
        int age
    ){}

    @Test
    void asdfasdfasd() {
        Decision<Person> person = new MyValidator().validate(new Person("Bob", 9));
    }


    record Validated_<A>(A value){}

    sealed interface Status2<A> {
        record Unvalidated<A>(A value) implements Status2<A> {}
        record Validated<A>(A value) implements Status2<A> {}
    }

    sealed interface Status {
        record Raw() implements Status {}
        record Validated() implements Status {}
    }

    sealed interface Foo {
        record A() implements Foo {}
        record B() implements Foo {}
        record C() implements Foo {}
    }

    sealed interface Bar {
        record A() implements Bar {}
        record B() implements Bar {}
    }
//    record Input<T extends Bar>(String name){}

//    Input<Bar.B> toB(Input<Bar.A> a) {
//        return launder(a);
//    }
//
//    @SuppressWarnings("unchecked")
//    static <T1 extends Bar, T2 extends Bar> Input<T2> launder(Input<T1> xs) {
//        return (Input<T2>) xs;
//    }
//
//    @SuppressWarnings("unchecked")
//    static <T1, T2> T2 launderAnything(T1 xs) {
//        return (T2) xs;
//    }
//
//    @SuppressWarnings("unchecked")
//    static <T1 extends Bar, T2 extends Bar> Object<T2> launderObj(Object<T1> xs) {
//        return (Input<T2>) xs;
//    }

    @SuppressWarnings("unchecked")
    static <A extends Status,B extends Status> MyInputData<B> tsf(MyInputData<A> xs) {
        return (MyInputData<B>) xs;
    }

    MyInputData<Status.Validated> asdasdf(MyInputData<Raw> stuff) {
        return tsf(stuff);
    }

    record MyThing(String name) {}

    Status2.Validated<MyThing> validate(Status2.Unvalidated<MyThing> thing) {
        return new Status2.Validated<>(thing.value());
    }


    void myDangerousMethod(Validated<MyThing> thing) {

    }


    record MyInputData<A extends Status>(
            String id,
            String name
    ){}

    @Test
    void doIt() {
        MyInputData<Raw> result = new MyInputData<>("id", "name");
        System.out.println(result);
//        MyInputData<Validated> r2 = launderAnything(result);
//        System.out.println(r2);

//        new Validated<>(new MyThing("Hello!!!"));
    }

    String getVersion(byte[] data) {
        validateHeader(data);
        return format("%s.%s", data[0], data[1]);
    }

    int getResponseType(byte[] data) {
        validateHeader(data);
        return data[3];
    }
    int getBodyLength(byte[] data) {
        validateHeader(data);
        return ByteBuffer.wrap(data, 3, 5).getInt();
    }

    enum ResponseType {TODO, TODO2}
    // INSTEAD DO:
    record Version(int major, int minor){}
    record Header(Version version, ResponseType type){}

    Header parseHeader(byte[] data) {
        if (!(data.length > HEADER_LENGTH && data[2] == MAGIC_BYTE)) {
            throw new IllegalArgumentException("Kaboom");
        } else {
            return new Header(
                new Version(data[0], data[1]),
                ResponseType.TODO
            );
        }
    }

    record NonEmpty<A>(A first, List<A> rest) {
        public List<A> value() {
            return Stream.concat(Stream.of(first), rest.stream()).toList();
        }
    }




    int HEADER_LENGTH = 12;
    int MAGIC_BYTE = 3;

    void validateHeader(byte[] data) {

    }





    enum TheirEnum {
        POP,
        TEA,
        COFFEE
    }

    enum OurEnum {
        COFFEE,
        SODA,
        TEA
    }

    interface DinkCatalog {
        List<TheirEnum> listAvailableDrinks();
    }

    class RobotBartender {
        private DinkCatalog catalog;

        private List<OurEnum> listAvailableDrinks() {
            List<TheirEnum> drinks = this.catalog.listAvailableDrinks();
            return drinks.stream().map(drink -> switch (drink) {
                case TheirEnum.POP -> OurEnum.SODA;
                case TheirEnum.COFFEE -> OurEnum.COFFEE;
                case TheirEnum.TEA -> OurEnum.TEA;
                default -> throw new IllegalArgumentException("wot");
            }).toList();
        }
    }




}

