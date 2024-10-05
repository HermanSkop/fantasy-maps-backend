package org.fantasymaps.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fantasymaps.backend.config.AppConfig;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManageMapItemDto {
    private int id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Map image URL cannot be blank")
    @Pattern(regexp = AppConfig.urlPattern, message = AppConfig.urlMismatchMessage)
    private String url;
    private LocalDate dateCreated;
    private Double price;
}
