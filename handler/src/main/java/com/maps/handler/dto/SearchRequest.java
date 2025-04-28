package com.maps.handler.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchRequest {

    @NotBlank(message = "Room name cannot be blank")
    private String room;
}
