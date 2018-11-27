package com.cricket.commerce.order.payment.processor;

import atg.nucleus.GenericService;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;

import com.cricket.integration.esp.CricketESPAdapter;

public class ProcProcessManageAutoTopUp extends GenericService implements PipelineProcessor {
  	  
	  private final int SUCCESS = 1;
	  
	  /* holds espAdapter instance to communicate ESP Layer */
	  private CricketESPAdapter espAdapter;
			
		/** get the CricketESPAdapter instance.
		 * @return
		 */
		public CricketESPAdapter getEspAdapter() {
			return espAdapter;
		}

		/** set the CricketESPAdapter instance.
		 * @param espAdapter
		 */
		public void setEspAdapter(CricketESPAdapter espAdapter) {
			this.espAdapter = espAdapter;
		}
	 
	  /**
	   * 1 - The processor completed
	   * @return an integer array of the valid return codes.
	   */
	  public int[] getRetCodes()
	  {
	    int[] ret = {SUCCESS};
	    return ret;
	  }  
                
	
	  /* (non-Javadoc)
	 * @see atg.service.pipeline.PipelineProcessor#runProcess(java.lang.Object, atg.service.pipeline.PipelineResult)
	 */
	public int runProcess(Object pParam, PipelineResult pResult) throws Exception
	  {
		/*long starttime	= 0L;
		long endtime 	= 0L;	
	    HashMap map = (HashMap) pParam;
	    Order pOrder = (Order) map.get(PipelineConstants.ORDER);*/
	    
	    logInfo("[ProcProcessManageAutoTopUp->runProcess()]: Entering into runProcess()...");
		//not in PhaseI
		logInfo("[ProcProcessManageAutoTopUp->runProcess()]: Exiting runProcess(), with return SUCCESS...");
	    return SUCCESS;
	  }
}
 