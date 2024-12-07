package org.fantasymaps.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManageBundleItemDto {
    private int id;
    private String name;
    private LocalDate dateCreated;
    private Double price;
    private Set<String> coverMapsUrls;
}