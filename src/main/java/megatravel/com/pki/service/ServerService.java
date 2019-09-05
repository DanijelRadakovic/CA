package megatravel.com.pki.service;

import megatravel.com.pki.domain.Server;
import megatravel.com.pki.repository.ServerRepository;
import megatravel.com.pki.util.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {

    @Autowired
    private ServerRepository serverRepository;

    public List<Server> getAll() {
        return serverRepository.findAll();
    }

    public Server findById(Long id) {
        try {
            return serverRepository.findById(id).orElseThrow(() ->
                    new GeneralException("Requested server does not exist!", HttpStatus.BAD_REQUEST));
        } catch (InvalidDataAccessApiUsageException e) { // null id
            throw new GeneralException("Invalid ID!", HttpStatus.BAD_REQUEST);

        }
    }

    public Server save(Server server) {
        try {
            return serverRepository.save(server);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException("Invalid data received!", HttpStatus.BAD_REQUEST);
        }

    }

    public void remove(Long id) {
        serverRepository.deleteById(id);
    }
}
