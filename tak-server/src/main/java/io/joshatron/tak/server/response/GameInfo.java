package io.joshatron.tak.server.response;

public class GameInfo {

    private String white;
    private String black;
    private int size;
    private String first;
    private String start;
    private String end;
    private boolean done;
    private GameTurn[] turns;

    public GameInfo() {
        white = null;
        black = null;
        size = 0;
        first = null;
        start = null;
        end = null;
        done = false;
        turns = null;
    }

    public GameInfo(String white, String black, int size, String first, String start, String end, boolean done) {
        this.white = white;
        this.black = black;
        this.size = size;
        this.first = first;
        this.start = start;
        this.end = end;
        this.done = done;
        turns = null;
    }

    public GameInfo(String white, String black, int size, String first, String start, String end, boolean done, GameTurn[] turns) {
        this.white = white;
        this.black = black;
        this.size = size;
        this.first = first;
        this.start = start;
        this.end = end;
        this.done = done;
        this.turns = turns;
    }

    public String getWhite() {
        return white;
    }

    public void setWhite(String white) {
        this.white = white;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public GameTurn[] getTurns() {
        return turns;
    }

    public void setTurns(GameTurn[] turns) {
        this.turns = turns;
    }
}
