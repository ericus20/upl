package com.upsidle.shared.dto.mapper.product;

import com.upsidle.backend.persistent.domain.product.Cart;
import com.upsidle.shared.dto.mapper.UserDtoMapper;
import com.upsidle.shared.dto.product.CartDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * The CartDtoMapper class outlines the supported conversions between Cart entity and other data
 * transfer objects.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {UserDtoMapper.class, ProductDtoMapper.class})
public interface CartDtoMapper {

  /** The Dto mapper instance. */
  CartDtoMapper MAPPER = Mappers.getMapper(CartDtoMapper.class);

  /**
   * Convert and populate a cart to cartDto object.
   *
   * @param cart the cart
   * @return the productDto
   */
  CartDto to(Cart cart);
}
