package org.fantasymaps.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fantasymaps.backend.config.AppConfig;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMapDto {
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = AppConfig.namePattern, message = "Invalid name")
    private String name;
    private String description;
    private Double price;
    private MultipartFile file;
    private MapSizeDto size;
    private Set<TagDto> tags;
}
