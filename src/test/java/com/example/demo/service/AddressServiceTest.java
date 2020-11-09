package com.example.demo.service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.exception.InvalidObjectException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
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
    private AddressRepository addressRepository;

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
        address.setStreetName("Av dos Amarais");

        Mockito.when(addressRepository.findById(Mockito.any())).thenReturn(Optional.of(address));
        final AddressDTO addressDTO = addressService.findById(address.getId());
        assertThat(addressDTO).isNotNull();
        assertThat(addressDTO.getStreetName()).isEqualTo(address.getStreetName());
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
        addressDTO.setStreetName("Street name");
        return addressDTO;
    }
}
