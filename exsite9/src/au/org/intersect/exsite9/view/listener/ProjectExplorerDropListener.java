/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.view.listener;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import au.org.intersect.exsite9.domain.ResearchFile;

public class ProjectExplorerDropListener extends ViewerDropAdapter
{

    public ProjectExplorerDropListener(TreeViewer treeViewer)
    {
        super(treeViewer);
    }

    @Override
    public void drop(DropTargetEvent event)
    {
        System.out.println("Drop");
        
        Object target =  determineTarget(event);
        Object selection = LocalSelectionTransfer.getTransfer().getSelection();
        
        System.out.println("SELECTION= " + selection.toString());
        System.out.println("TARGET= " + target.toString());
            
        super.drop(event);
    }
    
    @Override
    public boolean performDrop(Object data)
    {
        System.out.println("Perform drop");
        return false;
    }

    @Override
    public boolean validateDrop(Object target, int operation, TransferData transferType)
    {
        if (target instanceof ResearchFile)
        {
            System.out.println("Invalid drop");
            return false;
        }
        return true;
    }

}
