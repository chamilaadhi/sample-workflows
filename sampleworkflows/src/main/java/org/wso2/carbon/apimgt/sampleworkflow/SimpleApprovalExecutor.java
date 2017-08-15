package org.wso2.carbon.apimgt.sampleworkflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.WorkflowExecutor;
import org.wso2.carbon.apimgt.core.api.WorkflowResponse;
import org.wso2.carbon.apimgt.core.exception.WorkflowException;

import org.wso2.carbon.apimgt.core.models.WorkflowStatus;
import org.wso2.carbon.apimgt.core.workflow.GeneralWorkflowResponse;
import org.wso2.carbon.apimgt.core.workflow.Workflow;

/**
 * Sample executor to demonstrate basic approval process. WorkflowReference ID will be printed on the logs and the
 * workflow task will be put in to On_hold state.  
 * refer SimpleApprovalExecutor.yml for config
 */
public class SimpleApprovalExecutor implements WorkflowExecutor {

    private static final Logger log = LoggerFactory.getLogger(SimpleApprovalExecutor.class);

    public WorkflowResponse execute(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: SimpleApprovalExecutor#execute() ");
        log.info(workflow.toString());
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(WorkflowStatus.CREATED);
        return workflowResponse;
    }

    public WorkflowResponse complete(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: SimpleApprovalExecutor#complete() ");
        log.info(workflow.toString());
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(workflow.getStatus());
        return workflowResponse;
    }

    public void cleanUpPendingTask(String workflowExtRef) throws WorkflowException {
        log.info("Executing Workflow Executor: SimpleApprovalExecutor#cleanUpPendingTask() ");
        log.info("workflowExtRef : " + workflowExtRef);
    }

}
