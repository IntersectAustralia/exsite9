/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.FieldOfResearch;

/**
 * Provides access to {@link FieldOfResearch}s.
 */
public interface IFieldOfResearchService
{
    /**
     * Obtains all the fields of research.
     * @return The fields of research.
     */
    public List<FieldOfResearch> getAll();
}
