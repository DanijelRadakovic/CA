package megatravel.com.ca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import megatravel.com.ca.converter.ServerConverter;
import megatravel.com.ca.domain.Server;
import megatravel.com.ca.domain.dto.ServerDTO;
import megatravel.com.ca.service.ServerService;
import org.everit.json.schema.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/server")
public class ServerController extends ValidationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    private ServerService serverService;

    /**
     * GET /api/server
     *
     * @return all available servers
     */
    @GetMapping
    public ResponseEntity<List<ServerDTO>> findAll() {
        LOGGER.info("action=getAllServers status=success");
        return new ResponseEntity<>(ServerConverter.fromEntityList(serverService.getAll(), ServerDTO::new),
                HttpStatus.OK);
    }

    /**
     * GET /api/server/{id}
     *
     * @param id of requested server
     * @return server with requested id
     */
    @GetMapping("{id}")
    public ResponseEntity<ServerDTO> findById(@PathVariable String id) {
        LOGGER.info("serverId={} action=get status=success", id);
        return new ResponseEntity<>(new ServerDTO(serverService.findById(Long.parseLong(id))), HttpStatus.FOUND);
    }

    /**
     * POST /api/server
     *
     * @param server that needs to be added
     * @return added server
     */
    @PostMapping
    public ResponseEntity<ServerDTO> save(@RequestBody String server) throws IOException, ValidationException {
        validateJSON(server, "server.json");
        LOGGER.info("action=saveServer status=success");
        return new ResponseEntity<>(new ServerDTO(serverService.save(
                new Server(new ObjectMapper().readValue(server, ServerDTO.class)))), HttpStatus.OK);
    }

    /**
     * DELETE /api/server/{id}
     *
     * @param id of vehicle that needs to be deleted
     * @return message about action results
     */
    @DeleteMapping(value = "{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> delete(@PathVariable String id) {
        serverService.remove(Long.parseLong(id));
        LOGGER.info("serverId={} action=removeServer status=success", id);
        return new ResponseEntity<>("Server successfully deleted!", HttpStatus.OK);
    }
}
