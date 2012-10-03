/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

/**
 * Types of search available to the user
 */
public enum SearchDefinition
{   
    ALL_METADATA_FIELDS("All Metadata Fields"), 
    METADATA_CATEGORY_NAME("Metadata Category Name"),
    METADATA_VALUE("Metadata Value"), 
    FREETEXT_METADATA_VALUE("Freetext Metadata Value"),
    METADATA_ATTRIBUTE("Metadata Attribute");
    
    private final String name;
    private SearchDefinition(final String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
    
    public static String[] toArray()
    {
        final String[] toReturn = new String[values().length];
        int i = 0;
        for (final SearchDefinition type : values())
        {
            toReturn[i] = type.toString();
            i++;
        }
        return toReturn;
    }

    public static SearchDefinition fromString(final String value)
    {
        for (final SearchDefinition type : values())
        {
            if (type.toString().equals(value))
            {
                return type;
            }
        }
        return null;
    }
}
