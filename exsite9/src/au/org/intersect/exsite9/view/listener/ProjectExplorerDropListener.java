/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.view.listener;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;
import au.org.intersect.exsite9.service.IGroupService;

/**
 * Custom listener for listening for drop events on the tree viewer of the project view
 */
public class ProjectExplorerDropListener extends ViewerDropAdapter
{
    private TreeViewer treeViewer;
    
    public ProjectExplorerDropListener(TreeViewer treeViewer)
    {
        super(treeViewer);
        this.treeViewer = treeViewer;
    }

    @Override
    /**
     * This will move the selected item(s) (groups or files) from their parent group
     * and onto the target group.
     * 
     * We will not drop selected items where:
     * 
     * 1. The selected item is the Project Group. (The Project Group can't be moved.)
     * 2. The selected item is the New Files Group. (The New Files Group can't be moved.) 
     * 3. The selected item is the target group. (ie drop onto itself)
     * 4. The selected item's parent group is the target group. (ie drop it where it already is)
     * 5. The selected item is a group or file and the target group is the New Files Group. (We can't move stuff into the New Files Group).
     * 6. The target group is a descendant of the selected item. (ie cause a loop in the hierarchy.)
     * 
     */
    public void drop(DropTargetEvent event)
    {
        List<HierarchyMoveDTO> moveList = new ArrayList<HierarchyMoveDTO>(0);
        Object newParent =  determineTarget(event);
        ITreeSelection treeSelection = (ITreeSelection) LocalSelectionTransfer.getTransfer().getSelection();
        
        for(TreePath path : treeSelection.getPaths())
        {
            Object selectedItem = path.getLastSegment();
            if ((selectedItem instanceof Project) || (selectedItem instanceof NewFilesGroup) || (selectedItem == newParent))
            {
                continue;
            }
            
            Object oldParent = path.getParentPath().getLastSegment();
            if (newParent == oldParent)
            {
                continue;
            }
            
            if (newParent instanceof NewFilesGroup)
            {
                continue;
            }

            if (newParent == null)
            {
                continue;
            }
            
            if ((selectedItem instanceof Group) && 
                (newParent instanceof Group) &&
                ((Group)selectedItem).isAnAncestorOf((Group)newParent))
            {
                continue;
            }

            moveList.add(new HierarchyMoveDTO(selectedItem, oldParent, newParent));
        }

        if(!moveList.isEmpty())
        {
            final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
            final String error = groupService.performHierarchyMove(moveList);
            if (error != null)
            {
                MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Could not complete move operation", error);
            }
            treeViewer.refresh();
        }
    }
    
    @Override
    public boolean performDrop(Object data)
    {
        return false;
    }

    @Override
    public boolean validateDrop(Object target, int operation, TransferData transferType)
    {
        if (target instanceof ResearchFile || target instanceof NewFilesGroup || target == null)
        {
            return false;
        }
        return true;
    }

}
