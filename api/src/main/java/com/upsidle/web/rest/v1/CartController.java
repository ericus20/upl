package com.upsidle.web.rest.v1;

import com.upsidle.annotation.Loggable;
import com.upsidle.backend.service.product.CartItemService;
import com.upsidle.constant.product.CartConstants;
import com.upsidle.enums.OperationStatus;
import com.upsidle.shared.dto.product.CartItemDto;
import com.upsidle.shared.util.core.SecurityUtils;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * This class handles all rest calls for carts.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(CartConstants.API_V1_CART_ROOT_URL)
@PreAuthorize("isFullyAuthenticated() && hasAnyRole(T(com.upsidle.enums.RoleType).values())")
public class CartController {

  private final CartItemService cartItemService;

  /**
   * Adds a cart item to the database.
   *
   * @param cartItemDto the cart item to add
   * @return if successful, a 201 response with the location in header of the cart item is returned.
   */
  @Loggable
  @PostMapping
  public ResponseEntity<OperationStatus> addCartItem(@RequestBody @Valid CartItemDto cartItemDto) {
    var userDto = SecurityUtils.getAuthorizedUserDto();
    var cart = cartItemService.createCartItem(cartItemDto, userDto);

    var location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{publicId}")
            .buildAndExpand(cart.getPublicId())
            .toUri();

    return ResponseEntity.created(location).body(OperationStatus.SUCCESS);
  }

  /**
   * Increments the quantity of a cart item by 1
   *
   * @param publicId the public id of the cart item to increment
   * @return if successful, a 200 response is returned.
   */
  @Loggable
  @PutMapping("/{publicId}/increment")
  public ResponseEntity<OperationStatus> incrementQuantity(@PathVariable String publicId) {
    var cart = cartItemService.incrementQuantity(publicId);

    return ResponseEntity.ok(OperationStatus.SUCCESS);
  }

  /**
   * Decrements the quantity of a cart item by 1
   *
   * @param publicId the public id of the cart item to decrement
   * @return if successful, a 200 response is returned.
   */
  @Loggable
  @PutMapping("/{publicId}/decrement")
  public ResponseEntity<OperationStatus> decrementQuantity(@PathVariable String publicId) {
    var rowsAffected = cartItemService.decrementQuantity(publicId);
    LOG.debug("Rows affected: {}", rowsAffected);

    return ResponseEntity.ok(OperationStatus.SUCCESS);
  }

  /**
   * Deletes a cart item from the database.
   *
   * @param publicId the public id of the cart item to delete
   * @return if successful, a 200 response is returned.
   */
  @Loggable
  @DeleteMapping("/{publicId}")
  public ResponseEntity<OperationStatus> deleteCartItem(@PathVariable String publicId) {
    cartItemService.delete(publicId);

    return ResponseEntity.ok(OperationStatus.SUCCESS);
  }
}
