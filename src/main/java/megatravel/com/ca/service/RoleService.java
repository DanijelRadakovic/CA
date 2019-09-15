package megatravel.com.ca.service;

import megatravel.com.ca.domain.rbac.Role;
import megatravel.com.ca.repository.RoleRepository;
import megatravel.com.ca.util.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        try {
            return roleRepository.findById(id).orElseThrow(() ->
                    new GeneralException("Role with given ID doesn't exists!", HttpStatus.BAD_REQUEST));
        } catch (InvalidDataAccessApiUsageException e) {
            throw new GeneralException("Failed to get role by given ID!", HttpStatus.BAD_REQUEST);
        }
    }

    public Role findByName(String name) {
        try {
            Role role = roleRepository.findRoleByName(name);
            if (role == null) {
                throw new GeneralException("Role with given name doesn't exists!", HttpStatus.BAD_REQUEST);
            } else {
                return role;
            }
        } catch (Exception e) {
            throw new GeneralException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    public Role save(Role role) {
        try {
            Role r = roleRepository.findRoleByName(role.getName());
            if (r != null) {
                throw new GeneralException("Role with given name already exists!", HttpStatus.CONFLICT);
            } else {
                return roleRepository.save(role);
            }
        } catch (Exception e) {
            throw new GeneralException("Failed to save new role!", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteById(Long id) {
        try {
            if (roleRepository.findById(id).isPresent()) {
                roleRepository.deleteById(id);
            } else {
                throw new GeneralException("Role with given id doesn't exists!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new GeneralException("Failed to delete role!", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteByName(String name) {
        try {
            Role role = roleRepository.findRoleByName(name);
            if (role != null) {
                roleRepository.delete(role);
            } else {
                throw new GeneralException("Role with given name doesn't exists!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new GeneralException("Failed to delete role!", HttpStatus.BAD_REQUEST);
        }
    }
}
