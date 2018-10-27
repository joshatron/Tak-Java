package io.joshatron.tak.server.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MarkRead {
    private Auth auth;
    private String[] ids;
    private Date start;
}