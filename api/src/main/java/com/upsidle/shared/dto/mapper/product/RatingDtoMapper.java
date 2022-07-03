package com.upsidle.shared.dto.mapper.product;

import com.upsidle.backend.persistent.domain.product.Rating;
import com.upsidle.shared.dto.product.RatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * The ProductDtoMapper class outlines the supported conversions between User entity and other data
 * transfer objects.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RatingDtoMapper {

  /** The Dto mapper instance. */
  RatingDtoMapper MAPPER = Mappers.getMapper(RatingDtoMapper.class);

  /**
   * Convert and populate a product to productDto object.
   *
   * @param rating the rating
   * @return the productDto
   */
  RatingDto toRatingDto(Rating rating);
}
