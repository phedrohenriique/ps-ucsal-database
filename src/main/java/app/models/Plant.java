package app.models;

public class Plant {

    Integer id_plant;
    String name_plant;
    String company_plant;
    String location_state_plant;
    String location_plant;

    public Plant(Integer id, String name, String company, String location_state, String location){
        this.id_plant = id;
        this.name_plant = name;
        this.company_plant = company;
        this.location_state_plant = location_state;
        this. location_plant = location;
    }

    @Override
    public String toString(){
        return "(id_plant="+this.id_plant+","+"id_name="+this.name_plant+")";
    }

}