package io.joshatron.tak.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
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

    public static HttpResponse createUser(String username, String password, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"username\": \"" + username + "\"," +
                "    \"password\": \"" + password + "\"" +
                "}";

        HttpClient httpClient = HttpUtils.createHttpClient();

        HttpPost request = new HttpPost(baseUrl + "/account/register");
        StringEntity entity = new StringEntity(payload);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(entity);

        return httpClient.execute(request);
    }

    public static HttpResponse changePassword(String username, String password, String newPassword, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }," +
                "    \"updated\": \"" + newPassword + "\"" +
                "}";

        HttpClient httpClient = HttpUtils.createHttpClient();

        HttpPost request = new HttpPost(baseUrl + "/account/changepass");
        StringEntity entity = new StringEntity(payload);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(entity);

        return httpClient.execute(request);
    }


    public static HttpResponse authenticate(String username, String password, HttpClient client) throws IOException {
        String payload = "{" +
                "    \"auth\": {" +
                "        \"username\": \"" + username + "\"," +
                "        \"password\": \"" + password + "\"" +
                "    }" +
                "}";

        HttpClient httpClient = HttpUtils.createHttpClient();

        HttpPost request = new HttpPost(baseUrl + "/account/authenticate");
        StringEntity entity = new StringEntity(payload);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(entity);

        return httpClient.execute(request);
    }

}
