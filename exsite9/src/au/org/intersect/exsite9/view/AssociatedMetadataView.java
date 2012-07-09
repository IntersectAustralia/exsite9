package au.org.intersect.exsite9.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import au.org.intersect.exsite9.util.Pair;

public final class AssociatedMetadataView extends ViewPart implements ISelectionListener
{
    public static final String ID = AssociatedMetadataView.class.getName();
    private TableViewer tableViewer;

    public AssociatedMetadataView()
    {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void createPartControl(final Composite parent)
    {
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


        getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(ProjectExplorerView.ID, this);

    }

    @Override
    public void setFocus()
    {
        // TODO Auto-generated method stub

    }

  

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
    {
        this.tableViewer.setInput(selection);
    }

    static class AssociatedMetadataContentProvider implements IStructuredContentProvider // maybe ILazyContentProvider
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
            // TODO Auto-generated method stub

        }

        @Override
        public void inputChanged(Viewer arg0, Object arg1, Object arg2)
        {
            // TODO Auto-generated method stub

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
                List<Pair<String, String>> stringsForTable = new ArrayList<Pair<String,String>>();

                final Set<Pair<MetadataCategory, MetadataValue>> metadataToBeMapped = new HashSet<Pair<MetadataCategory, MetadataValue>>(
                        MetadataAssignableUtils.getCategoryToValueMapping(this.selectedMetadataAssignables.get(0)));

                for (int i = 1; i < this.selectedMetadataAssignables.size(); i++)
                {
                    metadataToBeMapped.retainAll(MetadataAssignableUtils
                            .getCategoryToValueMapping(this.selectedMetadataAssignables.get(i)));
                }

                for (Iterator<Pair<MetadataCategory, MetadataValue>> iterator = metadataToBeMapped.iterator(); iterator.hasNext();)
                {
                    Pair<MetadataCategory, MetadataValue> pair = (Pair<MetadataCategory, MetadataValue>) iterator
                            .next();
                    
                    Pair<String, String> stringPair = new Pair<String, String>(pair.getFirst().getName(), pair.getSecond().getValue());
                    stringsForTable.add(stringPair);

                }
                return stringsForTable.toArray();
            }
            return new Object[0];
        }
    }

    private static List<Pair<String, String>> getAllNotEmptyFields(Project project)
    {
        List<Pair<String, String>> fieldsToDisplay = new ArrayList<Pair<String, String>>();

        checkFieldIsNotEmpty(project.getAccessRights(), "Access Rights", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getCitationInformation(), "Citation Information", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getCollectionType(), "Collection Type", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getDatesOfCapture(), "Dates Of Capture", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getDescription(), "Description", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getElectronicLocation(), "Electronic Location", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getIdentifier(), "Identifier", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getLatitudeLongitude(), "Latitude/Longitude", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getLicence(), "Licence", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getName(), "Name", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getOwner(), "Owner", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getPhysicalLocation(), "Physical Location", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getPlaceOrRegionName(), "Place Or Region Name", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getRelatedActivity(), "Related Activity", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getRelatedInformation(), "Related Information", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getRelatedParty(), "Related Party", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getRightsStatement(), "Rights Statement", fieldsToDisplay);
        checkFieldIsNotEmpty(project.getSubject(), "Subject", fieldsToDisplay);

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

}
