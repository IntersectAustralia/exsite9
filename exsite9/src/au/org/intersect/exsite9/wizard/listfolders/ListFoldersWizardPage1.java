package au.org.intersect.exsite9.wizard.listfolders;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import au.org.intersect.exsite9.domain.Folder;

public class ListFoldersWizardPage1 extends WizardPage implements SelectionListener
{
    private org.eclipse.swt.widgets.List folderList;

    private Composite container;
    private Button removeButton;
    
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

        Composite rowComp = new Composite(container, SWT.NULL);

        RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        rowLayout.pack = false;
        rowLayout.justify = true;
        rowComp.setLayout(rowLayout);

        this.removeButton = new Button(rowComp, SWT.PUSH);
        removeButton.setText("Remove");
        removeButton.addSelectionListener(this);
        removeButton.setEnabled(false);

        this.folderList.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                removeButton.setEnabled(folderList.getSelectionCount() > 0);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e)
            {
            }
        });
        
        setControl(this.container);
        setPageComplete(false);
    }

    @Override
    public void widgetSelected(SelectionEvent e)
    {
        if (e.widget.equals(removeButton))
        {
            if (this.folderList.getSelectionCount() == 0)
            {
                return;
            }

            MessageDialog dialog = new MessageDialog(
                    null, "Remove folder", null, "All of the files you have added from this folder will be deleted.\nDo you want to continue?",
                    MessageDialog.QUESTION, new String[] {"Yes", "No"}, 0); // yes is the default
            int result = dialog.open();
            
            if(result == 0)
            {
                this.folderList.remove(this.folderList.getSelectionIndex());
                setPageComplete(true);
            }
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub
        
    }
}
