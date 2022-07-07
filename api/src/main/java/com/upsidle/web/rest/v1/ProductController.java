package com.upsidle.web.rest.v1;

import com.upsidle.annotation.Loggable;
import com.upsidle.backend.service.product.ProductService;
import com.upsidle.constant.product.ProductConstants;
import com.upsidle.shared.dto.product.ProductDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class handles all rest calls for products.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ProductConstants.API_V1_PRODUCTS_ROOT_URL)
@PreAuthorize("isFullyAuthenticated() && hasAnyRole(T(com.upsidle.enums.RoleType).values())")
public class ProductController {

  private final ProductService productService;

  @Loggable
  @GetMapping
  @SecurityRequirements
  public ResponseEntity<Page<ProductDto>> getProducts(final Pageable pageable) {
    var productsPage = productService.getProducts(pageable);

    return ResponseEntity.ok(productsPage);
  }
}
