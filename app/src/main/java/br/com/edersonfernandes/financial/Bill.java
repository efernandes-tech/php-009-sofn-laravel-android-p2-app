package br.com.edersonfernandes.financial;

/**
 * Created by ederson.fernandes on 02/05/2018.
 */

public class Bill {
    private String id;
    private String name;
    private Integer value;

    public Bill(String id, String name, Integer value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value){
        this.value= value;
    }
}
