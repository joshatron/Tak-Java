package io.joshatron.tak.server.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String user;
    private String timestamp;
    private String message;
    private String Id;
}
