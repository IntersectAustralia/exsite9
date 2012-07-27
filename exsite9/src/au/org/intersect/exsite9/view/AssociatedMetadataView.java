package au.org.intersect.exsite9.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.IMetadataAssignable;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.utils.MetadataAssignableUtils;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.util.AlphabeticalPairComparator;
import au.org.intersect.exsite9.util.Pair;

public final class AssociatedMetadataView extends ViewPart implements ISelectionListener, IExecutionListener
{
    public static final String ID = AssociatedMetadataView.class.getName();
    private TableViewer tableViewer;
    private Composite parent;

    public AssociatedMetadataView()
    {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void createPartControl(final Composite parent)
    {
        this.parent = parent;
        final ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(
                ICommandService.class);

        this.tableViewer = new TableViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL);

        final Table table = this.tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        this.tableViewer.setContentProvider(new AssociatedMetadataContentProvider());

        final TableViewerColumn nameColumn = new TableViewerColumn(this.tableViewer, SWT.NULL);
        nameColumn.getColumn().setText("Name");

        final TableViewerColumn valueColumn = new TableViewerColumn(tableViewer, SWT.NULL);
        valueColumn.getColumn().setText("Value");

        // use TableColumnLayout to make the columns auto re-sizable and split to width evenly
        TableColumnLayout layout = new TableColumnLayout();
        parent.setLayout(layout);
        layout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(50));
        layout.setColumnData(valueColumn.getColumn(), new ColumnWeightData(50));

        nameColumn.setLabelProvider(new ColumnLabelProvider()
        {

            @Override
            public String getText(Object element)
            {
                return ((Pair<String, String>) element).getFirst();
            }

        });

        valueColumn.setLabelProvider(new ColumnLabelProvider()
        {
            @Override
            public String getText(Object element)
            {
                return ((Pair<String, String>) element).getSecond();
            }

        });

        // Listen for selection changes in the treeViewer
        getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(ProjectExplorerView.ID, this);

        // Commands on which to refresh the table.
        final Command editProjectCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.EditProjectCommand");
        editProjectCommand.addExecutionListener(this);

        final Command addMetadataCategoryCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand");
        addMetadataCategoryCommand.addExecutionListener(this);

        final Command importMetadataSchemaCommand = commandService.getCommand("au.org.intersect.exsite9.commands.ImportMetadataSchemaCommand");
        importMetadataSchemaCommand.addExecutionListener(this);

        final Command removeMetadataCategoryCommand = commandService.getCommand("au.org.intersect.exsite9.commands.RemoveMetadataCategoryCommand");
        removeMetadataCategoryCommand.addExecutionListener(this);

        final Command editMetadataCategoryCommand = commandService.getCommand("au.org.intersect.exsite9.commands.EditMetadataCategoryCommand");
        editMetadataCategoryCommand.addExecutionListener(this);

        final Command removeMetadataCategoryWithWizardCommand = commandService.getCommand("au.org.intersect.exsite9.commands.RemoveMetadataCategoryWithWizardCommand");
        removeMetadataCategoryWithWizardCommand.addExecutionListener(this);
    }

    @Override
    public void setFocus()
    {
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
    {
        this.tableViewer.setInput(selection);
    }

    static class AssociatedMetadataContentProvider implements IStructuredContentProvider
    {
        private IGroupService groupService;
        private IResearchFileService researchFileService;
        private IProjectService projectService;

        private final List<IMetadataAssignable> selectedMetadataAssignables = new ArrayList<IMetadataAssignable>();

        public AssociatedMetadataContentProvider()
        {
            this.groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
            this.researchFileService = (IResearchFileService) PlatformUI.getWorkbench().getService(
                    IResearchFileService.class);
            this.projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
        }

        @Override
        public void dispose()
        {
        }

        @Override
        public void inputChanged(Viewer arg0, Object arg1, Object arg2)
        {
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object[] getElements(Object selection)
        {
            final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

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
                    final ResearchFile selectedResearchFile = this.researchFileService
                            .findResearchFileByID(((ResearchFile) selectedObject).getId());
                    this.selectedMetadataAssignables.add(selectedResearchFile);
                }
                else if (selectedObject instanceof Project)
                {
                    final Project selectedProject = this.projectService.findProjectById(((Project) selectedObject)
                            .getId());
                    // Its only possible for one project to be selected so the assignment to be done here
                    return getAllNotEmptyFields(selectedProject).toArray();
                }
            }

            if (!this.selectedMetadataAssignables.isEmpty())
            {
                List<Pair<String, String>> stringsForTable = new ArrayList<Pair<String, String>>();

                final Set<Pair<MetadataCategory, MetadataValue>> metadataToBeMapped = new HashSet<Pair<MetadataCategory, MetadataValue>>(
                        MetadataAssignableUtils.getCategoryToValueMapping(this.selectedMetadataAssignables.get(0)));

                for (int i = 1; i < this.selectedMetadataAssignables.size(); i++)
                {
                    metadataToBeMapped.retainAll(MetadataAssignableUtils
                            .getCategoryToValueMapping(this.selectedMetadataAssignables.get(i)));
                }

                for (Iterator<Pair<MetadataCategory, MetadataValue>> iterator = metadataToBeMapped.iterator(); iterator
                        .hasNext();)
                {
                    Pair<MetadataCategory, MetadataValue> pair = (Pair<MetadataCategory, MetadataValue>) iterator
                            .next();

                    Pair<String, String> stringPair = new Pair<String, String>(pair.getFirst().getName(), pair
                            .getSecond().getValue());

                    stringsForTable.add(stringPair);
                }

                Collections.sort(stringsForTable, new AlphabeticalPairComparator());

                return stringsForTable.toArray();
            }
            return new Object[0];
        }
    }

    private static List<Pair<String, String>> getAllNotEmptyFields(Project project)
    {
        List<Pair<String, String>> fieldsToDisplay = new ArrayList<Pair<String, String>>();

        checkFieldIsNotEmpty(project.getName(), "Name", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getOwner(), "Owner", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getDescription(), "Description", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getCollectionType(), "Collection Type", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getRightsStatement(), "Rights Statement", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getAccessRights(), "Access Rights", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getLicence(), "Licence", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getIdentifier(), "Identifier", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getSubject(), "Subject", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getElectronicLocation(), "Electronic Location", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getPhysicalLocation(), "Physical Location", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getPlaceOrRegionName(), "Place Or Region Name", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getLatitudeLongitude(), "Latitude/Longitude", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getDatesOfCapture(), "Dates Of Capture", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getCitationInformation(), "Citation Information", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getRelatedParty(), "Related Party", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getRelatedActivity(), "Related Activity", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getRelatedInformation(), "Related Information", fieldsToDisplay);

        return fieldsToDisplay;
    }

    private static void checkFieldIsNotEmpty(final String fieldValue, final String fieldKey,
            List<Pair<String, String>> fieldsToDisplay)
    {
        if (!fieldValue.isEmpty())
        {
            fieldsToDisplay.add(new Pair<String, String>(fieldKey, fieldValue));
        }
    }

    @Override
    public void notHandled(String arg0, NotHandledException arg1)
    {
    }

    @Override
    public void postExecuteFailure(String arg0, ExecutionException arg1)
    {
    }

    @Override
    public void postExecuteSuccess(String arg0, Object arg1)
    {
        refresh();
    }

    @Override
    public void preExecute(String arg0, ExecutionEvent arg1)
    {
    }

    public void refresh()
    {
        this.tableViewer.refresh();
    }

    /**
     * Used to enable/disable this view from other views.
     * 
     * @param enabled
     *            {@code true} to enable this view.
     */
    public void setEnabled(final boolean enabled)
    {
        this.parent.setEnabled(enabled);
        this.parent.setVisible(enabled);
    }

}
