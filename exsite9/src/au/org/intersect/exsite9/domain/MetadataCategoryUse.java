/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

/**
 * specifies whether a metadata category is required, recommended or optional
 */
public enum MetadataCategoryUse
{
    required, recommended, optional;
    
    public static String[] asArray()
    {
        String[] array = new String[values().length];
        
        int i=0;
        for(MetadataCategoryUse use : values())
        {
            array[i++] = use.toString();
        }
        
        return array;
    }
}
