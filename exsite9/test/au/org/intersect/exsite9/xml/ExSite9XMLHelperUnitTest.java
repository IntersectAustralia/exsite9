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
        assertFalse("Name begins with xml",ExSite9XMLHelper.isValidElementName(name));

        name = "xMlElement";
        assertFalse("Name begins with xml",ExSite9XMLHelper.isValidElementName(name));

        name = "1element";
        assertFalse("Name starts with a number",ExSite9XMLHelper.isValidElementName(name));

        name = "?element";
        assertFalse("Name starts with punctuation",ExSite9XMLHelper.isValidElementName(name));
        
        name = "cat 1";
        assertFalse("Name includes spaces",ExSite9XMLHelper.isValidElementName(name));
        
        name = "cat&dog";
        assertFalse("Name includes ampersand",ExSite9XMLHelper.isValidElementName(name));
        
        name = "greater>than";
        assertFalse("Name includes gretaer than",ExSite9XMLHelper.isValidElementName(name));
        
        name = "less<than";
        assertFalse("Name includes less than",ExSite9XMLHelper.isValidElementName(name));
        
        name = "element";
        assertTrue("Name is all letters",ExSite9XMLHelper.isValidElementName(name));
    }
}
