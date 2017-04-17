package org.wso2.carbon.apimgt.sampleworkflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.WorkflowExecutor;
import org.wso2.carbon.apimgt.core.api.WorkflowResponse;
import org.wso2.carbon.apimgt.core.exception.WorkflowException;
import org.wso2.carbon.apimgt.core.models.Workflow;
import org.wso2.carbon.apimgt.core.models.WorkflowStatus;
import org.wso2.carbon.apimgt.core.workflow.GeneralWorkflowResponse;

/**
 * Sample executor to demonstrate basic executor feature. refer LogExecutorConfig.yml for config
 */
public class LogExecutor implements WorkflowExecutor {

    private static final Logger log = LoggerFactory.getLogger(LogExecutor.class);

    public WorkflowResponse execute(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: LogExecutor#execute() ");
        log.info(workflow.toString());
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(WorkflowStatus.APPROVED);
        return workflowResponse;
    }

    public WorkflowResponse complete(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: LogExecutor#complete() ");
        log.info(workflow.toString());
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(workflow.getStatus());
        return workflowResponse;
    }

    public void cleanUpPendingTask(String workflowExtRef) throws WorkflowException {
        log.info("Executing Workflow Executor: LogExecutor#cleanUpPendingTask() ");
        log.info("workflowExtRef : " + workflowExtRef);
    }

}
