/*
 * $Id: CommonsMultipartRequestHandler.java 524895 2007-04-02 19:29:21Z germuska $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.laview.web.upload;

import org.apache.log4j.Logger;

import com.laview.web.servlet.commons.GlobalConfig;


public class CommonsMultipartRequestHandler implements MultipartRequestHandler, FormFileConstants {

	private final static Logger logger = Logger.getLogger(CommonsMultipartRequestHandler.class);
	
	/**
	 * 将  带单位 （K, M, G 单位）的尺寸转为一个以 byte 为单位的长整数，转换失败的情况下，则返回 defaultSize 
	 *
	 * @param sizeString
	 * @param defaultSize
	 * @return
	 */
    protected long convertSizeToBytes(String sizeString, long defaultSize) {
        int multiplier = 1;

        if (sizeString.endsWith("K")) {
            multiplier = 1024;
        } else if (sizeString.endsWith("M")) {
            multiplier = 1024 * 1024;
        } else if (sizeString.endsWith("G")) {
            multiplier = 1024 * 1024 * 1024;
        }

        if (multiplier != 1) {
            sizeString = sizeString.substring(0, sizeString.length() - 1);
        }

        long size = 0;

        try {
            size = Long.parseLong(sizeString);
        } catch (NumberFormatException nfe) {
            logger.warn("Invalid format for file size ('" + sizeString
                + "'). Using default.");
            size = defaultSize;
            multiplier = 1;
        }

        return (size * multiplier);
    }

	/* (non-Javadoc)
	 * @see com.laview.web.upload.MultipartRequestHandler#getUploadMaxSize()
	 */
	@Override
	public long getUploadMaxSize() {
        return convertSizeToBytes(GlobalConfig.getMaxFileSize(),
                DEFAULT_SIZE_MAX);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.upload.MultipartRequestHandler#getUploadMemMaxSize()
	 */
	@Override
	public long getUploadMemMaxSize() {
        return convertSizeToBytes(GlobalConfig.getMemFileSize(),
                DEFAULT_SIZE_THRESHOLD);
	}
	
}