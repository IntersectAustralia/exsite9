/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.startup;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

/**
 * Initializes the database at startup, so there is no delay when opening an existing or creating a new project.
 */
public final class InitializeDB implements IStartup
{

    /**
     * @{inheritDoc}
     */
    @Override
    public void earlyStartup()
    {
        // Initialize the Entity Manager Factory and try open a connection to the database.
        final EntityManagerFactory emf = (EntityManagerFactory) PlatformUI.getWorkbench().getService(EntityManagerFactory.class);
        final EntityManager em = emf.createEntityManager();
        em.close();
    }
}
