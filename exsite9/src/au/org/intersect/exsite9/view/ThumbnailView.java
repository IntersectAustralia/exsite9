package au.org.intersect.exsite9.view;

import org.eclipse.jface.viewers.ISelection;
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

public final class ThumbnailView extends ViewPart implements ISelectionListener
{
    public static final String ID = ThumbnailView.class.getName();

    @Override
    public void createPartControl(final Composite parent)
    {    
        FillLayout layout = new FillLayout();
        parent.setLayout(layout);
        
        final Image img = new Image(parent.getDisplay(), "/Users/jake/Desktop/some images/BBC - BBC Radio 1 Programmes - Gilles Peterson, Gilles' Final Show On Radio 1_1334192800211.png");
       
        final Label imgLabel = new Label(parent, SWT.NONE);
        imgLabel.setImage(img);
        imgLabel.setLayoutData(layout);
        
        parent.addControlListener(new ControlAdapter()
        {
            @Override
            public void controlResized(ControlEvent e)
            {
                super.controlResized(e);
                //TODO: resize image.
                int width2 = parent.getClientArea().width;
                int height2 = parent.getClientArea().height;
                imgLabel.setImage(new Image(parent.getDisplay(), img.getImageData().scaledTo(width2, height2)));
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
    public void selectionChanged(IWorkbenchPart arg0, ISelection arg1)
    {
        // TODO Auto-generated method stub
        
    }

}
