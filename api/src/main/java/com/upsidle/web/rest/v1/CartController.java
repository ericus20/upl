package com.upsidle.web.rest.v1;

import com.upsidle.annotation.Loggable;
import com.upsidle.backend.service.product.CartService;
import com.upsidle.constant.product.CartConstants;
import com.upsidle.shared.dto.product.CartDto;
import com.upsidle.shared.dto.product.ProductDto;
import com.upsidle.shared.util.core.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  private final CartService cartService;

  @Loggable
  @PostMapping
  @SecurityRequirements
  public ResponseEntity<CartDto> addProduct(@RequestBody @Valid ProductDto productDto) {
    var userDto = SecurityUtils.getAuthorizedUserDto();
    var cart = cartService.addProduct(productDto, userDto);

    return ResponseEntity.ok(cart);
  }
}
