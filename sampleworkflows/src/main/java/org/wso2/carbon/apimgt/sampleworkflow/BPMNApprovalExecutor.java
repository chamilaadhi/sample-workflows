package org.wso2.carbon.apimgt.sampleworkflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.WorkflowExecutor;
import org.wso2.carbon.apimgt.core.api.WorkflowResponse;
import org.wso2.carbon.apimgt.core.exception.WorkflowException;
import org.wso2.carbon.apimgt.core.models.Workflow;
import org.wso2.carbon.apimgt.core.models.WorkflowStatus;
import org.wso2.carbon.apimgt.core.workflow.GeneralWorkflowResponse;
import org.wso2.carbon.apimgt.sampleworkflow.util.PayloadUtil;
import org.wso2.carbon.apimgt.sampleworkflow.util.RestResponse;
import org.wso2.carbon.apimgt.sampleworkflow.util.RestUtil;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Sample executor to integrate BPMN engine for approval task. refer BPMNApprovalExecutorConfig.yml for configs
 * 
 */
public class BPMNApprovalExecutor implements WorkflowExecutor {

    private static final Logger log = LoggerFactory.getLogger(BPMNApprovalExecutor.class);
    private static final String WF_SCOPE = "apim:workflow_approve";
    private static final String RUNTIME_INSTANCE_RESOURCE_PATH = "/runtime/process-instances";

    private String clientId;
    private String clientSecret;
    private String tokenAPI;
    private String callbackUrl;
    private String processDefinitionKey;
    private String serviceEndpoint;
    private String username;
    private String password;

    public WorkflowResponse execute(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: BPMNApprovalExecutor#execute() ");

        String payload = PayloadUtil.buildBPMNProcessStartPalyload(processDefinitionKey,
                workflow.getExternalWorkflowReference(), clientId, clientSecret, WF_SCOPE, tokenAPI,
                workflow.getWorkflowDescription(), callbackUrl);

        Map<String, String> headers = new HashMap<String, String>();
        byte[] encodedAuth = Base64.getEncoder()
                .encode((username + ":" + password).getBytes(Charset.forName("ISO-8859-1")));
        headers.put("Authorization", "Basic " + new String(encodedAuth, Charset.forName("ISO-8859-1")));
        headers.put("Content-Type", "application/json");
        RestResponse response = null;
        try {
            response = RestUtil.doPost(serviceEndpoint + RUNTIME_INSTANCE_RESOURCE_PATH, payload, headers);
        } catch (Exception e) {
            log.error("Error in BPMNApprovalExecutor", e);
            throw new WorkflowException(e.getMessage());
        }

        if (response.getHttpStatus() != 201) {
            String msg = "Error while starting the process. http status code:  " + response.getHttpStatus()
                    + " response: " + response.getContent();
            log.error(msg);
            throw new WorkflowException(msg);
        }
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(WorkflowStatus.CREATED);
        return workflowResponse;
    }

    public WorkflowResponse complete(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: BPMNApprovalExecutor#complete() ");
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(workflow.getStatus());
        return workflowResponse;
    }

    public void cleanUpPendingTask(String workflowExtRef) throws WorkflowException {
        log.info("Executing Workflow Executor: BPMNApprovalExecutor#cleanUpPendingTask() ");
        // get processId related to the workflowreference
        String uri = serviceEndpoint + RUNTIME_INSTANCE_RESOURCE_PATH + "?businessKey=" + workflowExtRef;
        Map<String, String> headers = new HashMap<String, String>();
        byte[] encodedAuth = Base64.getEncoder()
                .encode((username + ":" + password).getBytes(Charset.forName("ISO-8859-1")));
        headers.put("Authorization", "Basic " + new String(encodedAuth, Charset.forName("ISO-8859-1")));
        try {
            RestResponse response = RestUtil.doGet(uri, headers);
            if (response.getHttpStatus() == 200) {
                String processId = PayloadUtil.getProcessId(response.getContent());
                if (processId != null) {
                    String deleteUri = serviceEndpoint + RUNTIME_INSTANCE_RESOURCE_PATH + "/" + processId;
                    RestUtil.doDelete(deleteUri, headers);
                }
            }
        } catch (Exception e) {
            log.error("Error while cleaning up pending task", e);
            throw new WorkflowException("Error while cleaning up pending task", e);
        }

    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getTokenAPI() {
        return tokenAPI;
    }

    public void setTokenAPI(String tokenAPI) {
        this.tokenAPI = tokenAPI;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
