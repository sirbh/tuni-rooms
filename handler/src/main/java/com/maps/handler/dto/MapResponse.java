package com.maps.handler.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MapResponse {

    private String room;
    private String location;
    private String image;

    
}
