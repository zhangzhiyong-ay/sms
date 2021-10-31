package net.henanyuanhang.sms.httpextension.factory;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;

import javax.net.ssl.SSLContext;
import java.util.concurrent.TimeUnit;

/**
 * httpclient5 工厂类
 */
public class HttpClient5Factory {

    /**
     * http(s) 客户端
     *
     * @return
     */
    public static CloseableHttpClient createHttpClient() {

        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial((x509Certificates, s) -> true).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslConnectionSocketFactory)
                    .build();

            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
            poolingHttpClientConnectionManager.setMaxTotal(200);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10, TimeUnit.SECONDS)
                    .setCookieSpec(StandardCookieSpec.IGNORE)
                    .setExpectContinueEnabled(true)
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager)
                    .setConnectionManagerShared(true)
                    .evictExpiredConnections()
                    .evictIdleConnections(TimeValue.of(30, TimeUnit.SECONDS))
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            return httpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
