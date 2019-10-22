/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.emc.emf;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class EmfWorkbench {
	
	private ResourceSet resourceSet;
	
	protected static String relative2absolute(String relative) {
		return "";
	}
	
	public void start() throws Exception {
		EmfModel model = new EmfModel();
		model.load();		
	}
	
	public Resource getModel(String modelFile, String metamodelFile){

		Map<String, Object> etfm = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
		if(!etfm.containsKey("*")) {
			etfm.put("*", new XMIResourceFactoryImpl());
		}
		
		resourceSet = new ResourceSetImpl();
		
		Resource metamodel = resourceSet.createResource(URI.createURI(""));
		
		try {
			metamodel.load(EmfWorkbench.class.getResourceAsStream(metamodelFile), Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Iterator<EObject> it =  metamodel.getContents().iterator();
		while (it.hasNext()) {
			EPackage p = (EPackage) it.next();
			System.out.println(p.getNsURI());
			//etfm.put(p.getNsURI(), p);
			resourceSet.getPackageRegistry().put(p.getNsURI(), p);
		}
		
		Resource model = resourceSet.createResource(URI.createURI(""));
		
		try {
			model.load(EmfWorkbench.class.getResourceAsStream(modelFile), Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	public Resource getMetamodel(String metamodelFile){
		Map<String, Object> etfm = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
		if (!etfm.containsKey("*")) {
			etfm.put("*", new XMIResourceFactoryImpl());
		}
		
		resourceSet = new ResourceSetImpl();
		
		Resource metamodel = resourceSet.createResource(URI.createURI(""));
		
		try {
			metamodel.load(EmfWorkbench.class.getResourceAsStream(metamodelFile), Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return metamodel;
	}
	
}
