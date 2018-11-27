package com.cricket.dws;

import java.io.File;

import atg.service.datacollection.RotationAwareFormattingFileLogger;
import atg.vfs.VirtualFile;
import atg.vfs.VirtualFileSystem;
import atg.vfs.file.LocalFileSystem;

/**
 * Class will override the rotate method to copy the rotated files to new directory
 * @author TechM
 *
 */
public class CricketFormattingFileLogger extends RotationAwareFormattingFileLogger {
	
	
	protected File roatatedFileRoot;
	
	protected VirtualFileSystem rotateFileSystem;
	
		
	@Override
	public synchronized void rotate() {
		
		VirtualFile currentFile = getLogFile();
		
		if(currentFile != null){
			
			String currentfileName = currentFile.getName();
			
			if(isLoggingDebug()){
				logDebug("CricketFormattingFileLogger  :: "+currentfileName+" is in rotation progress ....");
			}
			
			super.rotate();
			
			boolean isSuccess = currentFile.renameTo(getRotateFileSystem().getFile(currentfileName));
			
			if(isSuccess){
				if(isLoggingDebug()){
					logDebug("CricketFormattingFileLogger  :: "+currentfileName+" is moved to "+ getRoatatedFileRoot().getAbsolutePath());
				}
			}else{
				if(isLoggingDebug()){
					logDebug("CricketFormattingFileLogger  :: "+currentfileName+" is already moved to "+ getRoatatedFileRoot().getAbsolutePath()+" or removed at source location");
				}
			}
		
		}
		
	}
	
	@Override
	protected void stopScheduledJob() {
		
		super.stopScheduledJob();
		
		VirtualFile currentFile = getLogFile();
		
		if(currentFile != null){
			
			String currentfileName = currentFile.getName();
			
			if(isLoggingDebug()){
				logDebug("CricketFormattingFileLogger  :: "+currentfileName+" is in rotation progress ....");
			}
			
			boolean isSuccess = currentFile.renameTo(getRotateFileSystem().getFile(currentfileName));
			
			if(isSuccess){
				if(isLoggingDebug()){
					logDebug("CricketFormattingFileLogger  :: "+currentfileName+" is moved to "+ getRoatatedFileRoot().getAbsolutePath());
				}
			}else{
				if(isLoggingDebug()){
					logDebug("CricketFormattingFileLogger  :: "+currentfileName+" is already moved to "+ getRoatatedFileRoot().getAbsolutePath()+" or removed at source location");
				}
			}
		
		}
		
		
	}

	/**
	 * @return the roatatedFileRoot
	 */
	public File getRoatatedFileRoot() {
		return roatatedFileRoot;
	}

	/**
	 * @param roatatedFileRoot the roatatedFileRoot to set
	 */
	public void setRoatatedFileRoot(File roatatedFileRoot) {
		this.roatatedFileRoot = roatatedFileRoot;
	}

	/**
	 * @return the rotateFileSystem
	 */
	public VirtualFileSystem getRotateFileSystem() {
		
		 if (this.rotateFileSystem == null) {
		      this.rotateFileSystem = new LocalFileSystem(getRoatatedFileRoot());
		    }
		    return this.rotateFileSystem;
	}

	/**
	 * @param rotateFileSystem the rotateFileSystem to set
	 */
	public void setRotateFileSystem(VirtualFileSystem rotateFileSystem) {
		this.rotateFileSystem = rotateFileSystem;
	}

}
