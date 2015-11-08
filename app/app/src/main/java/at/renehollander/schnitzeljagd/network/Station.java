package at.renehollander.schnitzeljagd.network;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Station {

    private final String name;
    private final String description;
    private final Navigation navigation;
    private final Answer answer;

}
