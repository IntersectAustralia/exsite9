/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import au.org.intersect.exsite9.domain.File;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.view.provider.ProjectExplorerViewConentProvider;
import au.org.intersect.exsite9.view.provider.ProjectExplorerViewInput;
import au.org.intersect.exsite9.view.provider.ProjectExplorerViewLabelProvider;

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

        final TreeViewer treeViewer = new TreeViewer(parent);
        treeViewer.setContentProvider(new ProjectExplorerViewConentProvider());
        treeViewer.setLabelProvider(new ProjectExplorerViewLabelProvider());

        // Provide some mock stuff.
        final Project project = new Project("My Project");
        final Group group1 = new Group("Group 1");
        final Group group2 = new Group("Group 2");
        group1.getFiles().add(new File("File 1"));
        group1.getFiles().add(new File("File 2"));
        project.getGroups().add(group1);
        project.getGroups().add(group2);
        final ProjectExplorerViewInput wrapper = new ProjectExplorerViewInput(project);
        treeViewer.setInput(wrapper);

        treeViewer.expandAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus()
    {

    }

}
