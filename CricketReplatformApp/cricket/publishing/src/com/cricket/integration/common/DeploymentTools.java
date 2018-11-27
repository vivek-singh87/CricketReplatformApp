package com.cricket.integration.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.CreateException;

import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.epub.project.Process;
import atg.epub.project.ProcessHome;
import atg.epub.project.ProjectConstants;
import atg.nucleus.GenericService;
import atg.process.action.ActionConstants;
import atg.process.action.ActionException;
import atg.repository.RepositoryItem;
import atg.security.Persona;
import atg.security.ThreadSecurityManager;
import atg.security.User;
import atg.userdirectory.UserDirectoryUserAuthority;
import atg.versionmanager.VersionManager;
import atg.versionmanager.WorkingContext;
import atg.versionmanager.Workspace;
import atg.versionmanager.exceptions.VersionException;
import atg.workflow.ActorAccessException;
import atg.workflow.MissingWorkflowDescriptionException;
import atg.workflow.WorkflowConstants;
import atg.workflow.WorkflowException;
import atg.workflow.WorkflowManager;
import atg.workflow.WorkflowView;
import com.cricket.common.constants.CricketCommonConstants;

/**
 * The class contains common methods that can be used to automatically create BCC projects from 
 * backend code for storing third party feeds or any other purpose
 * 
 * @author Tech Mahindra
 *
 */
public class DeploymentTools extends GenericService {
	
	/**
	 * Variable which holds reference to OOTB VersionManager class
	 */
	private VersionManager mVersionManager;
	/**
	 * Variable which holds reference to OOTB WorkflowManager class
	 */
	private WorkflowManager mWorkflowManager;
	/**
	 * Variable which holds reference to OOTB UserDirectoryUserAuthority class
	 */
	private UserDirectoryUserAuthority mUserAuthority;
	/**
	 * Variable which holds PersonaPrefix for the BCC project to be created
	 */
	private String mPersonaPrefix;
	/**
	 * Variable which holds UserName for the BCC project to be created
	 */
	private String mUserName;
	/**
	 * Variable which holds WorkflowName for the BCC project to be created
	 */
	private String mWorkflowName;
	/**
	 * Variable which holds TaskOutcomeId for the BCC project to be created
	 */
	private String mTaskOutcomeId;
	/**
	 * Variable which holds ActivityId for the BCC project to be created
	 */
	private String mActivityId;
	
	
	  /**
	   * This is the starting point for the service. In order to start it, the executeImport() method needs to
	   * be called by another service. This method begins a transaction and sets the security
	   * context on the thread for the user specified in the userName property. Next, it creates a project
	   * and then calls importUserData(). Next, it attempts to advance the project's workflow. Finally, it
	   * unsets the security context and commits the transaction.
	   *
	   * <b>NOTE!!! - This code only creates a single transaction and is suitable for imports which can fit
	   * into the context of a single transaction. If you are doing large imports, then you must handle batching
	   * the transaction in the importUserData() method.</b>
	   */
	public Process createProject(TransactionDemarcation td, String projectNamePrefix) throws 
			VersionException, WorkflowException, CreateException, ActionException, TransactionDemarcationException, Exception {
		
		assumeUserIdentity();
		DateFormat dateFormat = new SimpleDateFormat(CricketCommonConstants.SIMPLE_DATE_FORMAT);
	    Date date = new Date();
	    String projectName = projectNamePrefix + dateFormat.format(date);
	    vlogDebug("Project Name : " + projectName);
	    vlogDebug("Workflow Name : " + getWorkflowName());
	    vlogDebug("Activity Id : " + getActivityId());
	    ProcessHome processHome = ProjectConstants.getPersistentHomes().getProcessHome();
	    Process process = processHome.createProcessForImport(projectName, getWorkflowName(), getActivityId());
	    String wkspName = process.getProject().getWorkspace();
	    vlogDebug("Workspace Name : " + wkspName);
	    Workspace wksp = getVersionManager().getWorkspaceByName(wkspName);
	    WorkingContext.pushDevelopmentLine(wksp); 
	    return process;		
	}
	
	  /**
	   * This method advances the workflow to the next state. If using an unaltered copy of the import-late
	   * or import-early workflows, then the taskOutcomeId property should not need to be changed
	   * (default is '4.1.1'). If you are using a different workflow or an altered version of the import-xxxx
	   * workflows, then the taskOutcomeId can be found in the wdl file for the respective workflow.
	   *
	   * @param pProcess the atg.epub.project.Process object (the project)
	   */
	public void advanceWorkflow(Process pProcess) throws WorkflowException, ActionException
	  {
	    RepositoryItem processWorkflow = pProcess.getProject().getWorkflow();
	    String workflowProcessName = processWorkflow.getPropertyValue("processName").toString();
	    String subjectId = pProcess.getId();

	    try {
	      // an alternative would be to use the global workflow view at
	      WorkflowView wv = getWorkflowManager().getWorkflowView(ThreadSecurityManager.currentUser());

	      wv.fireTaskOutcome(workflowProcessName, WorkflowConstants.DEFAULT_WORKFLOW_SEGMENT,
	                          subjectId,
	                          getTaskOutcomeId(),
	                          ActionConstants.ERROR_RESPONSE_DEFAULT);

	    } catch (MissingWorkflowDescriptionException e) {
	      throw e;
	    } catch (ActorAccessException e) {
	      throw e;
	    } catch (ActionException e) {
	      throw e;
	    } catch (UnsupportedOperationException e) {
	      throw e;
	    }
	  }

	  /**
	   * This method sets the security context for the current thread so that the code executes correctly
	   * against secure resources.
	   *
	   * @return true if the identity was assumed, false otherwise
	   */
	protected boolean assumeUserIdentity() {
		if (getUserAuthority() == null)
			return false;

		User newUser = new User();
		Persona persona = (Persona) getUserAuthority().getPersona(getPersonaPrefix() + getUserName());
	    if (persona == null)
	        return false;

	    // create a temporary User object for the identity
	    newUser.addPersona(persona);

	    // replace the current User object
	    ThreadSecurityManager.setThreadUser(newUser);

	    return true;
	  }

	 /**
	   * This method unsets the security context on the current thread.
	   */
	public void releaseUserIdentity() {
		ThreadSecurityManager.setThreadUser(null);
	}
	
	/**
	 * 
	 * @return the mVersionManager
	 */
	public VersionManager getVersionManager() {
	    return mVersionManager;
	}
	
	/**
	 * 
	 * @param pVersionManager
	 */
	public void setVersionManager(VersionManager pVersionManager) {
	    mVersionManager = pVersionManager;
	}
	
	/**
	 * 
	 * @return the mWorkflowManager
	 */
	public WorkflowManager getWorkflowManager() {
	    return mWorkflowManager;
	}
	
	/**
	 * 
	 * @param pWorkflowManager
	 */
	public void setWorkflowManager(WorkflowManager pWorkflowManager) {
	    mWorkflowManager = pWorkflowManager;
	}
	
	/**
	 * 
	 * @return the mUserAuthority
	 */
	public UserDirectoryUserAuthority getUserAuthority() {
		return mUserAuthority;
	}
	
	/**
	 * 
	 * @param pUserAuthority
	 */
	public void setUserAuthority(UserDirectoryUserAuthority pUserAuthority) {
		mUserAuthority = pUserAuthority;
	}
	
	/**
	 * 
	 * @return the mPersonaPrefix
	 */
	public String getPersonaPrefix() {
	    return mPersonaPrefix;
	}
	
	/**
	 * 
	 * @param pPersonaPrefix
	 */
	public void setPersonaPrefix(String pPersonaPrefix) {
		mPersonaPrefix = pPersonaPrefix;
	}
	
	/**
	 * 
	 * @return the mUserName
	 */
	public String getUserName() {
		return mUserName;
	}

	/**
	 * 
	 * @param pUserName
	 */
	public void setUserName(String pUserName) {
		mUserName = pUserName;
	}
	
	/**
	 * 
	 * @return the mWorkflowName
	 */
	public String getWorkflowName() {
		return mWorkflowName;
	}

	/**
	 * 
	 * @param string
	 */
	public void setWorkflowName(String string) {
		mWorkflowName = string;
	}
	
	/**
	 * 
	 * @return the mTaskOutcomeId
	 */
	public String getTaskOutcomeId() {
	    return mTaskOutcomeId;
	}

	/**
	 * 
	 * @param pTaskOutcomeId
	 */
	public void setTaskOutcomeId(String pTaskOutcomeId) {
		mTaskOutcomeId = pTaskOutcomeId;
	}

	/**
	 * 
	 * @return the mActivityId
	 */
	public String getActivityId() {
		return mActivityId;
	}

	/**
	 * 
	 * @param pActivityId
	 */
	public void setActivityId(String pActivityId) {
		mActivityId = pActivityId;
	}	
}
