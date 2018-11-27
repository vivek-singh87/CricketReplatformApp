package atg.commerce.order.processor; 
 
import java.util.HashMap;

import com.cricket.commerce.order.CricketOrderImpl;

import atg.commerce.order.PipelineConstants;
import atg.commerce.order.processor.ProcSetPaymentGroupAmount;
import atg.service.pipeline.PipelineResult;
/*
 * in case of downgrade plan or addon and price is -ve : we skipping the oob call
 */
public class CricketSetPaymentGroupAmount extends ProcSetPaymentGroupAmount
{
  @Override
	public int runProcess(Object pParam, PipelineResult arg1) throws Exception {
		 HashMap<?,?> paramMap = (HashMap<?,?>) pParam;
		   	String orderType = null;     
		    CricketOrderImpl cricketOrder = (CricketOrderImpl) paramMap.get(PipelineConstants.ORDER);
		    if(null !=cricketOrder && cricketOrder.getPriceInfo().getAmount() < 0.0)
		    {
		    	return 1;
		    }else{
		    	return super.runProcess(pParam, arg1);
		    }
	}
}
 