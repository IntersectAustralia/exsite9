/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view;

import java.util.Set;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectManager;

/**
 * View component used for browsing Metadata.
 */
public final class MetadataBrowserView extends ViewPart implements IExecutionListener, SelectionListener
{
    public static final String ID = MetadataBrowserView.class.getName();

    private Composite parent;

    /**
     * 
     */
    public MetadataBrowserView()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void createPartControl(final Composite parent)
    {
        this.parent = parent;

        final ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);

        final Command newProjectCommand = commandService.getCommand("au.org.intersect.exsite9.commands.NewProjectCommand");
        newProjectCommand.addExecutionListener(this);

        final Command openProjectCommand = commandService.getCommand("au.org.intersect.exsite9.commands.OpenProjectCommand");
        openProjectCommand.addExecutionListener(this);
    }

    private void initLayout(final Set<MetadataCategory> metadataCategories)
    {
        final ExpandBar expandBar = new ExpandBar(this.parent, SWT.BORDER | SWT.V_SCROLL);
        expandBar.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

        for (final MetadataCategory metadataCategory : metadataCategories)
        {
            final Composite composite = new Composite(expandBar, SWT.NONE);
            composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

            final GridLayout gridLayout = new GridLayout(8, false);
            gridLayout.marginLeft = 10;
            gridLayout.marginRight = 10;
            gridLayout.marginTop = 10;
            gridLayout.marginBottom = 10;
            gridLayout.verticalSpacing = 15;
            composite.setLayout(gridLayout);

            for (final String metaDataValue : metadataCategory.getValues())
            {
                final Button button = new Button(composite, SWT.TOGGLE);
                button.setText(metaDataValue);
                button.addSelectionListener(this);
            }

            final ExpandItem expandItem = new ExpandItem(expandBar, SWT.NONE);
            expandItem.setText(metadataCategory.getName());
            expandItem.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
            expandItem.setControl(composite);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setFocus()
    {

    }

    @Override
    public void notHandled(final String commandId, final NotHandledException exception)
    {
        
    }

    @Override
    public void postExecuteFailure(final String commandId, final ExecutionException exception)
    {
        
    }

    @Override
    public void postExecuteSuccess(final String commandId, final Object returnValue)
    {
        if (commandId.equals("au.org.intersect.exsite9.commands.NewProjectCommand") ||
            commandId.equals("au.org.intersect.exsite9.commands.OpenProjectCommand"))
        {
            final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
            final Project project = projectManager.getCurrentProject();
            if (project != null)
            {
                initLayout(project.getMetadataCategories());
            }
        }
    }

    @Override
    public void preExecute(final String commandId, final ExecutionEvent event)
    {
        
    }

    @Override
    public void widgetSelected(final SelectionEvent e)
    {
        final Button button = (Button) e.widget;
        if (button.getSelection())
        {
            System.out.println(button.getText() + " Selected");
        }
        else
        {
            System.out.println(button.getText() + " Not Selected");
        }
    }

    @Override
    public void widgetDefaultSelected(final SelectionEvent e)
    {
        
    }
}
