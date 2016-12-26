/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.upload;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.laview.commons.file.FilenameUtils;
import com.laview.commons.file.StoreService;

/**
 * 提供一个上传文件存取服务，使用者可以方便地存取上传文件
 *
 * @author laview_chen
 * @since: v1.0
 */
public abstract class UploadFileStoreService extends StoreService{

	/**
	 * 保存上传文件到指定路径 。。。是路径，而文件的名称将取 上传时的文件名称
	 *
	 * @param file          要保存的上传文件
	 * @param path          相对路径
	 * @return				返回保存之后的 path
	 * @throws IOException
	 */
	public String saveToPath(FormFile file, String path) throws IOException, FileNotFoundException {
		String fileName = FilenameUtils.addLastSeparatorIfNoExists(path) + file.getFileName();
		return save(file.getInputStream(), fileName);
	}
	
	/**
	 * 将上传文件，以文件名为 file 进行保存。。。 file 可以包含相对路径
	 *
	 * @param upload
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public String saveToFile(FormFile upload, String file) throws IOException, FileNotFoundException{
		return save(upload.getInputStream(), file);
	}
}