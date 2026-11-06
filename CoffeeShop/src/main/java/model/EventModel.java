package model;

import enums.EventStatus;
import enums.EventVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventModel {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private Integer durationMinutes;
    private Integer capacity;
    private EventVisibility visibility;
    private EventStatus status;
    private String imageUrl;

    public String getFormattedDate(){
        if(startTime == null){
            return "";
        }
        return startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getFormattedTime(){
        if (startTime == null){
            return "";
        }
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
