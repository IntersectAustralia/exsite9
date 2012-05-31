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

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.view.ProjectExplorerView;

/**
 * Provides labels to {@link ProjectExplorerView}
 */
public final class ProjectExplorerViewLabelProvider extends StyledCellLabelProvider
{
    /**
     * Constructor
     */
    public ProjectExplorerViewLabelProvider()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void update(final ViewerCell cell)
    {
        Object element = cell.getElement();
        final StyledString text = new StyledString();

        if (element instanceof Project)
        {
            final Project project = (Project) element;
            text.append(project.getName());
            cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_HOME_NAV));
        }
        else if (element instanceof Group)
        {
            final Group group = (Group) element;
            text.append(group.getName());
            cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
            text.append(" (" + (group.getGroups().size() + group.getResearchFiles().size()) + ")", StyledString.COUNTER_STYLER);
        }
        else if (element instanceof ResearchFile)
        {
            final ResearchFile rf = (ResearchFile) element;
            text.append(rf.getName());
            cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
        }
        else
        {
            throw new IllegalArgumentException("Unknown type in project explorer tree");
        }

        cell.setText(text.toString());
        cell.setStyleRanges(text.getStyleRanges());
        super.update(cell);
    }
}