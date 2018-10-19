package io.joshatron.tak.server.request;

import lombok.Data;

@Data
public class PassChange {
    private Auth auth;
    private String updated;
}
