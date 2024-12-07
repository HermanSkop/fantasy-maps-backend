package org.fantasymaps.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.fantasymaps.backend.config.AppConfig;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO for {@link org.fantasymaps.backend.model.product.Bundle} creation
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBundleDto implements Serializable {
    Integer creatorId;
    Double price;
    @Pattern(message = AppConfig.nameMismatchMessage, regexp = AppConfig.namePattern)
    String name;
    Set<Integer> maps = new HashSet<>();
}