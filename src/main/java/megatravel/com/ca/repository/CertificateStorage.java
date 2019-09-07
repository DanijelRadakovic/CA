package megatravel.com.ca.repository;

import megatravel.com.ca.domain.cert.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificateStorage extends JpaRepository<Certificate, Long> {

    @Query(value = "SELECT * FROM certificate WHERE number = ?1", nativeQuery = true)
    List<Certificate> findBySerialNumber(Long serialNumber);

    @Query(value = "SELECT * FROM certificate WHERE active = true", nativeQuery = true)
    List<Certificate> findAllActive();

    @Query(value = "SELECT * FROM certificate WHERE type = 0", nativeQuery = true)
    List<Certificate> findAllCA();

    @Query(value = "SELECT * FROM certificate WHERE active = true and type = 0", nativeQuery = true)
    List<Certificate> findAllActiveCA();

    @Query(value = "SELECT * FROM certificate WHERE type = 1", nativeQuery = true)
    List<Certificate> findAllClients();

    @Query(value = "SELECT * FROM certificate WHERE active = true and type = 1", nativeQuery = true)
    List<Certificate> findAllActiveClients();
}
