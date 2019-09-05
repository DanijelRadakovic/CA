package megatravel.com.pki.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import megatravel.com.pki.converter.CertificateConverter;
import megatravel.com.pki.domain.DTO.cer.CertificateDistributionDTO;
import megatravel.com.pki.service.CertificateDistributionService;
import megatravel.com.pki.util.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/dist")
public class CertificateDistributionController extends ValidationController {

    private static final Logger logger = LoggerFactory.getLogger(CertificateController.class);

    @Autowired
    private CertificateDistributionService distributionService;

    /**
     * POST /api/dist
     *
     * @param request that needs to be processed
     * @return message about action results
     */
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    //@PreAuthorize("hasAuthority('SECADMIN')")
    public ResponseEntity<String> save(@RequestBody String request) throws IOException, ValidationException {
        validateJSON(request, "distribution.json");
        CertificateDistributionDTO cert = new ObjectMapper().readValue(request, CertificateDistributionDTO.class);
        distributionService.distribute(CertificateConverter.toEntity(cert));
        logger.info("action=distributeCert serialNumber={} status=success", cert.getSerialNumber());
        return new ResponseEntity<>("Certificate successfully distributed!", HttpStatus.OK);
    }
}
