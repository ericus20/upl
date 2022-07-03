package com.upsidle.backend.persistent.domain.product;

import com.upsidle.backend.persistent.domain.base.BaseEntity;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

/**
 * The category model for the application.
 *
 * @author Stephen Boakye
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Category extends BaseEntity<Long> implements Serializable {
  @Serial private static final long serialVersionUID = 2412831733001851062L;

  @Column(unique = true, nullable = false)
  @NotBlank(message = "Category name is required")
  private String name;

  @JsonIgnore
  @ToString.Exclude
  @OneToMany(mappedBy = "category")
  private Set<Product> products = new HashSet<>();

  public Category(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Category category) || !super.equals(o)) {
      return false;
    }
    return Objects.equals(getName(), category.getName());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof Category;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getName());
  }
}
