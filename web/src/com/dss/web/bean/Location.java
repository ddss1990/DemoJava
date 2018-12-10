package com.dss.web.bean;

/**
 * User: DSS
 * Date: 2018/12/10
 * Time: 17:40
 * Description: Location Bean
 */
public class Location {
    private Integer locationId;
    private String city;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", city='" + city + '\'' +
                '}';
    }
}
