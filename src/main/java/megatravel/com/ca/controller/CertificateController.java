package megatravel.com.ca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import megatravel.com.ca.converter.CertificateConverter;
import megatravel.com.ca.domain.dto.cer.CertificateDTO;
import megatravel.com.ca.domain.dto.cer.CertificateDistributionDTO;
import megatravel.com.ca.domain.dto.cer.CertificateRequestDTO;
import megatravel.com.ca.domain.enums.RevokeReason;
import megatravel.com.ca.service.CertificateService;
import megatravel.com.ca.util.exception.ValidationException;
import org.bouncycastle.asn1.x500.X500Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/cer")
public class CertificateController extends ValidationController {

    private static final Logger logger = LoggerFactory.getLogger(CertificateController.class);

    @Autowired
    private CertificateService certificateService;

    /**
     * GET /api/cer
     *
     * @return all available certificates
     */
    @GetMapping
    public ResponseEntity<List<CertificateDTO>> getAll() {
        logger.info("action=getAllCertificates status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAll(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * GET /api/cer/active
     *
     * @return all available active certificates
     */
    @GetMapping("/active")
    public ResponseEntity<List<CertificateDTO>> getAllActive() {
        logger.info("action=getAllActiveCertificates status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAllActive(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * GET /api/cer/ca
     *
     * @return all available certificate authorities
     */
    @GetMapping("/ca")
    public ResponseEntity<List<CertificateDTO>> getAllCA() {
        logger.info("action=getAllCA status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAllCA(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * GET /api/cer/ca/active
     *
     * @return all available active certificate authorities
     */
    @GetMapping("/ca/active")
    public ResponseEntity<List<CertificateDTO>> getAllActiveCA() {
        logger.info("action=getAllActiveCA status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAllActiveCA(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * GET /api/cer/client
     *
     * @return all available client certificates
     */
    @GetMapping("/client")
    public ResponseEntity<List<CertificateDTO>> getAllClients() {
        logger.info("action=getAllClients status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAllClients(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * GET /api/cer/client/active
     *
     * @return all available active client certificates
     */
    @GetMapping("/client/active")
    public ResponseEntity<List<CertificateDTO>> getAllActiveClients() {
        logger.info("action=getAllClients status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAllActiveClients(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * POST /api/cer
     *
     * @param request that contains certificate data
     * @return message about action results
     */
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> save(@RequestBody String request) throws IOException, ValidationException {
        validateJSON(request, "certificate.json");
        CertificateRequestDTO cer = new ObjectMapper().readValue(request, CertificateRequestDTO.class);
        try {
            X500Name name = CertificateConverter.toX500Name(cer.getSubjectDN(), cer.getBasicConstraints().iscA());
            certificateService.save(name, cer.getIssuerSN(), CertificateConverter.toExtensionHolders(cer));
            logger.info("action=generateCert dn={} status=success", name);
            return new ResponseEntity<>("Certificate successfully created.", HttpStatus.OK);
        } catch (NullPointerException e) {
            logger.info("action=generateCert status=failure cause=BasicConstraintsOmitted");
            return new ResponseEntity<>("Basic Constraints field must be present in certificate.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * DELETE /api/cer/{id}/{reason}
     *
     * @param id     of certificate that needs to be deleted
     * @param reason for certificate revocation
     * @return message about action results
     */
    @DeleteMapping(value = "/{id}/{reason}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> delete(@PathVariable String id, @PathVariable String reason) {
        certificateService.remove(Long.parseLong(id), RevokeReason.valueOf(reason.toUpperCase()));
        logger.info("action=removeCert certId={} status=success", id);
        return new ResponseEntity<>("Certificate successfully deleted.", HttpStatus.OK);
    }

    /**
     * POST /api/cer/dist
     *
     * @param request that contains distribution data
     * @return message about action results
     */
    @PostMapping(value = "/dist", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> distribute(@RequestBody String request) throws IOException, ValidationException {
        validateJSON(request, "distribution.json");
        CertificateDistributionDTO cert = new ObjectMapper().readValue(request, CertificateDistributionDTO.class);
        certificateService.distribute(CertificateConverter.toEntity(cert));
        logger.info("action=distributeCert serialNumber={} status=success", cert.getSerialNumber());
        return new ResponseEntity<>("Certificate successfully distributed.", HttpStatus.OK);
    }
}