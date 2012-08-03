/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

/**
 * A Metadata Category type.
 */
public enum MetadataCategoryType
{
    /**
     * A Metadata Category type that holds controlled metadata value fields.
     */
    CONTROLLED_VOCABULARY("Controlled Vocabulary"),

    /**
     * A Metadata Category type that holds a free-text metadata value.
     */
    FREETEXT("Free Text");

    private final String name;

    private MetadataCategoryType(final String name)
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
        for (final MetadataCategoryType type : values())
        {
            toReturn[i] = type.toString();
            i++;
        }
        return toReturn;
    }

    public static MetadataCategoryType fromString(final String value)
    {
        for (final MetadataCategoryType type : values())
        {
            if (type.toString().equals(value))
            {
                return type;
            }
        }
        return null;
    }
}
