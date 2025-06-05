package com.maps.mapservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MapResponse {

    @JsonProperty("image")
    private String image;

    @JsonProperty("coordinates")
    private List<Float> coordinates;
}
