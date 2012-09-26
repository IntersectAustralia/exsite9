/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Creates the contents of the thumbnail tab and determines what actions are taken when selections are made
 */
public final class ThumbnailView extends ViewPart implements ISelectionListener
{
    public static final String ID = ThumbnailView.class.getName();
    private static final Logger LOG = Logger.getLogger(ThumbnailView.class);
    private Composite parent;
    private Label imgLabel;
    private Image image;
    private Image imageUnavailable;

    @Override
    public void createPartControl(final Composite parent)
    {
        this.parent = parent;
        FillLayout layout = new FillLayout();
        parent.setLayout(layout);

        imageUnavailable = new Image(parent.getDisplay(), getClass().getResourceAsStream(
                "/icons/ThumbnailUnavailable.JPG"));

        final ProjectExplorerView projectExplorerView = (ProjectExplorerView) ViewUtils.getViewByID(PlatformUI
                .getWorkbench().getActiveWorkbenchWindow(), ProjectExplorerView.ID);
        projectExplorerView.refresh();
        final ISelection currentSelection = projectExplorerView.getSelection();

        createImageFromTreeSelection(currentSelection);

        imgLabel = new Label(parent, SWT.NONE);
        imgLabel.setImage(image);
        imgLabel.setLayoutData(layout);

        parent.addControlListener(new ControlAdapter()
        {
            @Override
            public void controlResized(ControlEvent e)
            {
                super.controlResized(e);

                createScaledImage();
            }
        });

        // Listen for selection changes in the treeViewer
        getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(ProjectExplorerView.ID, this);
    }

    @Override
    public void setFocus()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectionChanged(IWorkbenchPart view, ISelection selection)
    {
        createImageFromTreeSelection(selection);
        createScaledImage();
    }

    private void createImageFromTreeSelection(ISelection selection)
    {
        if (!(selection instanceof IStructuredSelection))
        {
            LOG.error("Unknown selection type in project view");
            return;
        }

        final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

        @SuppressWarnings("unchecked")
        final List<Object> selectedObjects = structuredSelection.toList();

        if (selectedObjects.size() == 1 && selectedObjects.get(0) instanceof ResearchFile)
        {
            File file = ((ResearchFile) selectedObjects.get(0)).getFile();

            if (file.exists())
            {
                try
                {
                    image = new Image(parent.getDisplay(), file.getAbsolutePath());
                }
                catch (SWTException e)
                {
                    image = imageUnavailable;
                }
            }
            else
            {
                image = imageUnavailable;
            }

        }
        else
        {
            image = imageUnavailable;
        }
    }

    private void createScaledImage()
    {
        int parentWindowWidth = parent.getClientArea().width;
        int parentWindowHeight = parent.getClientArea().height;
        double originalImageWidth = (double) (this.image.getBounds().width);
        double originalImageHeight = (double) (this.image.getBounds().height);
        int optimumWidth;
        int optimumHeight;
        double originalImageRatio = originalImageHeight / originalImageWidth;

        if (originalImageRatio < 1)
        { // meaning height is less than width

            optimumHeight = (int) (parentWindowWidth * originalImageRatio);

            if (optimumHeight > parentWindowHeight)
            {
                optimumHeight = parentWindowHeight;
                optimumWidth = (int) (parentWindowHeight / originalImageRatio);
            }
            else
            {
                optimumWidth = parentWindowWidth;
            }

        }
        else if (originalImageRatio > 1)
        { // meaning height is greater than width

            optimumWidth = (int) (parentWindowHeight / originalImageRatio);

            if (optimumWidth > parentWindowWidth)
            {
                optimumWidth = parentWindowWidth;
                optimumHeight = (int) (parentWindowWidth * originalImageRatio);
            }
            else
            {
                optimumHeight = parentWindowHeight;
            }
        }
        else
        {
            optimumHeight = parentWindowHeight > parentWindowWidth ? parentWindowWidth : parentWindowHeight;
            optimumWidth = parentWindowWidth > parentWindowHeight ? parentWindowHeight : parentWindowWidth;
        }

        this.imgLabel.setImage(new Image(this.parent.getDisplay(), this.image.getImageData().scaledTo(optimumWidth,
                optimumHeight)));
    }

}
