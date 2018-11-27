// no copyright notice
package webservices;


public class UpdateWebProductSEIImpl
  extends atg.webservice.ManagedComponentProperties
  implements javax.xml.rpc.server.ServiceLifecycle,
             UpdateWebProductSEI
{
  public static final String sServiceName = "updateWebProduct";
  
  public static final String sNucleusPath = "/com/cricket/common/ws/CricketPublishingWS";

  public static final String sFunctionalName = "null"; 
  
  public static boolean sRegistered = false;
  
  public static boolean sEnabled = true; 
  
  public java.lang.String updateWebProduct(java.lang.String inventoryFeedXML) 
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
                    "updateWebProduct",
                    getParameterNameValueMap(new String[] {"inventoryFeedXML"},
                                             new Object[] { inventoryFeedXML }) )) 
    {
      if (isLoggingDebug())
        logDebug(sServiceName + " error: Access denied using " + sFunctionalName);
      throw new atg.security.SecurityException("Access denied.");
    }
     
    try {
      // look up the Nucleus service and try to call it
     javax.naming.InitialContext ic = new javax.naming.InitialContext(); 
     com.cricket.common.ws.CricketPublishingWS service = (com.cricket.common.ws.CricketPublishingWS) ic.lookup("dynamo:/com/cricket/common/ws/CricketPublishingWS"); 
     if (isLoggingDebug())
       logDebug(sServiceName + " debug: executing com.cricket.common.ws.CricketPublishingWS.updateWebProduct");
     return service.updateWebProduct(inventoryFeedXML);
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
    return "updateWebProduct";
  }
}   
