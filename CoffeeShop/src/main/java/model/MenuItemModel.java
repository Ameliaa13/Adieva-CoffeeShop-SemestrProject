package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemModel {

    private Long id;
    private String name;
    private Long categoryId;
    private Double price;
    private String description;
    private String imageUrl;
    private Double calories;
    private Double proteins;
    private Double fats;
    private Double carbohydrates;
}
