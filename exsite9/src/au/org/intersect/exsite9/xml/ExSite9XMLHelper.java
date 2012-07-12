package au.org.intersect.exsite9.xml;

public class ExSite9XMLHelper
{

    /**
     * Checks that string is a valid xml element name
     * 
     *   Names can contain letters, numbers and other characters
     *   Names cannot start with a number or punctuation character
     *   Names cannot start with the letters xml (case insensitive)
     *   Names cannot contain spaces
     * 
     * @param name The string to check
     * @return True if valid
     */
    public static boolean isValidElementName(String name)
    {
        
        // Can't start with punctuation or a number
        if ( ! name.matches("^[a-zA-Z].*"))
        {
            return false;
        }
        
        // Can't start with xml (in any case)
        if (name.toLowerCase().startsWith("xml"))
        {
            return false;
        }
        
        // Can't contain spaces, ampersands, greater than or less than
        if(name.matches("^[a-zA-Z].*[\\s&<>].*"))
        {
            return false;
        }
        
        return true;
    }
}
