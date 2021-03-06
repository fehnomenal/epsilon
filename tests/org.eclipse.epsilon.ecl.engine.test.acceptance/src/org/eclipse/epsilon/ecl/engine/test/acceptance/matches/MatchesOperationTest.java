/*********************************************************************
 * Copyright (c) 2018 The University of York.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.ecl.engine.test.acceptance.matches;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.function.Supplier;
import org.eclipse.epsilon.ecl.IEclModule;
import org.eclipse.epsilon.ecl.engine.test.acceptance.EclAcceptanceTestUtil;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * 
 * @author Sina Madani
 * @since 1.6
 */
@RunWith(Parameterized.class)
public class MatchesOperationTest {

	IEclModule module;
	String script = "CompareInstance.ecl";
	
	public MatchesOperationTest(Supplier<? extends IEclModule> moduleGetter) {
		this.module = moduleGetter.get();
	}
	
	@Parameters(name = "{0}")
	public static Iterable<Supplier<? extends IEclModule>> modules() {
		return EclAcceptanceTestUtil.modules();
	}
	
	@Before
	public void setup() throws Exception {
		module.parse(getClass().getResource(script).toURI());
		loadEmfModel("Left");
		loadEmfModel("Right");
		module.execute();
	}
	
	@Test
	public void testCorrectNumberOfMatches() throws Exception {
		MatchTrace allMatches = module.getContext().getMatchTrace();
		if (allMatches.size() != 1) {
			fail("Expected 1 match, but got: "+System.lineSeparator()+allMatches.toString(module.getContext()));
		}
		assertEquals("TLN (Left!TLN, Right!TLN)", allMatches.iterator().next().getRule().toString());
	}
	
	private EmfModel loadEmfModel(String modelName) throws Exception {
		EmfModel model = new EmfModel();
		model.setName(modelName);
		model.setCachingEnabled(true);
		model.setConcurrent(true);
		model.setMetamodelFile(getClass().getResource("mymetamodel.ecore").toURI().getPath().toString());
		model.setModelFile(getClass().getResource(modelName+".model").toURI().getPath().toString());
		module.getContext().getModelRepository().addModel(model);
		model.load();
		return model;
	}
}
