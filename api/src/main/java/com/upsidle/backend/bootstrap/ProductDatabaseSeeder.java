package com.upsidle.backend.bootstrap;

import com.upsidle.backend.persistent.domain.product.Product;
import com.upsidle.backend.service.product.ProductService;
import com.upsidle.shared.util.ProductUtils;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * A convenient class to initializes and save product data on application start.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDatabaseSeeder implements CommandLineRunner {

  private final RestTemplate restTemplate;
  private final ProductService productService;

  @Override
  @Transactional
  public void run(String... args) {
    var headers = new HttpHeaders();
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));

    var entity = new HttpEntity<String>(headers);

    List<Product> response =
        restTemplate
            .exchange(
                "https://fakestoreapi.com/products",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Product>>() {})
            .getBody();

    LOG.info("Response: {}", response);
    if (Objects.nonNull(response)) {
      response.forEach(
          product -> productService.createProduct(ProductUtils.convertToProductDto(product)));
    }
  }
}
