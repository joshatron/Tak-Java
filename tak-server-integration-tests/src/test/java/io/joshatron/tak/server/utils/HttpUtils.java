package io.joshatron.tak.server.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class HttpUtils {

    public static String baseUrl = "https://localhost:8080";

    public static HttpClient createHttpClient() {
        try {
            SSLContextBuilder builder = SSLContexts.custom();
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    return true;
                }
            });
            SSLContext sslContext = builder.build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslContext, new X509HostnameVerifier() {
                @Override
                public void verify(String host, SSLSocket ssl)
                        throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert)
                        throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns,
                                   String[] subjectAlts) throws SSLException {
                }

                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create().register("https", sslsf)
                    .build();

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);

            return HttpClients.custom().setConnectionManager(cm).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getBasicAuthString(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    public static HttpResponse createUser(String username, String password, HttpClient client) throws IOException {
        String payload = "{";
        if(username != null) {
            payload += "\"username\": \"" + username + "\"";
        }
        if(password != null) {
            if(username != null) {
                payload += ",";
            }
            payload += "\"password\": \"" + password + "\"";
        }
        payload += "}";

        HttpPut request = new HttpPut(baseUrl + "/account/register");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse changePassword(String username, String password, String newPassword, HttpClient client) throws IOException {
        String payload = "{";
        if(newPassword != null) {
            payload += "\"updated\": \"" + newPassword + "\"";
        }
        payload += "}";

        HttpPost request = new HttpPost(baseUrl + "/account/changepass");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        if(username != null && password != null) {
            request.setHeader("Authorization", getBasicAuthString(username, password));
        }
        request.setEntity(entity);

        return client.execute(request);
    }


    public static HttpResponse authenticate(String username, String password, HttpClient client) throws IOException {
        HttpGet request = new HttpGet(baseUrl + "/account/authenticate");
        if(username != null && password != null) {
            request.setHeader("Authorization", getBasicAuthString(username, password));
        }

        return client.execute(request);
    }

    public static HttpResponse requestFriend(String username, String password, String other, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }," +
                "    \"other\": \"" + other + "\"" +
                "}";

        HttpPost request = new HttpPost(baseUrl + "/social/request");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse cancelFriendRequest(String username, String password, String other, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }," +
                "    \"other\": \"" + other + "\"" +
                "}";

        HttpPost request = new HttpPost(baseUrl + "/social/cancel");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse respondToRequest(String username, String password, String other, String response, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }," +
                "    \"friend\": \"" + other + "\"," +
                "    \"response\": \"" + response + "\"" +
                "}";

        HttpPost request = new HttpPost(baseUrl + "/social/response");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse checkIncomingFriendRequests(String username, String password, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }" +
                "}";

        HttpPost request = new HttpPost(baseUrl + "/social/incoming");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse checkOutgoingFriendRequests(String username, String password, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }" +
                "}";

        HttpPost request = new HttpPost(baseUrl + "/social/outgoing");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse blockUser(String username, String password, String other, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }," +
                "    \"other\": \"" + other + "\"" +
                "}";

        HttpPost request = new HttpPost(baseUrl + "/social/block");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse unblockUser(String username, String password, String other, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }," +
                "    \"other\": \"" + other + "\"" +
                "}";

        HttpPost request = new HttpPost(baseUrl + "/social/unblock");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse getFriends(String username, String password, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }" +
                "}";

        HttpPost request = new HttpPost(baseUrl + "/social/friends");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse getBlocked(String username, String password, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }" +
                "}";

        HttpPost request = new HttpPost(baseUrl + "/social/blocked");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse sendMessage(String username, String password, String recipient, String message, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }," +
                "    \"recipient\": \"" + recipient + "\"," +
                "    \"message\": \"" + message + "\"" +
                "}";

        HttpPost request = new HttpPost(baseUrl + "/social/send");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }

    public static HttpResponse recieveMessages(String username, String password, String[] senders, String start, String read, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }";

        if(senders != null) {
            payload += ",\"senders\": [";
            for(int i = 0; i < senders.length; i++) {
                payload += "\"" + senders[i] + "\"";
                if(i + 1 != senders.length) {
                    payload += ",";
                }
            }
            payload += "]";
        }

        if(start != null) {
            payload += ",\"start\": \"" + start + "\"";
        }
        if(read != null) {
            payload += ",\"read\": \"" + read + "\"";
        }

        payload += "}";

        HttpPost request = new HttpPost(baseUrl + "/social/read");
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        return client.execute(request);
    }
}
