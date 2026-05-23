package ru.parovozik.backend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record GroupEventRequest(@JsonProperty("day_start") String day_start,
                                @JsonProperty("day_end") String day_end,
                                @JsonProperty("time_start") String time_start,
                                @JsonProperty("time_end") String time_end,
                                @JsonProperty("time_pick") long time_pick,
                                @JsonProperty("importance") int importance,
                                @JsonProperty("title") String title,
                                @JsonProperty("body") String body,
                                @JsonProperty("colour") String colour) {
}
