/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

/**
 * 返回文件下载信息， Framework 根据这些信息来处理 Response，以方便使用。
 *
 * 要注意：
 * 
 *   1)如果是对于大文件（例如  100M 或 G 级以上），建议不使用本方法处理，可以直接使用 HttpServletResponse
 * 又或者  HTTP 流来进行，因为这里的控制比较简单。
 * 
 *   2）不要在这个返回值上添加  @ResponseBody 注解，这样的话，返回值有可能会被当作是 JSON 等输出返回
 * 
 * @author laview_chen
 * @since: v1.0
 */
public interface FileDownloadResult {

	/**
	 * 完整的文件目录 路径
	 *
	 * @return
	 */
	public String getFileFullName();
	
	/**
	 * 文件的大小
	 *
	 * @return
	 */
	public long getFileSize();
}