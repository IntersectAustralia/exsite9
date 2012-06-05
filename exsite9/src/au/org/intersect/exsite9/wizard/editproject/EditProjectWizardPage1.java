package au.org.intersect.exsite9.wizard.editproject;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

public class EditProjectWizardPage1 extends WizardPage implements KeyListener
{
    private ValidatingField<String> projectNameField;

    private Text projectDescriptionText;
    private Text projectOwnerText;

    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private Composite container;

    public Project project;

    protected EditProjectWizardPage1(final Project selectedProject)
    {
        super("Edit Project");
        setTitle("Edit Project");
        setDescription("Please amend the details of your project.");
        this.project = selectedProject;
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
        projectNameLabel.setText("Project Name");

        projectNameField = this.stringValidatorToolkit.createTextField(this.container, new IFieldValidator<String>()
        {
            @Override
            public boolean warningExist(final String conents)
            {
                return false;
            }

            @Override
            public boolean isValid(final String contents)
            {
                return !(contents.trim().isEmpty());
            }

            @Override
            public String getWarningMessage()
            {
                return "";
            }

            @Override
            public String getErrorMessage()
            {
                return "Project name must not be empty.";
            }
        }, true, project.getName());

        this.projectNameField.getControl().addKeyListener(this);

        final Label projectOwnerLabel = new Label(this.container, SWT.NULL);
        projectOwnerLabel.setText("Project Owner");

        this.projectOwnerText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectOwnerText.setText(project.getOwner());
        this.projectOwnerText.addKeyListener(this);

        final Label projectDescriptionLabel = new Label(this.container, SWT.NULL);
        projectDescriptionLabel.setText("Project Description");

        this.projectDescriptionText = new Text(this.container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        this.projectDescriptionText.setText(project.getDescription());
        this.projectDescriptionText.addKeyListener(this);

        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);

        this.projectNameField.getControl().setLayoutData(singleLineGridData);
        this.projectOwnerText.setLayoutData(singleLineGridData);
        this.projectDescriptionText.setLayoutData(multiLineGridData);

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
        setPageComplete(this.projectNameField.isValid());
    }

    public String getProjectName()
    {
        return this.projectNameField.getContents().trim();
    }

    public String getProjectOwner()
    {
        return this.projectOwnerText.getText().trim();
    }

    public String getProjectDescription()
    {
        return this.projectDescriptionText.getText().trim();
    }

}
