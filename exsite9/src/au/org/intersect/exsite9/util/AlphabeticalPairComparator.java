package au.org.intersect.exsite9.util;

import java.util.Comparator;

public class AlphabeticalPairComparator implements Comparator<Pair<String, String>>
{

    @Override
    public int compare(Pair<String, String> pair1, Pair<String, String> pair2)
    {
        int result = compareStrings(pair1.getFirst(), pair2.getFirst());
        
        return result == 0 ? compareStrings(pair1.getSecond(), pair2.getSecond()) : result;
    }

    private int compareStrings(String pair1, String pair2)
    {
        return pair1.compareToIgnoreCase(pair2);
    }
}
