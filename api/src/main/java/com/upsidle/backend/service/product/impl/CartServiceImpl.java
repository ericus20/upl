package com.upsidle.backend.service.product.impl;

import com.upsidle.backend.persistent.domain.product.Cart;
import com.upsidle.backend.persistent.repository.UserRepository;
import com.upsidle.backend.persistent.repository.product.CartRepository;
import com.upsidle.backend.persistent.repository.product.ProductRepository;
import com.upsidle.backend.service.product.CartService;
import com.upsidle.constant.product.ProductConstants;
import com.upsidle.constant.user.UserConstants;
import com.upsidle.exception.user.UserNotFoundException;
import com.upsidle.shared.dto.UserDto;
import com.upsidle.shared.dto.product.CartDto;
import com.upsidle.shared.dto.product.ProductDto;
import com.upsidle.shared.util.CartUtils;
import com.upsidle.shared.util.core.ValidationUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  @Override
  @Transactional
  public CartDto addProduct(final ProductDto productDto, final UserDto userDto) {
    ValidationUtils.validateInputs(productDto, userDto);

    var storedUser = userRepository.findByEmail(userDto.getEmail());
    if (Objects.isNull(storedUser)) {
      throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
    }

    var storedProduct = productRepository.findByTitle(productDto.getTitle());
    if (Objects.isNull(storedProduct)) {
      throw new IllegalArgumentException(ProductConstants.PRODUCT_MUST_NOT_BE_NULL);
    }

    var cart = cartRepository.findByUser(storedUser);
    if (Objects.nonNull(cart)) {
      cart.addProduct(storedProduct);
    } else {
      cart = new Cart(storedProduct, storedUser);
    }

    var storedCart = cartRepository.save(cart);
    LOG.debug("Cart updated: {}", storedCart);

    return CartUtils.convertToCartDto(storedCart);
  }

  @Override
  public CartDto findByUser(UserDto userDto) {
    ValidationUtils.validateInputs(userDto, UserConstants.USER_NOT_FOUND);

    var storedUser = userRepository.findByEmail(userDto.getEmail());
    var storedCart = cartRepository.findByUser(storedUser);

    if (Objects.nonNull(storedCart)) {
      return CartUtils.convertToCartDto(storedCart);
    }

    return null;
  }

  @Override
  public Page<CartDto> getProducts(Pageable pageable) {
    return null;
  }
}
