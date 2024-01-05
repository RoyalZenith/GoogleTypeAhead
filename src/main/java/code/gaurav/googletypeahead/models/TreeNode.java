package code.gaurav.googletypeahead.models;

import code.gaurav.googletypeahead.constants.Contants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;


@Getter
@Setter
public class TreeNode {
    private Map<Character, TreeNode> child;
    private String[] topNTerms;
    private HashMap<String, Double> topNTermsMap;
    private  Double freq;

    public TreeNode(){
        child = new HashMap<>();
        topNTerms = new String[Contants.TOP_N_TERMS];
        Arrays.fill(topNTerms,"");
        topNTermsMap = new HashMap<>();
        freq = 0.0;
    }

    public void insertPattern(String pattern, Double freq){
        TreeNode curr = this;
        for(char ch:pattern.toCharArray()){
            if(!curr.child.containsKey(ch)){
                curr.child.put(ch,new TreeNode());
            }
            curr = curr.child.get(ch);
        }
        curr.freq += freq;
        //update the frequency in top-terms hashmap string as well
        updateFrequency(curr.freq,pattern);
    }

    public void updateFrequency(Double freq, String pattern){
        TreeNode curr = this;
        for(char ch:pattern.toCharArray()){
            curr = curr.child.get(ch);
            Map<String , Double> currTopNTermsMap = curr.topNTermsMap;
            String[] currTopNTerms = curr.topNTerms;
            if(currTopNTermsMap.containsKey(pattern)){
                currTopNTermsMap.put(pattern,freq);
                for(int i=Contants.TOP_N_TERMS-1;i>0;i--){
                    if(!pattern.equals(currTopNTerms[i])){
                        continue;
                    }
                    int valueComp = currTopNTermsMap.get(currTopNTerms[i-1]).compareTo(currTopNTermsMap.get(currTopNTerms[i]));
                    if( valueComp < 0 || valueComp ==0 && currTopNTerms[i-1].compareTo(currTopNTerms[i]) < 0){
                        String temp = currTopNTerms[i];
                        currTopNTerms[i] = currTopNTerms[i-1];
                        currTopNTerms[i-1] = temp;
                    }else {
                        break;
                    }
                }
            }else if(currTopNTermsMap.size() < Contants.TOP_N_TERMS){
                int size = currTopNTermsMap.size();
                currTopNTerms[size] = pattern;
                currTopNTermsMap.put(pattern,freq);
                for(int i=size;i>0;i--){
                    int value = currTopNTermsMap.get(currTopNTerms[i-1]).compareTo(currTopNTermsMap.get(currTopNTerms[i]));
                    if(value < 0 || value == 0 && currTopNTerms[i-1].compareTo(currTopNTerms[i]) < 0){
                        String temp = currTopNTerms[i];
                        currTopNTerms[i] = currTopNTerms[i-1];
                        currTopNTerms[i-1] = temp;
                    }else{
                        break;
                    }
                }
            }else{
                int size = Contants.TOP_N_TERMS;
                // last String having greator then freq
                int valueComp = currTopNTermsMap.get(currTopNTerms[size-1]).compareTo(freq);
                if( valueComp > 0 || valueComp == 0 && currTopNTerms[size-1].compareTo(pattern) > 0){
                    continue;
                }
                currTopNTermsMap.remove(currTopNTerms[size-1]);
                currTopNTerms[size-1] = pattern;
                currTopNTermsMap.put(pattern,freq);
                for(int i=size-1;i>0;i--){
                    int value = currTopNTermsMap.get(currTopNTerms[i-1]).compareTo(currTopNTermsMap.get(currTopNTerms[i]));
                    if(value < 0 || value == 0 && currTopNTerms[i-1].compareTo(currTopNTerms[i]) < 0){
                        String temp = currTopNTerms[i];
                        currTopNTerms[i] = currTopNTerms[i-1];
                        currTopNTerms[i-1] = temp;
                    }else{
                        break;
                    }
                }

            }
        }
    }

    public List<String> getTopNTermsForPattern(String pattern){
        List<String> ans = new ArrayList<>();
        TreeNode curr = this;
        for(char ch:pattern.toCharArray()){
            if(!curr.child.containsKey(ch)){
                return ans;
            }
            curr = curr.child.get(ch);
        }
        ans.addAll(Arrays.asList(curr.topNTerms));
        return ans;
    }


    public void updateFrequencyByTime(Double time) {
        TreeNode curr = this;
        updateFrequencyByTimeUtil(curr,time);
    }

    private void updateFrequencyByTimeUtil(TreeNode curr, Double time) {
        if(curr == null){
            return;
        }
        for(Map.Entry<Character, TreeNode> ele:curr.child.entrySet()){
            updateFrequencyByTimeUtil(ele.getValue(),time);
        }
        curr.freq = curr.freq/time;
        for(Map.Entry<String, Double> ele:curr.topNTermsMap.entrySet()){
            Double newFreq = ele.getValue() / time;
            curr.topNTermsMap.put(ele.getKey(),newFreq);
        }
    }

}
