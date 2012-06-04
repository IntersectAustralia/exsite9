package au.org.intersect.exsite9.wizard.openproject;

import java.util.Iterator;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import au.org.intersect.exsite9.domain.Project;

public class OpenProjectWizardPage1 extends WizardPage implements SelectionListener
{
    private List projectList;

    private Composite container;

    public java.util.List<Project> projectItems;

    public Project selectedProject;

    /**
     * Constructor
     */
    public OpenProjectWizardPage1()
    {
        super("Open Project");
        setTitle("Open Project");
        setDescription("Please choose the project you would like to open.");
    }

    /**
     * @{inheritDoc
     */
    public void createControl(final Composite parent)
    {
        this.container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 2;

        final Label selectProjectLabel = new Label(this.container, SWT.NULL);
        selectProjectLabel.setText("Select Project");

        this.projectList = new List(this.container, SWT.BORDER | SWT.SINGLE | SWT.WRAP | SWT.V_SCROLL);

        for (Iterator<Project> iterator = projectItems.iterator(); iterator.hasNext();)
        {
            this.projectList.add(iterator.next().getName());
        }
        this.projectList.addSelectionListener(this);

        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);

        this.projectList.setLayoutData(multiLineGridData);

        setControl(this.container);
        setPageComplete(false);
    }

    @Override
    public void widgetSelected(SelectionEvent e)
    {
        boolean pageCompleted = false;
        for (Iterator<Project> iterator = projectItems.iterator(); iterator.hasNext();)
        {
           String actual = this.projectList.getSelection()[0];
           Project expected = iterator.next();
           String expectedString = expected.getName();
            if (actual.equals(expectedString))
            {
                selectedProject = expected;
                pageCompleted = true;
            }
        }
        setPageComplete(pageCompleted);

    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub

    }

}
