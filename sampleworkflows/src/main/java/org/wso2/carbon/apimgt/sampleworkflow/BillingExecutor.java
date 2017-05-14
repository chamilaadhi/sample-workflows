package org.wso2.carbon.apimgt.sampleworkflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.WorkflowExecutor;
import org.wso2.carbon.apimgt.core.api.WorkflowResponse;
import org.wso2.carbon.apimgt.core.exception.APIManagementException;
import org.wso2.carbon.apimgt.core.exception.WorkflowException;
import org.wso2.carbon.apimgt.core.models.SubscriptionWorkflow;
import org.wso2.carbon.apimgt.core.models.Workflow;
import org.wso2.carbon.apimgt.core.models.WorkflowStatus;
import org.wso2.carbon.apimgt.core.workflow.GeneralWorkflowResponse;
import org.wso2.carbon.apimgt.core.workflow.HttpWorkflowResponse;
import org.wso2.carbon.apimgt.sampleworkflow.dao.BillingDao;

/**
 * Sample executor to demonstrate basic executor feature. refer BillingExecutor.yml for config. This sample is written
 * as a sample executor to use with subscription workflow
 */
public class BillingExecutor implements WorkflowExecutor {

    private static final Logger log = LoggerFactory.getLogger(BillingExecutor.class);
    private String billingEngineUrl;
    private String apimStoreUrl;
    private String callbackUrl;
    

    public String getBillingEngineUrl() {
        return billingEngineUrl;
    }

    public void setBillingEngineUrl(String billingEngineUrl) {
        this.billingEngineUrl = billingEngineUrl;
    }

    public String getApimStoreUrl() {
        return apimStoreUrl;
    }

    public void setApimStoreUrl(String apimStoreUrl) {
        this.apimStoreUrl = apimStoreUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public WorkflowResponse execute(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: BillingExecutor#execute() ");
        SubscriptionWorkflow subsWorkflow;
        boolean userExists = false;

        if (workflow instanceof SubscriptionWorkflow) {
            subsWorkflow = (SubscriptionWorkflow) workflow;

            try {
                BillingDao billingDao = BillingDao.getInstance();
                userExists = billingDao.userExists(subsWorkflow.getSubscriber());
            } catch (APIManagementException e) {
                log.error("Error occurred while accessing Database: " + e.getMessage(), e);
                throw new WorkflowException("Error occurred while accessing Database: " + e.getMessage(), e);
            }
            log.info("BillingExecutor: user " + subsWorkflow.getSubscriber() + " exists: " + userExists);
        }
       
        if (!userExists) {
            HttpWorkflowResponse httpworkflowResponse = new HttpWorkflowResponse();
            String url = billingEngineUrl + "?CallbackUrl=" + callbackUrl + "&workflowRefId="
                    + workflow.getExternalWorkflowReference() + "&reDirectUrl=" + apimStoreUrl;
            httpworkflowResponse.setRedirectUrl(url);
            httpworkflowResponse.setRedirectConfirmationMsg(
                    "You will be redirected to a page to setup your billing account Information");
            return httpworkflowResponse;
        } else {
            WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
            workflowResponse.setWorkflowStatus(WorkflowStatus.APPROVED);
            return workflowResponse;
        }       
    }

    public WorkflowResponse complete(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: BillingExecutor#complete() ");
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(workflow.getStatus());
        return workflowResponse;
    }

    public void cleanUpPendingTask(String workflowExtRef) throws WorkflowException {
        log.info("Executing Workflow Executor: BillingExecutor#cleanUpPendingTask() ");
        log.info("workflowExtRef : " + workflowExtRef);
    }

}
