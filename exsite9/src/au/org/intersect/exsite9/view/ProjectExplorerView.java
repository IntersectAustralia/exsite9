/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

/**
 * This is the ViewPart that will hold the Project Explorer UI component.
 */
public final class ProjectExplorerView extends ViewPart
{
    // This needs to match what is defined in the plugin.xml
    public static final String ID = ProjectExplorerView.class.getName();

    /**
     * Constructor
     */
    public ProjectExplorerView()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createPartControl(final Composite parent)
    {
        this.setPartName("Project View");

        final Label label = new Label(parent, SWT.NONE);
        label.setText("This is the placeholder for the project explorer view");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus()
    {

    }

}
