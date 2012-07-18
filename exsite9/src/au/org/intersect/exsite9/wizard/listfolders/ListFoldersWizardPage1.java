package au.org.intersect.exsite9.wizard.listfolders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.swt.widgets.DirectoryDialog;

import au.org.intersect.exsite9.domain.Folder;

public class ListFoldersWizardPage1 extends WizardPage implements SelectionListener
{
    private org.eclipse.swt.widgets.List folderList;

    private Composite container;
    private Button removeButton;
    private Button editButton;
    
    private List<Folder> folders;
    private List<Folder> deletedFolderList = new ArrayList<Folder>();
    private Map<Folder, String> foldersAndNewPaths = new HashMap<Folder, String>();

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
            this.folderList.add(folder.getFolder().getAbsolutePath());
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
        
        this.editButton = new Button(rowComp, SWT.PUSH);
        this.editButton.setText("Edit Path");
        this.editButton.addSelectionListener(this);
        this.editButton.setEnabled(false);

        this.folderList.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                enableOrDisableTheButtons();
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
            {   enableOrDisableTheButtons();
                return;
            }

            MessageDialog dialog = new MessageDialog(
                    null, "Remove folder", null, "All of the files you have added from this folder will be deleted.\nDo you want to continue?",
                    MessageDialog.QUESTION, new String[] {"Yes", "No"}, 0); // yes is the default
            int result = dialog.open();
            
            if(result == 0)
            {
                this.deletedFolderList.add(this.folders.get(this.folderList.getSelectionIndex()));
                this.folderList.remove(this.folderList.getSelectionIndex());
                setPageComplete(true);
            }
        }
        else if (e.widget.equals(editButton))
        {
            if (this.folderList.getSelectionCount() == 0)
            {   enableOrDisableTheButtons();
                return;
            }

            final DirectoryDialog directoryDialog = new DirectoryDialog(this.container.getShell(), SWT.OPEN);
            directoryDialog.setMessage("Choose the new path for this folder");
            directoryDialog.setText("Choose a folder");

            final String path = directoryDialog.open();
            if (path != null)
            {
                this.foldersAndNewPaths.put(this.folders.get(this.folderList.getSelectionIndex()), path);
                //TODO: edit path in UI
                setPageComplete(true);
            }
        }
                
                
                
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e)
    {
      
    }
    
    public List<String> getFolderList()
    {
        return Arrays.asList(this.folderList.getItems());
    }
    
    public Map<Folder, String> getFoldersAndTheirUpdatedPaths()
    {
        return this.foldersAndNewPaths;
    }
    
    private void enableOrDisableTheButtons()
    {
        removeButton.setEnabled(folderList.getSelectionCount() > 0);
        editButton.setEnabled(folderList.getSelectionCount() == 1);        
    }

    public List<Folder> getDeletedFolderList()
    {
        return this.deletedFolderList;
    }
}
