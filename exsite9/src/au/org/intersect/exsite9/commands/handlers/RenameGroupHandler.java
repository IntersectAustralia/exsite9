package au.org.intersect.exsite9.commands.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.service.IGroupService;

public class RenameGroupHandler implements IHandler
{

    @Override
    public void addHandlerListener(final IHandlerListener handlerListener)
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
        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event)
                .getActivePage().getSelection();
        final Object selectedObject = selection.getFirstElement();
        
        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
  
        InputDialog userInput = new InputDialog(HandlerUtil.getActiveWorkbenchWindow(event).getShell(),
                "Enter New Name", "Enter the name you wish to use", ((Group) selectedObject).getName(),
                new IInputValidator()
                {

                    @Override
                    public String isValid(String contents)
                    {
                        if (contents.trim().isEmpty())
                        {
                            return "Name must not be empty.";
                        }

                        if (contents.trim().length() >= 255)
                        {
                            return "Name is too long.";
                        }                         
                         
                         Group parent = groupService.getParent((Group) selectedObject);
                        
                         for (final Group existingChildGroup : parent.getGroups())
                         {
                             if (existingChildGroup.getName().equalsIgnoreCase(contents.trim()))
                             {
                             return "A Group with that name already exists at this level.";
                             }
                         }
                        return null;
                    }
                });
        userInput.open();

        if (userInput.getValue() != null && !userInput.getValue().trim().isEmpty())
        {
            groupService.renameGroup(((Group) selectedObject), userInput.getValue().trim());
        }

        return ((Group) selectedObject);

    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public boolean isHandled()
    {
        return true;
    }

    @Override
    public void removeHandlerListener(final IHandlerListener handlerListener)
    {
        // TODO Auto-generated method stub

    }

}
