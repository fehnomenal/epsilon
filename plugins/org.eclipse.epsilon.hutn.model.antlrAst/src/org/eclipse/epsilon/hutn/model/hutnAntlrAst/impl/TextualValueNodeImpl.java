/**
 * *******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 * ******************************************************************************
 *
 * $Id: TextualValueNodeImpl.java,v 1.3 2008/08/14 12:37:29 dkolovos Exp $
 */
package org.eclipse.epsilon.hutn.model.hutnAntlrAst.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.epsilon.antlr.postprocessor.model.antlrAst.impl.NodeImpl;

import org.eclipse.epsilon.hutn.model.hutnAntlrAst.HutnAntlrAstPackage;
import org.eclipse.epsilon.hutn.model.hutnAntlrAst.TextualValueNode;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Textual Value Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class TextualValueNodeImpl extends NodeImpl implements TextualValueNode {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TextualValueNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return HutnAntlrAstPackage.Literals.TEXTUAL_VALUE_NODE;
	}

} //TextualValueNodeImpl
