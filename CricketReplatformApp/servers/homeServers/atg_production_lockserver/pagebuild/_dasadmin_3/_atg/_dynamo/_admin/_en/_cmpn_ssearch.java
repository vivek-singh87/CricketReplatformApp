package _dasadmin_3._atg._dynamo._admin._en;

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

public class _cmpn_ssearch
extends atg.servlet.jsp.DynamoJspPageServlet implements atg.servlet.pagecompile.AttrCompiledServlet {
  public String getFileCacheAttributeName() {
    return "__002fatg_002fdynamo_002fadmin_002fen_002fcmpn_ssearch_xjhtml";
  }
  
  public static final long SOURCE_MODIFIED_TIME = 1360147184000L;
  public class _Param_0_true extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      ByteFileData __fileData = null;
      {
        __fileData = (ByteFileData)         request.getAttribute(getFileCacheAttributeName())        ;
        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        ByteBufferedBodyContent out = (ByteBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 32-35 */
            __fileData.writeBytes (959, 160, out);
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
  public class _Param_1_default extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      ByteFileData __fileData = null;
      {
        __fileData = (ByteFileData)         request.getAttribute(getFileCacheAttributeName())        ;
        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        ByteBufferedBodyContent out = (ByteBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 36-39 */
            __fileData.writeBytes (1158, 159, out);
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
  _Param_1_default m_Param_1_default = this. new _Param_1_default();
  {
    m_Param_1_default.setParent(this);
  }
  public class _SubServlet_2 extends PageSubServlet {
    {
      this.setParameter("true", m_Param_0_true);
      this.setParameter("default", m_Param_1_default);
    }
  }

  _SubServlet_2 m_SubServlet_2 = this. new _SubServlet_2();
  {
    m_SubServlet_2.setParent(this);
  }
  public class _Param_3_unset extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      ByteFileData __fileData = null;
      {
        __fileData = (ByteFileData)         request.getAttribute(getFileCacheAttributeName())        ;
        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        ByteBufferedBodyContent out = (ByteBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
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
  _Param_3_unset m_Param_3_unset = this. new _Param_3_unset();
  {
    m_Param_3_unset.setParent(this);
  }
  public class _Param_4_empty extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      ByteFileData __fileData = null;
      {
        __fileData = (ByteFileData)         request.getAttribute(getFileCacheAttributeName())        ;
        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        ByteBufferedBodyContent out = (ByteBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 64-64 */
            __fileData.writeBytes (2189, 35, out);
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
  _Param_4_empty m_Param_4_empty = this. new _Param_4_empty();
  {
    m_Param_4_empty.setParent(this);
  }
  public class _Param_5_output extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      ByteFileData __fileData = null;
      {
        __fileData = (ByteFileData)         request.getAttribute(getFileCacheAttributeName())        ;
        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        ByteBufferedBodyContent out = (ByteBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 65-65 */
            __fileData.writeBytes (2266, 25, out);
            out.print(ServletUtil.toString(((((Integer)request.getObjectParameter("count")).intValue() % 2) == 1) ? "even" : "odd"));
            /*** lines: 65-67 */
            __fileData.writeBytes (2380, 13, out);
            request.setParameter("path", request.getObjectParameter("element.absoluteName"), 
null, null);
            request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
            /*** lines: 68-68 */
            __fileData.writeBytes (2451, 16, out);
            out.print(request.encodeURL ("/nucleus/" + ServletUtil.toString(request.getParameter("path")), true, true, false, true));
            /*** lines: 68-68 */
            __fileData.writeBytes (2506, 2, out);
            if (!request.serviceParameter("element.absoluteName", request, response, PageCompileServlet.getEscapeHTMLTagConverter(), null)) {
            /*** lines: 69-68 */
            __fileData.writeBytes (2546, 3, out);
            }
            request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
            /*** lines: 68-69 */
            __fileData.writeBytes (2559, 25, out);
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
  _Param_5_output m_Param_5_output = this. new _Param_5_output();
  {
    m_Param_5_output.setParent(this);
  }
  public class _SubServlet_6 extends PageSubServlet {
    {
      this.setParameter("context", "/");
      this.setParameter("recursive", "true");
      this.setParameter("sortBy", "path");
      this.setParameter("ignoreRequest", "false");
      this.setParameter("ignoreSession", "false");
      this.setParameter("empty", m_Param_4_empty);
      this.setParameter("output", m_Param_5_output);
    }
  }

  _SubServlet_6 m_SubServlet_6 = this. new _SubServlet_6();
  {
    m_SubServlet_6.setParent(this);
  }
  public class _Param_7_default extends PageSubServlet {
    
    //-------------- The service method
    public void service (DynamoHttpServletRequest request,
                         DynamoHttpServletResponse response)
        throws ServletException, IOException
    {
      ByteFileData __fileData = null;
      {
        __fileData = (ByteFileData)         request.getAttribute(getFileCacheAttributeName())        ;
        DynamoJspPageContext pageContext = (DynamoJspPageContext)request.getAttribute(DynamoJspPageContext.REQUEST_PAGE_CONTEXT_ATTR);
        
        ByteBufferedBodyContent out = (ByteBufferedBodyContent)pageContext.getOut();
        
        int _jspTempReturn;
        
        try {
          String _saveBaseDir = null;
          try {
            if ((_saveBaseDir = request.getBaseDirectory()) != null)
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 52-56 */
            __fileData.writeBytes (1682, 92, out);
            try {
              request.pushFrame();
              request.setParameter("showAll", request.getObjectParameter("showAll"));
              request.setParameter("nameExpression", request.getObjectParameter("query"));
              m_SubServlet_6.serviceByName(request, response, "/atg/dynamo/admin/ForEachNucleusComponent", 56);
            }
            finally {
              request.popFrame();
            }
            /*** lines: 70-72 */
            __fileData.writeBytes (2612, 24, out);
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
  _Param_7_default m_Param_7_default = this. new _Param_7_default();
  {
    m_Param_7_default.setParent(this);
  }
  public class _SubServlet_8 extends PageSubServlet {
    {
      this.setParameter("unset", m_Param_3_unset);
      this.setParameter("default", m_Param_7_default);
    }
  }

  _SubServlet_8 m_SubServlet_8 = this. new _SubServlet_8();
  {
    m_SubServlet_8.setParent(this);
  }
  
  public static final String[] INCLUDED_SOURCE_URIS = null;
  public static final long[] INCLUDED_SOURCE_MODIFIED_TIMES = null;
  public static String[] _jspDynGetSourceURIs() {
    return INCLUDED_SOURCE_URIS;
  }
  
  public final static String _JSP_ENCODING =   null  ;
  
  public static String _jspGetEncoding() {
    return _JSP_ENCODING;
  }
  

  //-------------------------------
  {
  
    DropletImports _imports = new DropletImports();
    this. setParameter("_imports", _imports);
    _imports.addImport("/atg/dynamo/admin/ForEachNucleusComponent");
    _imports.addImport("/atg/dynamo/droplet/Switch");
  }
  
  //-------------- The _jspService method
  public void _jspService (DynamoHttpServletRequest request,
                       DynamoHttpServletResponse response)
      throws ServletException, IOException
  {
    ByteFileData __fileData = null;
    try {
      __fileData = (ByteFileData)       request.getAttribute(getFileCacheAttributeName())      ;
      JspFactory _jspFactory = DynamoJspFactory.getDynamoJspFactory();
      DynamoJspPageContext pageContext = (DynamoJspPageContext)_jspFactory.getPageContext(
        this, request, response, 
        null,true, JspWriter.DEFAULT_BUFFER, true);
        ServletConfig config = getServletConfig();
        ServletContext application = config.getServletContext();
        HttpSession session = pageContext.getSession();
        Object page = this;
      
      ByteBufferedBodyContent out = (ByteBufferedBodyContent)pageContext.getOut();
      
      int _jspTempReturn;
      
      try {

        FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
        DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
        /*** lines: 1-1 */
        __fileData.writeBytes (0, 59, out);
        out.print(request.encodeURL ("/atg/dynamo/admin/admin.css", false, true, true, true));
        /*** lines: 1-10 */
        __fileData.writeBytes (86, 132, out);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 13-13 */
        __fileData.writeBytes (328, 64, out);
        out.print(request.encodeURL ("/", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        _form = ((DropletEventServlet)request.getAttribute(DropletConstants.DROPLET_EVENT_ATTRIBUTE)).addForm("/atg/dynamo/admin/en/cmpn-search.jhtml.1");
        if (_form != null) {
         String actionURI = ServletUtil.getRequestURI(request, "cmpn-search.jhtml        ");
        _form.addActionURL(actionURI);
        }
        request.setParameter("_form", _form);
        /*** lines: 13-13 */
        __fileData.writeBytes (393, 256, out);
        out.print(request.encodeURL ("cmpn-search.jhtml", true, true, false, true));
        /*** lines: 13-13 */
        __fileData.writeBytes (666, 83, out);
        {
          String _t = request.getParameter("query");
          if (_t != null)
            out.print(ServletUtil.escapeHtmlString(_t));
        }
        /*** lines: 13-30 */
        __fileData.writeBytes (760, 100, out);
        try {
          request.pushFrame();
          request.setParameter("value", request.getObjectParameter("showAll"));
          m_SubServlet_2.serviceByName(request, response, "/atg/dynamo/droplet/Switch", 30);
        }
        finally {
          request.popFrame();
        }
        /*** lines: 40-47 */
        __fileData.writeBytes (1341, 191, out);
        if (_form != null && _form.needsEvents()){
        out.print("<input type=\"hidden\" name=\"_DARGS\" value=\"/atg/dynamo/admin/en/cmpn-search.jhtml.1\"");
        if (_form != null && _form.isXMLMimeType(request))
        out.print("/>");
        else
        out.print(">");
        out.print("<input type=\"hidden\" name=\"_dynSessConf\" value=\"" + request.getSessionConfirmationNumber() + "\"");        if (_form != null && _form.isXMLMimeType(request))
        out.print("/>");
        else
        out.print(">");
        }
        _form = null;
        /*** lines: 47-49 */
        __fileData.writeBytes (1532, 13, out);
        try {
          request.pushFrame();
          request.setParameter("value", request.getObjectParameter("query"));
          m_SubServlet_8.serviceByName(request, response, "/atg/dynamo/droplet/Switch", 49);
        }
        finally {
          request.popFrame();
        }
        /*** lines: 73-77 */
        __fileData.writeBytes (2660, 18, out);
/* @version $Id: //product/DAS/version/10.2/release/DAS/admin/atg/dynamo/admin/en/cmpn-search.jhtml#1 $$Change: 735822 $*/        /*** lines: 77-77 */
        __fileData.writeBytes (2813, 1, out);
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
