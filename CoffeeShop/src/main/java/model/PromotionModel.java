package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionModel {

    private Long id;
    private String title;
    private String shortDescription;
    private String description;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
}
