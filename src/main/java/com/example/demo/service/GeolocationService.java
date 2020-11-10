package com.example.demo.service;

import com.example.demo.dto.GeolocationDTO;
import com.example.demo.model.Address;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GeolocationService {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s";

    private final RestTemplate restTemplate;

    public GeolocationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Optional<GeolocationDTO> getGeolocationFromAddress(final Address address) {
        final String key = getKey();
        final String addressStr = getAddressStr(address);

        final String url = String.format(BASE_URL, addressStr, key);
        final GeolocationDTO result = this.restTemplate.getForObject(url, GeolocationDTO.class);
        return Optional.ofNullable(result);
    }

    private String getAddressStr(final Address address) {
        return String.format("%d %s, %s, %s", address.getNumber(), address.getStreetName(), address.getCity(), address.getState());
    }

    private String getKey() {
        return "AIzaSyDTK0igIQTCi5EYKL9tzOIJ9N6FUASGZos";
    }
}
