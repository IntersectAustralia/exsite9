package au.org.intersect.exsite9.util;

import java.util.Comparator;

/**
 * Comparator to compare a pair of Strings alphabetically.
 */
public final class AlphabeticalPairComparator implements Comparator<Pair<String, String>>
{
    /**
     * @{inheritDoc}
     */
    @Override
    public int compare(final Pair<String, String> pair1, final Pair<String, String> pair2)
    {
        final int result = compareStrings(pair1.getFirst(), pair2.getFirst());
        return result == 0 ? compareStrings(pair1.getSecond(), pair2.getSecond()) : result;
    }

    private int compareStrings(final String s1, final String s2)
    {
        return s1.compareToIgnoreCase(s2);
    }
}
