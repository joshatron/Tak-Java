package io.joshatron.tak.server.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Base64;

@Data
@AllArgsConstructor
public class Auth {

    private String username;
    private String password;

    //Constructor for basic auth
    public Auth(String basicAuth) {
        //Decode from base 64
        String decoded = new String(Base64.getDecoder().decode(basicAuth.replace("Basic ", "")));
        //Makes sure there is only one :
        if(decoded.length() - decoded.replace(":", "").length() == 1) {
            String[] auth = decoded.split(":");
            username = auth[0];
            password = auth[1];
        }
    }
}
