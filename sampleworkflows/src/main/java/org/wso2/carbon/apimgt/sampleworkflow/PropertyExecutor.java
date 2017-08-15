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
 * This sample executor demonstrate how to pass external parameters to the executor through configurations.
 * refer PropertyExecutorConfig.yml for the configurations
 */
public class PropertyExecutor implements WorkflowExecutor {
    private static final Logger log = LoggerFactory.getLogger(PropertyExecutor.class);

    // define private fields similar to 'name' key in 'property' element
    private String username;
    private String password;

    // implement getter/setter methods for each fields.
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

    public WorkflowResponse execute(Workflow workflow) throws WorkflowException {

        log.info("Executing Workflow Executor: PropertyExecutor#execute() ");

        log.info("username : " + username);
        log.info("password : " + password);

        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(WorkflowStatus.APPROVED);
        return workflowResponse;
    }

    public WorkflowResponse complete(Workflow workflow) throws WorkflowException {
        log.info("Executing Workflow Executor: PropertyExecutor#complete() ");
        WorkflowResponse workflowResponse = new GeneralWorkflowResponse();
        workflowResponse.setWorkflowStatus(workflow.getStatus());
        return workflowResponse;
    }

    public void cleanUpPendingTask(String workflowExtRef) throws WorkflowException {
        log.info("Executing Workflow Executor: PropertyExecutor#cleanUpPendingTask() ");
    }

}
