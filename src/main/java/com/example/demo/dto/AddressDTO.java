package com.example.demo.dto;

import com.example.demo.model.Address;

public class AddressDTO {

    private Long id;
    private String streetName;
    private Long number;
    private String complement;
    private String neighbourhood;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private Float latitude;
    private Float longitude;

    public AddressDTO() {
    }

    public AddressDTO(Address address) {
        this.id = address.getId();
        this.city = address.getCity();
        this.state = address.getState();
        this.number = address.getNumber();
        this.country = address.getCountry();
        this.zipCode = address.getZipCode();
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
        this.streetName = address.getStreetName();
        this.complement = address.getComplement();
        this.neighbourhood = address.getNeighbourhood();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}
