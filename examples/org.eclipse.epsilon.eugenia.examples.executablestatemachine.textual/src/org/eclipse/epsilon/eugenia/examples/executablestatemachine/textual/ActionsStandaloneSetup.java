/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.eugenia.examples.executablestatemachine.textual;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class ActionsStandaloneSetup extends ActionsStandaloneSetupGenerated{

	public static void doSetup() {
		new ActionsStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

