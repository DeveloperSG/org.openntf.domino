/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.design.impl;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public class OnDiskFile implements Serializable {

	private static final long serialVersionUID = -3298261314433290242L;

	private String name_;
	private long dbTimeStamp_;
	private long diskTimeStamp_;

	private DesignMapping odpMapping;

	private transient boolean processed;
	private transient File file_;
	private transient String path_;
	private String md5_;

	public OnDiskFile(final File parent, final File file) {
		setFile(parent, file);
		setProcessed(false);

		// example:
		// parent 	= C:\documents\odp\
		// file 	= C:\documents\odp\Code\Scriptlibraries\lib.lss
		// odpFolder= C:\documents\odp\Code\Scriptlibraries
		// relUri 	= lib.lss

		odpMapping = DesignMapping.valueOf(parent, file);
		File odpFolder = new File(parent, odpMapping.getOnDiskFolder());
		URI relUri = odpFolder.toURI().relativize(file.toURI());

		String ext = odpMapping.getOnDiskFileExtension();

		if (ext == null) {
			// no extension, so use the relative file uri
			name_ = relUri.getPath();
		} else if (ext.equals("*")) {
			// name is "*", so use the unescaped part.
			name_ = relUri.getPath();
		} else if (ext.startsWith(".")) {
			name_ = relUri.getPath();
		} else {
			name_ = ext;
		}

	}

	public String getName() {
		return name_;
	}

	public File getFile() {
		return file_;
	}

	public Class<?> getImplementingClass() {
		return odpMapping.getImplClass();
	}

	public long getDbTimeStamp() {
		return dbTimeStamp_;
	}

	public void setDbTimeStamp(final long timeStamp) {
		this.dbTimeStamp_ = timeStamp;
	}

	public long getDiskTimeStamp() {
		return diskTimeStamp_;
	}

	public void setDiskTimeStamp(final long timeStamp) {
		this.diskTimeStamp_ = timeStamp;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(final boolean processed) {
		this.processed = processed;
	}

	public void setFile(final File parent, final File file) {
		this.file_ = file;
		path_ = parent.toURI().relativize(file.toURI()).getPath();

	}

	public void setMD5(final String md5) {
		this.md5_ = md5;
	}

	public String getMD5() {
		return md5_;
	}

	public String getPath() {
		return path_;
	}

	@Override
	public String toString() {
		return "OnDiskFile [name=" + name_ + ", odpMapping=" + odpMapping + ", file=" + file_ + "]";
	}

}
