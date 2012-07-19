/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import au.org.intersect.exsite9.Activator;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.IMetadataAssignable;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.RootGroup;
import au.org.intersect.exsite9.domain.utils.AlphabeticalMetadataCategoryComparator;
import au.org.intersect.exsite9.domain.utils.AlphabeticalMetadataValueComparator;
import au.org.intersect.exsite9.domain.utils.MetadataAssignableUtils;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.util.Pair;
import au.org.intersect.exsite9.view.widgets.MetadataButtonWidget;

/**
 * View component used for browsing Metadata.
 */
public final class MetadataBrowserView extends ViewPart implements IExecutionListener, SelectionListener,
        ISelectionListener
{
    public static final String ID = MetadataBrowserView.class.getName();

    private static final Logger LOG = Logger.getLogger(MetadataBrowserView.class);

    private ExpandBar expandBar;
    private Composite parent;
    private Composite placeholder;
    private RowData rowData;

    private final IGroupService groupService;
    private final IResearchFileService researchFileService;

    /**
     * The list of currently selected (if any) groups on the RHS.
     */
    private final List<IMetadataAssignable> selectedMetadataAssignables = new ArrayList<IMetadataAssignable>();

    /**
     * The metadata buttons that are currently shown on the page - keyed by metadata category for easy lookup.
     */
    private final Map<Pair<MetadataCategory, MetadataValue>, MetadataButtonWidget> metadataButtons = new HashMap<Pair<MetadataCategory, MetadataValue>, MetadataButtonWidget>();

    /**
     * 
     */
    public MetadataBrowserView()
    {
        this.groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        this.researchFileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void createPartControl(final Composite parent)
    {
        final ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(
                ICommandService.class);

        final Command newProjectCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.NewProjectCommand");
        newProjectCommand.addExecutionListener(this);

        final Command openProjectCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.OpenProjectCommand");
        openProjectCommand.addExecutionListener(this);

        final Command addMetadataCategoryCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand");
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

        // So we can listen for selections in the project tree view
        getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(ProjectExplorerView.ID, this);
    }

    private void initLayout(final List<MetadataCategory> metadataCategories)
    {
        if (this.expandBar != null)
        {
            this.expandBar.dispose();
        }

        if (this.placeholder != null)
        {
            this.placeholder.dispose();
        }

        this.metadataButtons.clear();
        if (metadataCategories.isEmpty())
        {
            this.placeholder = new Composite(this.parent, SWT.BORDER);
            return;
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
            expandBarComposite.addControlListener(new ControlAdapter()
            {
                @Override
                public void controlResized(ControlEvent e)
                {
                    super.controlResized(e);
                    expandItem.setHeight(expandBarComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
                }
            });

            final ToolBar toolBar = new ToolBar(headerComposite, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
            new ToolItem(toolBar, SWT.SEPARATOR);
            final ToolItem editButtonItem = new ToolItem(toolBar, SWT.NULL);

            final Image editImage = Activator.getImageDescriptor("/icons/icon_metadata_16.png").createImage();

            // This is how you gain access to Eclipse's built-in icons
            // final Image editImage =
            // PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_SYNCED);

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

            // Sort the metadata values.
            final List<MetadataValue> sortedMetadataValues = new ArrayList<MetadataValue>(metadataCategory.getValues());
            Collections.sort(sortedMetadataValues, new AlphabeticalMetadataValueComparator());

            for (final MetadataValue metadataValue : sortedMetadataValues)
            {
                final MetadataButtonWidget mdbw = new MetadataButtonWidget(buttonComposite, SWT.TOGGLE,
                        metadataCategory, metadataValue);
                mdbw.setText(metadataValue.getValue());
                mdbw.addSelectionListener(this);
                final Pair<MetadataCategory, MetadataValue> pair = new Pair<MetadataCategory, MetadataValue>(
                        metadataCategory, metadataValue);
                this.metadataButtons.put(pair, mdbw);
            }

            buttonComposite.pack();
            buttonComposite.layout(true);
            expandItem.setExpanded(true);
        }

        this.expandBar.pack();
        this.expandBar.layout();

        this.parent.layout();
    }

    /**
     * @{inheritDoc
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
        if (commandId.equals("au.org.intersect.exsite9.commands.NewProjectCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.OpenProjectCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand"))
        {
            final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(
                    IProjectManager.class);
            final Project project = projectManager.getCurrentProject();
            if (project != null)
            {
                initLayout(project.getSchema().getMetadataCategories());

                if (!project.getSchema().getMetadataCategories().isEmpty())
                {
                    // Refresh this page according to the currently selected items in the RHS.
                    final ProjectExplorerView projectExplorerView = (ProjectExplorerView) ViewUtils.getViewByID(
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow(), ProjectExplorerView.ID);
                    projectExplorerView.refresh();
                    final ISelection currentSelection = projectExplorerView.getSelection();
                    selectionChanged(projectExplorerView, currentSelection);
                }
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
        // Check that the selected groups does not contain a new group OR the project node - we CANNOT assign metadata
        // to them.

        final MetadataButtonWidget button = (MetadataButtonWidget) e.widget;
        final MetadataCategory metadataCategory = button.getMetadataCategory();
        final MetadataValue metadataValue = button.getMetadataValue();

        // You are not able to apply metadata to the New Files Group
        if (!Collections2.filter(this.selectedMetadataAssignables, Predicates.instanceOf(NewFilesGroup.class)).isEmpty())
        {
            MessageDialog.openError(getSite().getWorkbenchWindow().getShell(), "Error",
                    "Metadata cannot be assigned to the new files group.");
            button.setSelection(false);
            return;
        }
        else if (!Collections2.filter(this.selectedMetadataAssignables, Predicates.instanceOf(RootGroup.class)).isEmpty())
        {
            MessageDialog.openError(getSite().getWorkbenchWindow().getShell(), "Error",
                    "Metadata cannot be assigned to a project.");
            button.setSelection(false);
            return;
        }

        if (this.selectedMetadataAssignables.size() > 1)
        {
            final boolean performOperation = MessageDialog
                    .openConfirm(getSite().getWorkbenchWindow().getShell(), "Caution",
                            "The metadata operation is about to be performed on all selected items. Are you sure you wish to proceed?");
            if (!performOperation)
            {
                button.setSelection(!button.getSelection());
                return;
            }
        }

        if (button.getSelection())
        {
            for (final IMetadataAssignable metadataAssignable : this.selectedMetadataAssignables)
            {
                if (metadataAssignable instanceof Group)
                {
                    // We need to reload the object model from the database because the user may have updated it's metadata associations
                    // and wishes to perform another action on it (without selecting another one in between, which would force a reload from the DB)
                    final Group freshGroup = this.groupService.findGroupByID(((Group)metadataAssignable).getId());
                    groupService.associateMetadata(freshGroup, metadataCategory, metadataValue);
                }
                else if (metadataAssignable instanceof ResearchFile)
                {
                    final ResearchFile freshResearchFile = this.researchFileService.findResearchFileByID(((ResearchFile)metadataAssignable).getId());
                    researchFileService.associateMetadata(freshResearchFile, metadataCategory, metadataValue);
                }
            }
        }
        else
        {
            for (final IMetadataAssignable metadataAssignable : this.selectedMetadataAssignables)
            {
                if (metadataAssignable instanceof Group)
                {
                    final Group freshGroup = this.groupService.findGroupByID(((Group)metadataAssignable).getId());
                    groupService.disassociateMetadata(freshGroup, metadataCategory, metadataValue);
                }
                else if (metadataAssignable instanceof ResearchFile)
                {
                    final ResearchFile freshResearchFile = this.researchFileService.findResearchFileByID(((ResearchFile)metadataAssignable).getId());
                    researchFileService.disassociateMetadata(freshResearchFile, metadataCategory, metadataValue);
                }
            }
        }
        
     // Refresh the table in the Associated Metadata View when a value button is selected or de-selected
        final AssociatedMetadataView associatedMetadataView = (AssociatedMetadataView) ViewUtils.getViewByID(
                PlatformUI.getWorkbench().getActiveWorkbenchWindow(), AssociatedMetadataView.ID);
        associatedMetadataView.refresh();
        
     // Refresh the Project Explorer View according to the currently selected items in the RHS.
        final ProjectExplorerView projectExplorerView = (ProjectExplorerView) ViewUtils.getViewByID(
                PlatformUI.getWorkbench().getActiveWorkbenchWindow(), ProjectExplorerView.ID);
        projectExplorerView.refresh();        
    }

    @Override
    public void widgetDefaultSelected(final SelectionEvent e)
    {

    }

    /**
     * Listens to selection changes on the tree viewer in the project view on the LHS.
     * 
     * @{inheritDoc
     */
    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
    {
        if (!(selection instanceof IStructuredSelection))
        {
            LOG.error("Unknown selection type");
            return;
        }

        final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

        @SuppressWarnings("unchecked")
        final List<Object> selectedObjects = structuredSelection.toList();

        this.selectedMetadataAssignables.clear();

        for (final Object selectedObject : selectedObjects)
        {
            if (selectedObject instanceof Group)
            {
                final Group selectedGroup = this.groupService.findGroupByID(((Group) selectedObject).getId());
                this.selectedMetadataAssignables.add(selectedGroup);
            }
            else if (selectedObject instanceof ResearchFile)
            {
                final ResearchFile selectedResearchFile = this.researchFileService.findResearchFileByID(((ResearchFile) selectedObject).getId());
                this.selectedMetadataAssignables.add(selectedResearchFile);
            }
            else if (selectedObject instanceof Project)
            {
                Project project = ((Project) selectedObject);
                final Group selectedGroup = this.groupService.findGroupByID(project.getRootNode().getId());
                this.selectedMetadataAssignables.add(selectedGroup);
            }
        }

        resetMetadataValueButtons();
        if (!this.selectedMetadataAssignables.isEmpty())
        {
            // Determine a common set of buttons that should be pressed and press them.
            final Set<Pair<MetadataCategory, MetadataValue>> intersection = new HashSet<Pair<MetadataCategory, MetadataValue>>(
                    MetadataAssignableUtils.getCategoryToValueMapping(this.selectedMetadataAssignables.get(0)));

            for (int i = 1; i < this.selectedMetadataAssignables.size(); i++)
            {
                intersection.retainAll(MetadataAssignableUtils.getCategoryToValueMapping(this.selectedMetadataAssignables.get(i)));
            }
            setMetadataValuesButtonsPressed(intersection);
        }
    }

    /**
     * Used to enable/disable this view from other views.
     * @param enabled {@code true} to enable this view.
     */
    public void setEnabled(final boolean enabled)
    {
        this.parent.setEnabled(enabled);
        this.parent.setVisible(enabled);
    }

    /**
     * Sets the metadata values that are pressed according to the metadata associations provided.
     * 
     * @param metadataButtons
     *            the metadata buttons to press.
     */
    private void setMetadataValuesButtonsPressed(final Collection<Pair<MetadataCategory, MetadataValue>> metadataButtons)
    {
        for (final Pair<MetadataCategory, MetadataValue> pair : metadataButtons)
        {
            final MetadataButtonWidget metadataButtonWidget = this.metadataButtons.get(pair);
            if (metadataButtonWidget != null)
            {
                metadataButtonWidget.setSelection(true);
            }
        }
    }

    /**
     * Depresses (i.e. makes them NOT clicked) all the metadata value buttons currently on the page.
     */
    private void resetMetadataValueButtons()
    {
        for (final MetadataButtonWidget metadataButtonWidget : this.metadataButtons.values())
        {
            metadataButtonWidget.setSelection(false);
        }
    }

    private static final class EditMetadataCategorySelectionListener implements SelectionListener
    {
        private final MetadataCategory metadataCategory;

        public EditMetadataCategorySelectionListener(final MetadataCategory metadataCategory)
        {
            this.metadataCategory = metadataCategory;
        }

        @Override
        public void widgetSelected(final SelectionEvent event)
        {
            // Fire the command including the metadata category ID as an argument.
            final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            final ICommandService commandService = (ICommandService) window.getService(ICommandService.class);
            final Command command = commandService
                    .getCommand("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand");

            final IParameter iparam;
            try
            {
                iparam = command
                        .getParameter("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand.categoryParameter");
            }
            catch (final NotDefinedException e)
            {
                LOG.error("Cannot add parameter to command", e);
                return;
            }

            final Parameterization params = new Parameterization(iparam, metadataCategory.getId().toString());
            final ArrayList<Parameterization> parameters = new ArrayList<Parameterization>();
            parameters.add(params);

            // Build the parameterized command
            final ParameterizedCommand pc = new ParameterizedCommand(command,
                    parameters.toArray(new Parameterization[parameters.size()]));

            // Execute the command
            final IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
            try
            {
                handlerService.executeCommand(pc, null);
            }
            catch (final ExecutionException e)
            {
                LOG.error("Cannot execute paramertized command", e);
            }
            catch (final NotDefinedException e)
            {
                LOG.error("Cannot execute paramertized command", e);
            }
            catch (final NotEnabledException e)
            {
                LOG.error("Cannot execute paramertized command", e);
            }
            catch (final NotHandledException e)
            {
                LOG.error("Cannot execute paramertized command", e);
            }
        }

        @Override
        public void widgetDefaultSelected(final SelectionEvent e)
        {
        }
    }
}
