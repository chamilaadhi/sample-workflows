package org.wso2.carbon.apimgt.sampleworkflow.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    public static RestResponse doPost(String uri, String payload, Map<String, String> headersMap)  {
        
        String output = null;
        int httpStatus = 0;

        BufferedReader in = null;
        OutputStream outputStream = null;
        URL url;
        try {
            url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");

            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }
            // Send post request
            con.setDoOutput(true);
            outputStream = con.getOutputStream();

            outputStream.write(payload.getBytes("UTF-8"));
            httpStatus = con.getResponseCode();

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }      
            output = response.toString();
            
        } catch (MalformedURLException e) {
            log.error("Malformed url " , e);
        } catch (IOException e) {
            log.error("I/O exception " , e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (outputStream != null) {             
                    outputStream.close();
                }

            } catch (IOException e) {
                log.error("I/O exception " , e);
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
    public static RestResponse doGet(String uri, Map<String, String> headersMap) {
        
        String output = null;
        int httpStatus = 0;
        BufferedReader in = null;
        URL url;
        try {
            url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }

            httpStatus = con.getResponseCode();

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            output = response.toString();
        } catch (MalformedURLException e) {
            log.error("Malformed url " , e);
        } catch (IOException e) {
            log.error("I/O exception " , e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
   
            } catch (IOException e) {
                log.error("I/O exception " , e);
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
    public static RestResponse doDelete(String uri, Map<String, String> headersMap) {
        
        String output = null;
        int httpStatus = 0;
        BufferedReader in = null;
        URL url;
        try {
            url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("DELETE");

            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }

            httpStatus = con.getResponseCode();

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            output = response.toString();
        } catch (MalformedURLException e) {
            log.error("Malformed url " , e);
        } catch (IOException e) {
            log.error("I/O exception " , e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
   
            } catch (IOException e) {
                log.error("I/O exception " , e);
            }
        }        

        return new RestResponse(output, httpStatus);
    }

}
