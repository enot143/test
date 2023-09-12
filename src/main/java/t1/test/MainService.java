package t1.test;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainService {
    public String getCharsFrequency(String s){
        int[] charArray = new int[26];
        for (int i = 0; i < s.length(); i++){
            charArray[s.charAt(i) - 97]++;
        }

        List<Map.Entry<Character, Integer>> charFrequency = new ArrayList<>();
        for (int i = 0; i < charArray.length; i++){
            if (charArray[i] != 0){
                charFrequency.add(Map.entry((char) (i + 97), charArray[i]));
            }
        }

        charFrequency.sort((o1, o2) -> {
            int valueCompare = o1.getValue().compareTo(o2.getValue());
            if (valueCompare == 0) {
                return o2.getKey().compareTo(o1.getKey());
            }
            return valueCompare;
        });

        StringBuilder result = new StringBuilder();
        for (int i = charFrequency.size() - 1; i >= 0; i--){
            result.append(charFrequency.get(i).getKey())
                    .append(":")
                    .append(charFrequency.get(i).getValue())
                    .append(",");
        }
        return result.deleteCharAt(result.length() - 1).toString();
    }
}
