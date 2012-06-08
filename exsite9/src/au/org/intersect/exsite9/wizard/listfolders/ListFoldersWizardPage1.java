package au.org.intersect.exsite9.wizard.listfolders;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import au.org.intersect.exsite9.domain.Folder;

public class ListFoldersWizardPage1 extends WizardPage
{
    private org.eclipse.swt.widgets.List folderList;

    private Composite container;

    private List<Folder> folders;

    public ListFoldersWizardPage1(final List<Folder> folders)
    {
        super("Folders");
        setTitle("Folders");
        setDescription("These folders are mapped to your project.");
        this.folders = folders;
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
        
        this.folderList = new org.eclipse.swt.widgets.List(this.container, SWT.BORDER | SWT.SINGLE | SWT.WRAP | SWT.V_SCROLL);

        for (final Folder folder : this.folders)
        {
            this.folderList.add(folder.getPath());
        }

        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);

        this.folderList.setLayoutData(multiLineGridData);

        setControl(this.container);
        setPageComplete(false);
    }
}
