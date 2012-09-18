/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import au.org.intersect.exsite9.Activator;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.IMetadataAssignable;
import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.RootGroup;
import au.org.intersect.exsite9.domain.utils.AlphabeticalMetadataAttributeValueComparator;
import au.org.intersect.exsite9.domain.utils.AlphabeticalMetadataValueComparator;
import au.org.intersect.exsite9.domain.utils.IDMetadataCategoryComparator;
import au.org.intersect.exsite9.domain.utils.MetadataAssignableUtils;
import au.org.intersect.exsite9.domain.utils.MetadataAssociationTripletComparator;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IMetadataCategoryService;
import au.org.intersect.exsite9.service.IMetadataCategoryViewConfigService;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.util.Triplet;
import au.org.intersect.exsite9.validators.MetadataValueValidator;
import au.org.intersect.exsite9.view.listener.MetadataCategorySelectionListener;
import au.org.intersect.exsite9.view.widgets.MetadataAttributeValuesComboWidget;
import au.org.intersect.exsite9.view.widgets.MetadataButtonWidget;
import au.org.intersect.exsite9.view.widgets.MetadataCategoryExpandItem;
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
    private final IMetadataCategoryViewConfigService metadataCategoryViewConfigService;

    /**
     * The list of currently selected (if any) groups on the RHS.
     */
    private final List<IMetadataAssignable> selectedMetadataAssignables = new ArrayList<IMetadataAssignable>();

    /**
     * The metadata buttons that are currently shown on the page - keyed by metadata category for easy lookup.
     */
    private final Map<Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>, MetadataButtonWidget> metadataButtons = new HashMap<Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>, MetadataButtonWidget>();

    private final Map<MetadataCategory, Composite> freeTextParentComposites = new HashMap<MetadataCategory, Composite>();

    private final Map<MetadataCategory, List<FreeTextRowComposite>> freeTextRows = new HashMap<MetadataCategory, List<FreeTextRowComposite>>();

    /**
     * 
     */
    public MetadataBrowserView()
    {
        this.groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        this.researchFileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);
        this.metadataCategoryService = (IMetadataCategoryService) PlatformUI.getWorkbench().getService(IMetadataCategoryService.class);
        this.metadataCategoryViewConfigService = (IMetadataCategoryViewConfigService) PlatformUI.getWorkbench().getService(IMetadataCategoryViewConfigService.class);
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
        this.freeTextParentComposites.clear();
        this.freeTextRows.clear();
        this.rows.clear();

        if (metadataCategories.isEmpty())
        {
            this.placeholder = new Composite(this.parent, SWT.BORDER);
            return;
        }

        this.expandBar = new ExpandBar(this.parent, SWT.BORDER | SWT.V_SCROLL);
        this.expandBar.addExpandListener(new ExpandListener()
        {
            @Override
            public void itemExpanded(final ExpandEvent e)
            {
                final MetadataCategoryExpandItem expandItem = (MetadataCategoryExpandItem) e.item;
                final MetadataCategory metadataCategory = expandItem.getMetadataCategory();
                metadataCategoryViewConfigService.setExpanded(metadataCategory, true);
            }
            
            @Override
            public void itemCollapsed(final ExpandEvent e)
            {
                final MetadataCategoryExpandItem expandItem = (MetadataCategoryExpandItem) e.item;
                final MetadataCategory metadataCategory = expandItem.getMetadataCategory();
                metadataCategoryViewConfigService.setExpanded(metadataCategory, false);
            }
        });

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

            final MetadataCategoryExpandItem expandItem = new MetadataCategoryExpandItem(this.expandBar, SWT.NONE, metadataCategory);
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

            if((metadataCategory.getDescription() != null) && (!metadataCategory.getDescription().isEmpty())){
                final ToolItem helpButtonItem = new ToolItem(toolBar, SWT.FLAT);
                final Image helpImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_LCL_LINKTO_HELP);
                helpButtonItem.setImage(helpImage);
                helpButtonItem.setToolTipText(metadataCategory.getDescription());
            }
            
            new ToolItem(toolBar, SWT.SEPARATOR);
            toolBar.pack();


            if (metadataCategory.getType() == MetadataCategoryType.CONTROLLED_VOCABULARY)
            {
                final Composite contentComposite = new Composite(expandBarComposite, SWT.NONE);
                layoutRow(contentComposite);

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
                    final MetadataButtonWidget mdbw = new MetadataButtonWidget(contentComposite, SWT.TOGGLE, metadataCategory, metadataValue);
                    mdbw.setText(metadataValue.getValue());
                    mdbw.addSelectionListener(this);
                    final Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue> triplet = new Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>(metadataCategory, metadataValue, null);
                    this.metadataButtons.put(triplet, mdbw);
                }
            }
            else
            {
                freeTextParentComposites.put(metadataCategory, expandBarComposite);
            }
            final boolean expanded = metadataCategoryViewConfigService.isExpanded(metadataCategory);
            expandItem.setExpanded(expanded);
        }

        packAndLayout();
    }

    private void layoutRow(final Composite contentComposite)
    {
        final RowData rowData = new RowData();
        rowData.width = parent.getClientArea().width;
        contentComposite.setLayoutData(rowData);
        rows.add(rowData);
    }

    private void packAndLayout()
    {
        expandBar.pack();
        expandBar.layout();
        parent.layout();
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
                    groupService.associateMetadata(freshGroup, metadataCategory, metadataValue, null);
                }
                else if (metadataAssignable instanceof ResearchFile)
                {
                    final ResearchFile freshResearchFile = this.researchFileService.findResearchFileByID(((ResearchFile)metadataAssignable).getId());
                    researchFileService.associateMetadata(freshResearchFile, metadataCategory, metadataValue, null);
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
            if (selectedObject instanceof Group && !(selectedObject instanceof NewFilesGroup))
            {
                final Group selectedGroup = this.groupService.findGroupByID(((Group) selectedObject).getId());
                this.selectedMetadataAssignables.add(selectedGroup);
            }
            else if (selectedObject instanceof ResearchFile)
            {
                final ResearchFile selectedResearchFile = this.researchFileService.findResearchFileByID(((ResearchFile) selectedObject).getId());
                this.selectedMetadataAssignables.add(selectedResearchFile);
            }
        }

        resetMetadataValueWidgets();
        if (!this.selectedMetadataAssignables.isEmpty())
        {
            // Determine a common set of buttons that should be pressed and press them.
            final Set<Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>> intersection = new HashSet<Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>>(
                    MetadataAssignableUtils.getCategoryToValueMapping(this.selectedMetadataAssignables.get(0)));

            for (int i = 1; i < this.selectedMetadataAssignables.size(); i++)
            {
                intersection.retainAll(MetadataAssignableUtils.getCategoryToValueMapping(this.selectedMetadataAssignables.get(i)));
            }
            setMetadataValueWidgets(new ArrayList<Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>>(intersection));
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
     * @param metadataCollectionTrio the metadata to select.
     */
    private void setMetadataValueWidgets(final List<Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>> metadataCollectionTrio)
    {
        Collections.sort(metadataCollectionTrio, new MetadataAssociationTripletComparator());
        for (final Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue> triplet : metadataCollectionTrio)
        {
            final MetadataCategory metadataCategory = triplet.getFirst();
            if (metadataCategory.getType() == MetadataCategoryType.CONTROLLED_VOCABULARY)
            {
                final MetadataButtonWidget metadataButtonWidget = this.metadataButtons.get(triplet);
                if (metadataButtonWidget != null)
                {
                    metadataButtonWidget.setSelection(true);
                }
            }
            else
            {
                final Composite composite = freeTextParentComposites.get(metadataCategory);
                final MetadataValue metadataValue = triplet.getSecond();
                final MetadataAttributeValue metadataAttributeValue = triplet.getThird();

                final List<FreeTextRowComposite> rowsForMetadataCategory;
                if (this.freeTextRows.containsKey(metadataCategory))
                {
                    rowsForMetadataCategory = this.freeTextRows.get(metadataCategory);
                }
                else
                {
                    rowsForMetadataCategory = new ArrayList<FreeTextRowComposite>();
                    this.freeTextRows.put(metadataCategory, rowsForMetadataCategory);
                }

                final FreeTextRowComposite row = new FreeTextRowComposite(composite, SWT.NULL, metadataCategory, metadataValue, metadataAttributeValue,
                        rowsForMetadataCategory.isEmpty());
                rowsForMetadataCategory.add(row);
                layoutRow(row.getComposite());
            }
        }

        for (final MetadataCategory mdc : this.freeTextParentComposites.keySet())
        {
            if (!this.freeTextRows.containsKey(mdc))
            {
                final Composite composite = freeTextParentComposites.get(mdc);
                final FreeTextRowComposite row = new FreeTextRowComposite(composite, SWT.NULL, mdc, null, null, true);
                this.freeTextRows.put(mdc, new ArrayList<MetadataBrowserView.FreeTextRowComposite>(Arrays.asList(row)));
                layoutRow(row.getComposite());
            }
        }

        packAndLayout();
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

        for (final List<FreeTextRowComposite> freeTextRows : this.freeTextRows.values())
        {
            for (final FreeTextRowComposite freeTextRow : freeTextRows )
            {
                freeTextRow.getComposite().dispose();
            }
        }
        this.freeTextRows.clear();
        packAndLayout();
    }

    /**
     * Represents a row used for Free Text Metadata Categories.
     */
    public final class FreeTextRowComposite
    {
        private final Composite contentComposite;
        private final MetadataCategory metadataCategory;
        private final MetadataValue metadataValue;
        private final MetadataAttributeValue metadataAttributeValue;
        private final boolean first;

        public FreeTextRowComposite(final Composite parent, final int style, final MetadataCategory mdc, final MetadataValue metadataValue,
                final MetadataAttributeValue metadataAttribtueValue, final boolean first)
        {
            this.contentComposite = new Composite(parent, style);
            this.metadataCategory = mdc;
            this.metadataValue = metadataValue;
            this.metadataAttributeValue = metadataAttribtueValue;
            this.first = first;
            init();
        }

        public Composite getComposite()
        {
            return contentComposite;
        }

        private void init()
        {
            final MetadataAttribute metadataAttribute = metadataCategory.getMetadataAttribute();
            final boolean hasAttributes = metadataAttribute != null;

            final int numCols = hasAttributes ? 7 : 5;

            final GridLayout gridLayout = new GridLayout(numCols, false);
            contentComposite.setLayout(gridLayout);

            final MetadataAttributeValuesComboWidget combo;
            if (hasAttributes)
            {
                final Label attributeNameLabel = new Label(contentComposite, SWT.NULL);
                attributeNameLabel.setText(metadataAttribute.getName() + ":");
                combo = new MetadataAttributeValuesComboWidget(contentComposite, SWT.READ_ONLY);
                final List<MetadataAttributeValue> values = new ArrayList<MetadataAttributeValue>(metadataAttribute.getMetadataAttributeValues());
                Collections.sort(values, new AlphabeticalMetadataAttributeValueComparator());
                combo.setItems(values);
            }
            else
            {
                combo = null;
            }
            
            final MetadataTextWidget freeTextField = new MetadataTextWidget(contentComposite, SWT.BORDER | SWT.SINGLE);
            freeTextField.setText(metadataValue == null ? "" : metadataValue.getValue());
            freeTextField.setMetadataValue(metadataValue);
            freeTextField.setToolTipText(freeTextField.getText());

            if (combo != null && metadataAttributeValue != null)
            {
                combo.select(metadataAttributeValue);
                combo.setMetadataAttributeValue(metadataAttributeValue);
            }

            final GridData fillGridLayout = new GridData(GridData.FILL_HORIZONTAL);
            freeTextField.setLayoutData(fillGridLayout);

            final Button resetButton = new Button(contentComposite, SWT.PUSH);
            resetButton.setText("Reset");
            resetButton.setEnabled(false);

            final Button applyButton = new Button(contentComposite, SWT.PUSH);
            applyButton.setText("Apply");
            applyButton.setEnabled(false);

            final Button minusButton = new Button(contentComposite, SWT.PUSH);
            minusButton.setText("-");
            minusButton.setEnabled(!first);

            final Button plusButton = new Button(contentComposite, SWT.PUSH);
            plusButton.setText("+");

            if (combo != null)
            {
                combo.addSelectionListener(new SelectionListener()
                {
                    @Override
                    public void widgetSelected(final SelectionEvent e)
                    {
                        if (!validMetadataAssignablesSelected(false))
                        {
                            resetButton.setEnabled(false);
                            applyButton.setEnabled(false);
                            return;
                        }
                        
                        final MetadataAttributeValue currentValue = combo.getSelectedMetadataAttributeValue();
                        final boolean differentAttributeValue = !Objects.equal(metadataAttributeValue, currentValue);

                        final String originalText = metadataValue == null ? "" : metadataValue.getValue();
                        final String newText = freeTextField.getText().trim();
                        final boolean differentTextValue = !Objects.equal(newText, originalText);
                        freeTextField.setToolTipText(newText);
                        resetButton.setEnabled(differentAttributeValue || differentTextValue);
                        applyButton.setEnabled((differentAttributeValue && !newText.isEmpty()) || (differentTextValue && !newText.isEmpty()));
                    }
                    
                    @Override
                    public void widgetDefaultSelected(SelectionEvent e)
                    {
                    }
                });
            }

            resetButton.addSelectionListener(new SelectionListener()
            {
                @Override
                public void widgetSelected(final SelectionEvent e)
                {
                    final String originalText = metadataValue == null ? "" : metadataValue.getValue();
                    freeTextField.setText(originalText);
                    freeTextField.setSelection(originalText.length(), originalText.length());
                    freeTextField.setToolTipText(freeTextField.getText());
                    if (combo != null)
                    {
                        if (metadataAttributeValue != null)
                        {
                            combo.select(metadataAttributeValue);
                        }
                        else
                        {
                            combo.select(0);
                        }
                    }
                    resetButton.setEnabled(false);
                    applyButton.setEnabled(false);
                }
                
                @Override
                public void widgetDefaultSelected(final SelectionEvent e)
                {
                }
            });

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
                    final MetadataAttributeValue metadataAttributeValue = combo != null ? combo.getSelectedMetadataAttributeValue() : null;

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
                        disassociateFreeText(updatedMetadataCategory, oldAssociatedValue);
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
                                groupService.associateMetadata(freshGroup, updatedMetadataCategory, metadataValue, metadataAttributeValue);
                            }
                            else if (metadataAssignable instanceof ResearchFile)
                            {
                                final ResearchFile freshResearchFile = researchFileService.findResearchFileByID(((ResearchFile)metadataAssignable).getId());
                                researchFileService.associateMetadata(freshResearchFile, updatedMetadataCategory, metadataValue, metadataAttributeValue);
                            }
                        }
                        freeTextField.setMetadataValue(metadataValue);
                        applyButton.setEnabled(false);
                        resetButton.setEnabled(false);
                    }
                    refreshRelatedViews();
                }
                
                @Override
                public void widgetDefaultSelected(final SelectionEvent event)
                {
                }
            });

            plusButton.addSelectionListener(new SelectionListener()
            {
                @Override
                public void widgetSelected(final SelectionEvent e)
                {
                    final Composite composite = freeTextParentComposites.get(metadataCategory);
                    final FreeTextRowComposite row = new FreeTextRowComposite(composite, SWT.NULL, metadataCategory, null, null, false);
                    freeTextRows.get(metadataCategory).add(row);
                    layoutRow(row.getComposite());
                    packAndLayout();
                }

                @Override
                public void widgetDefaultSelected(final SelectionEvent e)
                {
                }
            });

            minusButton.addSelectionListener(new SelectionListener()
            {
                @Override
                public void widgetSelected(final SelectionEvent e)
                {
                    // Remove metadata category/value mapping - which includes disassocation.
                    final MetadataCategory updatedMetadataCategory = metadataCategoryService.findById(metadataCategory.getId());
                    final MetadataValue oldAssociatedValue = freeTextField.getMetadataValue();

                    if (oldAssociatedValue != null)
                    {
                        disassociateFreeText(updatedMetadataCategory, metadataValue);
                    }

                    contentComposite.dispose();
                    packAndLayout();
                    refreshRelatedViews();
                }   

                @Override
                public void widgetDefaultSelected(final SelectionEvent e)
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

                    final String originalText = metadataValue == null ? "" : metadataValue.getValue();
                    final String newText = freeTextField.getText().trim();
                    final boolean enabled = !originalText.equals(newText);
                    freeTextField.setToolTipText(newText);
                    resetButton.setEnabled(enabled);
                    applyButton.setEnabled(enabled && !newText.isEmpty());
                }
                
                @Override
                public void keyPressed(final KeyEvent e)
                {
                }
            });
        }

        private void disassociateFreeText(final MetadataCategory updatedMetadataCategory, final MetadataValue oldAssociatedValue)
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
    }
}
