package com.upsidle.shared.util;

import com.upsidle.backend.persistent.domain.product.Cart;
import com.upsidle.constant.ErrorConstants;
import com.upsidle.shared.dto.mapper.product.CartDtoMapper;
import com.upsidle.shared.dto.product.CartDto;
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
   * @param cart stored cart
   * @return cart dto
   */
  public static CartDto convertToCartDto(final Cart cart) {
    return CartDtoMapper.MAPPER.to(cart);
  }
}
