package dev.philsca_capstone.avs_gsa.Models;

public class Flight {
    private String code;

    public Flight(){    }
    public Flight(String code){
        this.code = code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
