package com.example.demo.controller;

import com.example.demo.dto.AddressDTO;
import com.example.demo.service.AddressService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/address")
public class AddressController {

    private AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(value = "/{id}")
    public AddressDTO findAddress(@PathVariable("id") Long id) {
        return this.addressService.findById(id);
    }

    @PostMapping()
    public AddressDTO createAddress(@RequestBody AddressDTO newAddressDTO) {
        return this.addressService.create(newAddressDTO);
    }

    @PutMapping(value = "/{id}")
    public AddressDTO updateAddress(@PathVariable("id") Long id, @RequestBody AddressDTO newAddressDTO) {
        return this.addressService.update(id, newAddressDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void updateAddress(@PathVariable("id") Long id) {
        this.addressService.deleteById(id);
    }
}
