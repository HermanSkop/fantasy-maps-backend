package org.fantasymaps.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapSizeDto {
    private int widthSquares;
    private int heightSquares;
    private int squareSideLength;
}
