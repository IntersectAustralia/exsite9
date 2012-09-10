package au.org.intersect.exsite9.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public final class ThumbnailView extends ViewPart implements ISelectionListener
{
    public static final String ID = ThumbnailView.class.getName();

    @Override
    public void createPartControl(Composite parent)
    {        


     // Listen for selection changes in the treeViewer
//        getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(ProjectExplorerView.ID, this);
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
