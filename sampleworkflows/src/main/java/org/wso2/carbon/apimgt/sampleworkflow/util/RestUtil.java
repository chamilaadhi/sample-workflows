package org.wso2.carbon.apimgt.sampleworkflow.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Utility class for rest api invocation 
 */
public class RestUtil {
    private static final Logger log = LoggerFactory.getLogger(RestUtil.class);
    
    /**
     * Invoke post method
     * @param uri
     * @param payload
     * @param headersMap
     * @return RestResponse RestResponse
     * @throws Exception
     */
    public static RestResponse doPost(String uri, String payload, Map<String, String> headersMap) throws Exception {
        
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String output = null;
        int httpStatus = 0;
  
        try {

            client = HttpClients.createDefault();   
            httpPost = new HttpPost(uri);
            if (log.isDebugEnabled()) {
                log.debug("POST : " + uri);
                log.debug("payload : " + payload);
            }
            httpPost.setEntity(new StringEntity(payload));
            if (headersMap != null) {
                for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                    if (log.isDebugEnabled()) {
                        log.debug("Headers : " + entry.getKey() + ":" + entry.getValue());
                    }
                }
            }            
            response = client.execute(httpPost);
            output = IOUtils.toString(response.getEntity().getContent());           

            httpStatus = response.getStatusLine().getStatusCode();

            if (log.isDebugEnabled()) {
                log.debug("Response: " + httpStatus + " : " + output);
            }
            EntityUtils.consume(response.getEntity());
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
        return new RestResponse(output, httpStatus);
    }
    
    /**
     * Invoke GET method
     * @param uri
     * @param headersMap
     * @return RestResponse RestResponse
     * @throws Exception
     */
    public static RestResponse doGet(String uri, Map<String, String> headersMap) throws Exception {
        
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String output = null;
        int httpStatus = 0;
  
        try {

            client = HttpClients.createDefault();   
            httpGet = new HttpGet(uri);
            if (log.isDebugEnabled()) {
                log.debug("GET : " + uri);                
            }
 
            if (headersMap != null) {
                for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                    if (log.isDebugEnabled()) {
                        log.debug("Headers : " + entry.getKey() + ":" + entry.getValue());
                    }
                }
            }            
            response = client.execute(httpGet);
            output = IOUtils.toString(response.getEntity().getContent());
            httpStatus = response.getStatusLine().getStatusCode();

            if (log.isDebugEnabled()) {
                log.debug("Response: " + httpStatus + " : " + output);
            }
            EntityUtils.consume(response.getEntity());
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
        return new RestResponse(output, httpStatus);
    }
    
    /**
     * Invoke DELETE method
     * @param uri
     * @param headersMap
     * @return RestResponse RestResponse
     * @throws Exception
     */
    public static RestResponse doDelete(String uri, Map<String, String> headersMap) throws Exception {
        
        HttpDelete httpDelete = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        //String output = null;
        int httpStatus = 0;
  
        try {

            client = HttpClients.createDefault();   
            httpDelete = new HttpDelete(uri);
            if (log.isDebugEnabled()) {
                log.debug("DELETE : " + uri);                
            }
 
            if (headersMap != null) {
                for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                    httpDelete.addHeader(entry.getKey(), entry.getValue());
                    if (log.isDebugEnabled()) {
                        log.debug("Headers : " + entry.getKey() + ":" + entry.getValue());
                    }
                }
            }            
            response = client.execute(httpDelete);          
            httpStatus = response.getStatusLine().getStatusCode();

            if (log.isDebugEnabled()) {
                log.debug("Response: " + httpStatus);
            }
            EntityUtils.consume(response.getEntity());
        } finally {
            if (response != null) {
                IOUtils.closeQuietly(response);
            }
            if (client != null) {
                IOUtils.closeQuietly(client);
            }
            if (httpDelete != null) {
                httpDelete.releaseConnection();
            }
        }
        return new RestResponse(null, httpStatus);
    }
}
