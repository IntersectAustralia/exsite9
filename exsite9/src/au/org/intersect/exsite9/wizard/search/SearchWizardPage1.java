/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.search;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import au.org.intersect.exsite9.domain.ResearchFileSearchDefinition;

/**
 * Wizard page that is shown to the user when they want to search the project
 */
public class SearchWizardPage1 extends WizardPage
{
    private Composite container;
    private Text searchTerm;
    private Combo searchCategories;
    String[] searchFields;

    protected SearchWizardPage1(String[] searchFields)
    {
        super("Search");
        setTitle("Search");
        setDescription("Search across all files within this project.");
        this.searchFields = searchFields;
    }

    @Override
    public void createControl(final Composite parent)
    {
        this.container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout(2, false);
        this.container.setLayout(layout);
        
        final Label searchTermLabel = new Label(this.container, SWT.NULL);
        searchTermLabel.setText("Search Term:");
        
        searchTerm = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        searchTerm.addKeyListener(new KeyListener()
        {
            
            @Override
            public void keyReleased(KeyEvent e)
            {
                setPageComplete(searchTerm.getCharCount() > 0);
            }
            
            @Override
            public void keyPressed(KeyEvent e)
            {
            }
        });
        
        GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
        
        searchTerm.setLayoutData(gridData1);
        
        final Label searchInLabel = new Label(this.container, SWT.NULL);
        searchInLabel.setText("Search In:");  
        
        this.searchCategories = new Combo(this.container, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.SINGLE);
        searchCategories.setItems(searchFields);
        searchCategories.select(0);
        GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
        searchCategories.setLayoutData(gridData2);
        
        setControl(this.container);
        setPageComplete(false);
    }

    public String getSearchTerm()
    {
        return this.searchTerm.getText().trim();
    }
    
    public ResearchFileSearchDefinition getSearchCategory()
    {
        return ResearchFileSearchDefinition.fromString(this.searchCategories.getItem(this.searchCategories.getSelectionIndex()));
    }
}
