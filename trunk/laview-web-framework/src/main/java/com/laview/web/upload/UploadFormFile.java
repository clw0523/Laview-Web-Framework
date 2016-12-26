/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.fileupload.FileItem;


/**
 * 上传文件接口 FormFile 的一个实现 
 * 
 * 代码来自 Struts 源码中的  CommonsFormFile inner Classes
 *
 */
public class UploadFormFile implements FormFile, Serializable {

    /**
     * <p> The <code>FileItem</code> instance wrapped by this object.
     * </p>
     */
    FileItem fileItem;

    /**
     * Constructs an instance of this class which wraps the supplied file
     * item. </p>
     *
     * @param fileItem The Commons file item to be wrapped.
     */
    public UploadFormFile(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    /**
     * <p> Returns the content type for this file. </p>
     *
     * @return A String representing content type.
     */
    public String getContentType() {
        return fileItem.getContentType();
    }

    /**
     * <p> Sets the content type for this file. <p> NOTE: This method is
     * not supported in this implementation. </p>
     *
     * @param contentType A string representing the content type.
     */
    public void setContentType(String contentType) {
        throw new UnsupportedOperationException(
            "The setContentType() method is not supported.");
    }

    /**
     * <p> Returns the size, in bytes, of this file. </p>
     *
     * @return The size of the file, in bytes.
     */
    public int getFileSize() {
        return (int) fileItem.getSize();
    }

    /**
     * <p> Sets the size, in bytes, for this file. <p> NOTE: This method
     * is not supported in this implementation. </p>
     *
     * @param filesize The size of the file, in bytes.
     */
    public void setFileSize(int filesize) {
        throw new UnsupportedOperationException(
            "The setFileSize() method is not supported.");
    }

    /**
     * <p> Returns the (client-side) file name for this file. </p>
     *
     * @return The client-size file name.
     */
    public String getFileName() {
        return getBaseFileName(fileItem.getName());
    }

    /**
     * <p> Sets the (client-side) file name for this file. <p> NOTE: This
     * method is not supported in this implementation. </p>
     *
     * @param fileName The client-side name for the file.
     */
    public void setFileName(String fileName) {
        throw new UnsupportedOperationException(
            "The setFileName() method is not supported.");
    }

    /**
     * <p> Returns the data for this file as a byte array. Note that this
     * may result in excessive memory usage for large uploads. The use of
     * the {@link #getInputStream() getInputStream} method is encouraged
     * as an alternative. </p>
     *
     * @return An array of bytes representing the data contained in this
     *         form file.
     * @throws FileNotFoundException If some sort of file representation
     *                               cannot be found for the FormFile
     * @throws IOException           If there is some sort of IOException
     */
    public byte[] getFileData()
        throws FileNotFoundException, IOException {
        return fileItem.get();
    }

    /**
     * <p> Get an InputStream that represents this file.  This is the
     * preferred method of getting file data. </p>
     *
     * @throws FileNotFoundException If some sort of file representation
     *                               cannot be found for the FormFile
     * @throws IOException           If there is some sort of IOException
     */
    public InputStream getInputStream()
        throws FileNotFoundException, IOException {
        return fileItem.getInputStream();
    }

    /**
     * <p> Destroy all content for this form file. Implementations should
     * remove any temporary files or any temporary file data stored
     * somewhere </p>
     */
    public void destroy() {
        fileItem.delete();
    }

    /**
     * <p> Returns the base file name from the supplied file path. On the
     * surface, this would appear to be a trivial task. Apparently,
     * however, some Linux JDKs do not implement <code>File.getName()</code>
     * correctly for Windows paths, so we attempt to take care of that
     * here. </p>
     *
     * @param filePath The full path to the file.
     * @return The base file name, from the end of the path.
     */
    protected String getBaseFileName(String filePath) {
        // First, ask the JDK for the base file name.
        String fileName = new File(filePath).getName();

        // Now check for a Windows file name parsed incorrectly.
        int colonIndex = fileName.indexOf(":");

        if (colonIndex == -1) {
            // Check for a Windows SMB file path.
            colonIndex = fileName.indexOf("\\\\");
        }

        int backslashIndex = fileName.lastIndexOf("\\");

        if ((colonIndex > -1) && (backslashIndex > -1)) {
            // Consider this filename to be a full Windows path, and parse it
            // accordingly to retrieve just the base file name.
            fileName = fileName.substring(backslashIndex + 1);
        }

        return fileName;
    }

    /**
     * <p> Returns the (client-side) file name for this file. </p>
     *
     * @return The client-size file name.
     */
    public String toString() {
        return getFileName();
    }	
}