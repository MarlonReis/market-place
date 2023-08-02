package br.com.market.place.application.controler;

import br.com.market.place.domain.customer.boundary.legal.CreateLegalInputBoundary;
import br.com.market.place.domain.customer.boundary.legal.ReadLegalCustomerOutputBoundary;
import br.com.market.place.domain.customer.boundary.legal.UpdateLegalInputBoundary;
import br.com.market.place.domain.shared.boundary.ResponseBoundary;
import br.com.market.place.infrastructure.service.LegalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/customer/legal")
public class LegalController {
    private final LegalService service;

    @Autowired
    public LegalController(LegalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateLegalInputBoundary data) {
        service.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public void update(@RequestBody UpdateLegalInputBoundary data) {
        service.update(data);
    }

    @GetMapping("/document/{document}")
    public ResponseEntity<ResponseBoundary<ReadLegalCustomerOutputBoundary>> findLegalByDocument(@PathVariable("document") String document) {
        var response = service.findLegalCustomerByCNPJ(document);
        return ResponseEntity.ok(new ResponseBoundary<>(response, true));
    }

    @GetMapping("/{email}")
    public ResponseEntity<ResponseBoundary<ReadLegalCustomerOutputBoundary>> findLegalByEmail(
            @PathVariable("email") String email) {
        var response = service.findLegalCustomerByEmail(email);
        return ResponseEntity.ok(new ResponseBoundary<>(response, true));
    }

}