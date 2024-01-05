package code.gaurav.googletypeahead.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pair {
    private Long value;
    private String str;

    public Pair(Long value, String str){
        this.value = value;
        this.str = str;
    }
}
