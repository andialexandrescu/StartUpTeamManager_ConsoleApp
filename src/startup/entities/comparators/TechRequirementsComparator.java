package startup.entities.comparators;
import startup.entities.enums.TechStack;

import java.util.Comparator;
import java.util.Map;

public class TechRequirementsComparator implements Comparator<Map.Entry<TechStack, Integer>>
{
    @Override
    public int compare(Map.Entry<TechStack, Integer> e1, Map.Entry<TechStack, Integer> e2)
    {
        int compare = Integer.compare(e2.getValue(), e1.getValue()); // descending order by value
        if(compare == 0) // if values are equal, compare by key, which is represented by the Tech Stack
        {
            compare = e1.getKey().toString().compareTo(e2.getKey().toString()); // ascending by key
            // could've used ordinal() but it would be less readable
        }
        return compare;
    }
}