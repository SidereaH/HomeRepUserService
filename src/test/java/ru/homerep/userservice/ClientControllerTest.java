package ru.homerep.userservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.homerep.userservice.controllers.ClientController;
import ru.homerep.userservice.models.Client;
import ru.homerep.userservice.services.ClientService;
import ru.homerep.userservice.models.Status;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateClient() throws Exception {

        Client client = new Client(null, "John", "Doe", "Smith", "john.doe@example.com", "123456789", Status.CLIENT);
        Client savedClient = new Client(1L, "John", "Doe", "Smith", "john.doe@example.com", "123456789", Status.CLIENT);

        when(clientService.createClient(any(Client.class))).thenReturn(savedClient);


        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testGetAllClients() throws Exception {

        Client client1 = new Client(1L, "John", "Doe", "Smith", "john.doe@example.com", "123456789", Status.CLIENT);
        Client client2 = new Client(2L, "Jane", "Doe", "Smith", "jane.doe@example.com", "987654321",Status.CLIENT);

        when(clientService.getAllClients()).thenReturn(Arrays.asList(client1, client2));


        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testGetClientByIdFound() throws Exception {

        Client client = new Client(1L, "John", "Doe", "Smith", "john.doe@example.com", "123456789",Status.CLIENT);

        when(clientService.getClientById(1L)).thenReturn(client);


        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void testGetClientByIdNotFound() throws Exception {

        when(clientService.getClientById(1L)).thenReturn(null);


        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateClient() throws Exception {

        Client updatedClient = new Client(1L, "John", "Doe", "Smith", "john.doe@example.com", "123456789", Status.CLIENT);
        when(clientService.updateClient(eq(1L), any(Client.class))).thenReturn(updatedClient);


        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void testUpdateClientNotFound() throws Exception {

        Client updatedClient = new Client(1L, "John", "Doe", "Smith", "john.doe@example.com", "123456789", Status.CLIENT);
        when(clientService.updateClient(eq(1L), any(Client.class))).thenReturn(null);

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedClient)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteClient() throws Exception {

        when(clientService.deleteClient(1L)).thenReturn(true);


        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteClientNotFound() throws Exception {

        when(clientService.deleteClient(1L)).thenReturn(false);


        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isNotFound());
    }
}
