package com.upsidle.backend.service.product;

import com.upsidle.shared.dto.UserDto;
import com.upsidle.shared.dto.product.CartDto;
import com.upsidle.shared.dto.product.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This ShoppingCartService is the contract for the shopping cart operations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface CartService {

  /**
   * Adds the product to the shopping cart.
   *
   * @param productDto the productDto
   * @return the cartDto.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  CartDto addProduct(final ProductDto productDto, final UserDto userDto);

  /**
   * Returns a cart for the given userDto or null if a user could not be found.
   *
   * @param userDto The user associated to the user to find
   * @return the cart for the given user or null if a user could not be found
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  CartDto findByUser(final UserDto userDto);

  Page<CartDto> getProducts(final Pageable pageable);
}
