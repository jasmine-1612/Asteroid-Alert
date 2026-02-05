package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NasaNeoResponse {

    @JsonProperty("near_earth_objects")
    private Map<String , List<Asteroid> > nearEarthObjects;

    @JsonProperty("element_count")
    private long totalAsteroids;
}
