package io.joshatron.tak.server.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMessage {
    private Auth auth;
    private String recipient;
    private String message;
}
