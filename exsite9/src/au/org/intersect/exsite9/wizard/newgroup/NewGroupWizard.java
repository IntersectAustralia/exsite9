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
import au.org.intersect.exsite9.service.IGroupService;

/**
 * A Wizard that can be used to create a new group.
 */
public final class NewGroupWizard extends Wizard
{
    private final NewGroupWizardPage1 page1;

    private final Group parentGroup;
    private Group newGroup;

    /**
     * Constructor
     * @param parentGroup The group that the new group will be a child of.
     */
    public NewGroupWizard(final Group parentGroup)
    {
        super();
        setNeedsProgressMonitor(true);
        this.parentGroup = parentGroup;
        this.page1 = new NewGroupWizardPage1(parentGroup);
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
        this.newGroup = groupService.createNewGroup(newGroupName);
        groupService.addChildGroup(this.parentGroup, this.newGroup);

        return true;
    }

    public Group getNewGroup()
    {
        return this.newGroup;
    }

}
