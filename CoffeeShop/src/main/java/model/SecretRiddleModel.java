package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecretRiddleModel {

    private Long id;
    private Long eventId;
    private Long userId;
    private String riddleText;
    private String riddleHash;
    private LocalDateTime createdAt;
    private Boolean current;
}