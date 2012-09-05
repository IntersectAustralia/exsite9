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
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import au.org.intersect.exsite9.Activator;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.IMetadataAssignable;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.RootGroup;
import au.org.intersect.exsite9.domain.utils.AlphabeticalMetadataValueComparator;
import au.org.intersect.exsite9.domain.utils.IDMetadataCategoryComparator;
import au.org.intersect.exsite9.domain.utils.MetadataAssignableUtils;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IMetadataCategoryService;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.util.Pair;
import au.org.intersect.exsite9.validators.MetadataValueValidator;
import au.org.intersect.exsite9.view.listener.MetadataCategorySelectionListener;
import au.org.intersect.exsite9.view.widgets.MetadataButtonWidget;
import au.org.intersect.exsite9.view.widgets.MetadataTextWidget;

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

    private final List<RowData> rows = new ArrayList<RowData>();

    private final IGroupService groupService;
    private final IMetadataCategoryService metadataCategoryService;
    private final IResearchFileService researchFileService;

    /**
     * The list of currently selected (if any) groups on the RHS.
     */
    private final List<IMetadataAssignable> selectedMetadataAssignables = new ArrayList<IMetadataAssignable>();

    /**
     * The metadata buttons that are currently shown on the page - keyed by metadata category for easy lookup.
     */
    private final Map<Pair<MetadataCategory, MetadataValue>, MetadataButtonWidget> metadataButtons = new HashMap<Pair<MetadataCategory, MetadataValue>, MetadataButtonWidget>();

    private final Map<MetadataCategory, MetadataTextWidget> metadataFreeTextFields = new HashMap<MetadataCategory, MetadataTextWidget>();

    /**
     * 
     */
    public MetadataBrowserView()
    {
        this.groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        this.researchFileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);
        this.metadataCategoryService = (IMetadataCategoryService) PlatformUI.getWorkbench().getService(IMetadataCategoryService.class);
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

        final Command editProjectCommand = commandService.getCommand("au.org.intersect.exsite9.commands.EditProjectCommand");
        editProjectCommand.addExecutionListener(this);

        final Command importMetadataSchemaCommand = commandService.getCommand("au.org.intersect.exsite9.commands.ImportMetadataSchemaCommand");
        importMetadataSchemaCommand.addExecutionListener(this);

        final Command removeMetadataCategoryCommand = commandService.getCommand("au.org.intersect.exsite9.commands.RemoveMetadataCategoryCommand");
        removeMetadataCategoryCommand.addExecutionListener(this);

        final Command editMetadataCategoryCommand = commandService.getCommand("au.org.intersect.exsite9.commands.EditMetadataCategoryCommand");
        editMetadataCategoryCommand.addExecutionListener(this);

        final Command removeMetadataCategoryWithWizardCommand = commandService.getCommand("au.org.intersect.exsite9.commands.RemoveMetadataCategoryWithWizardCommand");
        removeMetadataCategoryWithWizardCommand.addExecutionListener(this);

        this.parent = parent;

        // So wrapping of the buttons in the rows will work
        this.parent.addControlListener(new ControlAdapter()
        {
            @Override
            public void controlResized(final ControlEvent e)
            {
                super.controlResized(e);
                for (final RowData rowData : rows)
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
        this.metadataFreeTextFields.clear();
        this.rows.clear();

        if (metadataCategories.isEmpty())
        {
            this.placeholder = new Composite(this.parent, SWT.BORDER);
            return;
        }

        this.expandBar = new ExpandBar(this.parent, SWT.BORDER | SWT.V_SCROLL);

        final List<MetadataCategory> sorted = new ArrayList<MetadataCategory>(metadataCategories);
        Collections.sort(sorted, new IDMetadataCategoryComparator());

        for (final MetadataCategory metadataCategory : sorted)
        {
            final Composite expandBarComposite = new Composite(expandBar, SWT.NONE);
            final RowLayout expandBarLayout = new RowLayout(SWT.VERTICAL);
            expandBarLayout.wrap = true;
            expandBarLayout.pack = true;
            expandBarLayout.justify = false;
            expandBarComposite.setLayout(expandBarLayout);
            
            final Composite headerComposite = new Composite(expandBarComposite, SWT.NONE);
            final Composite contentComposite = new Composite(expandBarComposite, SWT.NONE);
            
            contentComposite.setToolTipText(metadataCategory.getName());
            
            final ExpandItem expandItem = new ExpandItem(this.expandBar, SWT.NONE);
            expandItem.setText(metadataCategory.getName() + " (" + metadataCategory.getUse() + ")");
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
            final Image editImage = Activator.getImageDescriptor("/icons/icon_edit_16.png").createImage();
            editButtonItem.setImage(editImage);
            editButtonItem.addSelectionListener(new MetadataCategorySelectionListener(metadataCategory,
                    "au.org.intersect.exsite9.commands.AddMetadataCategoryCommand", "au.org.intersect.exsite9.commands.AddMetadataCategoryCommand.categoryParameter"));
            editButtonItem.setToolTipText("Edit category");
            
            final ToolItem removeButtonItem = new ToolItem(toolBar, SWT.NULL);
            final Image removeImage = Activator.getImageDescriptor("/icons/icon_exclude_16.png").createImage();
            removeButtonItem.setImage(removeImage);
            removeButtonItem.setToolTipText("Remove category");
            removeButtonItem.addSelectionListener(new MetadataCategorySelectionListener(metadataCategory,
                    "au.org.intersect.exsite9.commands.RemoveMetadataCategoryCommand", "au.org.intersect.exsite9.commands.RemoveMetadataCategoryCommand.categoryParameter"));

            final ToolItem helpButtonItem = new ToolItem(toolBar, SWT.NULL);
            final Image helpImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_LCL_LINKTO_HELP);
            helpButtonItem.setImage(helpImage);
            helpButtonItem.setToolTipText(metadataCategory.getName());
            
            new ToolItem(toolBar, SWT.SEPARATOR);
            toolBar.pack();

            final RowData rowData = new RowData();
            rowData.width = parent.getClientArea().width;
            contentComposite.setLayoutData(rowData);
            rows.add(rowData);

            if (metadataCategory.getType() == MetadataCategoryType.CONTROLLED_VOCABULARY)
            {
                final RowLayout buttonLayout = new RowLayout(SWT.HORIZONTAL);
                buttonLayout.marginLeft = 10;
                buttonLayout.marginRight = 10;
                buttonLayout.marginTop = 10;
                buttonLayout.marginBottom = 10;
                buttonLayout.wrap = true;
                buttonLayout.pack = true;
                buttonLayout.justify = false;
                contentComposite.setLayout(buttonLayout);
    
                // Sort the metadata values.
                final List<MetadataValue> sortedMetadataValues = new ArrayList<MetadataValue>(metadataCategory.getValues());
                Collections.sort(sortedMetadataValues, new AlphabeticalMetadataValueComparator());
    
                for (final MetadataValue metadataValue : sortedMetadataValues)
                {
                    final MetadataButtonWidget mdbw = new MetadataButtonWidget(contentComposite, SWT.TOGGLE,
                            metadataCategory, metadataValue);
                    mdbw.setText(metadataValue.getValue());
                    mdbw.addSelectionListener(this);
                    final Pair<MetadataCategory, MetadataValue> pair = new Pair<MetadataCategory, MetadataValue>(
                            metadataCategory, metadataValue);
                    this.metadataButtons.put(pair, mdbw);
                }
            }
            else
            {
                final GridLayout gridLayout = new GridLayout(3, false);
                contentComposite.setLayout(gridLayout);

                final MetadataTextWidget freeTextField = new MetadataTextWidget(contentComposite, SWT.BORDER | SWT.SINGLE);
                this.metadataFreeTextFields.put(metadataCategory, freeTextField);

                final GridData multiLineGridData = new GridData(GridData.FILL_HORIZONTAL);
                freeTextField.setLayoutData(multiLineGridData);

                final Button resetButton = new Button(contentComposite, SWT.PUSH);
                final Button applyButton = new Button(contentComposite, SWT.PUSH);

                resetButton.setText("Reset");
                resetButton.setEnabled(false);
                resetButton.addSelectionListener(new SelectionListener()
                {
                    @Override
                    public void widgetSelected(final SelectionEvent e)
                    {
                        final MetadataValue metadataValue = metadataFreeTextFields.get(metadataCategory).getMetadataValue();
                        final String originalText = metadataValue == null ? "" : metadataValue.getValue();
                        freeTextField.setText(originalText);
                        freeTextField.setSelection(originalText.length(), originalText.length());
                        resetButton.setEnabled(false);
                        applyButton.setEnabled(false);
                    }
                    
                    @Override
                    public void widgetDefaultSelected(final SelectionEvent e)
                    {
                    }
                });

                applyButton.setText("Apply");
                applyButton.setEnabled(false);
                applyButton.addSelectionListener(new SelectionListener()
                {
                    @Override
                    public void widgetSelected(final SelectionEvent event)
                    {
                        if (!validMetadataAssignablesSelected(true))
                        {
                            return;
                        }

                        // Handle the assignment.
                        if (selectedMetadataAssignables.size() > 1)
                        {
                            final boolean performOperation = MessageDialog.openConfirm(getSite().getWorkbenchWindow().getShell(),
                                "Caution","The metadata operation is about to be performed on all selected items. Are you sure you wish to proceed?");
                            if (!performOperation)
                            {
                                return;
                            }
                        }

                        final String newValue = freeTextField.getText();

                        final MetadataValueValidator validtor = new MetadataValueValidator(Collections.<MetadataValue>emptyList(), true);
                        if (!validtor.isValid(newValue))
                        {
                            MessageDialog.openError(getSite().getWorkbenchWindow().getShell(), "Cannot apply metadata", "Cannot apply metadata. " + validtor.getErrorMessage());
                            return;
                        }

                        // We need to reload the metadata category.
                        final MetadataCategory updatedMetadataCategory = metadataCategoryService.findById(metadataCategory.getId());

                        // Disassociate old value
                        final MetadataValue oldAssociatedValue = freeTextField.getMetadataValue();
                        if (oldAssociatedValue != null)
                        {
                            for (final IMetadataAssignable metadataAssignable : selectedMetadataAssignables)
                            {
                                if (metadataAssignable instanceof Group)
                                {
                                    // We need to reload the object model from the database because the user may have updated it's metadata associations
                                    // and wishes to perform another action on it (without selecting another one in between, which would force a reload from the DB)
                                    final Group freshGroup = groupService.findGroupByID(((Group)metadataAssignable).getId());
                                    groupService.disassociateMetadata(freshGroup, updatedMetadataCategory, oldAssociatedValue);
                                }
                                else if (metadataAssignable instanceof ResearchFile)
                                {
                                    final ResearchFile freshResearchFile = researchFileService.findResearchFileByID(((ResearchFile)metadataAssignable).getId());
                                    researchFileService.disassociateMetadata(freshResearchFile, updatedMetadataCategory, oldAssociatedValue);
                                }
                            }
                        }

                        // Add & Associate new value
                        if (!newValue.isEmpty())
                        {
                            // Persist the newly configured metadata category value.
                            final MetadataValue metadataValue = metadataCategoryService.addValueToMetadataCategory(updatedMetadataCategory, newValue);
                            for (final IMetadataAssignable metadataAssignable : selectedMetadataAssignables)
                            {
                                if (metadataAssignable instanceof Group)
                                {
                                    // We need to reload the object model from the database because the user may have updated it's metadata associations
                                    // and wishes to perform another action on it (without selecting another one in between, which would force a reload from the DB)
                                    final Group freshGroup = groupService.findGroupByID(((Group)metadataAssignable).getId());
                                    groupService.associateMetadata(freshGroup, updatedMetadataCategory, metadataValue);
                                }
                                else if (metadataAssignable instanceof ResearchFile)
                                {
                                    final ResearchFile freshResearchFile = researchFileService.findResearchFileByID(((ResearchFile)metadataAssignable).getId());
                                    researchFileService.associateMetadata(freshResearchFile, updatedMetadataCategory, metadataValue);
                                }
                            }
                        }
                        reloadView();
                        refreshRelatedViews();
                    }
                    
                    @Override
                    public void widgetDefaultSelected(final SelectionEvent event)
                    {
                    }
                });

                freeTextField.addKeyListener(new KeyListener()
                {
                    @Override
                    public void keyReleased(final KeyEvent e)
                    {
                        if (!validMetadataAssignablesSelected(false))
                        {
                            resetButton.setEnabled(false);
                            applyButton.setEnabled(false);
                            return;
                        }

                        final MetadataValue metadataValue = metadataFreeTextFields.get(metadataCategory).getMetadataValue();
                        final String originalText = metadataValue == null ? "" : metadataValue.getValue();
                        final boolean enabled = !originalText.equals(freeTextField.getText());
                        resetButton.setEnabled(enabled);
                        applyButton.setEnabled(enabled);
                    }
                    
                    @Override
                    public void keyPressed(final KeyEvent e)
                    {
                    }
                });
            }

            contentComposite.pack();
            contentComposite.layout(true);
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
                || commandId.equals("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.EditProjectCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.ImportMetadataSchemaCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.RemoveMetadataCategoryCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.EditMetadataCategoryCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.RemoveMetadataCategoryWithWizardCommand"))
        {
            reloadView();
        }
    }

    private void reloadView()
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
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

    @Override
    public void preExecute(final String commandId, final ExecutionEvent event)
    {

    }

    @Override
    public void widgetSelected(final SelectionEvent e)
    {
        final MetadataButtonWidget button = (MetadataButtonWidget) e.widget;

        if (!validMetadataAssignablesSelected(true))
        {
            button.setSelection(false);
            return;
        }

        final MetadataCategory metadataCategory = button.getMetadataCategory();
        final MetadataValue metadataValue = button.getMetadataValue();

        if (this.selectedMetadataAssignables.size() > 1)
        {
            final boolean performOperation = MessageDialog.openConfirm(getSite().getWorkbenchWindow().getShell(),
                "Caution","The metadata operation is about to be performed on all selected items. Are you sure you wish to proceed?");
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
        refreshRelatedViews();
    }

    /**
     * Ensures the user has selected valid metadata assignables in the project explorer view.
     * @return {@code true} if selection is valid, {@code false} otherwise.
     */
    private boolean validMetadataAssignablesSelected(final boolean showErrors)
    {
        // Check that the selected groups does not contain a new files group OR the project node - we CANNOT assign metadata
        // to them.
        if (!Collections2.filter(this.selectedMetadataAssignables, Predicates.instanceOf(NewFilesGroup.class)).isEmpty())
        {
            if (showErrors)
            {
                MessageDialog.openError(getSite().getWorkbenchWindow().getShell(), "Error", "Metadata cannot be assigned to the new files group.");
            }
            return false;
        }
        else if (!Collections2.filter(this.selectedMetadataAssignables, Predicates.instanceOf(RootGroup.class)).isEmpty())
        {
            if (showErrors)
            {
                MessageDialog.openError(getSite().getWorkbenchWindow().getShell(), "Error", "Metadata cannot be assigned to a project.");
            }
            return false;
        }
        return true;
    }

    /**
     * Refreshes related views - performed after some action is performed on this view that other views may be interested in.
     */
    private static void refreshRelatedViews()
    {
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

        resetMetadataValueWidgets();
        if (!this.selectedMetadataAssignables.isEmpty())
        {
            // Determine a common set of buttons that should be pressed and press them.
            final Set<Pair<MetadataCategory, MetadataValue>> intersection = new HashSet<Pair<MetadataCategory, MetadataValue>>(
                    MetadataAssignableUtils.getCategoryToValueMapping(this.selectedMetadataAssignables.get(0)));

            for (int i = 1; i < this.selectedMetadataAssignables.size(); i++)
            {
                intersection.retainAll(MetadataAssignableUtils.getCategoryToValueMapping(this.selectedMetadataAssignables.get(i)));
            }
            setMetadataValueWidgets(intersection);
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
     * Sets the metadata values that are selected.
     * @param metadataCollectionPair the metadata to select.
     */
    private void setMetadataValueWidgets(final Collection<Pair<MetadataCategory, MetadataValue>> metadataCollectionPair)
    {
        for (final Pair<MetadataCategory, MetadataValue> pair : metadataCollectionPair)
        {
            final MetadataCategory metadataCategory = pair.getFirst();
            if (metadataCategory.getType() == MetadataCategoryType.CONTROLLED_VOCABULARY)
            {
                final MetadataButtonWidget metadataButtonWidget = this.metadataButtons.get(pair);
                if (metadataButtonWidget != null)
                {
                    metadataButtonWidget.setSelection(true);
                }
            }
            else
            {
                final MetadataValue metadataValue = pair.getSecond();
                final MetadataTextWidget freeTextField = this.metadataFreeTextFields.get(metadataCategory);
                freeTextField.setText(metadataValue.getValue());
                freeTextField.setMetadataValue(metadataValue);
            }
        }
    }

    /**
     * Depresses (i.e. makes them NOT clicked) all the metadata value buttons currently on the page.
     */
    private void resetMetadataValueWidgets()
    {
        for (final MetadataButtonWidget metadataButtonWidget : this.metadataButtons.values())
        {
            metadataButtonWidget.setSelection(false);
        }
        for (final MetadataTextWidget freeTextFields : this.metadataFreeTextFields.values())
        {
            freeTextFields.setMetadataValue(null);
            freeTextFields.setText("");
        }
    }
}
