package io.joshatron.tak.server.requestbody;

public class Block {

    private Auth auth;
    private String block;

    public Block(Auth auth, String block) {
        this.auth = auth;
        this.block = block;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
