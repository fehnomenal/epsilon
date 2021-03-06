/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.eugenia.examples.flowchart.diagram.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;

public class LayoutUtil {
	
	public static void moveToCenter(WrappingLabel label, Figure parent, Graphics graphics) {
		Rectangle r = parent.getBounds();
		Rectangle newLabelBounds = new Rectangle();
		Point middle = new Point(r.x + r.width/2, r.y + r.height/2); 
		
		int textWidth = (label.getText().length() + 2) * graphics.getFontMetrics().getAverageCharWidth();
		int textHeight = graphics.getFontMetrics().getHeight();
		
		newLabelBounds.x = middle.x - textWidth/2;
		newLabelBounds.y = middle.y - textHeight/2;
		newLabelBounds.width = textWidth;
		newLabelBounds.height = textHeight;
		
		label.setBounds(newLabelBounds);
	}
	
}
