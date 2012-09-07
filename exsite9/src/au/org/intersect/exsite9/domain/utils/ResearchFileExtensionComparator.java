/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Compares research files by their extension.
 */
public class ResearchFileExtensionComparator extends ResearchFileNameComparator implements Comparator<ResearchFile>
{
    private static final Pattern FILE_EXTENSION_PATTERN = Pattern.compile("^.*\\.(.*)$");

    public ResearchFileExtensionComparator(final boolean ascending)
    {
        super(ascending);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public int compare(final ResearchFile rf1, final ResearchFile rf2)
    {
        final String extension1 = getExtension(rf1);
        final String extension2 = getExtension(rf2);

        final int compareIgnoreCase = extension1.compareToIgnoreCase(extension2);
        if (compareIgnoreCase != 0)
        {
            return compareIgnoreCase * super.factor;
        }
        return super.compare(rf1, rf2);
    }

    private static String getExtension(final ResearchFile rf)
    {
        final Matcher matcher = FILE_EXTENSION_PATTERN.matcher(rf.getFile().getName());
        if (matcher.matches())
        {
            return matcher.group(1);
        }
        return "";
    }
}
