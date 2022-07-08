package com.upsidle.shared.util;

import com.upsidle.backend.persistent.domain.product.CartItem;
import com.upsidle.constant.ErrorConstants;
import com.upsidle.shared.dto.mapper.product.CartItemDtoMapper;
import com.upsidle.shared.dto.product.CartItemDto;
import java.util.Set;
import net.datafaker.Faker;

/**
 * Cart utility class that holds cart related methods used across the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class CartUtils {

  private static final Faker FAKER = new Faker();

  private CartUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * Transfers data from entity to transfer object.
   *
   * @param cartItem stored cartItem
   * @return cartItem dto
   */
  public static CartItemDto convertToCartDto(final CartItem cartItem) {
    return CartItemDtoMapper.MAPPER.to(cartItem);
  }

  /**
   * Transfers data from entity to transfer object.
   *
   * @param cartItems stored cartItems
   * @return cartItems dto
   */
  public static Set<CartItemDto> convertToCartDto(final Set<CartItem> cartItems) {
    return CartItemDtoMapper.MAPPER.to(cartItems);
  }
}
