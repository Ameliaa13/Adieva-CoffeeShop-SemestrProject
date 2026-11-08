package model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemModel {

    private Long userId;
    private Long menuItemId;
    private Integer quantity;

    private MenuItemModel menuItemModel;

}
