/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.xml;

/**
 * Utilities used by exsite9 XML builders and parsers
 */
public class XMLUtils
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
    public static boolean isValidElementOrAttribute(String name)
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
        
        // Can't contain spaces, ampersands, greater than or less than, fwd or back slash
        // semi-colon, asterisk, open and close (), open and close {}, open and close []
        // plus sign, single quote, double quote, question mark, exclamation mark, back tick
        // tilde, at sign, dollar sign, percent sign, caret, equals, comma
        return !name.matches("^[a-zA-Z].*[\\s&<>/\\\\|;\\*\\(\\)\\{\\}\\[\\]\\+'\"\\?\\!`~@\\$\\%\\^=,].*");
    }
}
