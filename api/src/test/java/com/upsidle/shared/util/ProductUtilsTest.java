package com.upsidle.shared.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

class ProductUtilsTest {

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        AssertionError.class, () -> ReflectionUtils.newInstance(ProductUtils.class));
  }

  @Test
  void createProduct() {
    var product = ProductUtils.createProduct();

    Assertions.assertNotNull(product.getTitle());
  }
}
