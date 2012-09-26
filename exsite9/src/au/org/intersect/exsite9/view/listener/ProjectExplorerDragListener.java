/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.view.listener;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;

/**
 * Custom listener for listening for drag events on the tree viewer of the project view
 */
public class ProjectExplorerDragListener implements DragSourceListener
{
    private final TreeViewer treeViewer;
    
    public ProjectExplorerDragListener(TreeViewer treeViewer)
    {
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void dragStart(DragSourceEvent event)
    {
        // If we have a single selection we can cancel the drag/drop if the selection is
        // the project group or the new files group
        if(((ITreeSelection)treeViewer.getSelection()).size() == 1)
        {
            Object obj = ((ITreeSelection)treeViewer.getSelection()).toList().get(0);
            if ((obj instanceof NewFilesGroup) || (obj instanceof Project))
            {
                event.doit = false;
            }
        }
    }

    @Override
    public void dragSetData(DragSourceEvent event)
    {
        LocalSelectionTransfer.getTransfer().setSelection(treeViewer.getSelection());
    }

    @Override
    public void dragFinished(DragSourceEvent event)
    {
    }
}
