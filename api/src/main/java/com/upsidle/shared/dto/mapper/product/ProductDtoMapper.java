package com.upsidle.shared.dto.mapper.product;

import com.upsidle.backend.persistent.domain.product.Product;
import com.upsidle.shared.dto.mapper.CycleAvoidingMappingContext;
import com.upsidle.shared.dto.product.ProductDto;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * The ProductDtoMapper class outlines the supported conversions between Product entity and other
 * data transfer objects.
 *
 * <p><a
 * href="https://github.com/mapstruct/mapstruct-examples/tree/2f351577c1cb595c7c239eb6f8e7c000ed8a5ce2/mapstruct-mapping-with-cycles">...</a>
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {CategoryDtoMapper.class, RatingDtoMapper.class})
public interface ProductDtoMapper {

  /** The Dto mapper instance. */
  ProductDtoMapper MAPPER = Mappers.getMapper(ProductDtoMapper.class);

  /**
   * Convert and populate a product to productDto object.
   *
   * @param product the product
   * @return the productDto
   */
  @InheritInverseConfiguration
  ProductDto toProductDto(Product product, @Context CycleAvoidingMappingContext context);

  /**
   * Convert and populate products to list of productDto objects.
   *
   * @param products the products
   * @return the list of productDto
   */
  List<ProductDto> toProductDto(
      List<Product> products, @Context CycleAvoidingMappingContext context);

  /**
   * Convert and populate a productDto to Product object.
   *
   * @param productDto the productDto
   * @return the product
   */
  Product toProduct(ProductDto productDto, @Context CycleAvoidingMappingContext context);
}
