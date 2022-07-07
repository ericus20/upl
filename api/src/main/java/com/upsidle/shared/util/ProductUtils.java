package com.upsidle.shared.util;

import com.upsidle.backend.persistent.domain.product.Category;
import com.upsidle.backend.persistent.domain.product.Product;
import com.upsidle.constant.ErrorConstants;
import com.upsidle.constant.product.ProductConstants;
import com.upsidle.shared.dto.mapper.CycleAvoidingMappingContext;
import com.upsidle.shared.dto.mapper.product.ProductDtoMapper;
import com.upsidle.shared.dto.product.ProductDto;
import java.math.BigDecimal;
import java.util.List;
import net.datafaker.Faker;
import org.apache.commons.lang3.Validate;

/**
 * Product utility class that holds product related methods used across the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class ProductUtils {

  private static final Faker FAKER = new Faker();

  private ProductUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * creates a new product with generated content.
   *
   * @return the product
   */
  public static Product createProduct() {
    return createProduct(FAKER.commerce().productName());
  }

  /**
   * creates a new product with generated content and specified title.
   *
   * @return the product
   */
  public static Product createProduct(String title) {
    var product = new Product(title);
    product.setDescription(FAKER.commerce().department());
    product.setPrice(BigDecimal.valueOf(Double.parseDouble(FAKER.commerce().price())));
    product.setUnitsInStock(FAKER.number().numberBetween(1, 100));
    product.setActive(FAKER.bool().bool());

    return product;
  }

  /**
   * creates a new product category with generated content.
   *
   * @return the product category
   */
  public static Category createCategory() {
    return createCategory(FAKER.commerce().department());
  }

  /**
   * creates a new product category with generated content and specified name.
   *
   * @return the product category
   */
  public static Category createCategory(String name) {
    return new Category(name);
  }

  /**
   * Transfers data from entity to transfer object.
   *
   * @param product stored product
   * @return product dto
   */
  public static ProductDto convertToProductDto(final Product product) {
    var productDto =
        ProductDtoMapper.MAPPER.toProductDto(product, new CycleAvoidingMappingContext());
    Validate.notNull(productDto, ProductConstants.PRODUCT_MUST_NOT_BE_NULL);

    return productDto;
  }

  /**
   * Transfers data from entity to transfer object.
   *
   * @param products stored products
   * @return product dto
   */
  public static List<ProductDto> convertToProductDto(final List<Product> products) {
    return ProductDtoMapper.MAPPER.toProductDto(products, new CycleAvoidingMappingContext());
  }

  /**
   * Transfers data from transfer object to entity.
   *
   * @param productDto the productDto
   * @return product
   */
  public static Product convertToProduct(final ProductDto productDto) {
    var user = ProductDtoMapper.MAPPER.toProduct(productDto, new CycleAvoidingMappingContext());
    Validate.notNull(productDto, ProductConstants.PRODUCT_MUST_NOT_BE_NULL);

    return user;
  }
}
