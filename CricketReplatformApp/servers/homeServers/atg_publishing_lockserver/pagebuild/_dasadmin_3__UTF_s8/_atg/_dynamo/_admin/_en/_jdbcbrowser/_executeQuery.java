package _dasadmin_3__UTF_s8._atg._dynamo._admin._en._jdbcbrowser;

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

public class _executeQuery
extends atg.servlet.jsp.DynamoJspPageServlet implements atg.servlet.pagecompile.AttrCompiledServlet {
  public String getFileCacheAttributeName() {
    return "__002fatg_002fdynamo_002fadmin_002fen_002fjdbcbrowser_002fexecuteQuery_xjhtml";
  }
  
  public static final long SOURCE_MODIFIED_TIME = 1360147184000L;
  static final atg.droplet.PropertyName _beanName0 = atg.droplet.PropertyName.createPropertyName("/atg/dynamo/admin/jdbcbrowser/ExecuteQueryDroplet.query");
  static final atg.droplet.PropertyName _beanName1 = atg.droplet.PropertyName.createPropertyName("/atg/dynamo/admin/jdbcbrowser/ExecuteQueryDroplet.longForm");
  static final atg.droplet.PropertyName _beanName2 = atg.droplet.PropertyName.createPropertyName("/atg/dynamo/admin/jdbcbrowser/ExecuteQueryDroplet.operation");
  static final atg.droplet.PropertyName _beanName3 = atg.droplet.PropertyName.createPropertyName("/atg/dynamo/admin/jdbcbrowser/ExecuteQueryDroplet.operation");
  public class _SubServlet_0 extends PageSubServlet {
  }

  _SubServlet_0 m_SubServlet_0 = this. new _SubServlet_0();
  {
    m_SubServlet_0.setParent(this);
  }
  public class _Param_1_displayResultSet extends PageSubServlet {
    
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
              request.setBaseDirectory("/atg/dynamo/admin/en/jdbcbrowser/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 21-23 */
            __fileData.writeChars (590, 28, (CharFileDataSink)out);
            try {
              request.pushFrame();
              m_SubServlet_0.serviceByName(request, response, "/atg/dynamo/admin/jdbcbrowser/ResultSetDroplet", 23);
            }
            finally {
              request.popFrame();
            }
            /*** lines: 24-25 */
            __fileData.writeChars (666, 3, (CharFileDataSink)out);
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
  _Param_1_displayResultSet m_Param_1_displayResultSet = this. new _Param_1_displayResultSet();
  {
    m_Param_1_displayResultSet.setParent(this);
  }
  public class _Param_2_displayUpdateCount extends PageSubServlet {
    
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
              request.setBaseDirectory("/atg/dynamo/admin/en/jdbcbrowser/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 26-27 */
            __fileData.writeChars (715, 21, (CharFileDataSink)out);
            if (!request.serviceParameter("updateCount", request, response, PageCompileServlet.getEscapeHTMLTagConverter(), null)) {
            }
            /*** lines: 27-28 */
            __fileData.writeChars (775, 8, (CharFileDataSink)out);
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
  _Param_2_displayUpdateCount m_Param_2_displayUpdateCount = this. new _Param_2_displayUpdateCount();
  {
    m_Param_2_displayUpdateCount.setParent(this);
  }
  public class _SubServlet_3 extends PageSubServlet {
    {
      this.setParameter("displayResultSet", m_Param_1_displayResultSet);
      this.setParameter("displayUpdateCount", m_Param_2_displayUpdateCount);
    }
  }

  _SubServlet_3 m_SubServlet_3 = this. new _SubServlet_3();
  {
    m_SubServlet_3.setParent(this);
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
    _imports.addImport("/atg/dynamo/admin/jdbcbrowser/ExecuteQueryDroplet");
    _imports.addImport("/atg/dynamo/admin/jdbcbrowser/ResultSetDroplet");
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
        /*** lines: 4-4 */
        __fileData.writeChars (133, 62, (CharFileDataSink)out);
        out.print(request.encodeURL ("/atg/dynamo/admin/admin.css", false, true, true, true));
        /*** lines: 4-10 */
        __fileData.writeChars (222, 10, (CharFileDataSink)out);
        if (!request.serviceParameter("operation", request, response, PageCompileServlet.getEscapeHTMLTagConverter(), null)) {
        }
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 10-10 */
        __fileData.writeChars (269, 160, (CharFileDataSink)out);
        out.print(request.encodeURL ("/", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 10-10 */
        __fileData.writeChars (430, 21, (CharFileDataSink)out);
        out.print(request.encodeURL ("index.jhtml", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        /*** lines: 10-20 */
        __fileData.writeChars (462, 57, (CharFileDataSink)out);
        try {
          request.pushFrame();
          m_SubServlet_3.serviceByName(request, response, "/atg/dynamo/admin/jdbcbrowser/ExecuteQueryDroplet", 20);
        }
        finally {
          request.popFrame();
        }
        _form = ((DropletEventServlet)request.getAttribute(DropletConstants.DROPLET_EVENT_ATTRIBUTE)).addForm("/atg/dynamo/admin/en/jdbcbrowser/executeQuery.jhtml.2");
        if (_form != null) {
         String actionURI = ServletUtil.getRequestURI(request, "executeQuery.jhtml        ");
        _form.addActionURL(actionURI);
        }
        request.setParameter("_form", _form);
        request.addQueryParameter("_DARGS", "/atg/dynamo/admin/en/jdbcbrowser/executeQuery.jhtml.2");
        request.addQueryParameter("_dynSessConf", Long.toString(request.getSessionConfirmationNumber()));        /*** lines: 29-29 */
        __fileData.writeChars (803, 16, (CharFileDataSink)out);
        out.print(request.encodeURL ("executeQuery.jhtml", true, true, false, true, false));
        /*** lines: 29-29 */
        __fileData.writeChars (837, 115, (CharFileDataSink)out);
        {
          String _pn = "/atg/dynamo/admin/jdbcbrowser/ExecuteQueryDroplet.query";
          /*** lines: 29-29 */
          __fileData.writeChars (988, 16, (CharFileDataSink)out);
          out.print(" name=\"");
          out.print(_pn);
          out.print('"');
          /*** lines: 29-29 */
          __fileData.writeChars (1004, 1, (CharFileDataSink)out);
          out.print(DropletDescriptor.getPropertyHtmlStringValue(request, response, _pn, true, null, null));
          /*** lines: 29-29 */
          __fileData.writeChars (1005, 11, (CharFileDataSink)out);
          if (_form != null) _form.addTag(request, response, null, _pn, null, null, DropletConstants.PRIORITY_DEFAULT, null, null, null, false);
        }
        /*** lines: 29-29 */
        __fileData.writeChars (1016, 26, (CharFileDataSink)out);
        {
          String _pn = "/atg/dynamo/admin/jdbcbrowser/ExecuteQueryDroplet.longForm";
          out.print(" value=true");
          if (DropletDescriptor.matchesPropertyValue(request, response, _beanName1, "true", true,null, null))
          if (_form != null && _form.isXMLMimeType(request))
            out.print(" checked=\"checked\"");
          else
            out.print(" checked");
          out.print(" name=\"");
          out.print(_pn);
          out.print('"');
          /*** lines: 29-29 */
          __fileData.writeChars (1081, 1, (CharFileDataSink)out);
          if (_form != null) _form.addTag(request, response, null, _pn, "checkbox", "false", DropletConstants.PRIORITY_DEFAULT, null, null, null, false);
        }
        /*** lines: 29-29 */
        __fileData.writeChars (1082, 69, (CharFileDataSink)out);
        /*** lines: 29-29 */
        __fileData.writeChars (1191, 27, (CharFileDataSink)out);
        {
          String _pn = "/atg/dynamo/admin/jdbcbrowser/ExecuteQueryDroplet.operation";
          out.print(" name=\"");
          out.print(_pn);
          out.print('"');
          /*** lines: 29-29 */
          __fileData.writeChars (1218, 1, (CharFileDataSink)out);
          if (_form != null) _form.addTag(request, response, null, _pn, "submit", null, DropletConstants.SUBMIT_PRIORITY_DEFAULT, null, null, null, false);
        }
        /*** lines: 29-29 */
        __fileData.writeChars (1219, 20, (CharFileDataSink)out);
        /*** lines: 29-29 */
        __fileData.writeChars (1279, 29, (CharFileDataSink)out);
        {
          String _pn = "/atg/dynamo/admin/jdbcbrowser/ExecuteQueryDroplet.operation";
          out.print(" name=\"");
          out.print(_pn);
          out.print('"');
          /*** lines: 29-29 */
          __fileData.writeChars (1308, 1, (CharFileDataSink)out);
          if (_form != null) _form.addTag(request, response, null, _pn, "submit", null, DropletConstants.SUBMIT_PRIORITY_DEFAULT, null, null, null, false);
        }
        /*** lines: 29-50 */
        __fileData.writeChars (1309, 27, (CharFileDataSink)out);
/* @version $Id: //product/DAS/version/10.2/release/DAS/admin/atg/dynamo/admin/en/jdbcbrowser/executeQuery.jhtml#2 $$Change: 742374 $ */        /*** lines: 50-51 */
        __fileData.writeChars (1485, 2, (CharFileDataSink)out);
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
