package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.view.widgets.MetadataValuesListWidget;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

public class AddMetadataCategoryWizardPage1 extends WizardPage implements KeyListener, SelectionListener
{
    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private Composite container;

    private ValidatingField<String> categoryNameField;
    private org.eclipse.swt.widgets.List metadataValuesListWidget;
    private Button removeButton;
    private Button addButton;
    private Button editButton;

    private Project project;
    private List<MetadataValue> metadataValues;
    private String metadataCategoryName;

    protected AddMetadataCategoryWizardPage1(String pageTitle, String pageDescription, Project project, String metadataCategoryName,
            List<MetadataValue> metadataValues)
    {
        super(pageTitle);    
        setTitle(pageTitle);
        setDescription(pageDescription);
        
        this.project = project;
        this.metadataValues = metadataValues;
        this.metadataCategoryName = metadataCategoryName;
    }

    @Override
    public void createControl(final Composite parent)
    {
        this.container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 3;

        this.stringValidatorToolkit = new StringValidationToolkit(SWT.TOP | SWT.LEFT, 1, true);
        this.stringValidatorToolkit.setDefaultErrorMessageHandler(this.errorMessageHandler);

        final Label projectNameLabel = new Label(this.container, SWT.NULL);
        projectNameLabel.setText("Category Title");

        categoryNameField = this.stringValidatorToolkit.createTextField(this.container, new IFieldValidator<String>()
        {
            private String errorMessage;

            @Override
            public boolean warningExist(final String conents)
            {
                return false;
            }

            @Override
            public boolean isValid(final String contents)
            {
                if (contents.trim().isEmpty())
                {
                    this.errorMessage = "Category title must not be empty.";
                    return false;
                }

                if (contents.trim().length() >= 255)
                {
                    this.errorMessage = "Category title is too long.";
                    return false;
                }

                final List<MetadataCategory> existingCategories = project.getMetadataCategories();

                for (final MetadataCategory existingCategory : existingCategories)
                {
                    if (existingCategory.getName().equalsIgnoreCase(contents.trim())
                            && !contents.trim().equalsIgnoreCase(metadataCategoryName))
                    {
                        this.errorMessage = "A Category with that name already exists for this project.";
                        return false;
                    }
                }
                return true;
            }

            @Override
            public String getWarningMessage()
            {
                return "";
            }

            @Override
            public String getErrorMessage()
            {
                return this.errorMessage;
            }
        }, true, metadataCategoryName);

        this.categoryNameField.getControl().addKeyListener(this);

        // empty cell due to having 3 columns below
        new Label(container, SWT.NULL);

        final Label metadataValuesLabel = new Label(this.container, SWT.NULL);
        metadataValuesLabel.setText("Metadata Values");

        this.metadataValuesListWidget = new org.eclipse.swt.widgets.List(this.container, SWT.BORDER | SWT.SINGLE | SWT.WRAP
                | SWT.V_SCROLL);
        
        for (MetadataValue value : this.metadataValues) 
        {
			this.metadataValuesListWidget.add(value.getValue());
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

        this.categoryNameField.getControl().setLayoutData(singleLineGridData);
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

        final RowData addButtonGridData = new RowData();
        final RowData removeButtonGridData = new RowData();
        final RowData editButtonGridData = new RowData();

        this.addButton.setLayoutData(addButtonGridData);
        this.removeButton.setLayoutData(removeButtonGridData);
        this.editButton.setLayoutData(editButtonGridData);

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
            InputDialog userInput = new InputDialog(getShell(), "Enter Value", "Enter a new metadata value", "",
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

            this.metadataValuesListWidget.add(userInput.getValue().trim());
            this.metadataValues.add(new MetadataValue(userInput.getValue().trim()));

        }
        else if (e.widget.equals(removeButton))
        {
            if (this.metadataValuesListWidget.getSelectionCount() == 0)
            {
                return;
            }

            this.metadataValues.remove(this.metadataValuesListWidget.getSelectionIndex());
            this.metadataValuesListWidget.remove(this.metadataValuesListWidget.getSelectionIndex());
        }
        else if (e.widget.equals(editButton))
        {
            if(this.metadataValuesListWidget.getSelectionCount() == 0)
            {
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

            if (userInput.getValue() == null || userInput.getValue().trim() == "")
            {
                return;
            }
            
            this.metadataValues.get(this.metadataValuesListWidget.getSelectionIndex()).setValue(userInput.getValue().trim());
            this.metadataValuesListWidget.setItem(this.metadataValuesListWidget.getSelectionIndex(), userInput.getValue().trim());
        }
        
        setPageComplete(this.categoryNameField.isValid());
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e)
    {

    }

    public String getMetadataCategoryName()
    {
        return this.categoryNameField.getContents().trim();
    }

    public List<MetadataValue> getMetadataCategoryValues()
    {
        return this.metadataValues;
    }
}
