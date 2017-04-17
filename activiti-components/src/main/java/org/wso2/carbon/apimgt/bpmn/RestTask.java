package org.wso2.carbon.apimgt.bpmn;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.wso2.carbon.apimgt.bpmn.util.RestResponse;
import org.wso2.carbon.apimgt.bpmn.util.RestUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * Task for rest api invocation. Code is a simplified version of WSO2 Business Process Server's
 * org.wso2.carbon.bpmn.extensions.rest.RESTTask implementation
 * 
 * @see <a href=
 *      "https://github.com/wso2/carbon-business-process/blob/v4.4.10/components/bpmn/org.wso2.carbon.bpmn.extensions/src/main/java/org/wso2/carbon/bpmn/extensions/rest/RESTTask.java"
 *      </a>
 */
public class RestTask implements JavaDelegate {

    private static final Log log = LogFactory.getLog(RestTask.class);
    private Expression serviceURL;
    private Expression headers;
    private Expression method;
    private Expression input;
    private Expression description;
    private Expression outputMappings;
    private Expression basicAuthUsername;
    private Expression basicAuthPassword;

    private JsonNode jsonHeaders = null;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String authHeader;

    public void execute(DelegateExecution execution) {

        try {
            if (description != null) {
                String desc = description.getValue(execution).toString();
                log.info("Executing RestTask : " + desc);
            }
            String url = null;
            if (serviceURL != null) {
                url = serviceURL.getValue(execution).toString();
                if (basicAuthUsername != null && basicAuthPassword != null) {
                    String username = basicAuthUsername.getValue(execution).toString();
                    String password = basicAuthPassword.getValue(execution).toString();
                    String combinedCredentials = username + ":" + password;
                    byte[] encodedCredentials = Base64
                            .encodeBase64(combinedCredentials.getBytes(StandardCharsets.UTF_8));
                    authHeader = "Basic " + new String(encodedCredentials);
                }
            }
            if (headers != null) {
                String headerContent = headers.getValue(execution).toString();
                jsonHeaders = objectMapper.readTree(headerContent);

            }
            RestResponse response;
            if (HttpPost.METHOD_NAME.equalsIgnoreCase(method.getValue(execution).toString().trim())) {
                String inputContent = input.getValue(execution).toString();
                response = RestUtil.invokePOST(new URI(url), jsonHeaders, inputContent, authHeader);
            } else if (HttpGet.METHOD_NAME.equalsIgnoreCase(method.getValue(execution).toString().trim())) {
                response = RestUtil.invokeGET(new URI(url), jsonHeaders, authHeader);
            } else {
                String errorMsg = "Unsupported http method. The REST task only supports GET, POST operations";
                throw new BpmnError(errorMsg);
            }
           
            if (response == null) {
                String errorMsg = "Error while invoking endpoint: null response";
                log.error(errorMsg);
                throw new BpmnError(errorMsg);
            }
            if (response.getHttpStatus() >= 400) {
                String errorMsg = "Error while invoking endpoint: " + response.toString();
                log.error(errorMsg);
                throw new BpmnError(errorMsg);
            }

            String outputString = response.getContent();
            if (!response.getContent().isEmpty() && response.getContentType().contains("application/json")) {
                if(outputMappings != null){
                    String outMappings = outputMappings.getValue(execution).toString();
                    outMappings = outMappings.trim();
                    String[] mappings = outMappings.split(",");
                    for (String mapping : mappings) {
                        String[] mappingParts = mapping.split(":");
                        String varName = mappingParts[0];
                        String expression = mappingParts[1];
                        Object value = JsonPath.read(outputString, expression);
                        execution.setVariableLocal(varName, value);
                    }
                }               
            } else if (!response.getContentType().contains("application/json")) {
                String errorMsg = "Task supports only application/json";
                log.error(errorMsg);
                throw new BpmnError(errorMsg);
            } else {
                String errorMsg = "Error while completing the rest call: " + response.toString();
                log.error(errorMsg);
                throw new BpmnError(errorMsg);
            }

        } catch (URISyntaxException e) {
            log.error(e);
            throw new BpmnError(e.getLocalizedMessage());
        } catch (JsonProcessingException e) {
            log.error(e);
            throw new BpmnError(e.getLocalizedMessage());
        } catch (IOException e) {
            log.error(e);
            throw new BpmnError(e.getLocalizedMessage());
        } 
    }

}
