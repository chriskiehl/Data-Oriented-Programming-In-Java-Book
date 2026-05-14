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
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Listing9_26_to_9_27 {
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






        interface Subtotals {}

        interface ShippingUpsell {}

    /**
     * ───────────────────────────────────────────────────────
     * Listing 9.26 thought 9.27
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        class __ {
            record Item(/*...*/) {}
            record ValidCart(/*...*/) {}



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
}
