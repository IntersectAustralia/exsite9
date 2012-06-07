/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.view.listener;

import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;

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
        System.out.println("Drag Start");
        
        @SuppressWarnings("unchecked")
        List<Object> selection = ((ITreeSelection)treeViewer.getSelection()).toList();
        
        if (selection.size() == 1){
            Object obj = selection.get(0);
            if ((obj instanceof NewFilesGroup) || (obj instanceof Project))
            {
                event.doit = false;
                System.out.println("Drag cancelled");
            }
        }
    }

    @Override
    public void dragSetData(DragSourceEvent event)
    {
        System.out.println("Drag Set Data");
        LocalSelectionTransfer.getTransfer().setSelection(treeViewer.getSelection());
    }

    @Override
    public void dragFinished(DragSourceEvent event)
    {
        System.out.println("Drag Finished.");
    }

}
