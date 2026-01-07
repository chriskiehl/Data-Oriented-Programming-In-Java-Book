/**
 * NOTE!
 * The current listings are quick copy/paste examples
 * from the book rather than the usual guided tour.
 *
 * These will be redone in the usual style once all
 * revisions are done on the book.
 */
package dop.chapter09;

import dop.chapter09.external.world.Annotations.Entity;
import dop.chapter09.external.world.Annotations.Id;
import dop.chapter09.external.world.Annotations.Path;
import dop.chapter09.external.world.Annotations.Post;
import dop.chapter09.external.world.EverythingElse.Response;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import lombok.experimental.Accessors;
import org.checkerframework.common.aliasing.qual.Unique;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Listings {
    // These are defined out here so they can be referenced
    // as needed throughout the listings.
    @Entity
    @Data
    public static class Cart {
        @Id
        String cartId;
        List<CartItem> active;
        List<CartItem> saved;
        BigDecimal subtotal;
        boolean hasPriceChanged;
        List<CartItem> recommended;
        boolean hasGiftOptions;

        public void addOrUpdateItems(AddItemsRequest request) {}
        public void recalculateSubtotal(){}
    }
    @Data
    public static class CartItem {
        @Id
        String id;
        String productId;
        String sellerId;
        long desiredQty;
        long availableQty;
        BigDecimal latestPrice;
        BigDecimal lastNotifiedPrice;
        Details productDetails;

        CartItem copy() {
            // this method isn't implemented in the book.
            // Assume it creates a fresh copy.
            return this;
        }
    }


    @Data
    @AllArgsConstructor
    public static class Details {
        String category;
        String color;
        // and so on.
    }

    @Data
    public static class AddItemsRequest {
        @NotEmpty @Unique List<ItemInfo> items;
    }
    @Data
    public static class ItemInfo {
        @NotBlank @NotNull String productId;
        String sellerId;
        @Min(1) long quantity;
        BigDecimal displayedPrice;
    }

    /**
     * ───────────────────────────────────────────────────────
     * Dependencies used throughout the examples
     * ───────────────────────────────────────────────────────
     */

    interface CartRepo {
        Cart loadCart(String customerId);
        void save(Cart cart);
    }

    interface Session {
        String getCustomer();
    }

    interface ValidationError {}

    interface CartItemValidator {
        Set<ValidationError> validate(AddItemsRequest request);
    }
    interface InventoryService {
        Inventory checkInventory(String productId);
    }

    interface Inventory {
        long getAvailableQty();
    }

    interface SellerService {
        Offer getOffer(String sellerIds);
        List<Offer> getOffers(Set<String> sellerIds);
    }
    interface Offer {
        BigDecimal getCurrentPrice();
    }

    interface ProductService {
        ProductInfo getProductDetails(String productId);
    }

    @Data
    public static class ProductInfo {
        String category;
        String promotedSeller;
    }

    public static <K,V> Map<K,V> toMap(
            Collection<V> items,
            Function<V,K> keyGetter) {
        return items.stream().collect(Collectors.toMap(keyGetter, Function.identity()));
    }

    public static <K, V1, V2> Map<K,V2> mapValues(
            Map<K,V1> m,
            Function<V1, V2> f) {
        return m.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> f.apply(entry.getValue()))
                );
    }




    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.1
     * ───────────────────────────────────────────────────────
     */
    void listing9_1() {
        @Entity
        @Data
        @Accessors(fluent = true)
        class Cart {
            @Id
            String cartId;
            List<CartItem> active;
            List<CartItem> saved;
            BigDecimal subtotal;
            boolean hasPriceChanged;
            // ...
            List<CartItem> recommended;
            boolean hasGiftOptions;
            // plus dozens more attributes
            // and numerous methods for modifying the cart
        }
        @Data
        @Accessors(fluent = true)
        class CartItem {
            @Id
            String id;
            String productId;
            String sellerId;
            long desiredQty;
            long availableQty;
            BigDecimal latestPrice;
            BigDecimal lastNotifiedPrice;
            Details productDetails;
            // ...
            // and another dozen or so here, too
            // ...
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.2
     * ───────────────────────────────────────────────────────
     */
    void listing9_2() {
        class __ {
            class Cart {
                List<CartItem> active;
                List<CartItem> saved;

                private String qualifiedId(CartItem item) {
                    return item.getProductId() + item.getSellerId();
                }
                public void addOrUpdateItems(AddItemsRequest request) {
                    List<CartItem> items = this.convertToCartItems(request);
                    var activeItemsById = toMap(this.active, this::qualifiedId);
                    var savedItemsById = toMap(this.saved, this::qualifiedId);
                    for (CartItem incoming : items) {
                        String key = this.qualifiedId(incoming);
                        if (activeItemsById.containsKey(key)
                                && !savedItemsById.containsKey(key)) {
                            CartItem existing = activeItemsById.get(key);
                            existing.setDesiredQty(existing.getDesiredQty() + incoming.getDesiredQty());
                        }
                        else if (savedItemsById.containsKey(key)) {
                            CartItem saved = savedItemsById.get(key);
                            this.saved.remove(saved);
                            saved.setDesiredQty(saved.getDesiredQty() + incoming.getDesiredQty());
                            if (activeItemsById.containsKey(key)) {
                                var activeItem = activeItemsById.get(key);
                                activeItemsById.get(key).setDesiredQty(activeItem.getDesiredQty() + saved.getDesiredQty());
                            } else {
                                this.active.add(saved);
                            }
                        }
                        else {
                            this.active.add(incoming);
                        }
                    }
                }
                private List<CartItem> convertToCartItems(AddItemsRequest request) {
                    return request.getItems().stream().map(external -> {
                        CartItem item = new CartItem();
                        item.setProductId(external.getProductId());
                        item.setSellerId(external.getSellerId());
                        item.setDesiredQty(external.getQuantity());
                        return item;
                    }).toList();
                }
            }
            public static <K,V> Map<K,V> toMap(
                    Collection<V> items,
                    Function<V,K> keyGetter) {
                return items.stream().collect(Collectors.toMap(keyGetter, Function.identity()));
            }
        }
    }




    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.3
     * ───────────────────────────────────────────────────────
     */
    void listing9_3() {
        class __ {
            class StoreController {
                CartRepo cartRepo;
                CartItemValidator cartItemValidator;
                InventoryService inventoryService;
                SellerService sellerService;
                ProductService productService;

                @Post
                @Path("/add-items")
                public Response addCartItems(Session session, AddItemsRequest request) {
                    Cart cart = cartRepo.loadCart(session.getCustomer());
                    var errors = cartItemValidator.validate(request);
                    if (!errors.isEmpty()) {
                        return Response.BAD_REQUEST(errors);
                    }
                    cart.addOrUpdateItems(request);
                    cart.getActive().forEach(item -> {
                        updateMetadata(item);
                        updateInventory(item);
                    });
                    this.checkForPriceDriftAndUpdate(cart);
                    for (CartItem item : cart.getActive()) {
                        if (item.getDesiredQty() > item.getAvailableQty()) {
                            if (item.getAvailableQty() > 0) {
                                CartItem copy = item.copy();
                                copy.setDesiredQty(item.getDesiredQty() - item.getAvailableQty());
                                cart.getSaved().add(copy);
                                item.setDesiredQty(item.getAvailableQty());
                            } else {
                                cart.getActive().remove(item);
                                cart.getSaved().add(item);
                            }
                        }
                    }
                    cart.recalculateSubtotal();
                    this.checkIfEligibleForFreeShipping(cart);
                    this.updateRecommendedItems(cart);
                    cartRepo.save(cart);
                    return Response.OK(cart);
                }

                void checkIfEligibleForFreeShipping(Cart cart){}
                void updateRecommendedItems(Cart cart){}

                private void updateInventory(CartItem item) {
                    var response = inventoryService.checkInventory(item.getProductId());
                    item.setAvailableQty(response.getAvailableQty());
                }
                private void checkForPriceDriftAndUpdate(Cart cart) {
                    for (CartItem item : cart.getActive()) {
                        Offer offer = sellerService.getOffer(item.getSellerId());
                        if (!offer.getCurrentPrice().equals(item.getLastNotifiedPrice())) {
                            item.setLatestPrice(offer.getCurrentPrice());
                            cart.setHasPriceChanged(true);
                        }
                    }
                }
                private void updateMetadata(CartItem item) {
                    String sellerId = item.getSellerId();
                    Details details = item.getProductDetails();
                    var response = productService.getProductDetails(item.getProductId());
                    details.setCategory(response.getCategory());
                    // Setting brand, color, material etc. Elided for space
                    if (sellerId == null) {
                            sellerId = response.getPromotedSeller();
                    }
                    item.setSellerId(sellerId);
                }
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.4 through 9.5
     * ───────────────────────────────────────────────────────
     * Strange modeling that blurs responsibilities
     */
    void listing9_4_to_9_5() {
        @Entity
        class Cart {
            @Id
            String cartId;
            List<CartItem> active;
            List<CartItem> saved;
            BigDecimal subtotal;
            boolean hasPriceChanged;
            List<CartItem> recommended;
            boolean hasGiftOptions;
        }
        class CartItem {
            // ...
            long desiredQty;
            long availableQty;  // What are inventory counts doing here?
            // ...
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.6 through 9.7
     * ───────────────────────────────────────────────────────
     *
     */
    void listing9_6_to_9_7() {
        class StoreController {
            CartRepo cartRepo;
            CartItemValidator cartItemValidator;
            InventoryService inventoryService;
            SellerService sellerService;
            ProductService productService;

            @Post
            @Path("/add-items")
            public Response addCartItems(Session session, AddItemsRequest request) {
                Cart cart = cartRepo.loadCart(session.getCustomer());
                var errors = cartItemValidator.validate(request);
                if (!errors.isEmpty()) {
                    return Response.BAD_REQUEST(errors);
                }
                cart.addOrUpdateItems(request);     // mutates!
                cart.getActive().forEach(item -> {  // mutates!
                    updateMetadata(item);  // mutates!
                    updateInventory(item);          // mutates!
                });
                this.checkForPriceDriftAndUpdate(cart);    // mutates!
                for (CartItem item : cart.getActive()) {
                    if (item.getDesiredQty() > item.getAvailableQty()) {
                        if (item.getAvailableQty() > 0) {
                            CartItem copy = item.copy();
                            copy.setDesiredQty(item.getDesiredQty() - item.getAvailableQty());
                            cart.getSaved().add(copy);                   // mutates!
                            item.setDesiredQty(item.getAvailableQty());  // mutates!
                        } else {
                            cart.getActive().remove(item);              // mutates!
                            cart.getSaved().add(item);                  // mutates!
                        }
                    }
                }
                cart.recalculateSubtotal();                  // mutates!
                this.checkIfEligibleForFreeShipping(cart);   // mutates!
                this.updateRecommendedItems(cart);           // mutates!
                cartRepo.save(cart);
                return Response.OK(cart);
            }

            void checkIfEligibleForFreeShipping(Cart cart){/*...*/}
            void updateRecommendedItems(Cart cart){/*...*/}
            private void updateInventory(CartItem item) {/*...*/}
            private void checkForPriceDriftAndUpdate(Cart cart) {/*...*/}
            private void updateMetadata(CartItem item) {/*...*/}
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.8
     * ───────────────────────────────────────────────────────
     *
     */
    void listing9_8() {
        class __ {
            class Cart {
                List<CartItem> active;
                // ...
                void addOrUpdateItems(AddItemsRequest request) {
                    // logic here
                    this.assertInvariants();
                }
                private void assertInvariants() {
                    // make sure we didn’t end up with duplicates, and that
                    // each item is in the right cart (active vs saved) based
                    // on available inventory and desired quantities
                }

                public List<CartItem> getActive() {
                    return this.active;
                }
            }

            void someActionSomewhereElse(Cart cart) {
                cart.getActive().getFirst().setDesiredQty(10);
            }
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.9
     * ───────────────────────────────────────────────────────
     */
    void listing9_9() {
        class StoreController {
            CartRepo cartRepo;
            CartItemValidator cartItemValidator;
            InventoryService inventoryService;
            SellerService sellerService;
            ProductService productService;

            @Post
            @Path("/add-items")
            public Response addCartItems(Session session, AddItemsRequest request) {
                Cart cart = cartRepo.loadCart(session.getCustomer());
                var errors = cartItemValidator.validate(request);
                if (!errors.isEmpty()) {
                    return Response.BAD_REQUEST(errors);
                }
                cart.addOrUpdateItems(request);
                // NEW!
                this.checkForPriceDriftAndUpdate(cart);    // If we move it up here everything breaks.
                cart.getActive().forEach(item -> {
                    updateMetadata(item);
                    updateInventory(item);
                });
                // [REMOVED] this.checkForPriceDriftAndUpdate(cart);
                // ...
                return Response.OK(cart);
            }

            void checkIfEligibleForFreeShipping(Cart cart){/*...*/}
            void updateRecommendedItems(Cart cart){/*...*/}
            private void updateInventory(CartItem item) {/*...*/}
            private void checkForPriceDriftAndUpdate(Cart cart) {/*...*/}
            private void updateMetadata(CartItem item) {/*...*/}
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.10
     * ───────────────────────────────────────────────────────
     */
    void listing9_10() {
        class StoreController {
            CartRepo cartRepo;
            CartItemValidator cartItemValidator;
            InventoryService inventoryService;
            SellerService sellerService;
            ProductService productService;

            @Post
            @Path("/add-items")
            public Response addCartItems(Session session, AddItemsRequest request) {
                Cart cart = cartRepo.loadCart(session.getCustomer());
                var errors = cartItemValidator.validate(request);
                if (!errors.isEmpty()) {
                    return Response.BAD_REQUEST(errors);
                }
                cart.addOrUpdateItems(request);            // Invalid
                cart.getActive().forEach(item -> {
                    updateMetadata(item);                  // Invalid
                    updateInventory(item);                 // Invalid
                });
                this.checkForPriceDriftAndUpdate(cart);    // Invalid
                for (CartItem item : cart.getActive()) {   // Invalid
                    // cart balancing logic                // Invalid
                }                                          // Invalid
                cart.recalculateSubtotal();                // Finally Valid!
                return Response.OK(cart);
            }

            void checkIfEligibleForFreeShipping(Cart cart){/*...*/}
            void updateRecommendedItems(Cart cart){/*...*/}
            private void updateInventory(CartItem item) {/*...*/}
            private void checkForPriceDriftAndUpdate(Cart cart) {/*...*/}
            private void updateMetadata(CartItem item) {/*...*/}
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.11
     * ───────────────────────────────────────────────────────
     */
    void listing9_11() {
        class Cart {
            List<CartItem> active;
            List<CartItem> saved;

            private String qualifiedId(CartItem item) {
                return item.getProductId() + item.getSellerId();
            }
            public void addOrUpdateItems(AddItemsRequest request) {
                List<CartItem> items = this.convertToCartItems(request);  // The original sin
                var activeItemsById = toMap(this.active, this::qualifiedId);
                var savedItemsById = toMap(this.saved, this::qualifiedId);
                for (CartItem incoming : items) {
                    // elided
                }
            }
            private List<CartItem> convertToCartItems(AddItemsRequest request) {
                return request.getItems().stream().map(external -> {
                    CartItem item = new CartItem();  // An item is creation, but only partially
                    item.setProductId(external.getProductId());
                    item.setSellerId(external.getSellerId());
                    item.setDesiredQty(external.getQuantity());
                    return item;
                }).toList();
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.12
     * ───────────────────────────────────────────────────────
     */
    void listing9_12() {
        class __ {
            class ItemInfo {
                @NotBlank @NotNull String productId;
                // The original developer didn’t call this out as nullable or
                // Optional, but we find out later in the code that it can be!
                String sellerId;
                @Min(1) long quantity;
                BigDecimal displayedPrice;
            }

            class StoreController {
                CartRepo cartRepo;
                CartItemValidator cartItemValidator;
                InventoryService inventoryService;
                SellerService sellerService;
                ProductService productService;

                @Post
                @Path("/add-items")
                public Response addCartItems(Session session, AddItemsRequest request) {
                    Cart cart = cartRepo.loadCart(session.getCustomer());
                    var errors = cartItemValidator.validate(request); // The nulls pass validation
                    if (!errors.isEmpty()) {
                        return Response.BAD_REQUEST(errors);
                    }
                    cart.addOrUpdateItems(request);     //
                    cart.getActive().forEach(item -> {  // And propagate through the code
                        updateMetadata(item);           //
                        updateInventory(item);
                    });
                    this.checkForPriceDriftAndUpdate(cart);
                    for (CartItem item : cart.getActive()) {
                        // ...
                    }
                    cart.recalculateSubtotal();
                    this.checkIfEligibleForFreeShipping(cart);
                    this.updateRecommendedItems(cart);
                    cartRepo.save(cart);
                    return Response.OK(cart);
                }

                void checkIfEligibleForFreeShipping(Cart cart){}
                void updateRecommendedItems(Cart cart){}
                private void updateInventory(CartItem item) {/*...*/}
                private void checkForPriceDriftAndUpdate(Cart cart) {/*...*/}
                private void updateMetadata(CartItem item) {
                    String sellerId = item.getSellerId();
                    Details details = item.getProductDetails();
                    var response = productService.getProductDetails(item.getProductId());
                    details.setCategory(response.getCategory());
                    // Setting brand, color, material etc. Elided for space
                    if (sellerId == null) {
                        sellerId = response.getPromotedSeller();  // HERE! Something horrible happens.
                    }
                    item.setSellerId(sellerId);
                }
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.13
     * ───────────────────────────────────────────────────────
     */
    void listing9_13() {
        class __ {
            class Cart {
                List<CartItem> active;
                List<CartItem> saved;

                private String qualifiedId(CartItem item) {
                    //                             This creates an invalid ID that will never match
                    return item.getProductId() + item.getSellerId();
                }

                public void addOrUpdateItems(AddItemsRequest request) {
                    List<CartItem> items = this.convertToCartItems(request);
                    var activeItemsById = toMap(this.active, this::qualifiedId);
                    var savedItemsById = toMap(this.saved, this::qualifiedId);
                    for (CartItem incoming : items) {
                        String key = this.qualifiedId(incoming);         //
                        if (activeItemsById.containsKey(key)             // These lookups will all fail
                                && !savedItemsById.containsKey(key)) {
                            // ...
                        }
                        else if (savedItemsById.containsKey(key)) {
                            // ...
                        }
                        else {
                            this.active.add(incoming);   // This is the only branch hit
                        }
                    }
                }
                private List<CartItem> convertToCartItems(AddItemsRequest request) {
                    return List.of();  // ...
                }
            }
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.14
     * ───────────────────────────────────────────────────────
     */
    void listing9_14() {
        class __ {
            // We define this fully is a later listing
            record Item(long desiredQty, long availableQty){}

            record ValidCart(
                List<Item> active,
                List<Item> saved
            ) {
                ValidCart {
                    for (Item item : active) {
                        if (item.desiredQty() >= item.availableQty()
                                || item.desiredQty() <= 0) {
                            throw new IllegalArgumentException("...");
                        }
                    }
                    for (Item item : saved) {
                        if (!(item.desiredQty() > 0)) {
                            throw new IllegalArgumentException("...");
                        }
                    }
                    active = List.copyOf(active);
                    saved = List.copyOf(saved);
                }
            }
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.15 through 9.16
     * ───────────────────────────────────────────────────────
     */
    void listing9_15() {
        class __ {
            // We define this fully is a later listing
            record Item(long desiredQty, long availableQty,
                        String sellerId, String productId) {
            }

            record ValidCart(
                    Set<Item> active,
                    Set<Item> saved
            ) {
                ValidCart {
                    // Set doesn't have the right semantics for our data.
                    // We would have to defend it here
                    Set<String> byId = active.stream()
                            .map(x -> x.sellerId() + x.productId())
                            .collect(Collectors.toSet());

                    if (active.size() != byId.size()) {
                        // ...
                    }
                    // other invariants skipped for brevity
                }
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.17
     * ───────────────────────────────────────────────────────
     */
    void listing9_17() {
        class __ {
            // We define this fully is a later listing
            record Item(long desiredQty, long availableQty,
                        String sellerId, String productId) {
            }
            record ProductId(String value){}
            record SellerId(String value){}
            record ProductSeller(ProductId productId, SellerId sellerId){}

            record ValidCart(
                    Map<ProductSeller, Item> active,
                    Map<ProductSeller, Item> saved
            ) {
                ValidCart {
                    for (Item item : active.values()) {
                        if (item.desiredQty() >= item.availableQty()
                                || item.desiredQty() <= 0) {
                            throw new IllegalArgumentException("...");
                        }
                    }
                    for (Item item : saved.values()) {
                        if (item.desiredQty() <= 0) {
                            throw new IllegalArgumentException("...");
                        }
                    }
                }
            }
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.18
     * ───────────────────────────────────────────────────────
     */
    void listing9_18() {
        class CartItem {
            // ...
            long desiredQty;
            long availableQty;    // This doesn't belong here.
            // ...
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.19
     * ───────────────────────────────────────────────────────
     */
    void listing9_19() {
        record ProductSeller(String productId, String sellerId){}
        record Item(
            ProductSeller id,
            Details details,
            long desiredQty,
            long availableQty,
            BigDecimal lastNotifiedPrice,
            BigDecimal currentPrice
        ){}
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.20
     * ───────────────────────────────────────────────────────
     */
    void listing9_20() {
        record ProductSeller(String productId, String sellerId){}
        class __ {
            /* sealed */ interface OrderQuantity {
                long desired();
                long available();
            }
            record Constrained(long desired, long available) implements OrderQuantity {
                public Constrained {
                    if (desired > available) {
                        /*...*/
                    }
                }
            }
            record Unconstrained(long desired, long available) implements OrderQuantity {}

            // then we could do something like…
            record Item<Quantity extends OrderQuantity>(
                String productId,
                String sellerId,
                Quantity qty
                // ...
            ){}

            // and finally something like...
            record Cart(
                List<Item<Constrained>> active,
                List<Item<Unconstrained>> saved
            ){}
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.21
     * ───────────────────────────────────────────────────────
     */
    void listing9_21() {
        record ProductSeller(
                String productId,
                String sellerId
        ){
            ProductSeller {
                Objects.requireNonNull(productId);
                Objects.requireNonNull(sellerId);
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.22
     * ───────────────────────────────────────────────────────
     */
    void listing9_22() {
        record ProductSeller(String productId, String sellerId){}
        record Item(
                ProductSeller id,
                Details details,
                long desiredQty,
                long availableQty,
                BigDecimal lastNotifiedPrice,
                BigDecimal currentPrice
        ) {
            Item {
                Objects.requireNonNull(id);
                Objects.requireNonNull(details);
                Objects.requireNonNull(desiredQty);
                Objects.requireNonNull(availableQty);
                Objects.requireNonNull(lastNotifiedPrice);
                Objects.requireNonNull(currentPrice);
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.23
     * ───────────────────────────────────────────────────────
     */
    void listing9_23() {
        class AddItemsRequest {
            @NotEmpty @Unique List<ItemInfo> items;
        }
        class ItemInfo {
            @NotBlank @NotNull String productId;
            // [REMOVED] String sellerId;
            Optional<String> sellerId; // FIXED!
            @Min(1) long quantity;
            BigDecimal displayedPrice;
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.24
     * ───────────────────────────────────────────────────────
     */
    void listing9_24() {
        class __ {
            record ValidCart(/*...*/){}

            public void checkout(ValidCart cart) {
                // logic that’s guarded by its input type  #A
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.25
     * ───────────────────────────────────────────────────────
     */
    void listing9_25() {
        class __ {
            record ValidCart(/*...*/){}
            class StoreController {
                CartRepo cartRepo;
                CartItemValidator cartItemValidator;
                InventoryService inventoryService;
                SellerService sellerService;
                ProductService productService;

                @Post
                @Path("/add-items")
                public Response addCartItems(Session session, AddItemsRequest request) {
                    Cart cart = cartRepo.loadCart(session.getCustomer());
                    var errors = cartItemValidator.validate(request);
                    if (!errors.isEmpty()) {
                        return Response.BAD_REQUEST(errors);
                    }
                    cart.addOrUpdateItems(request);
                    cart.getActive().forEach(item -> {
                        updateMetadata(item);
                        updateInventory(item);
                    });
                    this.checkForPriceDriftAndUpdate(cart);
                    // -----------------------BOUNDARY---------------------------

                    // Planting a flag
                    ValidCart validCart = new ValidCart(/*...*/);


                    // -----------------------BOUNDARY---------------------------
                    cart.recalculateSubtotal();
                    this.checkIfEligibleForFreeShipping(cart);
                    this.updateRecommendedItems(cart);
                    cartRepo.save(cart);
                    return Response.OK(cart);
                }

                void checkIfEligibleForFreeShipping(Cart cart){}
                void updateRecommendedItems(Cart cart){}
                private void updateInventory(CartItem item) {/*...*/}
                private void checkForPriceDriftAndUpdate(Cart cart) {/*...*/}
                private void updateMetadata(CartItem item) {/*...*/}
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.26 thought 9.27
     * ───────────────────────────────────────────────────────
     */
    void listing9_26_to_9_27() {
        class __ {
            record Item(/*...*/) {}
            record ValidCart(/*...*/) {}
            interface Subtotals {}
            interface ShippingUpsell {}

            List<Item> getRecommendedItems(ValidCart cart) {
                // logic for retrieving relevant recommended items
                return null; // (so it compiles)
            }

            Subtotals computeSubtotals(ValidCart cart) {
                // logic for computing totals
                return null; // (so it compiles)
            }

            ShippingUpsell freeShippingProgress(ValidCart cart) {
                // logic checking the customer’s progress
                // towards free shipping.
                return null; // (so it compiles)
            }

            class StoreController {
                CartRepo cartRepo;
                CartItemValidator cartItemValidator;
                InventoryService inventoryService;
                SellerService sellerService;
                ProductService productService;

                @Post
                @Path("/add-items")
                public Response addCartItems(Session session, AddItemsRequest request) {
                    Cart cart = cartRepo.loadCart(session.getCustomer());
                    var errors = cartItemValidator.validate(request);
                    // ...
                    // -----------------------BOUNDARY---------------------------


                    ValidCart validCart = new ValidCart(/*...*/);
                    ShippingUpsell upsell = freeShippingProgress(validCart); // NEW
                    Subtotals subtotals = computeSubtotals(validCart);       // NEW
                    List<Item> recommended = getRecommendedItems(validCart); // NEW


                    // -----------------------BOUNDARY---------------------------
                    // [REMOVED] cart.recalculateSubtotal();
                    // [REMOVED] this.checkIfEligibleForFreeShipping(cart);
                    // [REMOVED] this.updateRecommendedItems(cart);
                    cartRepo.save(cart);
                    return Response.OK(cart);
                }
                private void updateInventory(CartItem item) {/*...*/}
                private void checkForPriceDriftAndUpdate(Cart cart) {/*...*/}
                private void updateMetadata(CartItem item) {/*...*/}
            }
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.28
     * ───────────────────────────────────────────────────────
     */
    void listing9_28() {
        class __ {
            interface Validator  {
                <A> Set<Error> validate(A input);
            }

        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.29
     * ───────────────────────────────────────────────────────
     */
    void listing9_29() {
        class AddItemsRequest {
            @NotEmpty
            @Unique
            List<ItemInfo> items;
        }
        class ItemInfo {
            @NotBlank @NotNull
            String productId;
            String sellerId;
            @Min(1) long quantity;
            BigDecimal displayedPrice;
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.30
     * ───────────────────────────────────────────────────────
     */
    void listing9_30() {
        record ProductSeller(String productId, String sellerId){}
        record Item(
            ProductSeller id,
            Details details,
            long desiredQty,
            long availableQty,
            BigDecimal lastNotifiedPrice,
            BigDecimal currentPrice
        ){}

        record ValidAddItemsRequest(
            Map<ProductSeller, Item> incoming
        ){}
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.31
     * ───────────────────────────────────────────────────────
     */
    void listing9_31() {
        /*
        public Response addCartItems(Session session, AddItemsRequest request) {
        ------------------   VALIDATION SECTION -------------------
         ┌─►// load the pricing info           #A
         ┌─►// load the inventory counts       #A
         ┌─►// load the product details        #A
         │  // validate = new ValidAddItemsRequest(...);
         │  ...
        -│------------------  VALIDATION SECTION -------------------
         │  ...
         │  cart.getActive().forEach(item -> {
         └─── updateMetadata(item);   #A
         └─── updateInventory(item);           #A
         │  });
         └─ checkForPriceDriftAndUpdate(cart); #A
            ...
        }

         */
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.32
     * ───────────────────────────────────────────────────────
     */
    void listing9_32() {
        class __ {
            record ProductSeller(String productId, String sellerId){}
            record Item(
                    ProductSeller productSeller,
                    long desiredQty,
                    long availableQty,
                    Details details,
                    BigDecimal lastNotifiedPrice,
                    BigDecimal currentPrice
            ){}
            @Data
            class AddItemsRequest {
                @NotEmpty @Unique List<ItemInfo> items;
            }
            @Data
            class ItemInfo {
                @NotBlank @NotNull String productId;
                // [REMOVED] String sellerId;
                Optional<String> sellerId; // FIXED!
                @Min(1) long quantity;
                BigDecimal displayedPrice;
            }

            interface CartItemValidator {
                Set<ValidationError> validate(AddItemsRequest request);
            }

            static class ValidationException extends RuntimeException {
                public ValidationException(String msg) {}
            }

            record ValidAddItemsRequest(
                Map<ProductSeller, Item> incoming
            ){}
            record ValidCart(/*...*/) {}
            interface Subtotals {}
            interface ShippingUpsell {}

            List<Item> getRecommendedItems(ValidCart cart) {
                // logic for retrieving relevant recommended items
                return null; // (so it compiles)
            }

            Subtotals computeSubtotals(ValidCart cart) {
                // logic for computing totals
                return null; // (so it compiles)
            }

            ShippingUpsell freeShippingProgress(ValidCart cart) {
                // logic checking the customer’s progress
                // towards free shipping.
                return null; // (so it compiles)
            }

            class StoreController {
                CartRepo cartRepo;
                CartItemValidator cartItemValidator;
                InventoryService inventoryService;
                SellerService sellerService;
                ProductService productService;

                ValidAddItemsRequest validate(AddItemsRequest request) {
                    Set<ValidationError> errors = cartItemValidator.validate(request);
                    if (!errors.isEmpty()) {
                        throw new ValidationException("...");
                    } else {
                        List<Item> incoming = request.getItems().stream()
                                .map(this::preloadEverythingWeNeed)
                                .toList();
                        return new ValidAddItemsRequest(
                            toMap(incoming, Item::productSeller)
                        );
                    }
                }

                Item preloadEverythingWeNeed(ItemInfo item) {
                    String productId = item.getProductId();
                    Optional<String> maybeSeller = item.getSellerId();
                    var inventoryResponse = inventoryService.checkInventory(productId);
                    var details = productService.getProductDetails(productId);
                    String promotedSeller = details.getPromotedSeller();
                    String chosenSeller = maybeSeller.orElse(promotedSeller);
                    var response = sellerService.getOffer(chosenSeller);
                    return new Item(
                        new ProductSeller(
                            productId,
                            chosenSeller
                        ),
                        item.getQuantity(),
                        inventoryResponse.getAvailableQty(),
                        new Details(details.getCategory(), null /*...*/),
                        item.getDisplayedPrice(),
                        response.getCurrentPrice()
                    );
                }


                @Post
                @Path("/add-items")
                public Response addCartItems(Session session, AddItemsRequest request) {
                    Cart cart = cartRepo.loadCart(session.getCustomer());
                    var errors = cartItemValidator.validate(request);
                    // ...
                    // -----------------------BOUNDARY---------------------------


                    ValidCart validCart = new ValidCart(/*...*/);
                    ShippingUpsell upsell = freeShippingProgress(validCart); // NEW
                    Subtotals subtotals = computeSubtotals(validCart);       // NEW
                    List<Item> recommended = getRecommendedItems(validCart); // NEW


                    // -----------------------BOUNDARY---------------------------
                    // [REMOVED] cart.recalculateSubtotal();
                    // [REMOVED] this.checkIfEligibleForFreeShipping(cart);
                    // [REMOVED] this.updateRecommendedItems(cart);
                    cartRepo.save(cart);
                    return Response.OK(cart);
                }
                private void updateInventory(CartItem item) {/*...*/}
                private void checkForPriceDriftAndUpdate(Cart cart) {/*...*/}
                private void updateMetadata(CartItem item) {/*...*/}
            }
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.33
     * ───────────────────────────────────────────────────────
     */
    void listing9_33() {
        class __ {
            record ProductSeller(String productId, String sellerId){}
            record Item(
                    ProductSeller productSeller,
                    long desiredQty,
                    long availableQty,
                    Details details,
                    BigDecimal lastNotifiedPrice,
                    BigDecimal currentPrice
            ){}
            @Data
            class AddItemsRequest {
                @NotEmpty @Unique List<ItemInfo> items;
            }
            @Data
            @AllArgsConstructor
            class ItemInfo {
                @NotBlank @NotNull String productId;
                // [REMOVED] String sellerId;
                Optional<String> sellerId; // FIXED!
                @Min(1) long quantity;
                BigDecimal displayedPrice;
            }

            interface CartItemValidator {
                Set<ValidationError> validate(AddItemsRequest request);
            }

            static class ValidationException extends RuntimeException {
                public ValidationException(String msg) {}
            }

            record ValidAddItemsRequest(
                    Map<ProductSeller, Item> incoming
            ){}
            record ValidCart(
                    Map<ProductSeller, Item> active,
                    Map<ProductSeller, Item> saved
            ) {}

            class StoreController {
                CartRepo cartRepo;
                CartItemValidator cartItemValidator;
                // ...

                ValidAddItemsRequest validate(Session session, AddItemsRequest request) {
                    Set<ValidationError> errors = cartItemValidator.validate(request);
                    if (!errors.isEmpty()) {
                        throw new ValidationException("...");
                    } else {
                        // The act of loading may reveal information
                        // that undoes the "valid-ness" of our Cart
                        ValidCart cart = this.loadCart(session.getCustomer());
                        List<Item> incoming = List.of(/*...*/);
                        return new ValidAddItemsRequest(Map.of(/*...*/));
                    }
                }
                ValidCart loadCart(String customerId) {
                    Cart legacyCart = cartRepo.loadCart(customerId);
                    return new ValidCart(
                        toMap(legacyCart.getActive().stream()
                            .map(legacy -> new ItemInfo(
                                legacy.getProductId(),
                                Optional.of(legacy.getSellerId()),
                                legacy.getDesiredQty(),
                                legacy.getLatestPrice()))
                            .map(this::preloadEverythingWeNeed)
                            .toList()
                            , Item::productSeller),
                        toMap(legacyCart.getSaved().stream()
                            .map(legacy -> new ItemInfo(
                                legacy.getProductId(),
                                Optional.of(legacy.getSellerId()),
                                legacy.getDesiredQty(),
                                legacy.getLatestPrice()))
                            .map(this::preloadEverythingWeNeed)
                            .toList()
                            , Item::productSeller)
                    );
                }

                Item preloadEverythingWeNeed(ItemInfo item) {
                    // body elided for space
                    return null;
                }
            }
        }
    }




    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.34
     * ───────────────────────────────────────────────────────
     */
    void listing9_34() {
        record ProductSeller(String productId, String sellerId){}
        record Item(ProductSeller productSeller){}
        record ValidCart(
                Map<ProductSeller, Item> active,
                Map<ProductSeller, Item> saved
        ){}
        record UnbalancedCart(
                Map<ProductSeller, Item> active,
                Map<ProductSeller, Item> saved
        ){}

        class __ {
            UnbalancedCart refreshCart(ValidCart cart) {
                return new UnbalancedCart(
                        toMap(cart.active().values().stream()
                                .map(this::preloadEverythingWeNeed)
                                .toList(),
                                Item::productSeller),
                        toMap(cart.saved().values().stream()
                                .map(this::preloadEverythingWeNeed)
                                .toList(),
                                Item::productSeller)
                );
            }

            Item preloadEverythingWeNeed(Item item) {
                // ...
                return null;
            }
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.35
     * ───────────────────────────────────────────────────────
     */
    void listing9_35() {
        record ProductSeller(String productId, String sellerId){}
        record Item(ProductSeller productSeller){}
        record ValidCart(
                Map<ProductSeller, Item> active,
                Map<ProductSeller, Item> saved
        ){}
        record UnbalancedCart(
                Map<ProductSeller, Item> active,
                Map<ProductSeller, Item> saved
        ){}

        class __ {
            CartItemValidator cartItemValidator;

            record ValidAddItemsRequest(
                UnbalancedCart cart,
                Map<ProductSeller, Item> incoming
            ){}

            ValidAddItemsRequest validate(Session session, AddItemsRequest request) {
                Set<ValidationError> errors = cartItemValidator.validate(request);
                if (!errors.isEmpty()) {
                    throw new ValidationException("...");
                } else {
                    ValidCart cart = this.loadCart(session.getCustomer());
                    UnbalancedCart updated = refreshCart(cart);
                    return new ValidAddItemsRequest(updated, ...);
                }
            }

            ValidCart loadCart(String customerId) {
                return null; // elided
            }

            UnbalancedCart refreshCart(ValidCart cart) {
                return null; // elided
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.36
     * ───────────────────────────────────────────────────────
     */
    void listing9_36() {
        record ProductSeller(String productId, String sellerId){}
        record Item(ProductSeller productSeller){}
        record ValidCart(
            Map<ProductSeller, Item> active,
            Map<ProductSeller, Item> saved
        ){}
        record UnbalancedCart(
            Map<ProductSeller, Item> active,
            Map<ProductSeller, Item> saved
        ){}

        class __ {
            // SO MUCH NOISE!
            UnbalancedCart refreshCart(ValidCart cart) {
                return new UnbalancedCart(
                        // This code is terrible to look at!
                        toMap(cart.active().values().stream()
                                        .map(this::preloadEverythingWeNeed)
                                        .toList(),
                                Item::productSeller),
                        toMap(cart.saved().values().stream()
                                        .map(this::preloadEverythingWeNeed)
                                        .toList(),
                                Item::productSeller)
                );
            }

            Item preloadEverythingWeNeed(Item item) {
                // ...
                return null;
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.37
     * ───────────────────────────────────────────────────────
     */
    void listing9_37() {
        class __ {
            public static <K, V1, V2> Map<K,V2> mapValues(
                    Map<K,V1> m,
                    Function<V1, V2> f) {
                return m.entrySet().stream()
                        .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> f.apply(entry.getValue()))
                        );
            }

        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.38
     * ───────────────────────────────────────────────────────
     */
    void listing9_38() {
        class __ {
            record ProductSeller(String productId, String sellerId){}
            record Item(ProductSeller productSeller){}
            record ValidCart(
                    Map<ProductSeller, Item> active,
                    Map<ProductSeller, Item> saved
            ){}
            record UnbalancedCart(
                    Map<ProductSeller, Item> active,
                    Map<ProductSeller, Item> saved
            ){}


            UnbalancedCart refreshCart(ValidCart cart) {
                return new UnbalancedCart(
                    mapValues(cart.active(), this::preloadEverythingWeNeed),
                    mapValues(cart.saved(), this::preloadEverythingWeNeed)
                );
            }

            Item preloadEverythingWeNeed(Item item) {
                // ...
                return null;
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.39
     * ───────────────────────────────────────────────────────
     */
    void listing9_39() {
        /*
        public void addOrUpdateItems(AddItemsRequest request) {
          List<CartItem> items = this.convertToCartItems(request);
          var activeItemsById = indexBy(this.active, this::qualifiedId)
          var savedItemsById = indexBy(this.saved, this::qualifiedId)
          for (CartItem incoming : items) {
            String key = this.qualifiedId(incoming);
            if (activeItemsById.containsKey(key)) {...}
            else if (savedItemsById.containsKey(key)) {...}
            else {...}
          }
        }
         */
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.40
     * ───────────────────────────────────────────────────────
     */
    void listing9_40() {
        // It would be nice if we had something like...
        /*
        // Requirement 3.1
        combineThese = intersect(incoming, cart.active)
        // Requirement 3.2
        moveTheseToActive = intersect(incoming, cart.saved)
         */
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.41
     * ───────────────────────────────────────────────────────
     */
    void listing9_41() {
        class __ {
            public static <K,V> Map<K,V> intersect(
                    BinaryOperator<V> op,
                    Map<K,V> xs,
                    Map<K,V> ys
            ) {
                Map<K,V> output = new HashMap<>();
                for (Map.Entry<K,V> x: xs.entrySet()) {
                    if (ys.containsKey(x.getKey())) {
                        output.put(
                                x.getKey(),
                                op.apply(x.getValue(),
                                        ys.get(x.getKey()))
                        );
                    }
                }
                return output;
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.42
     * ───────────────────────────────────────────────────────
     */
    void listing9_42() {
        class __ {

            public static <K,V> Map<K,V> intersect(
                    Map<K,V> xs,
                    Map<K,V> ys
            ) {
                return intersect((left, right) -> right, xs, ys);
            }

            public static <K,V> Map<K,V> intersect(
                    BinaryOperator<V> op,
                    Map<K,V> xs,
                    Map<K,V> ys
            ) {
                Map<K,V> output = new HashMap<>();
                for (Map.Entry<K,V> x: xs.entrySet()) {
                    if (ys.containsKey(x.getKey())) {
                        output.put(x.getKey(), op.apply(x.getValue(), ys.get(x.getKey())));
                    }
                }
                return output;
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.43
     * ───────────────────────────────────────────────────────
     */
    void listing9_43() {
        class __ {

            public static <K,V> Map<K,V> union(
                    BinaryOperator<V> op,
                    Map<K,V> xs,
                    Map<K,V> ys
            ) {
                HashMap<K, V> copy = new HashMap<>(xs);
                for (Map.Entry<K,V> entry : ys.entrySet()) {
                    copy.merge(entry.getKey(), entry.getValue(), op);
                }
                return Map.copyOf(copy);
            }

            public static <K,V> Map<K,V> union(
                    Map<K,V> xs,
                    Map<K,V> ys
            ) {
                return union((left, right) -> right, xs, ys);
            }

        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.44
     * ───────────────────────────────────────────────────────
     */
    void listing9_44() {
        class __ {

            public static <K,V> Map<K,V> except(Map<K,V> xs, Map<K,V> ys) {
                return xs.entrySet().stream()
                        .filter(x -> ys.containsKey(x.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.45
     * ───────────────────────────────────────────────────────
     */
    void listing9_45() {
        class __ {
            /*
            static UnbalancedCart mergeItems(ValidAddItemsRequest request) {
                Map<ProductSeller, Item> incoming = request.incoming();
                UnbalancedCart cart = request.cart();
                var promote = intersect(incoming, cart.saved);
                var active = union(Item::mergeQty, cart.active, incoming, promote);
                var saved = except(cart.saved, incoming);
                return new UnbalancedCart(active, saved);
            }
             */

        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.46
     * ───────────────────────────────────────────────────────
     */
    void listing9_46() {
        class __ {
            /*
            static ValidCart balanceCart(UnbalancedCart cart) {
              var moveToSaved = subset(cart.active, Predicates::noStock);
              var reallocate = subset(cart.active, Predicates::insufficientStock);
              var unmodified = except(cart.active, union(moveTosaved, reallocate));

              var retain = mapValues(reallocate, (item)->item.withDesiredQty(
                  x.availableQty()
              );
              var spillOver = mapValues(reallocate, (item)->item.withDesiredQty(
                  x.availableQty() – x.desiredQty()
              );

              return new ValidCart(
                  union(Item::mergeQty, unmodified, retain),
                  union(Item::mergeQty, cart.saved, moveToSaved, spillOver),
              );
            }
             */

        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.46
     * ───────────────────────────────────────────────────────
     */
    void listing9_47() {
        class __ {
            record ProductSeller(String productId, String sellerId){}
            @With
            record Item(ProductSeller id,
                        Details details,
                        long desiredQty,
                        long availableQty,
                        BigDecimal lastNotifiedPrice,
                        BigDecimal currentPrice){}
            record ValidCart(
                    Map<ProductSeller, Item> active,
                    Map<ProductSeller, Item> saved
            ){}
            record UnbalancedCart(
                    Map<ProductSeller, Item> active,
                    Map<ProductSeller, Item> saved
            ){}
            static ValidCart balance(UnbalancedCart cart) {
                Map<ProductSeller, Item> active = new HashMap<>();
                Map<ProductSeller, Item> saved = new HashMap<>();
                for (var entry : cart.active().entrySet()) {
                    Item item = entry.getValue();
                    if (item.availableQty() == 0) {
                        saved.put(entry.getKey(), item);
                    } else if (item.desiredQty > item.availableQty) {
                        long leftOver = item.availableQty() - item.desiredQty();
                        active.put(entry.getKey(), item.withDesiredQty(
                                item.availableQty()));
                        saved.put(entry.getKey(), item.withDesiredQty(leftOver));
                    } else {
                        active.put(entry.getKey(), entry.getValue());
                    }
                }
                return new ValidCart(active, saved);
            }


        }
    }
}
