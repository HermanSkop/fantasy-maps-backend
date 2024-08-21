package org.fantasymaps.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.fantasymaps.backend.config.UserConverter;
import org.fantasymaps.backend.dtos.Role;
import org.fantasymaps.backend.dtos.UserDto;
import org.fantasymaps.backend.model.user.Admin;
import org.fantasymaps.backend.model.user.Customer;
import org.fantasymaps.backend.model.user.Creator;
import org.fantasymaps.backend.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class UserConverterTest {

    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        UserConverter userConverter = new UserConverter();
        modelMapper = new ModelMapper();
        modelMapper.typeMap(Admin.class, UserDto.class).setConverter(userConverter.adminConverter());
        modelMapper.typeMap(Customer.class, UserDto.class).setConverter(userConverter.customerConverter());
        modelMapper.typeMap(Creator.class, UserDto.class).setConverter(userConverter.creatorConverter());
    }

    @Test
    public void testAdminConversion() {
        Admin admin = new Admin();
        admin.setUsername("admin1");
        admin.setEmail("admin@example.com");
        admin.setVerifiedEmail(true);

        UserDto userDto = modelMapper.map(admin, UserDto.class);

        assertEquals("admin1", userDto.getUsername());
        assertEquals("admin@example.com", userDto.getEmail());
        assertEquals(Role.ADMIN, userDto.getRole());
    }

    @Test
    public void testCustomerConversion() {
        Customer customer = new Customer();
        customer.setUsername("customer1");
        customer.setEmail("customer@example.com");
        customer.setVerifiedEmail(false);

        UserDto userDto = modelMapper.map(customer, UserDto.class);

        assertEquals("customer1", userDto.getUsername());
        assertEquals("customer@example.com", userDto.getEmail());
        assertEquals(Role.CUSTOMER, userDto.getRole());
    }

    @Test
    public void testCreatorConversion() {
        Creator creator = new Creator();
        creator.setUsername("creator1");
        creator.setEmail("creator@example.com");
        creator.setVerifiedEmail(true);

        UserDto userDto = modelMapper.map(creator, UserDto.class);

        assertEquals("creator1", userDto.getUsername());
        assertEquals("creator@example.com", userDto.getEmail());
        assertEquals(Role.CREATOR, userDto.getRole());
    }
}
