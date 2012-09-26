/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.util.List;

/**
 * used for both {@link Group} and {@link ResearchFile} to return their metadata associations
 */
public interface IMetadataAssignable
{
    List<MetadataAssociation> getMetadataAssociations();
}
