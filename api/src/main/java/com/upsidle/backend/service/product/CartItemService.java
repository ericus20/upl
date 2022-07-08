package com.upsidle.backend.service.product;

import com.upsidle.shared.dto.UserDto;
import com.upsidle.shared.dto.product.CartItemDto;
import java.util.Set;

/**
 * This ShoppingCartService is the contract for the shopping cart operations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface CartItemService {

  /**
   * Adds the product to the shopping cart.
   *
   * @param cartItemDto the cartItemDto
   * @return the cartDto.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  CartItemDto createCartItem(final CartItemDto cartItemDto, final UserDto userDto);

  /**
   * Returns a cart for the given userDto or null if a user could not be found.
   *
   * @param userDto The user associated to the user to find
   * @return the cart for the given user or null if a user could not be found
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  Set<CartItemDto> findByUser(final UserDto userDto);

  /**
   * Increments the count of the product.
   *
   * @param publicId the publicId of the cartItem to increment
   * @return the updated product count.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  Integer incrementQuantity(String publicId);

  /**
   * Decrements the count of the product.
   *
   * @param publicId the publicId of the cartItem to decrement
   * @return the updated product count.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  Integer decrementQuantity(String publicId);

  /**
   * Deletes the cart item from the shopping cart.
   *
   * @param publicId the publicId of the cart item to delete
   */
  void delete(String publicId);
}
