/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.provider;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.Activator;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.SubmissionPackage;

/**
 * 
 */
public final class SubmissionPackageBrowserViewLabelProvider extends StyledCellLabelProvider
{

    public SubmissionPackageBrowserViewLabelProvider()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void update(final ViewerCell cell)
    {
        final Object element = cell.getElement();
        final StyledString text = new StyledString();

        if (element instanceof Project)
        {
            final Project project = (Project) element;
            text.append(project.getName());
            text.append(" (" + project.getSubmissionPackages().size() + ")", StyledString.COUNTER_STYLER);
            cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_HOME_NAV));
        }
        else if (element instanceof SubmissionPackage)
        {
            final SubmissionPackage submissionPackage = (SubmissionPackage) element;
            text.append(submissionPackage.getName());

            cell.setImage(Activator.getImageDescriptor("/icons/icon_package_16.png").createImage());
        }
        else
        {
            throw new IllegalArgumentException("Unknown type in submission package tree");
        }

        cell.setText(text.toString());
        cell.setStyleRanges(text.getStyleRanges());
        super.update(cell);
    }
}
