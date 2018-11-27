package com.cricket.imageurl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;

import atg.core.util.StringUtils;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
/**
 * @author vs111655
 * Droplet will be used to display images from Liquid Pixel.
 * It will take relative image path which is configured in BCC and construct 
 * Liquid Pixel Image request setting height. 
 */
/**
 * @author SV111655
 *
 */
public class ImageURLLookupDroplet extends DynamoServlet {

	
	private String serverProtocol;	
	private String imageServer;
	private String imageURI;	
	private String imageChain;	
	private String chainFormat;	
	private String imageQuality;	
	private String defaultImageSize;
	private String pngImageSize;
	private String height;	
	private String pngImageFormat ="png";

	
	/**
	 * @param pReq
	 * @param pRes
	 * @throws ServletException
	 * @throws IOException
	 */

	public void service(DynamoHttpServletRequest pReq,
			DynamoHttpServletResponse pRes) throws ServletException,
			IOException {

		String imageLink = pReq.getParameter(CricketCommonConstants.IMAGE_LINK);
		String imageHeight = pReq.getParameter(CricketCommonConstants.IMAGE_HEIGHT);
		String imageFormat = CricketCommonConstants.STRING_NULL;
		String protocalType = pReq.getParameter("protocalType");
		StringBuilder buildURL = new StringBuilder();
		
		/*
		 * Setting height to zero if its null
		 */
		if(StringUtils.isEmpty(imageHeight)){
			imageHeight = String.valueOf(CricketCommonConstants.INTEGER_ZERO);
		} 
		if (isLoggingDebug()) {
		if( StringUtils.isEmpty(imageLink)){
			logDebug("Media External item does not contain any Image URL");
		}
		}
		/*
		 * extracting image format from given image url
		 */
		String extension = imageLink;
		if ( !StringUtils.isEmpty(extension) && extension.length() >= CricketCommonConstants.INTEGER_THREE) {
			imageFormat = extension.substring(extension.length() - CricketCommonConstants.INTEGER_THREE);
		} else{
			imageFormat = pngImageFormat;
		}
				
		if(protocalType != null && protocalType.equalsIgnoreCase("secure"))
			buildURL.append("https://");
		else
			buildURL.append(getServerProtocol());
		buildURL.append(getImageServer());
		buildURL.append(getImageURI());
		buildURL.append(imageLink);
		buildURL.append(getHeight());
		buildURL.append(imageHeight);
		buildURL.append(getChainFormat());
		buildURL.append(imageFormat);
		buildURL.append(getImageQuality());
		
		if(imageFormat.equalsIgnoreCase(pngImageFormat)){
			buildURL.append(getPngImageSize());
		} else {
			buildURL.append(getDefaultImageSize());
		}
		
		buildURL.append(getImageChain());		
		URL completeURL = EncodeURL(buildURL.toString());
		pReq.setParameter(CricketCommonConstants.URL, completeURL);
		pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
	}
	
	
	/**
	 * Method used to encode URL - Replace space with %20 code
	 * @param lpURL
	 * @return encodedURL
	 */
	private URL EncodeURL(String lpURL) {
		URL encodedURL = null;
		try {
			encodedURL = new URL(lpURL);
			URI encodedURI;
			encodedURI = new URI(encodedURL.getProtocol(), encodedURL.getUserInfo(), encodedURL.getHost(),
					encodedURL.getPort(), encodedURL.getPath(),	encodedURL.getQuery(), encodedURL.getRef());
			encodedURL = encodedURI.toURL();
			if (isLoggingDebug()) {
				logDebug("Protocol :: " + encodedURL.getProtocol());
				logDebug( "User Info ::" + encodedURL.getUserInfo());
				logDebug( "Port ::" + encodedURL.getPort());
				logDebug( "Path ::"	+ encodedURL.getPath());
				logDebug( "Query ::" + encodedURL.getQuery());
				logDebug("Ref ::" + encodedURL.getRef());
			}
		} catch (URISyntaxException e) {
			if (isLoggingError()) {
				logError("XXXXX URI Syntax Exception XXXXXX ::" + e);
			}
		} catch (MalformedURLException e) {
			if (isLoggingError()) {
				logError("XXXXX MAL Formed URL Exception XXXXXX ::" + e);
			}
		}
		if (isLoggingDebug()) {
			logDebug("Encoded URL :::: " + encodedURL);
		}
		return encodedURL;
	}
	public String getServerProtocol() {
		return serverProtocol;
	}

	public void setServerProtocol(String serverProtocol) {
		this.serverProtocol = serverProtocol;
	}

	public String getImageServer() {
		return imageServer;
	}

	public void setImageServer(String imageServer) {
		this.imageServer = imageServer;
	}

	public String getImageURI() {
		return imageURI;
	}

	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
	}
	public String getImageChain() {
		return imageChain;
	}

	public void setImageChain(String imageChain) {
		this.imageChain = imageChain;
	}

	public String getChainFormat() {
		return chainFormat;
	}

	public void setChainFormat(String chainFormat) {
		this.chainFormat = chainFormat;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getImageQuality() {
		return imageQuality;
	}


	public void setImageQuality(String pImageQuality) {
		imageQuality = pImageQuality;
	}


	public String getDefaultImageSize() {
		return defaultImageSize;
	}


	public void setDefaultImageSize(String pDefaultImageSize) {
		defaultImageSize = pDefaultImageSize;
	}


	public String getPngImageSize() {
		return pngImageSize;
	}


	public void setPngImageSize(String pPngImageSize) {
		pngImageSize = pPngImageSize;
	}


}
