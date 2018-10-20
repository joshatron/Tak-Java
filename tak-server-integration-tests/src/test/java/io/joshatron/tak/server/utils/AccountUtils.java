package io.joshatron.tak.server.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.junit.Assert;

import java.io.IOException;

public class AccountUtils {

    public static User addUser(String suite, String test, String user, String password, HttpClient client, int expected) throws IOException {
        HttpResponse response = HttpUtils.createUser(suite + test + user, password, client);
        Assert.assertEquals(expected, response.getStatusLine().getStatusCode());
        if(expected == HttpStatus.SC_NO_CONTENT) {
            User u = new User(suite + test + user, password);
            authenticate(u, client, HttpStatus.SC_NO_CONTENT);

            return u;
        }
        else {
            return null;
        }
    }

    public static void changePassword(User user, String newPass, HttpClient client, int expected) throws IOException {
        HttpResponse response;
        if(user != null) {
            response = HttpUtils.changePassword(user.getUsername(), user.getPassword(), newPass, client);
        }
        else {
            response = HttpUtils.changePassword(null, null, newPass, client);
        }
        Assert.assertEquals(expected, response.getStatusLine().getStatusCode());
        if(expected == HttpStatus.SC_NO_CONTENT) {
            user.setPassword(newPass);
            authenticate(user, client, HttpStatus.SC_NO_CONTENT);
        }
    }

    public static void authenticate(User user, HttpClient client, int expected) throws IOException {
        HttpResponse response;
        if(user != null) {
            response = HttpUtils.authenticate(user.getUsername(), user.getPassword(), client);
        }
        else {
            response = HttpUtils.authenticate(null, null, client);
        }
        Assert.assertEquals(expected, response.getStatusLine().getStatusCode());
    }
}
