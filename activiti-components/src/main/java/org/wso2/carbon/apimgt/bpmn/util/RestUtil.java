package org.wso2.carbon.apimgt.bpmn.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

public class RestUtil {

    private static final Log log = LogFactory.getLog(RestUtil.class);
    public static RestResponse invokeGET(URI uri, JsonNode headerMaps, String authHeader) {

        HttpGet httpGet = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        Header[] headers = null;
        int httpStatus = 0;
        String contentType = null;
        String output = null;
        try {
            // client = HttpClients.createDefault();
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            log.warn("Certificate validation is not implemented in org.wso2.carbon.apimgt.bpmn.util.RestUtil");
            httpGet = new HttpGet(uri);
            if (log.isDebugEnabled()) {
                log.debug("GET : " + uri);
            }
            processHeaderList(httpGet, headerMaps);
            if (authHeader != null) {
                httpGet.addHeader("Authorization", authHeader);
                if (log.isDebugEnabled()) {
                    log.debug( "Authorization : " + authHeader);
                }
            }
            response = client.execute(httpGet);
            output = IOUtils.toString(response.getEntity().getContent());
            headers = response.getAllHeaders();
            httpStatus = response.getStatusLine().getStatusCode();
            contentType = response.getEntity().getContentType().getValue();
            EntityUtils.consume(response.getEntity());
        } catch (Exception e) {
            log.error("Error while invokin GET to " + uri , e);
        } finally {
            if (response != null) {
                IOUtils.closeQuietly(response);
            }
            if (client != null) {
                IOUtils.closeQuietly(client);
            }
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        RestResponse restResponse = new RestResponse(contentType, output, headers, httpStatus);
        if (log.isDebugEnabled()) {
            log.debug("Response: " + restResponse.toString());
        }
        return restResponse;
    }

    public static RestResponse invokePOST(URI uri, JsonNode headerMaps, String payload, String authHeader) {

        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        Header[] headers = null;
        int httpStatus = 0;
        String contentType = null;
        String output = null;
        try {
            // client = HttpClients.createDefault();           
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            log.warn("Certificate validation is not implemented in org.wso2.carbon.apimgt.bpmn.util.RestUtil");
            httpPost = new HttpPost(uri);
            if (log.isDebugEnabled()) {
                log.debug("POST : " + uri);
            }
            httpPost.setEntity(new StringEntity(payload));
            processHeaderList(httpPost, headerMaps);
            if (authHeader != null) {
                httpPost.addHeader("Authorization", authHeader);
                if (log.isDebugEnabled()) {
                    log.debug( "Authorization : " + authHeader);
                }
            }
           
            response = client.execute(httpPost);
            output = IOUtils.toString(response.getEntity().getContent());
           
            headers = response.getAllHeaders();
            httpStatus = response.getStatusLine().getStatusCode();
            contentType = response.getEntity().getContentType().getValue();
            EntityUtils.consume(response.getEntity());
        } catch (Exception e) {
            log.error("Error while invokin POST to " + uri , e);
        } finally {
            if (response != null) {
                IOUtils.closeQuietly(response);
            }
            if (client != null) {
                IOUtils.closeQuietly(client);
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        RestResponse restResponse = new RestResponse(contentType, output, headers, httpStatus);
        if (log.isDebugEnabled()) {
            log.debug("Response: " + restResponse.toString());
        }
        return restResponse;
    }
    
    public static RestResponse invokePUT(URI uri, JsonNode headerMaps, String payload, String authHeader) {

        HttpPut httpPut = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        Header[] headers = null;
        int httpStatus = 0;
        String contentType = null;
        String output = null;
        try {
            // client = HttpClients.createDefault();           
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            log.warn("Certificate validation is not implemented in org.wso2.carbon.apimgt.bpmn.util.RestUtil");
            httpPut = new HttpPut(uri);
            if (log.isDebugEnabled()) {
                log.debug("PUT : " + uri);
            }
            httpPut.setEntity(new StringEntity(payload));
            processHeaderList(httpPut, headerMaps);
            if (authHeader != null) {
                httpPut.addHeader("Authorization", authHeader);
                if (log.isDebugEnabled()) {
                    log.debug( "Authorization : " + authHeader);
                }
            }
           
            response = client.execute(httpPut);
            output = IOUtils.toString(response.getEntity().getContent());
           
            headers = response.getAllHeaders();
            httpStatus = response.getStatusLine().getStatusCode();
            contentType = response.getEntity().getContentType().getValue();
            EntityUtils.consume(response.getEntity());
        } catch (Exception e) {
            log.error("Error while invokin POST to " + uri , e);
        } finally {
            if (response != null) {
                IOUtils.closeQuietly(response);
            }
            if (client != null) {
                IOUtils.closeQuietly(client);
            }
            if (httpPut != null) {
                httpPut.releaseConnection();
            }
        }
        RestResponse restResponse = new RestResponse(contentType, output, headers, httpStatus);
        if (log.isDebugEnabled()) {
            log.debug("Response: " + restResponse.toString());
        }
        return restResponse;
    }
    private static void processHeaderList(HttpRequestBase request, JsonNode jsonHeaders) {
        if (log.isDebugEnabled()) {
            log.debug("Request headers...");
        }
        if (jsonHeaders != null) {
            Iterator<String> iterator = jsonHeaders.fieldNames();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonHeaders.findValue(key).textValue();
                request.addHeader(key, value);
                if (log.isDebugEnabled()) {
                    log.debug( key + " : " + value);
                }
            }
        }

    }
}
