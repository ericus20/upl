package com.upsidle.backend.persistent.repository.product;

import com.upsidle.backend.persistent.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Product.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Repository
@RepositoryRestResource(exported = false)
public interface ProductRepository extends JpaRepository<Product, Long> {

  /**
   * Find product by title.
   *
   * @param title title used to search for product.
   * @return Product found.
   */
  Product findByTitle(String title);

  Product findByPublicId(String publicId);
}
