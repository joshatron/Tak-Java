package io.joshatron.tak.server.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameInfo {
    private String white;
    private String black;
    private int size;
    private String first;
    private String start;
    private String end;
    private boolean done;
    private GameTurn[] turns;
}
