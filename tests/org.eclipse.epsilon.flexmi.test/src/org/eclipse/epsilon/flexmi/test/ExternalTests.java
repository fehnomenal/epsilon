/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.flexmi.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExternalTests extends FlexmiTests {
	
	@Test
	public void testExternalFile() throws Exception {
		assertEval("EPackage.all.first().name", "p1", "external/external.flexmi");
		assertEval("EPackage.all.second().name", "p2", "external/external.flexmi");
	}
	
	@Test
	public void testExternalNested() throws Exception {
		assertEval("EPackage.all.first().name", "p1", "external/toplevel.flexmi");
	}
	
	@Test
	public void testExternalWithWarning() throws Exception {
		assertEquals(1, loadResource("external/external-with-warning.flexmi").getWarnings().size());
	}
	
	
}
