package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {

    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;

    @Builder.Default
    private List<OrderItemModel> items = new ArrayList<>();

    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "";
        }

        return createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }
}
