package org.fantasymaps.backend.model.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class MapSize {
    @Column(name = "width_squares", nullable = false)
    @DecimalMin(value = "1", message = "Width must be greater than 0")
    private int widthSquares;
    @Column(name = "height_squares", nullable = false)
    @DecimalMin(value = "1", message = "Height must be greater than 0")
    private int heightSquares;
    @Column(name = "square_side_length", nullable = false)
    @DecimalMin(value = "1", message = "Length must be greater than 0")
    private int squareSideLength;
    @Column(name = "unit", nullable = false)
    private Unit unit;
}