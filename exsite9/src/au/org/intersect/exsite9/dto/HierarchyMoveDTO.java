/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dto;

public final class HierarchyMoveDTO
{
    private final Object child;
    private final Object oldParent;
    private final Object newParent;
    
    public HierarchyMoveDTO(Object selection, Object oldParent, Object newParent)
    {
        this.child = selection;
        this.oldParent = oldParent;
        this.newParent = newParent;
    }

    public Object getChild()
    {
        return child;
    }

    public Object getOldParent()
    {
        return oldParent;
    }

    public Object getNewParent()
    {
        return newParent;
    }
    
}
