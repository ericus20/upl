package com.upsidle.shared.dto.product;

import com.upsidle.shared.dto.BaseDto;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

/**
 * The ProductDto transfers product details from outside into the application and vice versa.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProductDto extends BaseDto implements Serializable {
  @Serial private static final long serialVersionUID = -6342630857637389028L;

  private String title;
  private String image;
  private String description;
  private BigDecimal price;
  private boolean active;
  private int unitsInStock;

  private RatingDto rating;

  @JsonIgnore private CategoryDto category;
}
