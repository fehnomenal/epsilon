package org.eclipse.epsilon.eol.dt.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.common.dt.editor.AbstractModuleEditor;
import org.eclipse.epsilon.common.dt.editor.IModuleParseListener;
import org.eclipse.epsilon.common.dt.editor.contentassist.IAbstractModuleEditorTemplateContributor;
import org.eclipse.epsilon.common.dt.editor.contentassist.TemplateWithImage;
import org.eclipse.epsilon.commons.module.IModule;
import org.eclipse.epsilon.commons.util.ListBuilder;
import org.eclipse.epsilon.eol.dt.EolPlugin;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.swt.graphics.Image;

public class EolEditorBuiltinTypesTemplateContributor implements IAbstractModuleEditorTemplateContributor, IModuleParseListener {

	protected List<Template> templates = null;
	protected AbstractModuleEditor editor = null;
	
	Image typeImage = EolPlugin.getDefault().createImage("icons/type.png");
	
	public List<Template> getTemplates() {
		
		if (editor == null) return new ListBuilder<Template>().build();
		
		if (templates == null) {
			templates = new ArrayList<Template>();
			for (String type : editor.getTypes()) {
				templates.add(new TemplateWithImage(type, "built-in type", "", type, false, typeImage));
			}
		}
		return templates;
	}

	public void moduleParsed(AbstractModuleEditor editor, IModule module) {
		this.editor = editor;
	}

}