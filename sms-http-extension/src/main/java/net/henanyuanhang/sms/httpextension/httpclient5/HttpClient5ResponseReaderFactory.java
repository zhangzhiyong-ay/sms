package net.henanyuanhang.sms.httpextension.httpclient5;

import net.henanyuanhang.sms.httpextension.HttpResponse;
import net.henanyuanhang.sms.httpextension.ResponseReader;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class HttpClient5ResponseReaderFactory {

    public static <T extends HttpResponse> ResponseReader<T> createReader(Class<T> tClass, CloseableHttpResponse httpResponse) throws IOException, ParseException {
        return new ResponseReader<T>(tClass, httpResponse.getCode(), EntityUtils.toString(httpResponse.getEntity()));
    }
}
