/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.domain.utils.AlphabeticalMetadataCategoryComparator;

/**
 * 
 */
public final class ListMetadataCategoriesWizardPage extends WizardPage implements SelectionListener
{
    private final Schema schema;
    private final List<MetadataCategory> metadataCategories;
    private final List<MetadataCategory> metadataCategoriesToDelete = new ArrayList<MetadataCategory>();

    private Composite container;
    private org.eclipse.swt.widgets.List metadataCategoriesList;
    private Button removeButton;
    private MetadataCategory selectedMetadataCategory;

    private EditMetadataCategoryWizard wizard;
    private AddMetadataCategoryWizardPage1 nextPage;

    protected ListMetadataCategoriesWizardPage(final Schema schema)
    {
        super("Metadata Categories");
        setTitle("Metadata Categories");
        setDescription("Choose a metadata category to edit");
        this.schema = schema;
        this.metadataCategories = new ArrayList<MetadataCategory>(this.schema.getMetadataCategories());
        Collections.sort(this.metadataCategories, new AlphabeticalMetadataCategoryComparator());
    }

    @Override
    public void createControl(final Composite parent)
    {
        wizard = (EditMetadataCategoryWizard)getWizard();
        nextPage = wizard.getEditCategoryPage();

        this.container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 2;
        
        this.metadataCategoriesList = new org.eclipse.swt.widgets.List(this.container, SWT.BORDER | SWT.SINGLE | SWT.WRAP | SWT.V_SCROLL);

        for (final MetadataCategory metadataCategory : this.metadataCategories)
        {
            this.metadataCategoriesList.add(metadataCategory.getName());
        }

        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);
        this.metadataCategoriesList.setLayoutData(multiLineGridData);
        this.metadataCategoriesList.addSelectionListener(this);

        final Composite rowComp = new Composite(container, SWT.NULL);

        final RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        rowLayout.pack = false;
        rowLayout.justify = true;
        rowComp.setLayout(rowLayout);

        this.removeButton = new Button(rowComp, SWT.PUSH);
        this.removeButton.setText("Remove");
        this.removeButton.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                final int selectedIndex = metadataCategoriesList.getSelectionIndex();
                final MetadataCategory metadataCategoryToDelete = metadataCategories.get(selectedIndex);
                metadataCategoriesList.remove(selectedIndex);
                metadataCategoriesToDelete.add(metadataCategoryToDelete);
                removeButton.setEnabled(false);
                selectedMetadataCategory = null;
                nextPage.setPageComplete(true);
                setPageComplete(true);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });
        this.removeButton.setEnabled(false);

        setControl(this.container);
        setPageComplete(false);
    }

    @Override
    public void widgetSelected(final SelectionEvent event)
    {
        setWizardButtonState();
    }

    private void setWizardButtonState()
    {
        if (this.metadataCategoriesList.getSelectionCount() == 0)
        {
            this.removeButton.setEnabled(false);
            this.selectedMetadataCategory = null;
            nextPage.setPageComplete(true);
            setPageComplete(!this.metadataCategoriesToDelete.isEmpty());
        }
        else
        {
            final int selectedIndex = this.metadataCategoriesList.getSelectionIndex();
            this.selectedMetadataCategory = this.metadataCategories.get(selectedIndex);
            this.removeButton.setEnabled(true);
            nextPage.setPageComplete(!this.metadataCategoriesToDelete.isEmpty());
            setPageComplete(true);
        }
    }

    @Override
    public void widgetDefaultSelected(final SelectionEvent event)
    {
    }

    public IWizardPage getNextPage()
    {
        System.out.println("GET NEXT PAGE called!");

        if (this.selectedMetadataCategory == null)
        {
            System.out.println("NULL");
            return null;
        }

        // Configure the next page with the selected metadata category.
        nextPage.setMetadataCategory(selectedMetadataCategory);
        nextPage.setMetadataValues(selectedMetadataCategory.getValues());
        nextPage.reload();
        return nextPage;
    }

    MetadataCategory getSelectedMetadataCategory()
    {
        return this.selectedMetadataCategory;
    }

    List<MetadataCategory> getMetadataCategoriesToDelete()
    {
        return this.metadataCategoriesToDelete;
    }
}