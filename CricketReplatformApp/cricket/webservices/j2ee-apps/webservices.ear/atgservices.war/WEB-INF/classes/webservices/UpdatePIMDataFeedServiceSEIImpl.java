// no copyright notice
package webservices;


public class UpdatePIMDataFeedServiceSEIImpl
  extends atg.webservice.ManagedComponentProperties
  implements javax.xml.rpc.server.ServiceLifecycle,
             UpdatePIMDataFeedServiceSEI
{
  public static final String sServiceName = "updatePIMDataFeedService";
  
  public static final String sNucleusPath = "/com/cricket/integration/common/ws/CricketPublishingWS";

  public static final String sFunctionalName = "null"; 
  
  public static boolean sRegistered = false;
  
  public static boolean sEnabled = true; 
  
  public java.lang.String updatePIMDataFeedService(java.lang.String devicesDataXML) 
    throws java.rmi.RemoteException, atg.security.SecurityException
  {
    if (! sRegistered) { 
      sEnabled = register(null);
      sRegistered = true;
    }
     
    if (! sEnabled) {
      if (isLoggingDebug())
        logDebug(sServiceName + " error: disabled");
      throw new java.rmi.RemoteException("disabled");
    }

    if ((!"null".equals(sFunctionalName)) &&
         !getNucleusSecurityManager().hasAccess(
                    sFunctionalName, 
                    sNucleusPath, 
                    "updatePIMDataFeedService",
                    getParameterNameValueMap(new String[] {"devicesDataXML"},
                                             new Object[] { devicesDataXML }) )) 
    {
      if (isLoggingDebug())
        logDebug(sServiceName + " error: Access denied using " + sFunctionalName);
      throw new atg.security.SecurityException("Access denied.");
    }
     
    try {
      // look up the Nucleus service and try to call it
     javax.naming.InitialContext ic = new javax.naming.InitialContext(); 
     com.cricket.integration.common.ws.CricketPublishingWS service = (com.cricket.integration.common.ws.CricketPublishingWS) ic.lookup("dynamo:/com/cricket/integration/common/ws/CricketPublishingWS"); 
     if (isLoggingDebug())
       logDebug(sServiceName + " debug: executing com.cricket.integration.common.ws.CricketPublishingWS.updatePIMDataFeedService");
     return service.updatePIMDataFeedService(devicesDataXML);
    }
    catch (javax.naming.NamingException ne) {
      if (isLoggingError())
        logError(sServiceName + " error: " + ne, ne);
      throw new java.rmi.RemoteException(ne.getMessage(), ne);
    }
    
  }
  
  public void destroy() {
  }
  
  public void init(Object pObject)
    throws javax.xml.rpc.ServiceException
  {
    if (! sRegistered) {
      sEnabled = register(pObject);
      sRegistered = true;
    }
  }

   public String getComponentName() {
    return "updatePIMDataFeedService";
  }
}   
