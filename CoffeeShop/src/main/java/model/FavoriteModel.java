package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteModel {

    private Long userId;
    private Long menuItemId;

    private MenuItemModel menuItemModel;

}
