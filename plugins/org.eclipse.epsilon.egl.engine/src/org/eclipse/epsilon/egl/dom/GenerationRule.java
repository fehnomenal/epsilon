/*******************************************************************************
 * Copyright (c) 2012 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.egl.dom;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.common.util.AstUtil;
import org.eclipse.epsilon.egl.EglPersistentTemplate;
import org.eclipse.epsilon.egl.EglTemplate;
import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.execute.context.IEgxContext;
import org.eclipse.epsilon.egl.parse.EgxParser;
import org.eclipse.epsilon.eol.dom.ExecutableBlock;
import org.eclipse.epsilon.eol.dom.IExecutableModuleElement;
import org.eclipse.epsilon.eol.dom.IExecutableModuleElementParameter;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.ExecutorFactory;
import org.eclipse.epsilon.eol.execute.context.FrameStack;
import org.eclipse.epsilon.eol.execute.context.FrameType;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.parse.EolParser;
import org.eclipse.epsilon.eol.types.EolMap;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.epsilon.erl.dom.ExtensibleNamedRule;

public class GenerationRule extends ExtensibleNamedRule implements IExecutableModuleElementParameter, IExecutableModuleElement {
	
	protected Parameter sourceParameter;
	protected ExecutableBlock<Collection<?>> domainBlock;
	protected ExecutableBlock<String> targetBlock, templateBlock;
	protected ExecutableBlock<Boolean> guardBlock, overwriteBlock, mergeBlock;
	protected ExecutableBlock<Void> preBlock, postBlock;
	protected ExecutableBlock<EolMap<String, ?>> parametersBlock;
	
	@SuppressWarnings("unchecked")
	public void build(AST cst, IModule module) {
		super.build(cst, module);
		AST sourceParameterAst = cst.getFirstChild().getNextSibling();
		if (sourceParameterAst != null && sourceParameterAst.getType() == EolParser.FORMAL) {
			sourceParameter = (Parameter) module.createAst(sourceParameterAst, this);
		}
		
		templateBlock = (ExecutableBlock<String>) module.createAst(AstUtil.getChild(cst, EgxParser.TEMPLATE), this);
		guardBlock = (ExecutableBlock<Boolean>) module.createAst(AstUtil.getChild(cst, EgxParser.GUARD), this);
		targetBlock = (ExecutableBlock<String>) module.createAst(AstUtil.getChild(cst, EgxParser.TARGET), this);
		parametersBlock = (ExecutableBlock<EolMap<String, ?>>) module.createAst(AstUtil.getChild(cst, EgxParser.PARAMETERS), this);
		domainBlock = (ExecutableBlock<Collection<?>>) module.createAst(AstUtil.getChild(cst, EgxParser.DOMAIN), this);
		preBlock = (ExecutableBlock<Void>) module.createAst(AstUtil.getChild(cst, EgxParser.PRE), this);
		postBlock = (ExecutableBlock<Void>) module.createAst(AstUtil.getChild(cst, EgxParser.POST), this);
		overwriteBlock = (ExecutableBlock<Boolean>) module.createAst(AstUtil.getChild(cst, EgxParser.OVERWRITE), this);
		mergeBlock = (ExecutableBlock<Boolean>) module.createAst(AstUtil.getChild(cst, EgxParser.MERGE), this);
	}

	public Collection<?> getAllElements(IEgxContext context) throws EolRuntimeException {
		if (sourceParameter != null) {
			if (domainBlock == null) {
				return getAllInstances(sourceParameter, context, !isGreedy(context));
			}
			else {
				return domainBlock.execute(context, true);
			}
		}
		else {
			return Collections.singleton(null);
		}
	}
	
	@Override
	public Object execute(IEolContext context_, Object element) throws EolRuntimeException {
		IEgxContext context = (IEgxContext) context_;
		FrameStack frameStack = context.getFrameStack();
		
		if (sourceParameter != null) {
			frameStack.enterLocal(FrameType.PROTECTED, this, Variable.createReadOnlyVariable(sourceParameter.getName(), element));
		}
		else {
			frameStack.enterLocal(FrameType.PROTECTED, this);
		}
		
		if (guardBlock != null && !guardBlock.execute(context, false)) {
			frameStack.leaveLocal(this);
			return null;
		}
		
		if (preBlock != null) {
			preBlock.execute(context, false);
		}
		
		final boolean overwrite = (overwriteBlock == null) ? true : overwriteBlock.execute(context, false);
		final boolean merge = (mergeBlock == null) ? true : mergeBlock.execute(context, false);			
		final String templateName = (templateBlock == null) ? "" : templateBlock.execute(context, false);
		final EglTemplateFactory templateFactory = context.getTemplateFactory();
		final Map<URI, EglTemplate> templateCache = context.getTemplateCache();
		
		URI templateUri = templateFactory.resolveTemplate(templateName);
		EglTemplate eglTemplate;
		
		if (templateCache == null || (eglTemplate = templateCache.get(templateUri)) == null) {
			eglTemplate = templateFactory.load(templateUri);
			if (templateCache != null) {
				templateCache.put(templateUri, eglTemplate);
			}
		}

		if (sourceParameter != null) {
			eglTemplate.populate(sourceParameter.getName(), element);
		}
		
		if (parametersBlock != null) {
			for (Map.Entry<String, ?> entry : parametersBlock.execute(context, false).entrySet()) {
				eglTemplate.populate(entry.getKey(), entry.getValue());
			}
		}
		
		final String target = targetBlock != null ? targetBlock.execute(context, false) : "";
		
		File generated = null;
		if (eglTemplate instanceof EglPersistentTemplate) {
			generated = ((EglPersistentTemplate) eglTemplate).generate(target, overwrite, merge);
		}
		
		context.getInvokedTemplates().add(eglTemplate.getTemplate());
		
		if (postBlock != null) {
			context.getFrameStack().enterLocal(FrameType.UNPROTECTED, postBlock, Variable.createReadOnlyVariable("generated", generated));
			postBlock.execute(context, false);
			frameStack.leaveLocal(postBlock);
		}
		
		frameStack.leaveLocal(this);
		eglTemplate.reset();
		return null;
	}
	
	@Override
	public Object execute(IEolContext context) throws EolRuntimeException {
		ExecutorFactory executorFactory = context.getExecutorFactory();
		for (Object element : getAllElements((IEgxContext) context)) {
			executorFactory.execute(this, context, element);
		}
		return null;
	}

	/**
	 * Gets the model which the "transform" parameter type expression belongs to.
	 * @param context
	 * @return The model for the parameter.
	 * @throws EolRuntimeException
	 * @since 1.6
	 */
	public IModel getOwningModelForType(IEolContext context) throws EolRuntimeException {
		if (sourceParameter == null) return null;
		EolType parameterType = sourceParameter.getType(context);
		if (parameterType instanceof EolModelElementType) 
			return ((EolModelElementType)parameterType).getModel();
		else
			return null;
	}
	
	@Override
	public String toString() {
		String label = getName();
		if (sourceParameter != null) {
			label += " (" + sourceParameter.getTypeName() + ")";
		}
		return label;
	}

	@Override
	public AST getSuperRulesAst(AST cst) {
		return null;
	}
}
