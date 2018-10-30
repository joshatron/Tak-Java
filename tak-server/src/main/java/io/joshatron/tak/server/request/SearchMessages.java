package io.joshatron.tak.server.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SearchMessages {
    private Auth auth;
    private String[] senders;
    private Date start;
    private Date end;
    private boolean read;
}
