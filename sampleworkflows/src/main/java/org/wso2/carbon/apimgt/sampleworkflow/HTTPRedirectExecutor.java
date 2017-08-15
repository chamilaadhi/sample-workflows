package org.wso2.carbon.apimgt.sampleworkflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.WorkflowExecutor;
import org.wso2.carbon.apimgt.core.api.WorkflowResponse;
import org.wso2.carbon.apimgt.core.exception.WorkflowException;
import org.wso2.carbon.apimgt.core.models.WorkflowStatus;
import org.wso2.carbon.apimgt.core.workflow.GeneralWorkflowResponse;
import org.wso2.carbon.apimgt.core.workflow.HttpWorkflowResponse;
import org.wso2.carbon.apimgt.core.workflow.Workflow;

/**
 * Executor to demonstrate workflow with redirection to external page capability. refer
 */
public class HTTPRedirectExecutor implements WorkflowExecutor {
    private static final Logger log = LoggerFactory.getLogger(HTTPRedirectExecutor.class);
    
    private String redirectUrl;    

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public WorkflowResponse execute(Workflow workflow) throws WorkflowException {

        log.info("Executing Workflow Executor: SampleHTTPRedirectExecutor#execute() ");
        
        HttpWorkflowResponse workflowResponse = new HttpWorkflowResponse();
        workflowResponse.setRedirectConfirmationMsg("This is redirection message");
        
        String url = redirectUrl + "?externalRef=" + workflow.getExternalWorkflowReference();       

        workflowResponse.setAdditionalParameters("env", "dev");
        workflowResponse.setRedirectUrl(url);

        // in this sample we directly approve the task. set it to WorkflowStatus.CREATED to hold
        workflowResponse.setWorkflowStatus(WorkflowStatus.CREATED);
        return workflowResponse;
    }

    public WorkflowResponse complete(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: SampleHTTPRedirectExecutor#complete() ");
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(workflow.getStatus());
        return workflowResponse;
    }

    public void cleanUpPendingTask(String workflowExtRef) throws WorkflowException {
        log.info("Executing Workflow Executor: SampleHTTPRedirectExecutor#cleanUpPendingTask() ");
    }
    
}
