package au.org.intersect.exsite9;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 *
 * @author <a href="mailto:daniel@intersect.org.au">Daniel Yazbek</a>
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	/**
	 * Constructor
	 * @param configurer
	 */
    public ApplicationActionBarAdvisor(final IActionBarConfigurer configurer) {
        super(configurer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void makeActions(final IWorkbenchWindow window) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fillMenuBar(final IMenuManager menuBar) {
    }
    
}
