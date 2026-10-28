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

    public Integer getId_plant() {
        return id_plant;
    }

    public String getLocation_plant() {
        return location_plant;
    }

    public String getLocation_state_plant() {
        return location_state_plant;
    }

    public String getName_plant() {
        return name_plant;
    }

    public String getCompany_plant() {
        return company_plant;
    }

    @Override
    public String toString(){
        return "(id_plant="+this.id_plant+",id_name="+this.name_plant+")";
    }

}