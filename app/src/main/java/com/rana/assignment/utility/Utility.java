package com.rana.assignment.utility;

import com.rana.assignment.models.RowItem;
import com.rana.assignment.models.WordCountWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sandeeprana on 20/04/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class Utility {
    public static ArrayList<RowItem> transformHashMapIntoArraylistForPerformanceAndSimplity(HashMap<String, Integer> integerHashMap) {
        Object[] keySet = integerHashMap.keySet().toArray();
        ArrayList<RowItem> arrayList = new ArrayList<>();
        for (int i = 0; i < integerHashMap.size(); i++) {
            int wordCount = integerHashMap.get(keySet[i]);
            WordCountWrapper wrapper = new WordCountWrapper(keySet[i].toString(), wordCount);
            arrayList.add(wrapper);
        }
        return arrayList;
    }

    public static <K, V extends Comparable<? super V>> HashMap<K, V> sortHashMapByValue(HashMap<K, V> map) {
        List<HashMap.Entry<K, V>> list =
                new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<HashMap.Entry<K, V>>() {
            @Override
            public int compare(HashMap.Entry<K, V> o1, HashMap.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<K, V> result = new LinkedHashMap<>();
        for (HashMap.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
