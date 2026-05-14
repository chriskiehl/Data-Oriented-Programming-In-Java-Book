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


public class Listing9_33 {
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


    /**
     * ───────────────────────────────────────────────────────
     * Dependencies used throughout the examples
     * ───────────────────────────────────────────────────────
     */

    @Data
    public static class AddItemsRequest {
        @NotEmpty @Unique List<ItemInfo> items;
        }
    @Data
    @AllArgsConstructor
    public static class ItemInfo {
        @NotBlank @NotNull String productId;
        // [REMOVED] String sellerId;
        Optional<String> sellerId; // FIXED!
        @Min(1) long quantity;
        BigDecimal displayedPrice;
        }

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
     * Listing 9.33
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
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
}
