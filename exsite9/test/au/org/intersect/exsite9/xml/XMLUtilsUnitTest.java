/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests {@link XMLUtils}
 */
public final class XMLUtilsUnitTest
{
    @Test
    public void testIsValidElementName()
    {
        String name;
        
        name = "xmlElement";
        assertFalse("Name begins with xml",XMLUtils.isValidElementOrAttribute(name));

        name = "xMlElement";
        assertFalse("Name begins with xml",XMLUtils.isValidElementOrAttribute(name));

        name = "1element";
        assertFalse("Name starts with a number",XMLUtils.isValidElementOrAttribute(name));

        name = "?element";
        assertFalse("Name starts with punctuation",XMLUtils.isValidElementOrAttribute(name));
        
        name = "cat 1";
        assertFalse("Name includes spaces",XMLUtils.isValidElementOrAttribute(name));
        
        name = "cat&dog";
        assertFalse("Name includes ampersand",XMLUtils.isValidElementOrAttribute(name));
        
        name = "greater>than";
        assertFalse("Name includes gretaer than",XMLUtils.isValidElementOrAttribute(name));
        
        name = "less<than";
        assertFalse("Name includes less than",XMLUtils.isValidElementOrAttribute(name));
        
        name = "fwd/slash";
        assertFalse("Name includes fwd slash",XMLUtils.isValidElementOrAttribute(name));
        
        name = "back\\slash";
        assertFalse("Name includes back slash",XMLUtils.isValidElementOrAttribute(name));
        
        name = "pipe|symbol";
        assertFalse("Name includes pipe symbol",XMLUtils.isValidElementOrAttribute(name));
        
        name = "aster*isk";
        assertFalse("Name includes asterisk",XMLUtils.isValidElementOrAttribute(name));
        
        name = "semi;colon";
        assertFalse("Name includes semi colon",XMLUtils.isValidElementOrAttribute(name));
        
        name = "fwd(paren";
        assertFalse("Name includes (",XMLUtils.isValidElementOrAttribute(name));
        
        name = "close)paren";
        assertFalse("Name includes )",XMLUtils.isValidElementOrAttribute(name));
        
        name = "fwd{brace";
        assertFalse("Name includes {",XMLUtils.isValidElementOrAttribute(name));
        
        name = "open[sb";
        assertFalse("Name includes [",XMLUtils.isValidElementOrAttribute(name));
        
        name = "close]sb";
        assertFalse("Name includes ]",XMLUtils.isValidElementOrAttribute(name));
        
        name = "plus+sign";
        assertFalse("Name includes +",XMLUtils.isValidElementOrAttribute(name));
        
        name = "single'quote";
        assertFalse("Name includes '",XMLUtils.isValidElementOrAttribute(name));
        
        name = "double\"quote";
        assertFalse("Name includes \"",XMLUtils.isValidElementOrAttribute(name));
        
        name = "question?mark";
        assertFalse("Name includes ?",XMLUtils.isValidElementOrAttribute(name));
        
        name = "exclamation!mark";
        assertFalse("Name includes !",XMLUtils.isValidElementOrAttribute(name));
        
        name = "back`quote";
        assertFalse("Name includes `",XMLUtils.isValidElementOrAttribute(name));
        
        name = "til~de";
        assertFalse("Name includes ~",XMLUtils.isValidElementOrAttribute(name));
        
        name = "at@sign";
        assertFalse("Name includes @",XMLUtils.isValidElementOrAttribute(name));
        
        name = "dollar$sign";
        assertFalse("Name includes $",XMLUtils.isValidElementOrAttribute(name));
        
        name = "percent%sign";
        assertFalse("Name includes %",XMLUtils.isValidElementOrAttribute(name));

        name = "h^at";
        assertFalse("Name includes ^",XMLUtils.isValidElementOrAttribute(name));

        name = "equals=sign";
        assertFalse("Name includes =",XMLUtils.isValidElementOrAttribute(name));
        
        name = "com,ma";
        assertFalse("Name includes ,",XMLUtils.isValidElementOrAttribute(name));
        
        name = "element";
        assertTrue("Name is all letters",XMLUtils.isValidElementOrAttribute(name));
    }
}
