package io.joshatron.tak.server.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PassChange {
    private Auth auth;
    private String updated;
}
