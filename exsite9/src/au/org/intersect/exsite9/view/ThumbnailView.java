package au.org.intersect.exsite9.view;

import java.io.File;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import au.org.intersect.exsite9.domain.ResearchFile;

public final class ThumbnailView extends ViewPart implements ISelectionListener
{
    public static final String ID = ThumbnailView.class.getName();
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
        image = imageUnavailable;
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
    public void selectionChanged(IWorkbenchPart arg0, ISelection selection)
    {
        final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

        @SuppressWarnings("unchecked")
        final List<Object> selectedObjects = structuredSelection.toList();

        if (selectedObjects.size() == 1 && selectedObjects.get(0) instanceof ResearchFile)
        {
            File file = ((ResearchFile) selectedObjects.get(0)).getFile();

            String[] fileNameSplitAtPeriods = file.getName().split("\\.");
            String fileExtension = fileNameSplitAtPeriods[fileNameSplitAtPeriods.length - 1].trim();
            if (file.exists()
                    && (fileExtension.equalsIgnoreCase("GIF") || fileExtension.equalsIgnoreCase("BMP")
                            || fileExtension.equalsIgnoreCase("JPG") || fileExtension.equalsIgnoreCase("JPEG")
                            || fileExtension.equalsIgnoreCase("PNG") || fileExtension.equalsIgnoreCase("TIFF")))
            {
                image = new Image(parent.getDisplay(), file.getAbsolutePath());
                createScaledImage();
            }
            else
            {
                image = imageUnavailable;
                createScaledImage();
            }
        }
        else
        {
            image = imageUnavailable;
            createScaledImage();
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
            optimumWidth = parentWindowWidth;

        }
        else if (originalImageRatio > 1)
        { // meaning height is greater than width

            optimumHeight = parentWindowHeight;
            optimumWidth = (int) (parentWindowHeight / originalImageRatio);
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
