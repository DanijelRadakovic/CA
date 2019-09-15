package megatravel.com.ca.domain.dto;

import megatravel.com.ca.domain.Server;
import megatravel.com.ca.domain.enums.ServerType;

public class ServerDTO {
    private Long id;
    private String name;
    private String address;
    private ServerType type;

    public ServerDTO() {
    }

    public ServerDTO(Long id, String name, String address, ServerType type) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public ServerDTO(Server server) {
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
