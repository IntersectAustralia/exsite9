/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.view.listener;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;
import au.org.intersect.exsite9.service.IGroupService;

public class ProjectExplorerDropListener extends ViewerDropAdapter
{
    private TreeViewer treeViewer;
    
    public ProjectExplorerDropListener(TreeViewer treeViewer)
    {
        super(treeViewer);
        this.treeViewer = treeViewer;
    }

    @Override
    public void drop(DropTargetEvent event)
    {
        System.out.println("Drop");
        
        List<HierarchyMoveDTO> moveList = new ArrayList<HierarchyMoveDTO>(0);
        Object newParent =  determineTarget(event);
        ITreeSelection treeSelection = (ITreeSelection) LocalSelectionTransfer.getTransfer().getSelection();
        
        for(TreePath path : treeSelection.getPaths())
        {
            Object selection = path.getLastSegment();
            if ((selection instanceof Project) || (selection instanceof NewFilesGroup))
            {
                continue;
            }
            
            Object oldParent = path.getParentPath().getLastSegment();
            if (newParent == oldParent)
            {
                continue;
            }

            System.out.println("SELECTION= " + selection.toString());
            System.out.println("OLD PARENT= " + oldParent.toString());
            System.out.println("NEW PARENT= " + newParent.toString());
            
            moveList.add(new HierarchyMoveDTO(selection, oldParent, newParent));
        }
        
        if(! moveList.isEmpty())
        {
            final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
            groupService.performHierarchyMove(moveList);
            treeViewer.refresh();
        }
        
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
