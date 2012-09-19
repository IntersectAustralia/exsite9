package au.org.intersect.exsite9.commands.handlers;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.view.ProjectExplorerView;
import au.org.intersect.exsite9.view.ViewUtils;
import au.org.intersect.exsite9.wizard.search.SearchWizard;

public class SearchHandler implements IHandler
{

    @Override
    public void addHandlerListener(IHandlerListener arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project selectedProject = projectManager.getCurrentProject();
        if (selectedProject == null)
        {
            throw new IllegalStateException("Trying to search on a null project");
        }

        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
        final SearchWizard wizard = new SearchWizard(selectedProject);
        final WizardDialog wizardDialog = new WizardDialog(shell, wizard);
        wizardDialog.open();

        List<ResearchFile> searchResults = wizard.getSearchResults();
        
        final ProjectExplorerView projectExplorerView = (ProjectExplorerView) ViewUtils.getViewByID(PlatformUI
                .getWorkbench().getActiveWorkbenchWindow(), ProjectExplorerView.ID);
        
        projectExplorerView.setSelection(new StructuredSelection(searchResults));
        projectExplorerView.refresh();
        
        if (searchResults.isEmpty())
        {
            MessageDialog.openInformation(shell, "Search Results", "Your Search Returned 0 Results.");
        }
        
        return null;
    }

    @Override
    public boolean isEnabled()
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project currentProject = projectManager.getCurrentProject();
       
        return (currentProject == null) ? false : true;
    }

    @Override
    public boolean isHandled()
    {
        return true;
    }

    @Override
    public void removeHandlerListener(IHandlerListener arg0)
    {
        // TODO Auto-generated method stub
        
    }

}
