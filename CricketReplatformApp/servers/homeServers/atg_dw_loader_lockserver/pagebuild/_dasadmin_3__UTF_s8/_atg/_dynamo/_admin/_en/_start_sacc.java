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
import atg.nucleus.DynamoEnv;
import atg.applauncher.AppUtils;

public class _start_sacc
extends atg.servlet.jsp.DynamoJspPageServlet implements atg.servlet.pagecompile.AttrCompiledServlet {
  public String getFileCacheAttributeName() {
    return "__002fatg_002fdynamo_002fadmin_002fen_002fstart_sacc_xjhtml";
  }
  
  public static final long SOURCE_MODIFIED_TIME = 1360147184000L;
  java.util.Properties _cvtProps0 = new java.util.Properties();
  {
    _cvtProps0.put("valueishtml", "");
  }
  atg.droplet.TagConverter _cvt0 = atg.droplet.TagConverterManager.getTagConverterByName("ValueIsHTML");
  static final atg.droplet.PropertyName _beanName1 = atg.droplet.PropertyName.createPropertyName("/atg/devtools/Admin.launchHub");
  static final atg.droplet.PropertyName _beanName2 = atg.droplet.PropertyName.createPropertyName("/atg/dynamo/admin/LDAPCacheLoader.reload");
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
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            _form = ((DropletEventServlet)request.getAttribute(DropletConstants.DROPLET_EVENT_ATTRIBUTE)).addForm("/atg/dynamo/admin/en/start-acc.jhtml.1");
            if (_form != null) {
             String actionURI = ServletUtil.getRequestURI(request, "start-acc.jhtml            ");
            _form.addActionURL(actionURI);
            }
            request.setParameter("_form", _form);
            request.addQueryParameter("_DARGS", "/atg/dynamo/admin/en/start-acc.jhtml.1");
            request.addQueryParameter("_dynSessConf", Long.toString(request.getSessionConfirmationNumber()));            /*** lines: 102-102 */
            __fileData.writeChars (2958, 20, (CharFileDataSink)out);
            out.print(request.encodeURL ("start-acc.jhtml", true, true, false, true, false));
            /*** lines: 102-102 */
            __fileData.writeChars (2993, 114, (CharFileDataSink)out);
            {
              String _pn = "/atg/devtools/Admin.launchHub";
              /*** lines: 102-102 */
              __fileData.writeChars (3133, 1, (CharFileDataSink)out);
              if (_form != null) _form.addTag(request, response, "Start ACC in Server VM", _pn, "SUBMIT", null, DropletConstants.SUBMIT_PRIORITY_DEFAULT, null, null, null, false);
            }
            /*** lines: 102-109 */
            __fileData.writeChars (3134, 16, (CharFileDataSink)out);
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
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            /*** lines: 110-112 */
            __fileData.writeChars (3182, 44, (CharFileDataSink)out);
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
      this.setParameter("false", m_Param_0_false);
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
              request.setBaseDirectory("/atg/dynamo/admin/en/");
            FormTag _form = (FormTag) request.getObjectParameter(ServletUtil.FORM_NAME);
            DropletImports _imports = (DropletImports) request.getObjectParameter(ServletUtil.IMPORTS_NAME);
            _form = ((DropletEventServlet)request.getAttribute(DropletConstants.DROPLET_EVENT_ATTRIBUTE)).addForm("/atg/dynamo/admin/en/start-acc.jhtml.2");
            if (_form != null) {
             String actionURI = ServletUtil.getRequestURI(request, "start-acc.jhtml            ");
            _form.addActionURL(actionURI);
            }
            request.setParameter("_form", _form);
            if (_form != null && _form.needsEvents()) {
              request.addQueryParameter("_DARGS", "/atg/dynamo/admin/en/start-acc.jhtml.2");
              request.addQueryParameter("_dynSessConf", Long.toString(request.getSessionConfirmationNumber()));            }
            /*** lines: 127-127 */
            __fileData.writeChars (3642, 19, (CharFileDataSink)out);
            out.print(request.encodeURL ("start-acc.jhtml", true, true, false, true, false));
            /*** lines: 127-132 */
            __fileData.writeChars (3676, 181, (CharFileDataSink)out);
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
  public class _Param_4_false extends PageSubServlet {
    
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
            /*** lines: 133-136 */
            __fileData.writeChars (3890, 14, (CharFileDataSink)out);


      // Get the path of the Java VM.
      String javaHome = DynamoEnv.getProperty("java.home");
      boolean isWindows = DynamoEnv.getProperty("os.name").startsWith("Windows");
      String javaProgName = (isWindows ? "javaw.exe" : "java");
      String javaVM = javaHome + File.separator + "bin" + File.separator + javaProgName;

      // Get the Dynamo root and home directories.
      String dynamoRootPath = DynamoEnv.getProperty("atg.dynamo.root");
      File dynamoRoot = new File(dynamoRootPath).getCanonicalFile();
      String dynamoHomePath = DynamoEnv.getProperty("atg.dynamo.home");
      File dynamoHome = new File(dynamoHomePath).getCanonicalFile();

      // Get the port number for this Dynamo's RMI server.
      atg.service.dynamo.Configuration configuration = (atg.service.dynamo.Configuration) request.resolveName("/atg/dynamo/Configuration");
      String rmiPort = new Integer(configuration.getRmiPort()).toString();

      String jreVersion = DynamoEnv.getProperty("java.version");
      boolean isSupportedJre = AppUtils.checkJREVersion();

      // Generate the command to run the ACC launcher.
      String[] cmd;
      if (isSupportedJre) {
        cmd = new String[] {
        javaVM,
        "-Xms96m",
        "-Xmx160m",
        "-Djava.security.policy=lib" + File.separator + "java.policy",
        "-Djava.protocol.handler.pkgs=atg.net.www.protocol",
        "-Datg.dynamo.modulepath=" 
          + ( isWindows ? "\"" : "")
          + ( DynamoEnv.getProperty("atg.dynamo.modulepath") != null
              ? DynamoEnv.getProperty("atg.dynamo.modulepath")
              : DynamoEnv.getProperty("atg.dynamo.root") )
          + ( isWindows ? "\"" : ""),
        "-Djava.naming.factory.url.pkgs=atg.jndi.url",
        "-classpath",
        dynamoHomePath + File.separator + "lib" + File.separator + "launcher.jar" 
	  + File.pathSeparator + dynamoRootPath + File.separator 
          + "DAS-UI" + File.separator + "lib" + File.separator + "client-stubs.jar",
        "atg.applauncher.dynamo.LocalACCLauncher",
        "-host",
        "localhost",
        "-port",
        rmiPort,
        "-root",
        dynamoRoot.getPath(),
        "-home",
        dynamoHome.getPath()};
      }
      else {
        //
        // If the server is running in a JRE that is not supported by the ACC,
        // we use the startACC script to launch the ACC.
        //
        if (isWindows) {
          String launcher = dynamoRoot.getPath() +
                            File.separator + "DAS-UI" +
                            File.separator + "bin" +
                            File.separator + "shellLauncher.exe";
          String script = dynamoHome.getPath() +
                          File.separator + "bin" +
                          File.separator + "startACC.bat";
          cmd = new String[] {launcher, script};
        }
        else {
          cmd = new String[] {
            dynamoHome.getPath() + File.separator + "bin" + File.separator + "startACC"
          };
        }
      }

      try {
        Runtime.getRuntime().exec(cmd, null, dynamoHome);
        out.println("ATG Control Center has been launched.");
      }
      catch (IOException e) {
        out.println("<B>ERROR:</B> A fatal error occurred while launching ATG Control Center.");
      }
                /*** lines: 216-217 */
            __fileData.writeChars (7223, 3, (CharFileDataSink)out);
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
  _Param_4_false m_Param_4_false = this. new _Param_4_false();
  {
    m_Param_4_false.setParent(this);
  }
  public class _SubServlet_5 extends PageSubServlet {
    {
      this.setParameter("true", m_Param_3_true);
      this.setParameter("false", m_Param_4_false);
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
            _form = ((DropletEventServlet)request.getAttribute(DropletConstants.DROPLET_EVENT_ATTRIBUTE)).addForm("/atg/dynamo/admin/en/start-acc.jhtml.3");
            if (_form != null) {
             String actionURI = ServletUtil.getRequestURI(request, "start-acc.jhtml            ");
            _form.addActionURL(actionURI);
            }
            request.setParameter("_form", _form);
            request.addQueryParameter("_DARGS", "/atg/dynamo/admin/en/start-acc.jhtml.3");
            request.addQueryParameter("_dynSessConf", Long.toString(request.getSessionConfirmationNumber()));            /*** lines: 234-234 */
            __fileData.writeChars (7728, 202, (CharFileDataSink)out);
            out.print(request.encodeURL ("start-acc.jhtml", true, true, false, true, false));
            /*** lines: 234-234 */
            __fileData.writeChars (7945, 49, (CharFileDataSink)out);
            /*** lines: 234-234 */
            __fileData.writeChars (8041, 16, (CharFileDataSink)out);
            {
              String _pn = "/atg/dynamo/admin/LDAPCacheLoader.reload";
              out.print(" name=\"");
              out.print(_pn);
              out.print('"');
              /*** lines: 234-234 */
              __fileData.writeChars (8057, 1, (CharFileDataSink)out);
              if (_form != null) _form.addTag(request, response, null, _pn, "SUBMIT", null, DropletConstants.SUBMIT_PRIORITY_DEFAULT, null, null, null, false);
            }
            /*** lines: 234-242 */
            __fileData.writeChars (8058, 47, (CharFileDataSink)out);
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
  public class _Param_7_false extends PageSubServlet {
    
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
            /*** lines: 243-243 */
            __fileData.writeChars (8142, 24, (CharFileDataSink)out);
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
  _Param_7_false m_Param_7_false = this. new _Param_7_false();
  {
    m_Param_7_false.setParent(this);
  }
  public class _Param_8_default extends PageSubServlet {
    
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
            /*** lines: 244-244 */
            __fileData.writeChars (8205, 16, (CharFileDataSink)out);
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
  _Param_8_default m_Param_8_default = this. new _Param_8_default();
  {
    m_Param_8_default.setParent(this);
  }
  public class _SubServlet_9 extends PageSubServlet {
    {
      this.setParameter("true", m_Param_6_true);
      this.setParameter("false", m_Param_7_false);
      this.setParameter("default", m_Param_8_default);
    }
  }

  _SubServlet_9 m_SubServlet_9 = this. new _SubServlet_9();
  {
    m_SubServlet_9.setParent(this);
  }
  public class _Param_10_false extends PageSubServlet {
    
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
            /*** lines: 231-232 */
            __fileData.writeChars (7535, 9, (CharFileDataSink)out);
            try {
              request.pushFrame();
              request.setParameter("value", DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/LDAPCacheLoader.accountManager.memberOfCacheEnabled", true, null, null));
              m_SubServlet_9.serviceByName(request, response, "/atg/dynamo/droplet/Switch", 232);
            }
            finally {
              request.popFrame();
            }
            /*** lines: 245-246 */
            __fileData.writeChars (8246, 4, (CharFileDataSink)out);
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
  _Param_10_false m_Param_10_false = this. new _Param_10_false();
  {
    m_Param_10_false.setParent(this);
  }
  public class _Param_11_true extends PageSubServlet {
    
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
            /*** lines: 247-247 */
            __fileData.writeChars (8282, 32, (CharFileDataSink)out);
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
  _Param_11_true m_Param_11_true = this. new _Param_11_true();
  {
    m_Param_11_true.setParent(this);
  }
  public class _SubServlet_12 extends PageSubServlet {
    {
      this.setParameter("false", m_Param_10_false);
      this.setParameter("true", m_Param_11_true);
    }
  }

  _SubServlet_12 m_SubServlet_12 = this. new _SubServlet_12();
  {
    m_SubServlet_12.setParent(this);
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
    _imports.addImport("/atg/devtools/Admin");
    _imports.addImport("/java/net/InetAddress");
    _imports.addImport("/atg/dynamo/admin/Configure");
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
        /*** lines: 2-3 */
        __fileData.writeChars (119, 2, (CharFileDataSink)out);
        /*** lines: 7-9 */
        __fileData.writeChars (229, 2, (CharFileDataSink)out);
        /*** lines: 12-13 */
        __fileData.writeChars (308, 1, (CharFileDataSink)out);

  atg.ui.hub.HubAdmin admin = null;
  try {
   admin = (atg.ui.hub.HubAdmin)
  request.resolveName("/atg/devtools/Admin");
  } catch ( Exception e ) {
  // DAS-UI is probably not specified
  }
  
  //
  // NOTE: The ATG Control Center has not yet been ported to JRE 1.4, so we
  // need to ensure that it runs on JRE 1.3 only.
  //
  String javaVersion = DynamoEnv.getProperty("java.version");
  boolean isSupportedJRE = AppUtils.checkJREVersion();

        /*** lines: 29-29 */
        __fileData.writeChars (773, 61, (CharFileDataSink)out);
        out.print(request.encodeURL ("/atg/dynamo/admin/admin.css", false, true, true, true));
        request.setParameterDelimiter(ServletUtil.getParamDelimiter(request));
        /*** lines: 29-29 */
        __fileData.writeChars (861, 194, (CharFileDataSink)out);
        out.print(request.encodeURL ("/", true, true, false, true));
        request.setParameterDelimiter(DynamoHttpServletRequest.DEFAULT_PARAMETER_DELIMITER);
        /*** lines: 29-46 */
        __fileData.writeChars (1056, 87, (CharFileDataSink)out);

  if ( admin == null ) {
        /*** lines: 48-61 */
        __fileData.writeChars (1182, 512, (CharFileDataSink)out);

  } else {
        /*** lines: 63-64 */
        __fileData.writeChars (1719, 17, (CharFileDataSink)out);
        {
          Object _t = DropletDescriptor.getPropertyValue(request, response, "/atg/devtools/Admin.errorMessage", true, _cvt0, _cvtProps0);
          if (_t == null) {
          }
            out.print(ServletUtil.toString(_t));
        }
        /*** lines: 64-67 */
        __fileData.writeChars (1795, 19, (CharFileDataSink)out);


// Get the display from the atg.display property.  If the display
// does not exist then set the display to the current host name.
// The display property should be of the form "foo.atg.com:0.0". 
// If ':' cannot be found in the display property or the display
// property is ":0.0" then set the display to the current host name.
// Otherwise set the display to the display property minus the 
// ":0.0".
String display = DynamoEnv.getProperty("atg.dynamo.display");
String hostName = java.net.InetAddress.getLocalHost().getHostName();
if (null != display) {
  int index = display.indexOf(':');
  if ((-1 == index) || (0 == index)) {
    display = hostName;
  }
  else {
    display = display.substring(0,index);
  }
}
else {
  display = hostName;
}
        /*** lines: 90-93 */
        __fileData.writeChars (2580, 10, (CharFileDataSink)out);
 if (isSupportedJRE && request.resolveName("/atg/devtools/DevAgent", false) != null) {         /*** lines: 93-96 */
        __fileData.writeChars (2690, 68, (CharFileDataSink)out);
        {
          Object _t = DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/Configure.thisHostname", true, PageCompileServlet.getEscapeHTMLTagConverter(), null);
          if (_t == null) {
          }
            out.print(ServletUtil.toString(_t));
        }
        /*** lines: 96-100 */
        __fileData.writeChars (2811, 24, (CharFileDataSink)out);
        try {
          request.pushFrame();
          request.setParameter("value", DropletDescriptor.getPropertyValue(request, response, "/atg/devtools/Admin.hubActive", true, null, null));
          m_SubServlet_2.serviceByName(request, response, "/atg/dynamo/droplet/Switch", 100);
        }
        finally {
          request.popFrame();
        }
        /*** lines: 113-117 */
        __fileData.writeChars (3246, 17, (CharFileDataSink)out);
 }         /*** lines: 117-120 */
        __fileData.writeChars (3279, 78, (CharFileDataSink)out);
        {
          Object _t = DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/Configure.thisHostname", true, PageCompileServlet.getEscapeHTMLTagConverter(), null);
          if (_t == null) {
          }
            out.print(ServletUtil.toString(_t));
        }
        /*** lines: 120-125 */
        __fileData.writeChars (3410, 120, (CharFileDataSink)out);
        try {
          request.pushFrame();
          request.setParameter("value", request.getObjectParameter("launched"));
          m_SubServlet_5.serviceByName(request, response, "/atg/dynamo/droplet/IsNull", 125);
        }
        finally {
          request.popFrame();
        }
        /*** lines: 218-224 */
        __fileData.writeChars (7246, 83, (CharFileDataSink)out);

out.print(display + ".");
        /*** lines: 226-229 */
        __fileData.writeChars (7369, 10, (CharFileDataSink)out);
        try {
          request.pushFrame();
          request.setParameter("value", DropletDescriptor.getPropertyValue(request, response, "/atg/dynamo/admin/LDAPCacheLoader.accountManager", true, null, null));
          m_SubServlet_12.serviceByName(request, response, "/atg/dynamo/droplet/IsNull", 229);
        }
        finally {
          request.popFrame();
        }
        /*** lines: 248-251 */
        __fileData.writeChars (8334, 16, (CharFileDataSink)out);

  } // check if admin is set
        /*** lines: 253-255 */
        __fileData.writeChars (8393, 17, (CharFileDataSink)out);
/* @version $Id: //product/DAS/version/10.2/release/DAS/admin/atg/dynamo/admin/en/start-acc.jhtml#1 $$Change: 735822 $*/        /*** lines: 255-256 */
        __fileData.writeChars (8543, 2, (CharFileDataSink)out);
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
