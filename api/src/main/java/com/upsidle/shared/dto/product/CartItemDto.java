package com.upsidle.shared.dto.product;

import com.upsidle.shared.dto.BaseDto;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The CartItemDto transfers cartItem details from outside into the application and vice versa.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class CartItemDto extends BaseDto implements Serializable {
  @Serial private static final long serialVersionUID = -6342630857637389028L;

  private Integer quantity;

  private ProductDto product;
}
