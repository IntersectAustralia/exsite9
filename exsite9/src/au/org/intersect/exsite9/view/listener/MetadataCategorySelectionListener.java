package au.org.intersect.exsite9.view.listener;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import au.org.intersect.exsite9.domain.MetadataCategory;

/**
 * A Selection Listener for a MetadataCategory capable of executing a command and providing the Metadata Category ID as a parameter to the command.
 */
public final class MetadataCategorySelectionListener implements SelectionListener
{
    private static final Logger LOG = Logger.getLogger(MetadataCategorySelectionListener.class);

    private final MetadataCategory metadataCategory;
    private final String commandId;
    private final String parameterId;

    public MetadataCategorySelectionListener(final MetadataCategory metadataCategory, final String commandId, final String parameterId)
    {
        this.metadataCategory = metadataCategory;
        this.commandId = commandId;
        this.parameterId = parameterId;
    }

    @Override
    public void widgetSelected(final SelectionEvent event)
    {
        // Fire the command including the metadata category ID as an argument.
        final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final ICommandService commandService = (ICommandService) window.getService(ICommandService.class);
        final Command command = commandService.getCommand(this.commandId);

        final IParameter iparam;
        try
        {
            iparam = command.getParameter(this.parameterId);
        }
        catch (final NotDefinedException e)
        {
            LOG.error("Expecting parameter " + this.parameterId + " on command " + this.commandId, e);
            return;
        }

        final Parameterization params = new Parameterization(iparam, metadataCategory.getId().toString());
        final ArrayList<Parameterization> parameters = new ArrayList<Parameterization>();
        parameters.add(params);

        // Build the parameterized command
        final ParameterizedCommand pc = new ParameterizedCommand(command, parameters.toArray(new Parameterization[parameters.size()]));

        // Execute the command
        final IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
        try
        {
            handlerService.executeCommand(pc, null);
        }
        catch (final ExecutionException e)
        {
            LOG.error("Cannot execute paramertized command", e);
        }
        catch (final NotDefinedException e)
        {
            LOG.error("Cannot execute paramertized command", e);
        }
        catch (final NotEnabledException e)
        {
            LOG.error("Cannot execute paramertized command", e);
        }
        catch (final NotHandledException e)
        {
            LOG.error("Cannot execute paramertized command", e);
        }
    }

    @Override
    public void widgetDefaultSelected(final SelectionEvent event)
    {
    }

}
