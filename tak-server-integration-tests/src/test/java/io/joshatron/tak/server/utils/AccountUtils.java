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
            return new User(suite + test + user, password);
        }
        else {
            return null;
        }
    }

    public static void changePassword(User user, String newPass, HttpClient client, int expected) throws IOException {
        HttpResponse response = HttpUtils.changePassword(user.getUsername(), user.getPassword(), newPass, client);
        Assert.assertEquals(expected, response.getStatusLine().getStatusCode());
        if(expected == HttpStatus.SC_NO_CONTENT) {
            user.setPassword(newPass);
        }
    }
}
