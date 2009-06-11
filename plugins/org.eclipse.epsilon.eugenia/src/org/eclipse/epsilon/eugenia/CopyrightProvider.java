/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
******************************************************************************/

package org.eclipse.epsilon.eugenia;

import java.io.BufferedInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.epsilon.common.dt.util.LogUtil;
import org.eclipse.epsilon.eol.execute.context.Variable;

public class CopyrightProvider {
	
	public static Variable getCopyrightVariable(IFile base) {
		IFile file = base.getParent().getFile(new Path("copyright.txt"));
		String copyright = "";
		if (file.exists()) {
			try {
				BufferedInputStream bis = new BufferedInputStream(file.getContents());
				int c;
				while ((c = bis.read())!=-1) {
					copyright += (char) c;
				}
			} catch (Exception e) {
				LogUtil.log(e);
			}
		}
		
		return Variable.createReadOnlyVariable("copyright", copyright);
		
	}
	
}
 