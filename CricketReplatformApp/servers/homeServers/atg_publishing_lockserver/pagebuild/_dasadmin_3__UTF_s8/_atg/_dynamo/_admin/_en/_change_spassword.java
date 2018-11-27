package _dasadmin_3__UTF_s8._atg._dynamo._admin._en;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import atg.nucleus.*;
import atg.naming.*;
import atg.service.filecache.*;
import atg.servlet.*;
import atg.droplet.*;
import atg.servlet.pagecompile.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import atg.servlet.jsp.*;
import atg.security.*;
import atg.repository.*;

public class _change_spassword
extends atg.servlet.jsp.DynamoJspPageServlet implements atg.servlet.pagecompile.AttrCompiledServlet {
  public String getFileCacheAttributeName() {
    return "__002fatg_002fdynamo_002fadmin_002fen_002fchange_spassword_xjhtml";
  }
  
  public static final long SOURCE_MODIFIED_TIME = 1360147184000L;
  public class _Param_0_true extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      CharFileData __fileData = null;
      {
        __fileData = (CharFileData)
        request.getAttribute(getFileCacheAttributeName())        ;        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        CharBufferedBodyContent out = (CharBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 37-42 */
            __fileData.writeChars (1043, 111, (CharFileDataSink)out);
          }
          finally {
            if (_saveBaseDir != null) request.setBaseDirectory(_saveBaseDir);
          }
        } catch (Exception e) {
          // out.clear();
          out.clearBuffer();
          pageContext.handlePageException(e);
        }
      }
    }
  }
  _Param_0_true m_Param_0_true = this. new _Param_0_true();
  {
    m_Param_0_true.setParent(this);
  }
  public class _SubServlet_1 extends PageSubServlet {
    {
      this.setParameter("true", m_Param_0_true);
    }
  }

  _SubServlet_1 m_SubServlet_1 = this. new _SubServlet_1();
  {
    m_SubServlet_1.setParent(this);
  }
  public class _Param_2_true extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      CharFileData __fileData = null;
      {
        __fileData = (CharFileData)
        request.getAttribute(getFileCacheAttributeName())        ;        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        CharBufferedBodyContent out = (CharBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 49-52 */
            __fileData.writeChars (1342, 84, (CharFileDataSink)out);
            if (!request.serviceParameter("username", request, response, PageCompileServlet.getEscapeHTMLTagConverter(), null)) {
            }
            /*** lines: 52-55 */
            __fileData.writeChars (1453, 34, (CharFileDataSink)out);
          }
          finally {
            if (_saveBaseDir != null) request.setBaseDirectory(_saveBaseDir);
          }
        } catch (Exception e) {
          // out.clear();
          out.clearBuffer();
          pageContext.handlePageException(e);
        }
      }
    }
  }
  _Param_2_true m_Param_2_true = this. new _Param_2_true();
  {
    m_Param_2_true.setParent(this);
  }
  public class _SubServlet_3 extends PageSubServlet {
    {
      this.setParameter("true", m_Param_2_true);
    }
  }

  _SubServlet_3 m_SubServlet_3 = this. new _SubServlet_3();
  {
    m_SubServlet_3.setParent(this);
  }
  public class _Param_4_true extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      CharFileData __fileData = null;
      {
        __fileData = (CharFileData)
        request.getAttribute(getFileCacheAttributeName())        ;        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        CharBufferedBodyContent out = (CharBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 62-67 */
            __fileData.writeChars (1688, 95, (CharFileDataSink)out);
          }
          finally {
            if (_saveBaseDir != null) request.setBaseDirectory(_saveBaseDir);
          }
        } catch (Exception e) {
          // out.clear();
          out.clearBuffer();
          pageContext.handlePageException(e);
        }
      }
    }
  }
  _Param_4_true m_Param_4_true = this. new _Param_4_true();
  {
    m_Param_4_true.setParent(this);
  }
  public class _SubServlet_5 extends PageSubServlet {
    {
      this.setParameter("true", m_Param_4_true);
    }
  }

  _SubServlet_5 m_SubServlet_5 = this. new _SubServlet_5();
  {
    m_SubServlet_5.setParent(this);
  }
  public class _Param_6_true extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      CharFileData __fileData = null;
      {
        __fileData = (CharFileData)
        request.getAttribute(getFileCacheAttributeName())        ;        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        CharBufferedBodyContent out = (CharBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 73-78 */
            __fileData.writeChars (1963, 99, (CharFileDataSink)out);
          }
          finally {
            if (_saveBaseDir != null) request.setBaseDirectory(_saveBaseDir);
          }
        } catch (Exception e) {
          // out.clear();
          out.clearBuffer();
          pageContext.handlePageException(e);
        }
      }
    }
  }
  _Param_6_true m_Param_6_true = this. new _Param_6_true();
  {
    m_Param_6_true.setParent(this);
  }
  public class _SubServlet_7 extends PageSubServlet {
    {
      this.setParameter("true", m_Param_6_true);
    }
  }

  _SubServlet_7 m_SubServlet_7 = this. new _SubServlet_7();
  {
    m_SubServlet_7.setParent(this);
  }
  public class _Param_8_true extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      CharFileData __fileData = null;
      {
        __fileData = (CharFileData)
        request.getAttribute(getFileCacheAttributeName())        ;        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        CharBufferedBodyContent out = (CharBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 32-33 */
            __fileData.writeChars (849, 10, (CharFileDataSink)out);
/* Check to see if a username was specified */
                     /*** lines: 34-35 */
            __fileData.writeChars (928, 10, (CharFileDataSink)out);
            try {
              request.pushFrame();
              request.setParameter("value", request.getObjectParameter("username"));
              m_SubServlet_1.serviceByName(request, response, "/atg/dynamo/droplet/IsEmpty", 35);
            }
            finally {
              request.popFrame();
            }
            /*** lines: 43-45 */
            __fileData.writeChars (1174, 2, (CharFileDataSink)out);
/* Check to see if the current password was specified */            /*** lines: 45-47 */
            __fileData.writeChars (1245, 2, (CharFileDataSink)out);
            try {
              request.pushFrame();
              request.setParameter("value", request.getObjectParameter("current"));
              m_SubServlet_3.serviceByName(request, response, "/atg/dynamo/droplet/IsEmpty", 47);
            }
            finally {
              request.popFrame();
            }
            /*** lines: 56-59 */
            __fileData.writeChars (1507, 19, (CharFileDataSink)out);
/* Check to see if a new password was specified */             /*** lines: 59-60 */
            __fileData.writeChars (1590, 1, (CharFileDataSink)out);
            try {
              request.pushFrame();
              request.setParameter("value", request.getObjectParameter("newpass"));
              m_SubServlet_5.serviceByName(request, response, "/atg/dynamo/droplet/IsEmpty", 60);
            }
            finally {
              request.popFrame();
            }
            /*** lines: 68-70 */
            __fileData.writeChars (1803, 2, (CharFileDataSink)out);
/* Check to see if the password was confirmed */             /*** lines: 70-71 */
            __fileData.writeChars (1867, 1, (CharFileDataSink)out);
            try {
              request.pushFrame();
              request.setParameter("value", request.getObjectParameter("confirm"));
              m_SubServlet_7.serviceByName(request, response, "/atg/dynamo/droplet/IsEmpty", 71);
            }
            finally {
              request.popFrame();
            }
            /*** lines: 79-81 */
            __fileData.writeChars (2082, 2, (CharFileDataSink)out);

String username = request.getParameter("username"); 
String current = request.getParameter("current");
String newpass = request.getParameter("newpass");
String confirm = request.getParameter("confirm");

// make sure that all parameters were passed in
if ((username == null) || (username.equals("")) || 
     (current == null) || (current.equals("")) ||
     (newpass == null) || (newpass.equals("")) ||
     (confirm == null) || (confirm.equals(""))) {
 // something is amiss (the error checking above will inform the user)
 // so we won't change the password
} 
else { 
  // verify thatthe user exists
  RepositoryAccountManager ram = (RepositoryAccountManager) request.resolveName("/atg/dynamo/security/AdminAccountManager");
  Account acct = ram.getAccount(username);
  PasswordHasher ph= ram.getPasswordHasher();      
  if (acct == null)
    out.println("<LI><FONT color=\"red\">User " + username + " was not found.");
  else {
    String passwordForLogin = ph.hashPasswordForLogin(current);
    String hashedPassword = acct.getAttribute(ram.getPasswordProperty());
    Object hashKey = ph.getPasswordHashKey();
    boolean validPassword = false;
    if(ph instanceof PasswordHasher2) {
      passwordForLogin = ((PasswordHasher2) ph).hashPasswordForLogin(username, current);
      validPassword = ((PasswordHasher2) ph).checkPassword(username, passwordForLogin, hashedPassword, hashKey);
    }
    else {
      validPassword = ph.checkPassword(passwordForLogin, hashedPassword, hashKey);
    }
    // we need to check to make sure the current password provided is correct
    if (!validPassword){
      // the password provided does not match
      out.println("<LI><FONT color=\"red\">Current password provided does not match the user's password. Please try again.");
    } 
    else {
	  // check to make sure that the new password does not equal the current password. 
	  if (newpass.equals(current))
        out.println("<LI><FONT color=\"red\">New password equals current password. Please try again.");
      else {
	      // check to make sure that the confirm password matches the new password

    	  if (!newpass.equals(confirm))
        	out.println("<LI><FONT color=\"red\">New password does not match confirm password. Please try again.");
      	  else {
	      	// run the password through the strong password checker
    	  	PasswordRuleChecker pwdChecker = (PasswordRuleChecker) request.resolveName("/atg/dynamo/security/passwordchecker/AdminPasswordRuleChecker");
	    	if (pwdChecker.isEnabled() && !pwdChecker.checkRules(newpass, null))  {
		  out.println("<LI><FONT color=\"red\">" + pwdChecker.getLastRuleCheckedDescription() + ". Please try again.");
		  
		} else {	      
	    	  // change the password since everything is acceptable
	    	  String password = null;
                  if(ph instanceof PasswordHasher2) {
                    password = ((PasswordHasher2) ph).encryptPassword(username, newpass);
                  }
                  else {
                    password = ph.encryptPassword(newpass);
                  }
        	  acct.setAttribute(ram.getPasswordProperty(), password);
		
		  // update the timestamp for the last password update to now
	          MutableRepositoryItem ri = (MutableRepositoryItem)ram.getAccountItem(username, ram.getLoginDescriptorName());
		  Calendar cal = Calendar.getInstance();
		  ri.setPropertyValue(ram.getLastPasswordUpdatePropertyName(), cal.getTime() );
		  
		  // add the current password to the previousl password array		
	    	  PreviousNPasswordManager prevPwdMgr = (PreviousNPasswordManager) request.resolveName("/atg/dynamo/security/passwordchecker/AdminPreviousNPasswordManager");
                  String hashedCurrent = null;
                  if(ph instanceof PasswordHasher2) {
                    hashedCurrent = ((PasswordHasher2) ph).encryptPassword(username, current);
                  } 
                  else {
                    hashedCurrent = ph.encryptPassword(current);
                  }
		  prevPwdMgr.updatePreviousPasswordsProperty(ri, hashedCurrent); 

		  // flag that this session has a non-expired password
	    	  ExpiredPasswordAdminServlet prevPwdAdminServlet = (ExpiredPasswordAdminServlet) request.resolveName("/atg/dynamo/servlet/adminpipeline/ExpiredPasswordAdminServlet");
		  request.getSession().setAttribute(prevPwdAdminServlet.getPasswordExpiredSessionParam(), new Boolean(false));

		  out.println("<LI><FONT color=\"green\">Password for user " + username + " was successfully updated");
      
    		}
    	  }
       }
    }
  }               
}
            /*** lines: 173-175 */
            __fileData.writeChars (6686, 2, (CharFileDataSink)out);
          }
          finally {
            if (_saveBaseDir != null) request.setBaseDirectory(_saveBaseDir);
          }
        } catch (Exception e) {
          // out.clear();
          out.clearBuffer();
          pageContext.handlePageException(e);
        }
      }
    }
  }
  _Param_8_true m_Param_8_true = this. new _Param_8_true();
  {
    m_Param_8_true.setParent(this);
  }
  public class _Param_9_default extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      CharFileData __fileData = null;
      {
        __fileData = (CharFileData)
        request.getAttribute(getFileCacheAttributeName())        ;        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        CharBufferedBodyContent out = (CharBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 176-177 */
            __fileData.writeChars (6722, 1, (CharFileDataSink)out);
          }
          finally {
            if (_saveBaseDir != null) request.setBaseDirectory(_saveBaseDir);
          }
        } catch (Exception e) {
          // out.clear();
          out.clearBuffer();
          pageContext.handlePageException(e);
        }
      }
    }
  }
  _Param_9_default m_Param_9_default = this. new _Param_9_default();
  {
    m_Param_9_default.setParent(this);
  }
  public class _SubServlet_10 extends PageSubServlet {
    {
      this.setParameter("true", m_Param_8_true);
      this.setParameter("default", m_Param_9_default);
    }
  }

  _SubServlet_10 m_SubServlet_10 = this. new _SubServlet_10();
  {
    m_SubServlet_10.setParent(this);
  }
  
  public static final String[] INCLUDED_SOURCE_URIS = null;
  public static final long[] INCLUDED_SOURCE_MODIFIED_TIMES = null;
  public static String[] _jspDynGetSourceURIs() {
    return INCLUDED_SOURCE_URIS;
  }
  
  public final static String _JSP_ENCODING =   "UTF-8"  ;
  
  public static String _jspGetEncoding() {
    return _JSP_ENCODING;
  }
  

  //-------------------------------
  {
  
    DropletImports _imports = new DropletImports();
    this. setParameter("_imports", _imports);
    _imports.addImport("/atg/dynamo/droplet/Switch");
    _imports.addImport("/atg/dynamo/droplet/IsEmpty");
  }
  
  //-------------- The _jspService method
  public void _jspService (DynamoHttpServletRequest request,
                       DynamoHttpServletResponse response)
      throws ServletException, IOException
  {
    CharFileData __fileData = null;
    try {
      __fileData = (CharFileData)
      request.getAttribute(getFileCacheAttributeName())      ;      JspFactory _jspFactory = DynamoJspFactory.getDynamoJspFactory();
      DynamoJspPageContext pageContext = (DynamoJspPageContext)_jspFactory.getPageContext(
        this, request, response, 
        null,true, JspWriter.DEFAULT_BUFFER, true);
        ServletConfig config = getServletConfig();
        ServletContext application = config.getServletContext();
        HttpSession session = pageContext.getSession();
        Object page = this;
      
      CharBufferedBodyContent out = (CharBufferedBodyContent)pageContext.getOut();
      
      int _jspTempReturn;
      
      try {

        FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
        DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
        /*** lines: 2-2 */
        __fileData.writeChars (46, 2, (CharFileDataSink)out);
        /*** lines: 3-3 */
        __fileData.writeChars (95, 62, (CharFileDataSink)out);
        out.print(request.encodeURL ("/atg/dynamo/admin/admin.css", false, true, true, true));
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 3-3 */
        __fileData.writeChars (184, 178, (CharFileDataSink)out);
        out.print(request.encodeURL ("/", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        /*** lines: 3-28 */
        __fileData.writeChars (363, 331, (CharFileDataSink)out);
        /*** lines: 28-30 */
        __fileData.writeChars (754, 2, (CharFileDataSink)out);
        try {
          request.pushFrame();
          request.setParameter("value", request.getObjectParameter("flag"));
          m_SubServlet_10.serviceByName(request, response, "/atg/dynamo/droplet/Switch", 30);
        }
        finally {
          request.popFrame();
        }
        _form = ((DropletEventServlet)request.getAttribute(DropletConstants.DROPLET_EVENT_ATTRIBUTE)).addForm("/atg/dynamo/admin/en/change-password.jhtml.1");
        if (_form != null) {
         String actionURI= ServletUtil.getRequestURI(request, ServletUtil.toString(request.getRequestURIWithQueryString()));
        _form.addActionURL(actionURI);
        }
        request.setParameter("_form", _form);
        if (_form != null && _form.needsEvents()) {
          request.addQueryParameter("_DARGS", "/atg/dynamo/admin/en/change-password.jhtml.1");
          request.addQueryParameter("_dynSessConf", Long.toString(request.getSessionConfirmationNumber()));        }
        /*** lines: 178-178 */
        __fileData.writeChars (6791, 44, (CharFileDataSink)out);
        out.print(request.encodeURL (ServletUtil.toString(request.getRequestURIWithQueryString()), true, true, false, true, false));
        /*** lines: 178-178 */
        __fileData.writeChars (6875, 90, (CharFileDataSink)out);
        out.print(request.encodeURL ("/atg/dynamo/admin/images/clear.gif", false, true, true, true));
        /*** lines: 178-178 */
        __fileData.writeChars (6999, 117, (CharFileDataSink)out);
        {
          String _t = request.getParameter("username");
          if (_t != null)
            out.print(ServletUtil.escapeHtmlString(_t));
        }
        /*** lines: 178-178 */
        __fileData.writeChars (7130, 94, (CharFileDataSink)out);
        {
          String _t = request.getParameter("current");
          if (_t != null)
            out.print(ServletUtil.escapeHtmlString(_t));
        }
        /*** lines: 178-178 */
        __fileData.writeChars (7237, 90, (CharFileDataSink)out);
        {
          String _t = request.getParameter("newpass");
          if (_t != null)
            out.print(ServletUtil.escapeHtmlString(_t));
        }
        /*** lines: 178-178 */
        __fileData.writeChars (7340, 94, (CharFileDataSink)out);
        {
          String _t = request.getParameter("confirm");
          if (_t != null)
            out.print(ServletUtil.escapeHtmlString(_t));
        }
        /*** lines: 178-197 */
        __fileData.writeChars (7447, 124, (CharFileDataSink)out);
/* @version $Id: //product/DAS/version/10.2/release/DAS/admin/atg/dynamo/admin/en/change-password.jhtml#1 $$Change: 735822 $*/        /*** lines: 197-197 */
        __fileData.writeChars (7710, 1, (CharFileDataSink)out);
      } catch (Exception e) {
        if (!(e instanceof EndOfPageException)) {
          // out.clear();
          out.clearBuffer();
          pageContext.handlePageException(e);
        }
      }
      finally {
        pageContext.cleanupPoppedBodyContent();
        out.close();
        _jspFactory.releasePageContext(pageContext);
      }
    }
    finally {
      if (__fileData != null) __fileData.close();
    }
  }
}
