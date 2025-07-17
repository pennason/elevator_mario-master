package com.shmashine.common.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Http 工具
 *
 * @author chenx
 */

@Slf4j
public class HttpTools {
    private CloseableHttpClient httpclient = HttpClients.createDefault();

    public void getUrl(String geturl) throws ClientProtocolException, IOException {

        try {
            HttpGet httpget = new HttpGet(geturl);

            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000).setSocketTimeout(6000).build();
            httpget.setConfig(requestConfig);

            log.info("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(final HttpResponse response)
                        throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            log.info("----------------------------------------");
            log.info(responseBody);

        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Object[] postUrlUTF8Json(String message, String posturl, Map<String, String> headMap) {

        int statusCode = 500;
        String bodyString = "";

        HttpPost httppost = new HttpPost(posturl);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
                .setConnectionRequestTimeout(1000).setSocketTimeout(5000).build();
        httppost.setConfig(requestConfig);

        if (headMap != null) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                httppost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        httppost.setHeader("Content-type", "application/json; charset=utf-8");
        httppost.setHeader("Accept", "application/json");
        if (message != null) {
            httppost.setEntity(new StringEntity(message, Charset.forName("UTF-8")));
        }

        CloseableHttpResponse response = null;

        try {

            response = httpclient.execute(httppost);
            statusCode = response.getStatusLine().getStatusCode();

            HttpEntity resen = response.getEntity();
            if (resen != null) {
                bodyString = EntityUtils.toString(resen);
            }


            log.info(bodyString);

        } catch (Exception e) {
            log.info("post  error", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.info("post close error", e);
            }
        }

        return new Object[]{statusCode, bodyString};

    }


    public Object[] putUrlUTF8Json(String message, String posturl, Map<String, String> headMap) {

        int statusCode = 500;
        String bodyString = "";

        HttpPut httpPut = new HttpPut(posturl);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
                .setConnectionRequestTimeout(1000).setSocketTimeout(5000).build();
        httpPut.setConfig(requestConfig);

        if (headMap != null) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                httpPut.setHeader(entry.getKey(), entry.getValue());
            }
        }

        httpPut.setHeader("Content-type", "application/json; charset=utf-8");
        httpPut.setHeader("Accept", "application/json");
        if (message != null) {
            httpPut.setEntity(new StringEntity(message, Charset.forName("UTF-8")));
        }

        CloseableHttpResponse response = null;

        try {

            response = httpclient.execute(httpPut);
            statusCode = response.getStatusLine().getStatusCode();

            HttpEntity resen = response.getEntity();
            if (resen != null) {
                bodyString = EntityUtils.toString(resen);
            }


            log.info(bodyString);

        } catch (Exception e) {
            log.info("post  error", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.info("post close error", e);
            }
        }

        return new Object[]{statusCode, bodyString};

    }

    public Object[] get(String url, Map<String, String> headMap) {

        int statusCode = 500;
        String bodyString = "";

        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000 * 120)
                .setConnectionRequestTimeout(1000 * 60).setSocketTimeout(5000 * 60).build();
        httpGet.setConfig(requestConfig);

        if (headMap != null) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }

        httpGet.setHeader("Content-type", "application/json; charset=utf-8");
        httpGet.setHeader("Accept", "application/json");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            statusCode = response.getStatusLine().getStatusCode();

            HttpEntity resen = response.getEntity();
            if (resen != null) {
                bodyString = EntityUtils.toString(resen);
            }
        } catch (Exception e) {
            log.info("post  error", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.info("post close error", e);
            }
        }
        return new Object[]{statusCode, bodyString};

    }

}
