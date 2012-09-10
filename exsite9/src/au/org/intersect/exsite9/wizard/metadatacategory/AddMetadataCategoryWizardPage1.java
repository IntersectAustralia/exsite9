package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.validators.MetadataCategoryNameValidator;
import au.org.intersect.exsite9.validators.MetadataValueValidator;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

public class AddMetadataCategoryWizardPage1 extends WizardPage implements KeyListener, SelectionListener
{
    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private Composite parent;
    private Composite container;

    private ValidatingField<String> categoryNameField;
    private Text descriptionText;
    private Combo typeDropDown;
    private Combo useDropDown;
    private org.eclipse.swt.widgets.List metadataValuesListWidget;
    private Button removeButton;
    private Button addButton;
    private Button editButton;
    private Button inextensibleCheckbox;

    private Schema schema;
    private List<MetadataValue> metadataValues;
    private MetadataCategory metadataCategory;
    
    private final List<Group> assignedGroups = new ArrayList<Group>();

    private final List<ResearchFile> assignedFiles = new ArrayList<ResearchFile>();
    private final List<MetadataValue> valuesToBeDisassociated = new ArrayList<MetadataValue>();

    protected AddMetadataCategoryWizardPage1(final String pageTitle, final String pageDescription, final Schema schema, final MetadataCategory metadataCategory,
            final List<MetadataValue> metadataValues)
    {
        super(pageTitle);    
        setTitle(pageTitle);
        setDescription(pageDescription);
        
        this.schema = schema;
        this.metadataValues = metadataValues;
        this.metadataCategory = metadataCategory;
    }

    @Override
    public void createControl(final Composite parent)
    {
        this.parent = parent;
        reload();
    }

    void reload()
    {
        if (this.container != null)
        {
            this.container.dispose();
        }
        this.container = new Composite(parent, SWT.NULL);

        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 3;

        this.stringValidatorToolkit = new StringValidationToolkit(SWT.TOP | SWT.LEFT, 1, true);
        this.stringValidatorToolkit.setDefaultErrorMessageHandler(this.errorMessageHandler);

        final Label projectNameLabel = new Label(this.container, SWT.NULL);
        projectNameLabel.setText("Category Title");

        categoryNameField = this.stringValidatorToolkit.createTextField(this.container, new MetadataCategoryNameValidator(this.schema.getMetadataCategories(), this.metadataCategory),
            true, metadataCategory == null ? "" : metadataCategory.getName());

        this.categoryNameField.getControl().addKeyListener(this);

        // empty cell due to having 3 columns below
        new Label(container, SWT.NULL);

        
        final Label categoryDescriptionLabel = new Label(this.container, SWT.NULL);
        categoryDescriptionLabel.setText("Description");

        this.descriptionText = new Text(this.container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        if(this.metadataCategory != null){
            this.descriptionText.setText((this.metadataCategory.getDescription()==null) ? "":this.metadataCategory.getDescription() );
        }
        this.descriptionText.addKeyListener(this);
        
        // we have 3 columns, we want description to be 3 rows big
        new Label(container, SWT.NULL);
        
        new Label(container, SWT.NULL);
        new Label(container, SWT.NULL);
        new Label(container, SWT.NULL);
        
        new Label(container, SWT.NULL);
        new Label(container, SWT.NULL);
        new Label(container, SWT.NULL);
        
        final Label typeLabel = new Label(this.container, SWT.NULL);
        typeLabel.setText("Type");

        this.typeDropDown = new Combo(this.container, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.SINGLE);
        this.typeDropDown.setItems(MetadataCategoryType.toArray());
        this.typeDropDown.select(MetadataCategoryType.CONTROLLED_VOCABULARY.ordinal());

        this.typeDropDown.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(final SelectionEvent event)
            {
                final int selectedIndex = typeDropDown.getSelectionIndex();
                if (selectedIndex == MetadataCategoryType.CONTROLLED_VOCABULARY.ordinal())
                {
                    addButton.setEnabled(true);
                    metadataValuesListWidget.setEnabled(true);
                }
                else
                {
                    addButton.setEnabled(false);
                    metadataValuesListWidget.setEnabled(false);
                }
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent event)
            {
            }
        });

        // empty cell due to having 3 columns below
        new Label(container, SWT.NULL);
        
        final Label useLabel = new Label(this.container, SWT.NULL);
        useLabel.setText("Use");

        this.useDropDown = new Combo(this.container, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.SINGLE);
        this.useDropDown.setItems(MetadataCategoryUse.asArray());
        this.useDropDown.select(MetadataCategoryUse.optional.ordinal());
        
        if (this.metadataCategory != null)
        {
            for (int i = 0; i < this.useDropDown.getItemCount(); i++)
            {
                if (this.useDropDown.getItem(i).equals(this.metadataCategory.getUse().toString()))
                {
                    this.useDropDown.select(i);
                }
            }
        }
        
        this.useDropDown.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                setPageComplete(categoryNameField.isValid()); 
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
      
            }
        });

        // empty cell due to having 3 columns below
        new Label(container, SWT.NULL);
                
        final Label inextensibleLabel = new Label(this.container, SWT.NULL);
        inextensibleLabel.setText("Inextensible");
        inextensibleLabel.setToolTipText("An inextensible category can NOT have values added to it at a later date");
        
        this.inextensibleCheckbox = new Button(this.container, SWT.CHECK);
        
        if (this.metadataCategory != null)
        {
            this.inextensibleCheckbox.setSelection(this.metadataCategory.isInextensible());
        }
        
        this.inextensibleCheckbox.addSelectionListener(new SelectionListener()
        {
            
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                setPageComplete(categoryNameField.isValid());                
                
                if (!inextensibleCheckbox.getSelection())
                {
                    metadataValuesListWidget.setEnabled(true);
                    addButton.setEnabled(true);
                }
                else if (inextensibleCheckbox.getSelection())
                {
                    metadataValuesListWidget.setEnabled(false);
                    addButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    editButton.setEnabled(false);
                }
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });

        // empty cell due to having 3 columns below
        new Label(container, SWT.NULL);
        
        final Label metadataValuesLabel = new Label(this.container, SWT.NULL);
        metadataValuesLabel.setText("Metadata Values");

        this.metadataValuesListWidget = new org.eclipse.swt.widgets.List(this.container, SWT.BORDER | SWT.SINGLE | SWT.WRAP | SWT.V_SCROLL);

        if (this.metadataCategory != null && this.metadataCategory.getType() == MetadataCategoryType.CONTROLLED_VOCABULARY)
        {
            for (MetadataValue value : this.metadataValues) 
            {
                this.metadataValuesListWidget.add(value.getValue());
            }
        }

        this.metadataValuesListWidget.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                removeButton.setEnabled(metadataValuesListWidget.getSelectionCount() > 0);
                editButton.setEnabled(metadataValuesListWidget.getSelectionCount() > 0);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e)
            {
            }
        });

        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);
        final GridData descriptionMultiLineGridData = new GridData(GridData.FILL_BOTH);
        descriptionMultiLineGridData.verticalSpan = 4;
        
        this.categoryNameField.getControl().setLayoutData(singleLineGridData);
        this.descriptionText.setLayoutData(descriptionMultiLineGridData);
        this.metadataValuesListWidget.setLayoutData(multiLineGridData);

        Composite rowComp = new Composite(container, SWT.NULL);

        RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        rowLayout.pack = false;
        rowLayout.justify = true;
        rowComp.setLayout(rowLayout);

        addButton = new Button(rowComp, SWT.PUSH);
        addButton.setText("Add...");
        addButton.addSelectionListener(this);

        this.removeButton = new Button(rowComp, SWT.PUSH);
        removeButton.setText("Remove");
        removeButton.addSelectionListener(this);
        removeButton.setEnabled(false);
        
        this.editButton = new Button(rowComp, SWT.PUSH);
        editButton.setText("Edit...");
        editButton.addSelectionListener(this);
        editButton.setEnabled(false);

        if (this.metadataCategory != null)
        {
            final int selectedTypeIndex = this.metadataCategory.getType().ordinal();
            typeDropDown.select(selectedTypeIndex);
            typeDropDown.setEnabled(false);
            if (selectedTypeIndex == MetadataCategoryType.CONTROLLED_VOCABULARY.ordinal())
            {
                addButton.setEnabled(true);
                metadataValuesListWidget.setEnabled(true);
            }
            else
            {
                addButton.setEnabled(false);
                metadataValuesListWidget.setEnabled(false);
            }
            
            if (this.metadataCategory.isInextensible() && !this.metadataCategory.isImported())
            {
                this.metadataValuesListWidget.setEnabled(false);
                this.addButton.setEnabled(false);
                this.removeButton.setEnabled(false);
                this.editButton.setEnabled(false);
            }
            else if (!this.metadataCategory.isInextensible() && this.metadataCategory.isImported())
            {
                this.inextensibleCheckbox.setEnabled(false);
            }
            else if (this.metadataCategory.isInextensible() && this.metadataCategory.isImported())
            {
                this.inextensibleCheckbox.setEnabled(false);
                this.metadataValuesListWidget.setEnabled(false);
                this.addButton.setEnabled(false);
                this.removeButton.setEnabled(false);
                this.editButton.setEnabled(false);
            }
        }

        final RowData addButtonGridData = new RowData();
        final RowData removeButtonGridData = new RowData();
        final RowData editButtonGridData = new RowData();

        this.addButton.setLayoutData(addButtonGridData);
        this.removeButton.setLayoutData(removeButtonGridData);
        this.editButton.setLayoutData(editButtonGridData);

        this.container.pack();
        this.container.layout();
        this.parent.layout();

        setControl(this.container);
        setPageComplete(false);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        setPageComplete(this.categoryNameField.isValid());
    }

    @Override
    public void widgetSelected(SelectionEvent e)
    {
        if (e.widget.equals(addButton))
        {
            final InputDialog userInput = new InputDialog(getShell(), "Enter Value", "Enter a new metadata value", "", new IInputValidator()
            {
                @Override
                public String isValid(final String contents)
                {
                    final MetadataValueValidator validator = new MetadataValueValidator(metadataValues);
                    if (validator.isValid(contents))
                    {
                        return null;
                    }
                    return validator.getErrorMessage();
                }
            });
            userInput.open();

            if (userInput.getValue() == null || userInput.getValue().trim().isEmpty())
            {
                return;
            }

            this.metadataValuesListWidget.add(userInput.getValue().trim());
            this.metadataValues.add(new MetadataValue(userInput.getValue().trim()));

        }
        else if (e.widget.equals(removeButton))
        {
            if (this.metadataValuesListWidget.getSelectionCount() == 0)
            {
                removeButton.setEnabled(metadataValuesListWidget.getSelectionCount() > 0);
                editButton.setEnabled(metadataValuesListWidget.getSelectionCount() > 0);
                return;
            }
          
            final int selectedIndex = this.metadataValuesListWidget.getSelectionIndex();

            // Check to see if the metadata value is assigned to anything - and throw a warning if it is being deleted!
            if (this.metadataCategory != null)
            {
                final MetadataValue metadataValueToDelete = this.metadataValues.get(selectedIndex);
                final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
                final IResearchFileService fileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);

                assignedGroups.clear();
                assignedFiles.clear();
                assignedGroups.addAll(groupService.getGroupsWithAssociatedMetadata(metadataCategory, metadataValueToDelete));
                assignedFiles.addAll(fileService.getResearchFilesWithAssociatedMetadata(metadataCategory, metadataValueToDelete));
                
                if (!assignedGroups.isEmpty() || !assignedFiles.isEmpty())
                {
                    String messageString = null;
                    
                    //when the value is associated with both groups and files then display this
                    if(!assignedGroups.isEmpty() && !assignedFiles.isEmpty())
                    {
                        messageString = "Metadata value '" + metadataValueToDelete.getValue() + "' is currently associated with "
                                + assignedGroups.size() + " group(s) and " + assignedFiles.size() + " file(s). Removing this value will cause these associations to also be deleted. Are you sure you want to proceed?";
                    }
                    //when the value is associated only with groups and not files then display this
                    else if(!assignedGroups.isEmpty() && assignedFiles.isEmpty())
                    {
                        messageString = "Metadata value '" + metadataValueToDelete.getValue() + "' is currently associated with "
                                + assignedGroups.size() + " group(s). Removing this value will cause these associations to also be deleted. Are you sure you want to proceed?";
                    }
                    //when the value is associated only with files and not groups then display this
                    else if(assignedGroups.isEmpty() && !assignedFiles.isEmpty())
                    {
                        messageString = "Metadata value '" + metadataValueToDelete.getValue() + "' is currently associated with "
                                + assignedFiles.size() + " file(s). Removing this value will cause these associations to also be deleted. Are you sure you want to proceed?";
                    }
                    
                    final boolean deleteAssociations = MessageDialog.openConfirm(getShell(), "", messageString);
                    if (!deleteAssociations)
                    {
                        return;
                    }
                    else if (deleteAssociations)
                    {
                        this.valuesToBeDisassociated.add(metadataValueToDelete);
                    }
                }
            }
            this.metadataValues.remove(selectedIndex);
            this.metadataValuesListWidget.remove(selectedIndex);
            
            //disable remove and edit buttons as the value is no longer there for selection
            removeButton.setEnabled(metadataValuesListWidget.getSelectionCount() > 0);
            editButton.setEnabled(metadataValuesListWidget.getSelectionCount() > 0);
        }
        else if (e.widget.equals(editButton))
        {
            if(this.metadataValuesListWidget.getSelectionCount() == 0)
            {
                removeButton.setEnabled(metadataValuesListWidget.getSelectionCount() > 0);
                editButton.setEnabled(metadataValuesListWidget.getSelectionCount() > 0);
                return;
            }
            
            InputDialog userInput = new InputDialog(getShell(), "Edit Value", "Enter the amended metadata value", this.metadataValuesListWidget.getSelection()[0],
                    new IInputValidator()
                    {
                        @Override
                        public String isValid(String contents)
                        {
                            if (contents.trim().isEmpty())
                            {
                                return "Value must not be empty.";
                            }

                            if (contents.trim().length() >= 255)
                            {
                                return "Value is too long.";
                            }

                            final String[] listOfValues = metadataValuesListWidget.getItems();

                            for (final String existingValue : listOfValues)
                            {
                                if (existingValue.equalsIgnoreCase(contents.trim()))
                                {
                                    return "A Value with that name already exists for this Category.";
                                }
                            }
                            return null;
                        }
                    });
            userInput.open();

            if (userInput.getValue() == null || userInput.getValue().trim().isEmpty())
            {
                return;
            }
            
            this.metadataValues.get(this.metadataValuesListWidget.getSelectionIndex()).setValue(userInput.getValue().trim());
            this.metadataValuesListWidget.setItem(this.metadataValuesListWidget.getSelectionIndex(), userInput.getValue().trim());
        }
        
        setPageComplete(this.categoryNameField.isValid());
    }
    
    @Override
    public IWizardPage getPreviousPage()
    {
        return null;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e)
    {

    }

    public String getMetadataCategoryName()
    {
        return this.categoryNameField.getContents().trim();
    }

    public String getCategoryDescription(){
        return this.descriptionText.getText().trim();
    }
    
    public MetadataCategoryType getMetadataCategoryType()
    {
        return MetadataCategoryType.values()[this.typeDropDown.getSelectionIndex()];
    }

    public MetadataCategoryUse getMetadataCategoryUse()
    {
        return MetadataCategoryUse.values()[this.useDropDown.getSelectionIndex()];
    }
    
    public List<MetadataValue> getMetadataCategoryValues()
    {
        return this.metadataValues;
    }
    
    public List<Group> getAssignedGroups()
    {
        return assignedGroups;
    }

    public List<ResearchFile> getAssignedFiles()
    {
        return assignedFiles;
    }

    public List<MetadataValue> getValuesToBeDisassociated()
    {
        return valuesToBeDisassociated;
    }

    public boolean getIsInextensible()
    {
       return this.inextensibleCheckbox.getSelection();
    }
    
    void setMetadataCategory(final MetadataCategory metadataCategory)
    {
        this.metadataCategory = metadataCategory;
    }

    void setMetadataValues(final List<MetadataValue> metadataValues)
    {
        this.metadataValues = metadataValues;
    }
}
