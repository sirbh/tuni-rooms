package com.maps.handler.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

import java.util.List;

import com.maps.handler.service.LocationDataService;
import com.maps.handler.contoller.LocationController;
import com.maps.handler.model.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @MockitoBean
    private LocationDataService locationDataService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void searchByRoom_ShouldReturnLocations_WhenRoomExists() throws Exception {

        Location location = new Location();
        location.setId(1L);  
        location.setRoom("TestRoom");
        location.setFileLocation("TestFileLocation");

        when(locationDataService.serachByRoom("TestRoom"))
            .thenReturn(List.of(location));

        // Act + Assert
        mockMvc.perform(get("/search")
                .param("room", "TestRoom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].room").value("TestRoom"))
                .andExpect(jsonPath("$[0].fileLocation").value("TestFileLocation"));
    }

    @Test
    void searchByRoom_ShouldReturnNotFound_WhenRoomNotExists() throws Exception {
        // Arrange
        when(locationDataService.serachByRoom("NonExistingRoom"))
            .thenThrow(new com.maps.handler.exception.ResourceNotFoundException("Location", "room", "NonExistingRoom"));

        // Act + Assert
        mockMvc.perform(get("/search")
                .param("room", "NonExistingRoom"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Location not found with room: 'NonExistingRoom'"));
    }
}
