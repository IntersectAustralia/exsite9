package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.Set;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

public class AddMetadataCategoryWizardPage1 extends WizardPage implements KeyListener, SelectionListener
{
    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private Composite container;
    
    public ValidatingField<String> categoryNameField;
    public List metadataValuesList;
    private Button removeButton;
    
    private Project project;

    protected AddMetadataCategoryWizardPage1(Project project)
    {
        super("Add Metadata Category");
        setTitle("Add Metadata Category");
        setDescription("Please enter the title of the metadata category you wish to create");
        this.project = project;
    }

    @Override
    public void createControl(Composite parent)
    {
        this.container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 2;

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
                
                final Set<MetadataCategory> existingCategories = project.getMetadataCategories();
                
                for (final MetadataCategory existingCategory: existingCategories)
                {
                    if (existingCategory.getName().equalsIgnoreCase(contents.trim()))
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
        }, true, "");

        this.categoryNameField.getControl().addKeyListener(this);

        final Label metadataValuesLabel = new Label(this.container, SWT.NULL);
        metadataValuesLabel.setText("Metadata Values");

        this.metadataValuesList = new List(this.container, SWT.BORDER | SWT.SINGLE | SWT.WRAP | SWT.V_SCROLL);
        this.metadataValuesList.addSelectionListener(new SelectionListener()
        {
            
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                removeButton.setEnabled(metadataValuesList.getSelectionCount() > 0);
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
                // TODO Auto-generated method stub
                
            }
        });
        
        Button addButton = new Button(container, SWT.PUSH);
        addButton.setText("Add");
        addButton.addSelectionListener(this);
        
        this.removeButton = new Button(container, SWT.PUSH);
        removeButton.setText("Remove");
        removeButton.addSelectionListener(this);
        removeButton.setEnabled(false);
        

        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);

        this.categoryNameField.getControl().setLayoutData(singleLineGridData);
        this.metadataValuesList.setLayoutData(multiLineGridData);

        setControl(this.container);
        setPageComplete(false);
        
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        setPageComplete(this.categoryNameField.isValid());        
    }

    @Override
    public void widgetSelected(SelectionEvent e)
    {
        String selectionText = e.text;
        
        if ("Add...".equals(selectionText))
        {
            InputDialog userInput = new InputDialog(getShell(), "Enter Value", "Enter a new metadata value", "", new IInputValidator()
            {
                private String errorMessage;
                
                @Override
                public String isValid(String contents)
                {
                    if (contents.trim().isEmpty())
                    {
                        return this.errorMessage = "Value must not be empty.";
                    }
                    
                    if (contents.trim().length() >= 255)
                    {
                        return this.errorMessage = "Value is too long.";
                    }
                                        
                    final String[] listOfValues = metadataValuesList.getItems();
                    
                    for (final String existingValue: listOfValues)
                    {
                        if (existingValue.equalsIgnoreCase(contents.trim()))
                        {
                            return this.errorMessage = "A Value with that name already exists for this Category.";
                        }
                    }
                    return null;
                }
            });
            
            this.metadataValuesList.add(userInput.getValue().trim());
            
        }
        else if ("remove".equals(selectionText))
        {
            if (this.metadataValuesList.getSelectionCount() == 0)
            {
                return;
            }
            
            this.metadataValuesList.remove(this.metadataValuesList.getSelectionIndex());
        }     
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub
        
    }

}
