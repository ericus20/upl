package com.upsidle.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The BaseDto provides base fields to be extended.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BaseDto {
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Long id;

  @EqualsAndHashCode.Include private int version;
  @EqualsAndHashCode.Include private String publicId;
  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDateTime updatedAt;
  private String updatedBy;
}
