package io.joshatron.tak.server.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    String username;
    String userId;
}
