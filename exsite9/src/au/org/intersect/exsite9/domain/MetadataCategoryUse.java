package au.org.intersect.exsite9.domain;

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
