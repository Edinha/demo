package com.example.demo.service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.GeolocationDTO;
import com.example.demo.exception.InvalidObjectException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceTest {

    @Mock
    private GeolocationDTO geolocationDTO;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private GeolocationService geolocationService;

    @InjectMocks
    private AddressService addressService;

    @Test(expected = NotFoundException.class)
    public void testNotFoundAddressById() {
        Mockito.when(addressRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        addressService.findById(123L);
    }

    @Test
    public void testAddressFound() {
        final Address address = new Address();
        address.setId(123L);
        address.setCity("City");
        address.setState("State");
        address.setZipCode("ZipCode");
        address.setCountry("Country");
        address.setComplement("Complement");
        address.setStreetName("Street name");
        address.setNeighbourhood("Neighbourhood");
        address.setNumber(321L);

        Mockito.when(addressRepository.findById(Mockito.any())).thenReturn(Optional.of(address));

        final AddressDTO addressDTO = addressService.findById(address.getId());
        assertThat(addressDTO).isNotNull();
        assertThat(addressDTO.getCity()).isEqualTo(address.getCity());
        assertThat(addressDTO.getState()).isEqualTo(address.getState());
        assertThat(addressDTO.getNumber()).isEqualTo(address.getNumber());
        assertThat(addressDTO.getZipCode()).isEqualTo(address.getZipCode());
        assertThat(addressDTO.getCountry()).isEqualTo(address.getCountry());
        assertThat(addressDTO.getComplement()).isEqualTo(address.getComplement());
        assertThat(addressDTO.getStreetName()).isEqualTo(address.getStreetName());
        assertThat(addressDTO.getNeighbourhood()).isEqualTo(address.getNeighbourhood());
    }

    @Test
    public void testValidateAddressAllNonNullableFields() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();
        fullAddressDTO.setCity(null);
        fullAddressDTO.setState(null);
        fullAddressDTO.setNumber(null);
        fullAddressDTO.setCountry(null);
        fullAddressDTO.setZipCode(null);
        fullAddressDTO.setStreetName(null);
        fullAddressDTO.setNeighbourhood(null);

        try {
            addressService.create(fullAddressDTO);
            Assert.fail();
        } catch (InvalidObjectException e) {
            final String message = e.getMessage();
            assertThat(message.contains("Number cannot be empty")).isTrue();
            assertThat(message.contains("Zip Code cannot be empty")).isTrue();
            assertThat(message.contains("City name cannot be empty")).isTrue();
            assertThat(message.contains("State name cannot be empty")).isTrue();
            assertThat(message.contains("Street name cannot be empty")).isTrue();
            assertThat(message.contains("Country name cannot be empty")).isTrue();
            assertThat(message.contains("Neighbourhood cannot be empty")).isTrue();
        }
    }

    @Test(expected = InvalidObjectException.class)
    public void testCreateAddressEmptyStreetName() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();
        fullAddressDTO.setStreetName(null);

        addressService.create(fullAddressDTO);
    }

    @Test
    public void testCreateAddressSuccessfully() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();

        addressService.create(fullAddressDTO);
        Mockito.verify(addressRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(geolocationService, Mockito.times(0)).getGeolocationFromAddress(Mockito.any());
    }

    @Test
    public void testCreateWithoutLatitudeAndLongitudeShouldCallGeolocationService() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();
        fullAddressDTO.setLatitude(null);
        fullAddressDTO.setLongitude(null);

        Mockito.when(geolocationService.getGeolocationFromAddress(Mockito.any())).thenReturn(Optional.empty());

        final AddressDTO result = addressService.create(fullAddressDTO);
        assertThat(result.getLatitude()).isNull();
        assertThat(result.getLongitude()).isNull();
        Mockito.verify(addressRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(geolocationService, Mockito.times(1)).getGeolocationFromAddress(Mockito.any());
    }

    @Test
    public void testCreateWithoutLongitudeShouldCallGeolocationService() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();
        fullAddressDTO.setLongitude(null);

        Mockito.when(geolocationDTO.getCoordinates()).thenReturn(Optional.of(new Pair<>(3F, -3F)));
        Mockito.when(geolocationService.getGeolocationFromAddress(Mockito.any())).thenReturn(Optional.of(geolocationDTO));

        final AddressDTO result = addressService.create(fullAddressDTO);
        assertThat(result.getLatitude()).isEqualTo(3F);
        assertThat(result.getLongitude()).isEqualTo(-3F);
        Mockito.verify(addressRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(geolocationService, Mockito.times(1)).getGeolocationFromAddress(Mockito.any());
    }

    @Test(expected = InvalidObjectException.class)
    public void testUpdateAddressEmptyStreetName() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();
        fullAddressDTO.setStreetName(null);

        addressService.update(fullAddressDTO.getId(), fullAddressDTO);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateAddressNotFoundId() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();
        Mockito.when(addressRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        addressService.update(fullAddressDTO.getId(), fullAddressDTO);
    }

    @Test
    public void testUpdateAddressSuccessfully() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();
        Mockito.when(addressRepository.findById(Mockito.any())).thenReturn(Optional.of(new Address()));

        addressService.update(fullAddressDTO.getId(), fullAddressDTO);
        Mockito.verify(addressRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testUpdateWithoutLongitudeShouldCallGeolocationServiceEmptyResultShouldChangeNothing() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();
        fullAddressDTO.setLatitude(null);
        fullAddressDTO.setLongitude(null);

        Mockito.when(addressRepository.findById(Mockito.any())).thenReturn(Optional.of(new Address()));
        Mockito.when(geolocationService.getGeolocationFromAddress(Mockito.any())).thenReturn(Optional.empty());

        final AddressDTO result = addressService.update(fullAddressDTO.getId(), fullAddressDTO);
        assertThat(result.getLatitude()).isNull();
        assertThat(result.getLongitude()).isNull();
        Mockito.verify(addressRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(geolocationService, Mockito.times(1)).getGeolocationFromAddress(Mockito.any());
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteAddressNotFoundId() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();
        Mockito.when(addressRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        addressService.deleteById(fullAddressDTO.getId());
    }

    @Test
    public void testDeleteAddressSuccessfully() {
        final AddressDTO fullAddressDTO = getFullAddressDTO();
        Mockito.when(addressRepository.findById(Mockito.any())).thenReturn(Optional.of(new Address()));

        addressService.deleteById(fullAddressDTO.getId());
        Mockito.verify(addressRepository, Mockito.times(1)).deleteById(Mockito.any());
    }

    private AddressDTO getFullAddressDTO() {
        final AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(123L);
        addressDTO.setCity("City");
        addressDTO.setState("State");
        addressDTO.setZipCode("ZipCode");
        addressDTO.setCountry("Country");
        addressDTO.setComplement("Complement");
        addressDTO.setStreetName("Street name");
        addressDTO.setNeighbourhood("Neighbourhood");
        addressDTO.setNumber(321L);
        addressDTO.setLatitude(1.2F);
        addressDTO.setLongitude(2.1F);
        return addressDTO;
    }
}
