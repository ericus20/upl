package com.upsidle.backend.persistent.domain.product;

import com.upsidle.backend.persistent.domain.base.BaseEntity;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The product rating model for the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Rating extends BaseEntity<Long> implements Serializable {

  @Serial private static final long serialVersionUID = 5801740651856592988L;

  /** The actual rating of the product. */
  private Double rate;

  /** The number of times the product has been rated. */
  private Integer count;

  public Rating(Double rate, Integer count) {
    this.rate = rate;
    this.count = count;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Rating rating) || !super.equals(o)) {
      return false;
    }
    return Objects.equals(getRate(), rating.getRate())
        && Objects.equals(getCount(), rating.getCount());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof Rating;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getRate(), getCount());
  }
}
