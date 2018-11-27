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

public class _index
extends atg.servlet.jsp.DynamoJspPageServlet implements atg.servlet.pagecompile.AttrCompiledServlet {
  public String getFileCacheAttributeName() {
    return "__002fatg_002fdynamo_002fadmin_002fen_002fjdbcbrowser_002findex_xjhtml";
  }
  
  public static final long SOURCE_MODIFIED_TIME = 1360147184000L;
  public class _Param_0_false extends PageSubServlet {
    
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
            /*** lines: 32-38 */
            __fileData.writeChars (998, 137, (CharFileDataSink)out);
            {
              Object _t = DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/jdbcbrowser/ConnectionPoolPointer.driver", true, PageCompileServlet.getEscapeHTMLTagConverter(), null);
              if (_t == null) {
              }
                out.print(ServletUtil.toString(_t));
            }
            /*** lines: 38-40 */
            __fileData.writeChars (1194, 40, (CharFileDataSink)out);
            {
              Object _t = DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/jdbcbrowser/ConnectionPoolPointer.URL", true, PageCompileServlet.getEscapeHTMLTagConverter(), null);
              if (_t == null) {
              }
                out.print(ServletUtil.toString(_t));
            }
            /*** lines: 40-42 */
            __fileData.writeChars (1290, 47, (CharFileDataSink)out);
            {
              Object _t = DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/jdbcbrowser/ConnectionPoolPointer.autoCommit", true, PageCompileServlet.getEscapeHTMLTagConverter(), null);
              if (_t == null) {
              }
                out.print(ServletUtil.toString(_t));
            }
            /*** lines: 42-45 */
            __fileData.writeChars (1400, 36, (CharFileDataSink)out);
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
  _Param_0_false m_Param_0_false = this. new _Param_0_false();
  {
    m_Param_0_false.setParent(this);
  }
  public class _Param_1_true extends PageSubServlet {
    
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
            /*** lines: 55-57 */
            __fileData.writeChars (1848, 41, (CharFileDataSink)out);
            {
              Object _t = DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/jdbcbrowser/ConnectionPoolPointer.driver", true, PageCompileServlet.getEscapeHTMLTagConverter(), null);
              if (_t == null) {
              }
                out.print(ServletUtil.toString(_t));
            }
            /*** lines: 57-59 */
            __fileData.writeChars (1948, 48, (CharFileDataSink)out);
            {
              Object _t = DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/jdbcbrowser/ConnectionPoolPointer.URL", true, PageCompileServlet.getEscapeHTMLTagConverter(), null);
              if (_t == null) {
              }
                out.print(ServletUtil.toString(_t));
            }
            /*** lines: 59-60 */
            __fileData.writeChars (2052, 17, (CharFileDataSink)out);
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
  _Param_1_true m_Param_1_true = this. new _Param_1_true();
  {
    m_Param_1_true.setParent(this);
  }
  public class _SubServlet_2 extends PageSubServlet {
    {
      this.setParameter("true", m_Param_1_true);
    }
  }

  _SubServlet_2 m_SubServlet_2 = this. new _SubServlet_2();
  {
    m_SubServlet_2.setParent(this);
  }
  public class _Param_3_true extends PageSubServlet {
    
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
            /*** lines: 46-52 */
            __fileData.writeChars (1468, 145, (CharFileDataSink)out);
            {
              Object _t = DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/jdbcbrowser/ConnectionPoolPointer.XADataSource", true, PageCompileServlet.getEscapeHTMLTagConverter(), null);
              if (_t == null) {
              }
                out.print(ServletUtil.toString(_t));
            }
            /*** lines: 52-53 */
            __fileData.writeChars (1678, 15, (CharFileDataSink)out);
            try {
              request.pushFrame();
              request.setParameter("value", DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/jdbcbrowser/ConnectionPoolPointer.isFakeXADataSource", true, null, null));
              m_SubServlet_2.serviceByName(request, response, "/atg/dynamo/droplet/Switch", 53);
            }
            finally {
              request.popFrame();
            }
            /*** lines: 61-64 */
            __fileData.writeChars (2093, 26, (CharFileDataSink)out);
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
  _Param_3_true m_Param_3_true = this. new _Param_3_true();
  {
    m_Param_3_true.setParent(this);
  }
  public class _SubServlet_4 extends PageSubServlet {
    {
      this.setParameter("false", m_Param_0_false);
      this.setParameter("true", m_Param_3_true);
    }
  }

  _SubServlet_4 m_SubServlet_4 = this. new _SubServlet_4();
  {
    m_SubServlet_4.setParent(this);
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
    _imports.addImport("/atg/dynamo/admin/jdbcbrowser/ConnectionPoolPointer");
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
        /*** lines: 3-3 */
        __fileData.writeChars (85, 61, (CharFileDataSink)out);
        out.print(request.encodeURL ("/atg/dynamo/admin/admin.css", false, true, true, true));
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 3-3 */
        __fileData.writeChars (173, 171, (CharFileDataSink)out);
        out.print(request.encodeURL ("/", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        /*** lines: 3-19 */
        __fileData.writeChars (345, 135, (CharFileDataSink)out);
        {
          Object _t = DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/jdbcbrowser/ConnectionPoolPointer.connectionPoolName", true, PageCompileServlet.getEscapeHTMLTagConverter(), null);
          if (_t == null) {
          }
            out.print(ServletUtil.toString(_t));
        }
        /*** lines: 19-30 */
        __fileData.writeChars (551, 305, (CharFileDataSink)out);
        try {
          request.pushFrame();
          request.setParameter("value", DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/jdbcbrowser/ConnectionPoolPointer.isDataSource", true, null, null));
          m_SubServlet_4.serviceByName(request, response, "/atg/dynamo/droplet/Switch", 30);
        }
        finally {
          request.popFrame();
        }
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2139, 50, (CharFileDataSink)out);
        out.print(request.encodeURL ("createTable.jhtml", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2206, 32, (CharFileDataSink)out);
        out.print(request.encodeURL ("dropTable.jhtml", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2253, 30, (CharFileDataSink)out);
        out.print(request.encodeURL ("executeQuery.jhtml", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2301, 33, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=tables", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2374, 31, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=columns", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2446, 38, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=typeInfo", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2526, 34, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaData.jhtml", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2578, 79, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=procedures", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2701, 30, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=procedureColumns", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2781, 36, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=schemas", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2858, 27, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=catalogs", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2927, 28, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=tableTypes", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (2999, 30, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=columnPrivileges", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (3079, 36, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=tablePrivileges", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (3164, 35, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=bestRowIdentifier", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (3250, 37, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=versionColumns", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (3335, 34, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=primaryKeys", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (3414, 31, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=importedKeys", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (3491, 32, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=exportedKeys", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (3569, 32, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=crossReference", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 65-65 */
        __fileData.writeChars (3649, 34, (CharFileDataSink)out);
        out.print(request.encodeURL ("viewMetaDataTable.jhtml?operation=indexInfo", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        /*** lines: 65-103 */
        __fileData.writeChars (3726, 47, (CharFileDataSink)out);
/* @version $Id: //product/DAS/version/10.2/release/DAS/admin/atg/dynamo/admin/en/jdbcbrowser/index.jhtml#2 $$Change: 742374 $ */        /*** lines: 103-104 */
        __fileData.writeChars (3915, 2, (CharFileDataSink)out);
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
