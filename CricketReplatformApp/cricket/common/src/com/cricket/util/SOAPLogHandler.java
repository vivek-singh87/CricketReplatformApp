package com.cricket.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import atg.nucleus.Nucleus;
import atg.servlet.ServletUtil;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.configuration.CricketConfiguration;

public class SOAPLogHandler extends GenericHandler {

    private Date date;
    private CricketConfiguration mCricketConfiguration;
    private static final String ORDER_ID="orderId";
    private static final String NO_ORDER="NOORDER";
    private static final String DATE_FORMAT="yyyy-MM-dd";
    private static final String SOAP_ENV_FAULT = "<SOAP-ENV:Fault>";
    private static final String EMPTY_STRING = "";
    private static final String REGEX_SPACE_CHARACTER = "\\s";   
    private static final String ERROR_DESCRIPTION = "Error</ns1:description>";
    private String soapRequestString;   
    
    
    
    public SOAPLogHandler(){
        date = Calendar.getInstance().getTime();
        if(null==mCricketConfiguration)
			mCricketConfiguration = (CricketConfiguration) Nucleus.getGlobalNucleus().resolveName("/com/cricket/configuration/CricketConfiguration");	
    } 

	@Override
    public QName[] getHeaders() {
        return null;
    }

    @Override
    public boolean handleFault(MessageContext context) {
        mCricketConfiguration.logDebug("FAULT: [" + date + "]\n" + getStringMessage(context));        
        return super.handleFault(context);
    }
    
    private String getStringMessage(MessageContext context){
        String res = null;
        
        try {
            SOAPMessageContext ctx = (SOAPMessageContext) context;
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ctx.getMessage().writeTo(stream);
            byte[] items = stream.toByteArray();
            
            res = new String(items);            
        }
        catch(Exception e){
            // nothing - just ensuring the method will not throw an exception in case something is wrong.
        	mCricketConfiguration.logError("exception in SOAPLogHandler : "+e,e);
        	 
        }

        return res;
    }

    @Override
    public boolean handleRequest(MessageContext context) {
    	 
    	String orderId = (String) context.getProperty(ORDER_ID);
    	soapRequestString = getStringMessage(context);
    	if(!orderId.equalsIgnoreCase(NO_ORDER) )
    		storeCallLogs(context,"_request");
    	/*if(mCricketConfiguration.isLoggingDebug())
    		mCricketConfiguration.logDebug("REQUEST: [" + date + "]\n" + soapRequestString);   */     
    	
     	/*ApplicationLogging mLogging =
    		    ClassLoggingFactory.getFactory().getLoggerForClass(SOAPLogHandler.class);*/
        return super.handleRequest(context);
    }

    /**
     * @param pContext
     * @param pMessageType
     */
    private void storeCallLogs(MessageContext pContext, String pMessageType) {
    	
    	if(mCricketConfiguration.isEspXMLLoggingEnabled()){
	    	org.apache.axis.MessageContext cont = (org.apache.axis.MessageContext) pContext;
	    	String requestHandlerName = cont.getOperation().getName()+ pMessageType;
	    	 
	    	if(mCricketConfiguration.isLoggingDebug())  {
	    		mCricketConfiguration.logDebug("Generating xml for "+requestHandlerName);
	    	}
	    	    	
	    	String fileName = "CricketOrder_"+(String) pContext.getProperty(ORDER_ID);    	 
	    	
	    	File parentFolder = createParentFolder(pContext);
	    	File subFolder = new File(parentFolder.getAbsolutePath(), fileName);
	    	if(!subFolder.exists()) {
	    			subFolder.mkdirs();
	    			if(mCricketConfiguration.isLoggingDebug()){
						mCricketConfiguration.logDebug("Created sub folder to save ESP calls request/response " + subFolder.getAbsolutePath());
					}
	    			
	    	}    	    	 
	    	try {
				generateStringToXML(pContext, subFolder, requestHandlerName);
				if(mCricketConfiguration.isLoggingDebug()){
					mCricketConfiguration.logDebug("XML file generated under "+subFolder.getAbsolutePath() +"for "+requestHandlerName);
				}
			} catch (TransformerConfigurationException e) {
				
				mCricketConfiguration.logError("Error while transforming the request/response to xml ", e);
				 
			} 
    	}
		
	}    
    /**
     * @param pContext
     * @return
     */
    private File createParentFolder(MessageContext pContext){
    	
    	 
    	SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    	Date todayDate = new Date();
    	String parentFolderName="ESPLOGS_"+ dateFormat.format(todayDate);    	
    	File espLogFolder = new File(mCricketConfiguration.getEspCallLogFileLocation(), parentFolderName);
    	if(!espLogFolder.exists()) {
    		espLogFolder.mkdirs();
    		if(mCricketConfiguration.isLoggingDebug()){
				mCricketConfiguration.logDebug("Created parent folder to save ESP calls request/response " + espLogFolder.getAbsolutePath());
			}	
    	}
    	
    	return espLogFolder;
    	
    }
	/**
	 * @param pContext
	 * @param subFolder
	 * @param requestHandlerName
	 * @throws TransformerConfigurationException 
	 * @throws TransformerFactoryConfigurationError
	 */
	private void generateStringToXML(MessageContext pContext, File subFolder,
			String requestHandlerName) throws TransformerConfigurationException {
		
    	  try {
    		  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
    		  DocumentBuilder builder = factory.newDocumentBuilder();
    		 
	            // Use String reader  
	            Document document = null;
	            if(requestHandlerName != null && requestHandlerName.contains("_request")){
	            	document = builder.parse( new InputSource(new StringReader( formatXml(getStringMessage(pContext) ) ) ) );
	            }else{
	            	document = builder.parse( new InputSource(new StringReader( getStringMessage(pContext) ) ) );
	            }
	            TransformerFactory tranFactory = TransformerFactory.newInstance();  
	            Transformer aTransformer = tranFactory.newTransformer();  
	            Source src = new DOMSource( document ); 
 	            Result dest = new StreamResult( new File( subFolder.getAbsolutePath()+"//"+requestHandlerName+"_"+ date.getTime() +".xml") );  
	            aTransformer.transform( src, dest );  
			} catch (SAXException e) {
				mCricketConfiguration.logError("Error while transforming the request/response to xml in generateStringToXML ", e);
			} catch (IOException e) {
				mCricketConfiguration.logError("Error while transforming the request/response to xml in generateStringToXML ", e);
			} catch (ParserConfigurationException e) {
				mCricketConfiguration.logError("Error while transforming the request/response to xml in generateStringToXML ", e);
			} catch (TransformerException e) {
				mCricketConfiguration.logError("Error while transforming the request/response to xml in generateStringToXML ", e);
			}           
         
	}

	@Override
    public boolean handleResponse(MessageContext context) {
	   	String orderId = (String) context.getProperty(ORDER_ID);
    	String responseInString = getStringMessage(context);    	
    	if(!orderId.equalsIgnoreCase(NO_ORDER) )
    		storeCallLogs(context, "_response");
    	/*if(mCricketConfiguration.isLoggingDebug())
    		mCricketConfiguration.logDebug("RESPONSE: [" + date + "]\n" + responseInString); */ 
    	if(!mCricketConfiguration.isEspXMLLoggingEnabled()){
	    	if(responseInString != null && (responseInString.contains(SOAP_ENV_FAULT) || (responseInString.contains(ERROR_DESCRIPTION)))){
	    		org.apache.axis.MessageContext cont = (org.apache.axis.MessageContext) context;
	        	String requestHandlerName = cont.getOperation().getName();
	    		mCricketConfiguration.logError(CricketESPConstants.WHOOP_KEYWORD + "ESP Exception for "+requestHandlerName+" "+    	
	    										CricketCommonConstants.SESSION_ID +ServletUtil.getCurrentRequest().getSession().getId()+
	    										CricketCommonConstants.ORDER_ID + orderId); 
	    		mCricketConfiguration.logError("Error SOAP request for "+requestHandlerName+" "+    	
												CricketCommonConstants.SESSION_ID +ServletUtil.getCurrentRequest().getSession().getId()+
												CricketCommonConstants.ORDER_ID + orderId);
	    		mCricketConfiguration.logError(soapRequestString);  
	    		mCricketConfiguration.logError("Error SOAP response for "+requestHandlerName+" "+    	
												CricketCommonConstants.SESSION_ID +ServletUtil.getCurrentRequest().getSession().getId()+
												CricketCommonConstants.ORDER_ID + orderId);
	    		String plainResponse = responseInString.replaceAll(REGEX_SPACE_CHARACTER,EMPTY_STRING);
	    		mCricketConfiguration.logError(plainResponse);
	    	  }
    	}
        return super.handleResponse(context);
    }
    
    private String formatXml(String xml) {
        try {
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
        } catch (Exception e) {
            return xml;
        }
    }

}
