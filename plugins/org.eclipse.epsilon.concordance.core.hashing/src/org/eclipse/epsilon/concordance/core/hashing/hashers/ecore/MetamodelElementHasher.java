/*******************************************************************************
 * Copyright (c) 2009 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 ******************************************************************************
 *
 * $Id$
 */
package org.eclipse.epsilon.concordance.core.hashing.hashers.ecore;


import java.util.Collections;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.epsilon.concordance.core.hashing.hashers.DelegatingHasher;
import org.eclipse.epsilon.concordance.core.hashing.hashers.java.TypeSafeHasher;


public class MetamodelElementHasher extends TypeSafeHasher<EModelElement> {
	
	private final Metafeatures metafeatures;

	public MetamodelElementHasher(String... featureNames) {
		this.metafeatures = new Metafeatures(featureNames);
	}
	
	public int hashSafely(EModelElement metamodelElement) {
		return hashSafely(metamodelElement, Collections.emptyList());
	}
	
	public int hashSafely(EModelElement metamodelElement, Object... otherValuesToIncludeInHash) {
		int result = 17;
		
		for (Object valueToHash : metafeatures.getValuesToHashFrom(metamodelElement)) {
			result = 37 * result + DelegatingHasher.getInstance().hash(valueToHash);
		}
		
		for (Object valueToHash : otherValuesToIncludeInHash) {
			result = 37 * result + DelegatingHasher.getInstance().hash(valueToHash);
		}
		
		return result;
	}
}
