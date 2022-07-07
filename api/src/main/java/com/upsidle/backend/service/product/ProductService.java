package com.upsidle.backend.service.product;

import com.upsidle.shared.dto.product.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This ProductService is the contract for the product service operations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface ProductService {

  /**
   * Create the productDto with the productDto instance given.
   *
   * @param productDto the productDto with updated information
   * @return the updated productDto.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  ProductDto createProduct(final ProductDto productDto);

  /**
   * Returns a user for the given title or null if a user could not be found.
   *
   * @param title The title associated to the user to find
   * @return a user for the given title or null if a user could not be found
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  ProductDto findByTitle(String title);

  Page<ProductDto> getProducts(final Pageable pageable);
}
