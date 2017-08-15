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
 * Sample executor to demonstrate rejection from workflow. refer RejectExecutor.yml for config. One sample usecase of
 * this executor is to use to disable application edit
 */
public class RejectExecutor implements WorkflowExecutor {

    private static final Logger log = LoggerFactory.getLogger(RejectExecutor.class);

    public WorkflowResponse execute(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: RejectExecutor#execute() ");
        log.info(workflow.toString());
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(WorkflowStatus.REJECTED);
        return workflowResponse;
    }

    public WorkflowResponse complete(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: RejectExecutor#complete() ");
        log.info(workflow.toString());
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(workflow.getStatus());
        return workflowResponse;
    }

    public void cleanUpPendingTask(String workflowExtRef) throws WorkflowException {
        log.info("Executing Workflow Executor: RejectExecutor#cleanUpPendingTask() ");
        log.info("workflowExtRef : " + workflowExtRef);
    }

}
