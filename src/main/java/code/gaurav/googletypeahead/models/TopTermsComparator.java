package code.gaurav.googletypeahead.models;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TopTermsComparator implements Comparator<String> {
    private Map<String, Long> map;

    // Constructor
    public TopTermsComparator() {
        this.map = new HashMap<>();
    }

    // Constructor with an existing map (used when constructing the TreeMap)
    public TopTermsComparator(Map<String, Long> map) {
        this.map = map;
    }

    @Override
    public int compare(String a, String b) {
        // Compare values first
        int valueCompare = map.get(a).compareTo(map.get(b));

        // If values are the same, compare keys lexicographically in reverse order
        if (valueCompare == 0) {
            return b.compareTo(a);
        }

        return valueCompare;
    }
}
