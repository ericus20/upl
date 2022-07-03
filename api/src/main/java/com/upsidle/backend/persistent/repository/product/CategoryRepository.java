package com.upsidle.backend.persistent.repository.product;

import com.upsidle.backend.persistent.domain.product.Category;
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
@RepositoryRestResource
public interface CategoryRepository extends JpaRepository<Category, Long> {

  /**
   * Find category by name.
   *
   * @param name name used to search for category.
   * @return Category found.
   */
  Category findByName(String name);
}
