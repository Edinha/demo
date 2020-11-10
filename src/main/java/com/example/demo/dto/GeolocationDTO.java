package com.example.demo.dto;

import javafx.util.Pair;

import java.util.List;
import java.util.Optional;

class Location {
    private Float lat;
    private Float lng;

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }
}

class Geometry {
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

class AddressResult {
    private Geometry geometry;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}

public class GeolocationDTO {
    private List<AddressResult> results;

    public Optional<Pair<Float, Float>> getCoordinates() {
        if (results.isEmpty()) {
            return Optional.empty();
        }

        final Location location = results.get(0).getGeometry().getLocation();
        return Optional.of(new Pair<>(location.getLat(), location.getLng()));
    }

    public List<AddressResult> getResults() {
        return results;
    }

    public void setResults(List<AddressResult> results) {
        this.results = results;
    }
}
