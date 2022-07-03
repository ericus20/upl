package com.upsidle.backend.service.product.impl;

import com.upsidle.backend.persistent.domain.product.Product;
import com.upsidle.backend.persistent.repository.product.CategoryRepository;
import com.upsidle.backend.persistent.repository.product.ProductRepository;
import com.upsidle.backend.service.product.ProductService;
import com.upsidle.constant.user.ProductConstants;
import com.upsidle.shared.dto.product.ProductDto;
import com.upsidle.shared.util.ProductUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The ProductServiceImpl class provides implementation for the ProductService definitions.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Override
  @Transactional
  public ProductDto createProduct(ProductDto productDto) {
    Validate.notNull(productDto, ProductConstants.PRODUCT_MUST_NOT_BE_NULL);

    var storedProduct = productRepository.findByTitle(productDto.getTitle());
    if (Objects.isNull(storedProduct)) {
      var product = ProductUtils.convertToProduct(productDto);

      if (Objects.isNull(productDto.getCategory())) {
        throw new IllegalArgumentException(ProductConstants.CATEGORY_MUST_NOT_BE_NULL);
      }

      var storedCategory = categoryRepository.findByName(productDto.getCategory().getName());
      if (Objects.nonNull(storedCategory)) {
        product.setCategory(storedCategory);
      } else {
        //        product.setCategory(categoryRepository.save(productDto.getCategory()));
      }

      storedProduct = productRepository.save(product);
      LOG.debug("Product created: {}", storedProduct);
    }

    return null;
  }

  @Override
  public ProductDto findByTitle(String title) {
    Validate.notNull(title, ProductConstants.BLANK_TITLE);

    var storedProduct = productRepository.findByTitle(title);
    if (Objects.isNull(storedProduct)) {
      return null;
    }
    return ProductUtils.convertToProductDto(storedProduct);
  }

  @Override
  public Page<ProductDto> getProducts(Pageable pageable) {
    Page<Product> productPage = productRepository.findAll(pageable);

    return productPage.map(ProductUtils::convertToProductDto);
  }
}
