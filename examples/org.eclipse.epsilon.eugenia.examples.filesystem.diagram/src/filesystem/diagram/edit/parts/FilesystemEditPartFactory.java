/*
 * 
 */
package filesystem.diagram.edit.parts;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.directedit.locator.CellEditorLocatorAccess;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import filesystem.diagram.part.FilesystemVisualIDRegistry;

/**
 * @generated
 */
public class FilesystemEditPartFactory implements EditPartFactory {

	/**
	 * @generated
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof View) {
			View view = (View) model;
			switch (FilesystemVisualIDRegistry.getVisualID(view)) {

			case FilesystemEditPart.VISUAL_ID:
				return new FilesystemEditPart(view);

			case DriveEditPart.VISUAL_ID:
				return new DriveEditPart(view);

			case DriveNameEditPart.VISUAL_ID:
				return new DriveNameEditPart(view);

			case Drive2EditPart.VISUAL_ID:
				return new Drive2EditPart(view);

			case DriveName2EditPart.VISUAL_ID:
				return new DriveName2EditPart(view);

			case FolderEditPart.VISUAL_ID:
				return new FolderEditPart(view);

			case FolderNameEditPart.VISUAL_ID:
				return new FolderNameEditPart(view);

			case ShortcutEditPart.VISUAL_ID:
				return new ShortcutEditPart(view);

			case ShortcutNameEditPart.VISUAL_ID:
				return new ShortcutNameEditPart(view);

			case FileEditPart.VISUAL_ID:
				return new FileEditPart(view);

			case FileNameEditPart.VISUAL_ID:
				return new FileNameEditPart(view);

			case DriveDriveContentsCompartmentEditPart.VISUAL_ID:
				return new DriveDriveContentsCompartmentEditPart(view);

			case DriveDriveContentsCompartment2EditPart.VISUAL_ID:
				return new DriveDriveContentsCompartment2EditPart(view);

			case FolderFolderContentsCompartmentEditPart.VISUAL_ID:
				return new FolderFolderContentsCompartmentEditPart(view);

			case SyncEditPart.VISUAL_ID:
				return new SyncEditPart(view);

			case SyncLastSyncEditPart.VISUAL_ID:
				return new SyncLastSyncEditPart(view);

			case ShortcutTargetEditPart.VISUAL_ID:
				return new ShortcutTargetEditPart(view);

			case WrappingLabelEditPart.VISUAL_ID:
				return new WrappingLabelEditPart(view);

			}
		}
		return createUnrecognizedEditPart(context, model);
	}

	/**
	 * @generated
	 */
	private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
		// Handle creation of unrecognized child node EditParts here
		return null;
	}

	/**
	 * @generated
	 */
	public static CellEditorLocator getTextCellEditorLocator(
			ITextAwareEditPart source) {
		return CellEditorLocatorAccess.INSTANCE
				.getTextCellEditorLocator(source);
	}
}
