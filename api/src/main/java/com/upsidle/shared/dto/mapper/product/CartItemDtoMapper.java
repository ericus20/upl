package com.upsidle.shared.dto.mapper.product;

import com.upsidle.backend.persistent.domain.product.CartItem;
import com.upsidle.shared.dto.product.CartItemDto;
import java.util.Set;
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
    uses = {ProductDtoMapper.class})
public interface CartItemDtoMapper {

  /** The Dto mapper instance. */
  CartItemDtoMapper MAPPER = Mappers.getMapper(CartItemDtoMapper.class);

  /**
   * Convert and populate a cartItem to cartDto object.
   *
   * @param cartItem the cartItem
   * @return the cartItemDto
   */
  CartItemDto to(CartItem cartItem);

  /**
   * Convert and populate cartItems to list of userDto objects.
   *
   * @param cartItems the cartItems
   * @return the list of cartItemDto
   */
  Set<CartItemDto> to(Set<CartItem> cartItems);

  /**
   * Convert and populate a cartDto to cartItem object.
   *
   * @param cartItemDto the cartDto
   * @return the cartItemDto
   */
  CartItem from(CartItemDto cartItemDto);
}
