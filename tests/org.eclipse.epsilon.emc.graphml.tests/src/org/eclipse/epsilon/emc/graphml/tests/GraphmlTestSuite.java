/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.emc.graphml.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({EdgeFeatureLabelParserTests.class,
               ValuedFeatureLabelParserTests.class, 
               StyleAttributesGetterTests.class})
public class GraphmlTestSuite {

	public static Test suite() {
		return new JUnit4TestAdapter(GraphmlTestSuite.class);
	}
}
