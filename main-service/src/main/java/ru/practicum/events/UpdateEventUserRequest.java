package ru.practicum.events;/* # parse("File Header.java")*/

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * File Name: UpdateEventUserRequest.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:16 PM (UTC+3)
 * Description:
 */

@Data
public class UpdateEventUserRequest {
    @Size(min = 20, message = "size must be between 20 and 2000")
    @Size(max = 2000, message = "size must be between 20 and 2000")
    private String annotation;

    private Long category;

    @Size(min = 20, message = "size must be between 20 and 7000")
    @Size(max = 7000, message = "size must be between 20 and 7000")
    private String description;

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Size(min = 3, message = "size must be between 3 and 120")
    @Size(max = 120, message = "size must be between 3 and 120")
    private String title;

    @Override
    public String toString() {

        Integer annLen;
        Integer descrLen;
        if (annotation != null) {
            annLen = annotation.length();
        } else {
            annLen = null;
        }
        if (description != null) {
            descrLen = description.length();
        } else {
            descrLen = null;
        }

        return "UpdateEventUserRequest{" +
                "annotation.length='" + annLen + '\'' +
                ", category=" + category +
                ", description.length='" + descrLen + '\'' +
                ", eventDate=" + eventDate +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", stateAction=" + stateAction +
                ", title='" + title + '\'' +
                '}';
    }

    public enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }
}
