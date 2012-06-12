/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.utils.AlphabeticalMetadataCategoryComparator;
import au.org.intersect.exsite9.service.IProjectManager;

/**
 * View component used for browsing Metadata.
 */
public final class MetadataBrowserView extends ViewPart implements IExecutionListener, SelectionListener
{
    public static final String ID = MetadataBrowserView.class.getName();

    private ExpandBar expandBar;
    private Composite parent;
    private RowData rowData;

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
        final ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);

        final Command newProjectCommand = commandService.getCommand("au.org.intersect.exsite9.commands.NewProjectCommand");
        newProjectCommand.addExecutionListener(this);

        final Command openProjectCommand = commandService.getCommand("au.org.intersect.exsite9.commands.OpenProjectCommand");
        openProjectCommand.addExecutionListener(this);
        
        final Command addMetadataCategoryCommand = commandService.getCommand("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand");
        addMetadataCategoryCommand.addExecutionListener(this);

        this.parent = parent;

        // So wrapping of the buttons in the rows will work
        this.parent.addControlListener(new ControlAdapter()
        {
            @Override
            public void controlResized(final ControlEvent e)
            {
                super.controlResized(e);
                if (rowData != null)
                {
                    rowData.width = parent.getClientArea().width;
                }
            }
        });
    }

    private void initLayout(final List<MetadataCategory> metadataCategories)
    {
        if (metadataCategories.isEmpty())
        {
            return;
        }

        if (this.expandBar != null)
        {
            this.expandBar.dispose();
        }
        this.expandBar = new ExpandBar(this.parent, SWT.BORDER | SWT.V_SCROLL);

        final List<MetadataCategory> sorted = new ArrayList<MetadataCategory>(metadataCategories);
        Collections.sort(sorted, new AlphabeticalMetadataCategoryComparator());

        for (final MetadataCategory metadataCategory : sorted)
        {
            final Composite expandBarComposite = new Composite(expandBar, SWT.NONE);
            final RowLayout expandBarLayout = new RowLayout(SWT.VERTICAL);
            expandBarLayout.wrap = true;
            expandBarLayout.pack = true;
            expandBarLayout.justify = false;
            expandBarComposite.setLayout(expandBarLayout);

            final Composite headerComposite = new Composite(expandBarComposite, SWT.NONE);
            final Composite buttonComposite = new Composite(expandBarComposite, SWT.NONE);

            final ExpandItem expandItem = new ExpandItem(this.expandBar, SWT.NONE);
            expandItem.setText(metadataCategory.getName());
            expandItem.setControl(expandBarComposite);
            expandItem.setHeight(expandBarComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);

            final ToolBar toolBar = new ToolBar(headerComposite, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
            new ToolItem(toolBar, SWT.SEPARATOR);
            final ToolItem editButtonItem = new ToolItem(toolBar, SWT.NULL);
            final Image editImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_SYNCED);
            editButtonItem.setImage(editImage);
            editButtonItem.addSelectionListener(new EditMetadataCategorySelectionListener(metadataCategory));
            new ToolItem(toolBar, SWT.SEPARATOR);
            toolBar.pack();

            this.rowData = new RowData();
            this.rowData.width = parent.getClientArea().width;
            buttonComposite.setLayoutData(rowData);

            final RowLayout buttonLayout = new RowLayout(SWT.HORIZONTAL);
            buttonLayout.marginLeft = 10;
            buttonLayout.marginRight = 10;
            buttonLayout.marginTop = 10;
            buttonLayout.marginBottom = 10;
            buttonLayout.wrap = true;
            buttonLayout.pack = true;
            buttonLayout.justify = false;
            buttonComposite.setLayout(buttonLayout);

            for (final String metaDataValue : metadataCategory.getValues())
            {
                final Button button = new Button(buttonComposite, SWT.TOGGLE);
                button.setText(metaDataValue);
                button.addSelectionListener(this);
            }

            buttonComposite.pack();
            buttonComposite.layout(true);
        }

        this.expandBar.pack();
        this.expandBar.layout();

        this.parent.layout();
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
            commandId.equals("au.org.intersect.exsite9.commands.OpenProjectCommand") ||
            commandId.equals("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand"))
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

    private static final class EditMetadataCategorySelectionListener implements SelectionListener
    {
        private final MetadataCategory metadataCategory;

        public EditMetadataCategorySelectionListener(final MetadataCategory metadataCategory)
        {
            this.metadataCategory = metadataCategory;
        }

        @Override
        public void widgetSelected(final SelectionEvent e)
        {
            System.out.println("Edit clicked for " + metadataCategory);
        }

        @Override
        public void widgetDefaultSelected(final SelectionEvent e)
        {
        }
    }
}
