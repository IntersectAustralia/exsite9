package au.org.intersect.exsite9.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExSite9XMLHelperUnitTest
{

    @Test
    public void testIsValidElementName()
    {
        String name;
        
        name = "xmlElement";
        assertFalse("Name begins with xml",XMLUtils.isValidElementName(name));

        name = "xMlElement";
        assertFalse("Name begins with xml",XMLUtils.isValidElementName(name));

        name = "1element";
        assertFalse("Name starts with a number",XMLUtils.isValidElementName(name));

        name = "?element";
        assertFalse("Name starts with punctuation",XMLUtils.isValidElementName(name));
        
        name = "cat 1";
        assertFalse("Name includes spaces",XMLUtils.isValidElementName(name));
        
        name = "cat&dog";
        assertFalse("Name includes ampersand",XMLUtils.isValidElementName(name));
        
        name = "greater>than";
        assertFalse("Name includes gretaer than",XMLUtils.isValidElementName(name));
        
        name = "less<than";
        assertFalse("Name includes less than",XMLUtils.isValidElementName(name));
        
        name = "fwd/slash";
        assertFalse("Name includes fwd slash",XMLUtils.isValidElementName(name));
        
        name = "back\\slash";
        assertFalse("Name includes back slash",XMLUtils.isValidElementName(name));
        
        name = "pipe|symbol";
        assertFalse("Name includes pipe symbol",XMLUtils.isValidElementName(name));
        
        name = "aster*isk";
        assertFalse("Name includes asterisk",XMLUtils.isValidElementName(name));
        
        name = "semi;colon";
        assertFalse("Name includes semi colon",XMLUtils.isValidElementName(name));
        
        name = "fwd(paren";
        assertFalse("Name includes (",XMLUtils.isValidElementName(name));
        
        name = "close)paren";
        assertFalse("Name includes )",XMLUtils.isValidElementName(name));
        
        name = "fwd{brace";
        assertFalse("Name includes {",XMLUtils.isValidElementName(name));
        
        name = "close}brace";
        assertFalse("Name includes }",XMLUtils.isValidElementName(name));
        
        
        name = "element";
        assertTrue("Name is all letters",XMLUtils.isValidElementName(name));
    }
}
