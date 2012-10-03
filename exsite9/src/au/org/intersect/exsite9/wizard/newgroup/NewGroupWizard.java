/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.newgroup;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IGroupService;

/**
 * A Wizard that can be used to create a new group.
 */
public final class NewGroupWizard extends Wizard
{
    private final NewGroupWizardPage1 page1;

    private final Group parentGroup;
    private Group newGroup;
    private final Project project;

    /**
     * Constructor
     * @param parentGroup The group that the new group will be a child of.
     * @param currentProject 
     */
    public NewGroupWizard(final Group parentGroup, Project currentProject)
    {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("New Group");
        this.parentGroup = parentGroup;
        this.page1 = new NewGroupWizardPage1(parentGroup);
        this.project = currentProject;
    }

    @Override
    public void addPages()
    {
        addPage(this.page1);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean performFinish()
    {
        final String newGroupName = this.page1.getNewGroupName();
        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        final Group theNewGroup = groupService.createNewGroup(newGroupName, this.project);
        groupService.addChildGroup(this.parentGroup, theNewGroup);
        this.newGroup = groupService.findGroupByID(theNewGroup.getId());

        return true;
    }

    public Group getNewGroup()
    {
        return this.newGroup;
    }

}
