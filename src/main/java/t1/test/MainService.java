package t1.test;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainService {
    public String getCharsFrequency(String s) {
        Map<Character, Integer> charFrequency = new HashMap<>();

        for (char ch : s.toCharArray()) {
            charFrequency.putIfAbsent(ch, 0);
            charFrequency.computeIfPresent(ch, (k, v) -> v + 1);
        }

        Comparator<Map.Entry<Character, Integer>> comparator = (o1, o2) -> {
            int valueCompare = o2.getValue().compareTo(o1.getValue());
            if (valueCompare == 0) {
                return o1.getKey().compareTo(o2.getKey());
            }
            return valueCompare;
        };

        StringBuilder result = new StringBuilder();

        charFrequency.entrySet().stream()
                .sorted(comparator)
                .forEachOrdered(entry ->
                        result.append("\"")
                                .append(entry.getKey())
                                .append("\"")
                                .append(":")
                                .append(entry.getValue())
                                .append(","));

        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}
