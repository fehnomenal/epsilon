/*******************************************************************************
 * Copyright (c) 2008 The University of York.
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
package org.eclipse.epsilon.hutn.xmi.test.integration.identifiers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.io.IOException;

import org.eclipse.epsilon.hutn.model.hutn.ReferenceSlot;
import org.eclipse.epsilon.hutn.xmi.test.integration.HutnXmiBridgeIntegrationTest;
import org.junit.BeforeClass;
import org.junit.Test;

public class IdentifiersBasedOnNames extends HutnXmiBridgeIntegrationTest {
	
	@BeforeClass
	public static void setup() throws IOException {
		integrationTestWithModelAsRoot("<contents xsi:type=\"families:Family\" xmi:id=\"_HFEnoCT9Ed6m9JDbGM4gGg\" pets=\"_JYTOgCT9Ed6m9JDbGM4gGg _HigxkCT9Ed6m9JDbGM4gGg _IgdnkCT9Ed6m9JDbGM4gGg\" name=\"The Smiths\"/>" + 
		                               "<contents xsi:type=\"families:Pet\" name=\"Goldie\" xmi:id=\"_HigxkCT9Ed6m9JDbGM4gGg\"/>" + 
		                               "<contents xsi:type=\"families:Pet\" xmi:id=\"_IgdnkCT9Ed6m9JDbGM4gGg\"/>" +
		                               "<contents xsi:type=\"families:Dog\" name=\"Lassie\" xmi:id=\"_JYTOgCT9Ed6m9JDbGM4gGg\"/>");	}
	
	@Test
	public void family() {
		assertEquals("The Smiths", getFamily().getIdentifier());
	}
	
	@Test
	public void firstPet() {
		assertEquals("Goldie", getFirstSlotOfModel().getValues().get(1).getIdentifier());
	}
		
	@Test
	public void secondPet() {
		assertEquals("Pet1", getFirstSlotOfModel().getValues().get(2).getIdentifier());
	}
		
	@Test
	public void dog() {
		assertEquals("Lassie", getFirstSlotOfModel().getValues().get(3).getIdentifier());
	}
	
	@Test
	public void slot() {
		assertThat(((ReferenceSlot)getFirstSlotOfFamily()).getValues(), containsInAnyOrder("Goldie", "Pet1", "Lassie"));
	}
}
