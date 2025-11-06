package dop.chapter09;

import dop.chapter05.the.existing.world.Annotations.Entity;
import dop.chapter05.the.existing.world.Annotations.Id;
import dop.chapter09.Annotations.Path;
import dop.chapter09.Annotations.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static dop.chapter09.Utils.*;
import static java.util.Optional.of;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/*
Setup:

Global retail.
Microservices. No one stable view of the world.

We know IDs and broker the checkout flow. That's it.

ProductDetailService tells us things like brand, color, manufacturer

InventoryService tells us how much stock we have on hand

Adding to the complexity, the same product can be sold by multiple
vendors, each at different prices. So:

OfferService tells us the price being offered for the product through the
current seller.

By combining all of these, we can get a complete view of the world

 */
@Log4j2
public class UpdatedSketch {

    enum CartType { ACTIVE, SAVED }


    interface Session {
        Cart findCart();
        String getCustomer();
        String getRegion();
        String getMarketplace();
        String getSegment();
    }



    @Data
    @AllArgsConstructor
    class Details {
        String brand;
        String category;
        String title;
    }


    @Data
    class Product {

    }

    @Data
    static class ProductDetailsRequest {
        String productId;
        String marketplace;
        String region;
    }

    @Data
    class DetailsResponse {
        String promotedSeller;
        String brand;
        String manufacturer;
        String color;
        String material;
        String category;
        String subCategory;
        String title;
    }
    interface ProductDetailService {
        DetailsResponse getProductDetails(String productId);
        DetailsResponse getProductDetails(String productId, String marketplaceId, String region);
    }

    @Data
    static class InventoryCheckResponse {
        long quantityAvailable;
    }
    interface InventoryService {
        InventoryCheckResponse checkInventory(String productId);
    }



    @Data
    @Builder
    static class GetOfferRequest {
        List<OfferItem> items;
    }

    @Data
    @Builder
    static class OfferItem {
        String productId;
        String offerId;
        Metadata metadata;
    }



    @Data
    static class GetOfferResponse {
        List<Offer> offers;
    }

    @Data
    static class Offer {
        String productId;
        String offerId;
        BigDecimal latestPrice;
        Currency currency;
    }

    @Data
    @AllArgsConstructor
    static class Metadata {
        String brand;
        String color;
    }
    interface OfferService {
        GetOfferResponse getOffers(List<String> sellerIds);
    }

    sealed interface PaymentMethod {
        record CreditCardTODO() implements PaymentMethod {}
        record WalletTODO() implements PaymentMethod {}
        record InvoiceTODO() implements PaymentMethod {}

    }

    @Test
    void howDoTreeSetsWork() {
        TreeSet<CartItem> set = new TreeSet<>(Comparator.comparing(CartItem::getProductId).thenComparing(CartItem::getSellerId));
        CartItem item1 = new CartItem();
        item1.setSellerId("1");
        item1.setProductId("1");
        set.add(item1);

        CartItem item2 = new CartItem();
        item2.setSellerId("2");
        item2.setProductId("2");
        set.add(item2);

        CartItem item3 = new CartItem();
        item3.setSellerId("1");
        item3.setProductId("1");
        set.add(item3);
        set.stream().forEach(System.out::println);
    }

    @With
    record Item(
            Optional<String> itemId,
            String productId,
            String sellerId,
            long quantityDesired,
            long quantityAvailable,
            Details details,
            BigDecimal lastNotifiedPrice,
            BigDecimal currentPrice
    ){
        Item mergeQty(Item other) {
            return this.withQuantityDesired(this.quantityDesired() + other.quantityDesired());
        }
    }


    @Data
    @Entity
    public static class Cart {
        @Id
        String cardId;
        List<CartItem> active;
        List<CartItem> saved;
        BigDecimal subtotal;
        BigDecimal shippingTotal;
        BigDecimal subtotalWithTax;
        boolean overnightShippingAvailable;
        boolean hasPriceChanged;
        String shippingAddressId;
        String destinationCountryCode;
        List<CartItem> recommended;
        boolean hasGiftOptions;
        PaymentMethod paymentMethod;
        Currency customerCurrency;
        // ...

        public void recalculateSubtotals() {
            BigDecimal subtotal;
            BigDecimal overnightEligible;
            for (CartItem item : this.active) {

            }
            this.subtotal = this.active.stream()
                .map(item -> item.getLatestPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        public static CartItem mergeQty(CartItem a, CartItem b) {
            return a.withQuantity(a.getQuantity() + b.getQuantity());
        }

        public void addOrUpdateItems(List<CartItem> items) {
            Map<String, CartItem> activeItemsById =
                indexBy(this.active, (item) -> item.getProductId() + item.getSellerId());
            Map<String, CartItem> savedItemsById =
                indexBy(this.saved, (item) -> item.getProductId() + item.getSellerId());
            for (CartItem incoming : items) {
                String key = incoming.getProductId() + incoming.getSellerId();
                if (activeItemsById.containsKey(key)) {
                    CartItem existing = activeItemsById.get(key);
                    existing.setQuantity(existing.getQuantity() + incoming.getQuantity());
                }
                else if (savedItemsById.containsKey(key)) {
                    // If the customer is adding an item that's already in their
                    // saved cart, we automatically promote it back to active.
                    // SPECIAL CASE:
                    // Wait. We update the activeItems first, so we will NOT eval
                    // this branch. Thus, we won't add any duplicates. But we will
                    // be in a potentially invalid state until we go and correct the
                    // inventory amounts later
                    CartItem saved = savedItemsById.get(key);
                    this.getSaved().remove(saved);
                    saved.setQuantity(saved.getQuantity() + incoming.getQuantity());
                    this.active.add(saved);
                }
                else {
                    // Seller ID still might be null at this point.
                    // So, it defacto won't match any existing items, which MUST
                    // have an SellerID. Other validation ensures that the products IDs
                    // are unique, so everything else is "safe" to add to the list.
                    this.active.add(incoming);
                }
            }
        }
    }

    record Identifier(String productId, String sellerId) implements Comparable<Identifier> {
        @Override
        public int compareTo(Identifier o) {
            return 0;
        }
    }

    static Comparator<CartItem> byKey = Comparator.comparing(CartItem::getProductId).thenComparing(CartItem::getSellerId);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class CartItem {
        @Id
        String id;
        String productId;
        String sellerId;
        long quantity;
        long quantityAvailable;
        BigDecimal lastNotifiedPrice;
        BigDecimal latestPrice;
        Details productDetails;

        public Identifier key() {
            return new Identifier(productId, sellerId);
        }

        CartItem copy() {
            // ...
            return this.toBuilder().build();
        }

        CartItem withQuantity(long qty) {
            return this.toBuilder().quantity(qty).build();
        }
    }




    @Data
    class ShoppingCartResponse{
        List<CartItem> cartItems;
    }

    record Response(){
        static Response OK(Object obj) {
            return new Response();
        }
        static Response BadRequest(Object obj) {
            return new Response();
        }
    }


    interface CartRepo {
        Cart loadCart(String customerId);
        void save(Cart cart);
    }

    record Error(){}

    interface Validator {
        Set<Error> validate(List<CartItem> items);
    }


    record PlaceOrderRequest(
        String cvvToken
    ){}


    @Data
    public static class AddItemRequest {
        @NotEmpty
        @Valid
        List<ItemInfo> items;
    }

    @Data
    static class ItemInfo {
        @NotBlank @NotNull
        String productId;
        String sellerId;
        @Min(1)
        long quantity;
        BigDecimal displayedPrice;
    }



    record ValidationError() {}
    interface CartItemValidator {
        Set<ValidationError> validate(List<ItemInfo> items);
    }

    record ProductSeller(String productId, String sellerId) {
        public static ProductSeller fromItem(Item item) {
            return new ProductSeller(item.productId(), item.sellerId());
        }
    }


    @Path("cart/")
    class InitialExampleBAD {
        ProductDetailService productService;
        OfferService sellerService;
        InventoryService inventoryService;
        CartRepo cartRepo;
        CartItemValidator cartItemValidator;
        //
        // Chicken and egg problems:
        // we can't do inventory checks until we know the final amount
        // of quantity being purchased after merging with existing data
        //
        // General Problem:
        //   * Secret knowledge everywhere
        //
        // Boundary Problems:
        //   * The database representation and the presentation are the same object
        //     Each carries around unnecessary cruft to satisfy the needs of the other
        //   * Using partially constructed objects to avoid creating new ones. Plus, "we're
        //     eventually creating them anyways"
        //   * Cart invariants aren't enforced and _cannot_ be enforced due to the design
        //   * Everyone gets to reach inside of cart and twiddle its knobs.
        //   * Shotgun parsing + working with potentially invalid data + making decisions
        //     you only find out are good or bad later.
        //
        //
        // Some standard OOP problems:
        //   Sequential Coupling: the sequencing of our calls is really important.
        //   When _can't_ enforce invariants because our design relies on
        //   Our cart is in an invalid state most of the time!
        //
        //
        // validation
        //   - fields look OK
        //          - Has inventory (we can't do this till we know the true totals)
        //   - Not already in cart
        //   - Items being added are unique (within their own area)
        // if product + offer already exists, merge and update counts
        // else: append to existing list
        // Next:
        //    enrich with product details
        //    enrich with latest pricing info and set flag if prices have changed
        //    if out of stock, move item to saved?
        @Post
        @Path("/add-items")
        public Response addCartItems(Session session, AddItemRequest request) {
            Cart cart = cartRepo.loadCart(session.getCustomer());
            Set<ValidationError> errors = cartItemValidator.validate(request.getItems());
            if (!errors.isEmpty()) {
                return Response.BadRequest(errors);
            }

            cart.addOrUpdateItems(convertToCartItem(request.getItems()));
            cart.getActive().forEach(item -> {
                updateMetadata(session, item);
                updateInventory(item);
            });
            this.checkForPriceDriftAndUpdate(cart);
            // Adjust cart based on current inventory status
            for (CartItem item : cart.getActive()) {
                if (item.getQuantity() > item.getQuantityAvailable()) {
                    if (item.getQuantityAvailable() > 0) {
                        CartItem copy = item.copy();
                        copy.setId(null);
                        copy.setQuantity(item.getQuantity() - item.getQuantityAvailable());
                        item.setQuantity(item.getQuantityAvailable());
                        cart.getSaved().add(copy);
                    } else {
                        cart.getActive().remove(item);
                        cart.getSaved().add(item);
                    }
                }
            }
            cart.recalculateSubtotals();
            this.updateIsQualifiedForOvernightShipping(cart);
            this.updateRecommendedItems(cart);
            cartRepo.save(cart);
            return Response.OK(cart);
        }

        private void updateIsQualifiedForOvernightShipping(Cart cart) {

        }

        private void updateRecommendedItems(Cart cart) {

        }

        private List<CartItem> convertToCartItem(List<ItemInfo> items) {
            return items.stream().map(incoming -> {
                CartItem item = new CartItem();
                item.setProductId(item.getProductId());
                item.setSellerId(item.getSellerId());
                item.setQuantity(item.getQuantity());
                return item;
            }).toList();
        }

        private void updateInventory(CartItem item) {
            InventoryCheckResponse response = inventoryService.checkInventory(item.getProductId());
            item.setQuantityAvailable(response.getQuantityAvailable());
        }

        private void checkForPriceDriftAndUpdate(Cart cart) {
            // sequential coupling - we can only call this _after_ we call
            // update metadata because that's what sets the SellerID
            GetOfferResponse response = sellerService.getOffers(cart.getActive().stream()
                    .map(CartItem::getSellerId)
                    .toList());
            Map<String, CartItem> itemsBySellerId = cart.getActive().stream()
                    .collect(toMap(CartItem::getSellerId, Function.identity()));

            for (Offer offer : response.getOffers()) {
                if (itemsBySellerId.containsKey(offer.getOfferId())) {
                    CartItem cartItem = itemsBySellerId.get(offer.getOfferId());
                    if (!offer.getLatestPrice().equals(cartItem.getLastNotifiedPrice())) {
                        cartItem.setLatestPrice(offer.getLatestPrice());
                        cart.setHasPriceChanged(true);
                    }
                }
            }
        }

        // NOTE! These have the APPEARANCE of being business logic. They have action-y names
        // and sound important "updateInventory," but they're just busywork unrelated to our
        // core work of adding things to carts.
        private void updateMetadata(Session session, CartItem item) {
            // Secret Knowledge - Seller ID can be null!
            String sellerId = item.getSellerId();
            Details details = item.getProductDetails();
            try {
                DetailsResponse response = productService.getProductDetails(
                    item.getProductId(),
                    session.getMarketplace(),
                    session.getRegion()
                );
                details.setCategory(response.getCategory());
                details.setCategory(response.getBrand());
                details.setTitle(response.getTitle());
                if (sellerId == null) {
                    // They've added from the listing page rather than
                    // picking a different seller. Add the [blah]
                    sellerId = response.getPromotedSeller();
                }
                item.setSellerId(sellerId);
            } catch (Exception e) {
                log.error("THAT AIN'T NO PRODUCT I EVER SEEN");
                throw e;
            }
        }
    }

    @Path("cart/")
    class InitialExampleForCopyPaste {
        ProductDetailService productService;
        OfferService sellerService;
        InventoryService inventoryService;
        CartRepo cartRepo;
        CartItemValidator cartItemValidator;
        @Post @Path("/add-items")
        public Response addCartItems(Session session, AddItemRequest request) {
            Cart cart = cartRepo.loadCart(session.getCustomer());
            cartItemValidator.validate(request.getItems());

            cart.addOrUpdateItems(convertToCartItem(request.getItems()));
            cart.getActive().forEach(item -> {
                updateMetadata(session, item);
                updateInventory(item);
            });
            this.checkForPriceDriftAndUpdate(cart);
            for (CartItem item : cart.getActive()) {
                if (item.getQuantity() > item.getQuantityAvailable()) {
                    if (item.getQuantityAvailable() > 0) {
                        CartItem copy = item.copy();
                        copy.setId(null);
                        copy.setQuantity(item.getQuantity() - item.getQuantityAvailable());
                        item.setQuantity(item.getQuantityAvailable());
                        cart.getSaved().add(copy);
                    } else {
                        cart.getActive().remove(item);
                        cart.getSaved().add(item);
                    }
                }
            }
            cart.recalculateSubtotals();
            cartRepo.save(cart);
            return Response.OK(cart);
        }

        private List<CartItem> convertToCartItem(List<ItemInfo> items) {
            return items.stream().map(incoming -> {
                CartItem item = new CartItem();
                item.setProductId(item.getProductId());
                item.setSellerId(item.getSellerId());
                item.setQuantity(item.getQuantity());
                return item;
            }).toList();
        }

        private void updateInventory(CartItem item) {
            InventoryCheckResponse response = inventoryService.checkInventory(item.getProductId());
            item.setQuantityAvailable(response.getQuantityAvailable());
        }

        private void checkForPriceDriftAndUpdate(Cart cart) {
            GetOfferResponse response = sellerService.getOffers(cart.getActive().stream()
                    .map(CartItem::getSellerId)
                    .toList());
            Map<String, CartItem> itemsBySellerId = indexBy(cart.getActive(), CartItem::getSellerId);

            for (Offer offer : response.getOffers()) {
                if (itemsBySellerId.containsKey(offer.getOfferId())) {
                    CartItem cartItem = itemsBySellerId.get(offer.getOfferId());
                    if (!offer.getLatestPrice().equals(cartItem.getLastNotifiedPrice())) {
                        cartItem.setLatestPrice(offer.getLatestPrice());
                        cart.setHasPriceChanged(true);
                    }
                }
            }
        }

        private void updateMetadata(Session session, CartItem item) {
            // Secret Knowledge - Seller ID can be null!
            String sellerId = item.getSellerId();
            Details details = item.getProductDetails();
            // implicit runtime exception if product is not found
            DetailsResponse response = productService.getProductDetails(item.getProductId());
            details.setCategory(response.getCategory());
            details.setCategory(response.getBrand());
            details.setTitle(response.getTitle());
            if (sellerId == null) {
                sellerId = response.getPromotedSeller();
            }
            item.setSellerId(sellerId);
        }
    }




    public static void assertAll(Collection<Item> items, Predicate<Item> pred) throws IllegalArgumentException {
        if (items.stream().allMatch(pred)) {
            throw new IllegalArgumentException("TODO");
        }
    }

    record CartChangeRequest(
        Map<ProductSeller, Item> active,
        Map<ProductSeller, Item> saved,
        List<Item> changes
    ) {}
    record UnbalancedCart(
        Map<ProductSeller, Item> active,
        Map<ProductSeller, Item> saved
    ) {}

    record ValidatedCart(
        Map<ProductSeller, Item> active,
        Map<ProductSeller, Item> saved
    ) {
        ValidatedCart {
            assertAll(active.values(), (item) -> item.quantityDesired() > item.quantityAvailable());
            assertAll(active.values(), (item) -> item.quantityDesired() > 0);
            assertAll(saved.values(),  (item) -> item.quantityDesired() > 0);
        }
    }

    class ImmutableCart {
        static ValidatedCart balance(UnbalancedCart cart) {
            Map<ProductSeller, Item> finalActive = new HashMap<>();
            Map<ProductSeller, Item> finalSaved = new HashMap<>();
            for (Map.Entry<ProductSeller, Item> entry : cart.active().entrySet()) {
                switch (entry.getValue()) {
                    case Item i when i.quantityAvailable() == 0 ->
                        finalSaved.put(entry.getKey(), i);
                    case Item i when i.quantityDesired() > i.quantityAvailable() ->
                        finalSaved.put(entry.getKey(), i);
                    default ->
                        finalActive.put(entry.getKey(), entry.getValue());
                }
            }
            return new ValidatedCart(finalActive, finalSaved);
        }

        public static ValidatedCart addItems(CartChangeRequest request) {
            UnbalancedCart unbalancedCart = mergeItems(request);
            return balance(unbalancedCart);
        }

        public static UnbalancedCart mergeItems(CartChangeRequest request) {
            Map<ProductSeller, Item> incoming = indexBy(request.changes(), ProductSeller::fromItem);

            var merged = intersectBy(request.active(), incoming, Item::mergeQty);
            var keepInActive = except(request.active(), incoming);
            var seeIfTheseAreInSaved = except(incoming, request.active());

            var promote = intersectBy(request.saved(), seeIfTheseAreInSaved, Item::mergeQty);
            var leaveInSaved = except(request.saved, seeIfTheseAreInSaved);
            var addThese = except(seeIfTheseAreInSaved, request.saved());

            var newActive = union(merged, keepInActive, promote, addThese);
            return new UnbalancedCart(newActive, leaveInSaved);
        }


        public static boolean hasNewerPrice(ValidatedCart cart) {
            return cart.active().values().stream().anyMatch(item ->
                !item.currentPrice.equals(item.lastNotifiedPrice)
            );
        }

        public static BigDecimal subtotal(ValidatedCart cart) {
            return cart.active()
                .values()
                .stream()
                .map(x -> x.currentPrice().multiply(BigDecimal.valueOf(x.quantityDesired())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    record RawItem(
        Optional<String> itemId,
        String productId,
        Optional<String> sellerId,
        long quantity,
        BigDecimal notifiedPrice
    ){}

    @Path("cart/")
    class InitialExampleIMPROVED {
        ProductDetailService productService;
        OfferService sellerService;
        InventoryService inventoryService;
        CartRepo cartRepo;
        CartItemValidator cartItemValidator;


        CartChangeRequest loadStuff(Session session, AddItemRequest request) {
            Cart legacy = session.findCart();
            return new CartChangeRequest(
                indexBy(legacy.getActive().stream().map(this::load), ProductSeller::fromItem),
                indexBy(legacy.getSaved().stream().map(this::load), ProductSeller::fromItem),
                request.getItems().stream().map(this::load).toList()
            );
        }

        private RawItem toRaw(CartItem item) {
            return new RawItem(of(item.getId()), item.getProductId(),
                    of(item.getSellerId()), item.getQuantity(), item.getLastNotifiedPrice());
        }
        private RawItem toRaw(ItemInfo item) {
            return new RawItem(Optional.empty(), item.getProductId(),
                    of(item.getSellerId()), item.getQuantity(), item.getDisplayedPrice());
        }
        Item load(CartItem item) {
            return load(toRaw(item));
        }
        Item load(ItemInfo item) {
            return load(toRaw(item));
        }
        Item load(RawItem item) {
            InventoryCheckResponse inventoryResponse = inventoryService.checkInventory(item.productId());
            DetailsResponse details = productService.getProductDetails(item.productId());
            String chosenSeller = item.sellerId().orElse(details.getPromotedSeller());
            GetOfferResponse response = sellerService.getOffers(List.of(chosenSeller));
            return new Item(
                item.itemId(),
                item.productId(),
                chosenSeller,
                item.quantity(),
                inventoryResponse.getQuantityAvailable(),
                new Details(details.getBrand(), details.getCategory(), details.getTitle()),
                item.notifiedPrice(),
                response.getOffers().getFirst().getLatestPrice()
            );
        }


        @Post
        @Path("/add-items")
        public Response addCartItems(Session session, AddItemRequest request) {
            CartChangeRequest cart = loadStuff(session, request);
            Set<ValidationError> errors = this.validate(cart);
            if (!errors.isEmpty()) {
                return Response.BadRequest(errors);
            }
            // Above here, we don't know if what we're dealing with is valid
            // ---------------------------------------------------
            //                     BOUNDARY
            // ---------------------------------------------------
            // Below here
            ValidatedCart validCart = ImmutableCart.addItems(cart);
            this.setFreeShippingUpsell(null);
            this.updateRecommendedItems(null); 

            return Response.OK(this.save(validCart, session));
        }

        Set<ValidationError> validate(CartChangeRequest cart) {
            return Set.of();
        }

        private void setFreeShippingUpsell(Cart cart) {

        }

        private void updateRecommendedItems(Cart cart) {

        }

        Cart save(ValidatedCart cart, Session session) {
            Cart legacyCart = session.findCart();
            legacyCart.setHasPriceChanged(ImmutableCart.hasNewerPrice(cart));
            legacyCart.setSubtotal(ImmutableCart.subtotal(cart));
            legacyCart.setActive(cart.active().values().stream().map(this::toCartItem).toList());
            legacyCart.setSaved(cart.saved().values().stream().map(this::toCartItem).toList());
            cartRepo.save(legacyCart);
            return legacyCart;
        }

        private CartItem toCartItem(Item item) {
            CartItem cartItem = new CartItem();
            cartItem.setId(item.itemId().orElse(null));
            cartItem.setProductId(item.productId());
            cartItem.setSellerId(item.sellerId());
            cartItem.setQuantity(item.quantityDesired());
            cartItem.setQuantityAvailable(item.quantityAvailable());
            // ...
            return cartItem;
        }
    }

    @Test
    void whatDoesAddingNullDo() {
        System.out.println("Hello " + null);
    }
}
