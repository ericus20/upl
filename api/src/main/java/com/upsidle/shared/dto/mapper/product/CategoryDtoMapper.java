package com.upsidle.shared.dto.mapper.product;

import com.upsidle.backend.persistent.domain.product.Category;
import com.upsidle.shared.dto.mapper.CycleAvoidingMappingContext;
import com.upsidle.shared.dto.product.CategoryDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * The CategoryDtoMapper class outlines the supported conversions between category entity and other
 * data transfer objects.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = {CycleAvoidingMappingContext.class, Context.class},
    uses = {CategoryDtoMapper.class})
public interface CategoryDtoMapper {

  /** The Dto mapper instance. */
  CategoryDtoMapper MAPPER = Mappers.getMapper(CategoryDtoMapper.class);

  /**
   * Convert and populate a product to productDto object.
   *
   * @param category the category
   * @return the productDto
   */
  CategoryDto toCategoryDto(Category category, @Context CycleAvoidingMappingContext context);
}
