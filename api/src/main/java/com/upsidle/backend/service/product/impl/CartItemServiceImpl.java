package com.upsidle.backend.service.product.impl;

import com.upsidle.backend.persistent.domain.product.CartItem;
import com.upsidle.backend.persistent.repository.UserRepository;
import com.upsidle.backend.persistent.repository.product.CartItemRepository;
import com.upsidle.backend.persistent.repository.product.ProductRepository;
import com.upsidle.backend.service.product.CartItemService;
import com.upsidle.constant.CacheConstants;
import com.upsidle.constant.product.ProductConstants;
import com.upsidle.constant.user.UserConstants;
import com.upsidle.exception.user.UserNotFoundException;
import com.upsidle.shared.dto.UserDto;
import com.upsidle.shared.dto.product.CartItemDto;
import com.upsidle.shared.util.CartUtils;
import com.upsidle.shared.util.core.ValidationUtils;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The CartServiceImpl class provides implementation for the CartService definitions.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartItemServiceImpl implements CartItemService {

  private final CartItemRepository cartItemRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  @Override
  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = CacheConstants.USERS, key = "#userDto.email"),
        @CacheEvict(value = CacheConstants.USERS, key = "#userDto.publicId"),
        @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.email")
      })
  public CartItemDto createCartItem(final CartItemDto cartItemDto, final UserDto userDto) {
    ValidationUtils.validateInputs(cartItemDto, cartItemDto.getProduct(), userDto);

    var storedUser = userRepository.findByEmail(userDto.getEmail());
    if (Objects.isNull(storedUser)) {
      throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
    }

    var storedProduct = productRepository.findByTitle(cartItemDto.getProduct().getTitle());
    if (Objects.isNull(storedProduct)) {
      throw new IllegalArgumentException(ProductConstants.PRODUCT_MUST_NOT_BE_NULL);
    }

    var cartItem = cartItemRepository.findByUserAndProduct(storedUser, storedProduct);
    if (Objects.nonNull(cartItem)) {
      cartItem.setQuantity(cartItem.getQuantity() + 1);
    } else {
      cartItem = new CartItem(storedProduct, storedUser);
    }

    var storedCart = cartItemRepository.save(cartItem);
    LOG.debug("CartItem created: {}", storedCart);

    return CartUtils.convertToCartDto(storedCart);
  }

  @Override
  public Set<CartItemDto> findByUser(UserDto userDto) {
    ValidationUtils.validateInputs(userDto, UserConstants.USER_NOT_FOUND);

    var storedUser = userRepository.findByEmail(userDto.getEmail());
    if (Objects.nonNull(storedUser)) {
      return CartUtils.convertToCartDto(storedUser.getCartItems());
    }

    return null;
  }

  @Override
  @Transactional
  public Integer incrementQuantity(String publicId) {
    ValidationUtils.validateInputs(publicId);

    var cartItem = cartItemRepository.findByPublicId(publicId);
    if (Objects.nonNull(cartItem)) {
      cartItem.setQuantity(cartItem.getQuantity() + 1);
      var persistedCartItem = cartItemRepository.save(cartItem);
      LOG.debug("CartItem updated: {}", persistedCartItem);

      return persistedCartItem.getQuantity();
    }

    return null;
  }

  @Override
  @Transactional
  public Integer decrementQuantity(String publicId) {
    ValidationUtils.validateInputs(publicId);

    var cartItem = cartItemRepository.findByPublicId(publicId);
    if (Objects.nonNull(cartItem)) {
      cartItem.setQuantity(cartItem.getQuantity() - 1);
      if (cartItem.getQuantity() == 0) {
        cartItemRepository.delete(cartItem);
      } else {
        var persistedCartItem = cartItemRepository.save(cartItem);
        LOG.debug("CartItem updated: {}", persistedCartItem);

        return persistedCartItem.getQuantity();
      }
    }

    return null;
  }

  @Override
  @Transactional
  public void delete(String publicId) {
    ValidationUtils.validateInputs(publicId);

    cartItemRepository.deleteByPublicId(publicId);
  }
}
