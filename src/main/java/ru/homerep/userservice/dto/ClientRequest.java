package ru.homerep.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.homerep.userservice.models.Client;
import ru.homerep.userservice.models.Status;
@Getter
@Setter
public class ClientRequest {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private Status status;
    private Double latitude;
    private Double longtitude;

    public Client toClient() {
        return new Client(id, firstName, middleName, lastName, email, phone, status);
    }

    public ClientRequest(Long id, String firstName, String middleName, String lastName, String email, String phone, Status status, Double latitude, Double longtitude) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }
}
