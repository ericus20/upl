package com.upsidle.shared.dto.product;

import com.upsidle.shared.dto.BaseDto;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The ProductDto transfers user details from outside into the application and vice versa.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class RatingDto extends BaseDto implements Serializable {
  @Serial private static final long serialVersionUID = -6342630857637389028L;

  /** The actual rating of the product. */
  private Double rate;

  /** The number of times the product has been rated. */
  private Integer count;

  public RatingDto(Double rate, Integer count) {
    this.rate = rate;
    this.count = count;
  }
}
