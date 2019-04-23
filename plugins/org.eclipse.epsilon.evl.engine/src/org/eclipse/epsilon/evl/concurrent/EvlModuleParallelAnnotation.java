/*********************************************************************
 * Copyright (c) 2018 The University of York.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.evl.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.erl.concurrent.IErlModuleParallelAnnotation;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.dom.ConstraintContext;
import org.eclipse.epsilon.evl.dom.GlobalConstraintContext;
import org.eclipse.epsilon.evl.execute.context.concurrent.IEvlContextParallel;

/**
 * Allows the user to mix and match sequential and parallel execution for
 * {@linkplain Constraint}s and {@linkplain ConstraintContext}s using the
 * <code>@parallel</code> annotation where desired.
 * 
 * @author Sina Madani
 * @since 1.6
 */
public class EvlModuleParallelAnnotation extends EvlModuleParallel implements IErlModuleParallelAnnotation {

	public EvlModuleParallelAnnotation() {
		super();
	}

	public EvlModuleParallelAnnotation(int parallelism) {
		super(parallelism);
	}
	
	@Override
	protected void checkConstraints() throws EolRuntimeException {
		final IEvlContextParallel context = getContext();
		
		for (ConstraintContext constraintContext : getConstraintContexts()) {
			final Collection<Constraint> constraintsToCheck = preProcessConstraintContext(constraintContext);
			final Collection<?> allOfKind = constraintContext.getAllOfSourceKind(context);
			final int numElements = allOfKind.size();
			final IModel model = constraintContext instanceof GlobalConstraintContext ?
				null : constraintContext.getType(context).getModel();
			
			if (constraintContext.hasAnnotation(PARALLEL_ANNOTATION_NAME)) {
				ArrayList<Runnable> jobs = new ArrayList<>();
				
				for (Object object : allOfKind) {
					if (shouldBeParallel(constraintContext, object, model, numElements)) {
						jobs.add(() -> {
							try {
								constraintContext.execute(constraintsToCheck, object, context);
							}
							catch (EolRuntimeException exception) {
								context.handleException(exception);
							}
						});
					}
					else {
						constraintContext.execute(constraintsToCheck, object, context);
					}
				}
				
				context.executeParallel(constraintContext, jobs);
			}		
			else {
				for (Constraint constraint : constraintsToCheck) {
					ArrayList<Runnable> jobs = new ArrayList<>();
					
					for (Object object : allOfKind) {
						if (constraintContext.appliesTo(object, context, false)) {
							if (shouldBeParallel(constraint, object, model, numElements)) {
								jobs.add(() -> {
									try {
										constraint.execute(object, context);
									}
									catch (EolRuntimeException exception) {
										context.handleException(exception);
									}
								});
							}
							else {
								constraint.execute(object, context);
							}
						}
					}
					
					context.executeParallel(constraint, jobs);
				}
			}
		}
	}
	
}
