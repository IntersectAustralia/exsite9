/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.provider;

import java.io.File;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.Activator;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.view.ProjectExplorerView;

/**
 * Provides labels to {@link ProjectExplorerView}
 */
public final class ProjectExplorerViewLabelProvider extends StyledCellLabelProvider
{
    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * Constructor
     */
    public ProjectExplorerViewLabelProvider()
    {
    }

    /**
     * @{inheritDoc
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
            cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_HOME_NAV));
        }
        else if (element instanceof Group)
        {
            final Group group = (Group) element;
            if (group instanceof NewFilesGroup)
            {
                text.append(group.getName(), StyledString.QUALIFIER_STYLER);
            }
            else
            {
                text.append(group.getName());
            }
            cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
            text.append(" (" + (group.getGroups().size() + group.getResearchFiles().size()) + ")",
                    StyledString.COUNTER_STYLER);
        }
        else if (element instanceof ResearchFile)
        {
            final ResearchFile rf = (ResearchFile) element;
            final File file = rf.getFile();
            text.append(file.getName());

            if (!file.exists())
            {
                cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));
            }
            else if (rf.getMetadataAssociations().isEmpty())
            {
                cell.setImage(Activator.getImageDescriptor("/icons/icon_warning_12.png").createImage());
            }
            else if (rf.isMissingRequiredMetadata())
            {
                cell.setImage(Activator.getImageDescriptor("/icons/icon_warning_12.png").createImage());
            }
            else
            {
                cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
            }
        }
        else
        {
            throw new IllegalArgumentException("Unknown type in project explorer tree");
        }

        cell.setText(text.toString());
        cell.setStyleRanges(text.getStyleRanges());
        super.update(cell);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public String getToolTipText(final Object element)
    {
        if (!(element instanceof ResearchFile))
        {
            return null;
        }

        final ResearchFile rf = (ResearchFile) element;
        final File file = rf.getFile();

        final StringBuilder sb = new StringBuilder();

        if (!file.exists())
        {
            sb.append("THIS FILE DOES NOT EXIST").append(NEW_LINE);
        }
        if (rf.getMetadataAssociations().isEmpty())
        {
            sb.append("THIS FILE HAS NO METADATA ASSOCIATIONS").append(NEW_LINE);
        }
        else if (rf.isMissingRequiredMetadata())
        {
            sb.append("THIS FILE IS MISSING REQUIRED METADATA").append(NEW_LINE);
        }
        sb.append(rf.getFile().getAbsolutePath());
        return sb.toString();
    }

    /**
     * @{inheritDoc
     */
    @Override
    public int getToolTipTimeDisplayed(final Object object)
    {
        return 10 * 1000;
    }

    /**
     * @{inheritDoc
     */
    @Override
    public int getToolTipDisplayDelayTime(final Object object)
    {
        return 200;
    }

    /**
     * @{inheritDoc
     */
    public Point getToolTipShift(final Object object)
    {
        return new Point(5, 5);
    }
}