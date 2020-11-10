package com.example.demo.service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.GeolocationDTO;
import com.example.demo.exception.InvalidObjectException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private AddressRepository addressRepository;
    private GeolocationService geolocationService;

    public AddressService(AddressRepository addressRepository, GeolocationService geolocationService) {
        this.addressRepository = addressRepository;
        this.geolocationService = geolocationService;
    }

    public AddressDTO findById(final Long id) {
        final Optional<Address> byId = this.addressRepository.findById(id);
        if (!byId.isPresent()) {
            throw new NotFoundException();
        }

        return new AddressDTO(byId.get());
    }

    public AddressDTO create(final AddressDTO newAddressDTO) {
        validateAddressFields(newAddressDTO);

        final Address address = fromDTO(newAddressDTO);
        address.setId(null);
        fillGeolocationIfEmpty(address);
        this.addressRepository.save(address);

        return new AddressDTO(address);
    }

    public AddressDTO update(final Long id, final AddressDTO newAddressDTO) {
        validateAddressFields(newAddressDTO);

        final Optional<Address> byId = this.addressRepository.findById(id);
        if (!byId.isPresent()) {
            throw new NotFoundException();
        }

        final Address address = fromDTO(newAddressDTO);
        address.setId(id);
        fillGeolocationIfEmpty(address);
        this.addressRepository.save(address);

        return new AddressDTO(address);
    }

    public void deleteById(final Long id) {
        final Optional<Address> byId = this.addressRepository.findById(id);
        if (!byId.isPresent()) {
            throw new NotFoundException();
        }

        this.addressRepository.deleteById(id);
    }

    private void fillGeolocationIfEmpty(final Address address) {
        if (address.getLatitude() != null && address.getLongitude() != null) {
            return;
        }

        final Optional<GeolocationDTO> geolocationFromAddress = this.geolocationService.getGeolocationFromAddress(address);
        geolocationFromAddress.flatMap(GeolocationDTO::getCoordinates).ifPresent(coordinates -> {
            address.setLatitude(coordinates.getKey());
            address.setLongitude(coordinates.getValue());
        });
    }

    private void validateAddressFields(final AddressDTO addressDTO) {
        final List<String> invalidReasons = new ArrayList<>();

        if (StringUtils.isNullOrEmpty(addressDTO.getStreetName())) {
            invalidReasons.add("Street name cannot be empty");
        }

        if (!Optional.ofNullable(addressDTO.getNumber()).isPresent()) {
            invalidReasons.add("Number cannot be empty");
        }

        if (StringUtils.isNullOrEmpty(addressDTO.getNeighbourhood())) {
            invalidReasons.add("Neighbourhood cannot be empty");
        }

        if (StringUtils.isNullOrEmpty(addressDTO.getCity())) {
            invalidReasons.add("City name cannot be empty");
        }

        if (StringUtils.isNullOrEmpty(addressDTO.getState())) {
            invalidReasons.add("State name cannot be empty");
        }

        if (StringUtils.isNullOrEmpty(addressDTO.getCountry())) {
            invalidReasons.add("Country name cannot be empty");
        }

        if (StringUtils.isNullOrEmpty(addressDTO.getZipCode())) {
            invalidReasons.add("Zip Code cannot be empty");
        }

        if (!invalidReasons.isEmpty()) {
            throw new InvalidObjectException(invalidReasons);
        }
    }

    private Address fromDTO(final AddressDTO addressDTO) {
        final Address address = new Address();
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setNumber(addressDTO.getNumber());
        address.setCountry(addressDTO.getCountry());
        address.setZipCode(addressDTO.getZipCode());
        address.setLatitude(addressDTO.getLatitude());
        address.setLongitude(addressDTO.getLongitude());
        address.setComplement(addressDTO.getComplement());
        address.setStreetName(addressDTO.getStreetName());
        address.setNeighbourhood(addressDTO.getNeighbourhood());
        return address;
    }
}
