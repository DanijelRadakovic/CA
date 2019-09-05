package megatravel.com.pki.domain;

import megatravel.com.pki.domain.DTO.ServerDTO;
import megatravel.com.pki.domain.enums.ServerType;

import javax.persistence.*;

@Entity
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ServerType type;

    public Server() {
    }

    public Server(Long id, String name, String address, ServerType type) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public Server(ServerDTO server) {
        this.id = server.getId();
        this.name = server.getName();
        this.address = server.getAddress();
        this.type = server.getType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ServerType getType() {
        return type;
    }

    public void setType(ServerType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
