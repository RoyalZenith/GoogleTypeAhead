package code.gaurav.googletypeahead.services;

import code.gaurav.googletypeahead.constants.Contants;
import code.gaurav.googletypeahead.models.TreeNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TypeAheadService {
    TreeNode root = new TreeNode();
    Map<String, Double> mp = new HashMap<>();
    public List<String> findAllStringForPattern(String pattern){
        return root.getTopNTermsForPattern(pattern.toLowerCase());
    }

    public void savePattern(String pattern) {
        mp.put(pattern, mp.getOrDefault(pattern,0.0)+1);
        if(mp.get(pattern).compareTo(Contants.THRESHOLD_FREQ) >= 0){
            // increase the frequencies
            root.insertPattern(pattern.toLowerCase(), mp.get(pattern));
            mp.remove(pattern);
        }
    }

    public void decayFrequency(Double time) {
        root.updateFrequencyByTime(time);
    }

    public void updateAllFrequency(){
        for(Map.Entry<String, Double> ele:mp.entrySet()){
            root.insertPattern(ele.getKey().toLowerCase(), ele.getValue());
        }
        mp.clear();
    }

}
