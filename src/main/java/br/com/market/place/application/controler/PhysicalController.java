package br.com.market.place.application.controler;

import br.com.market.place.domain.customer.boundary.physical.CreatePhysicalInputBoundary;
import br.com.market.place.domain.customer.boundary.physical.ReadPhysicalOutputBoundary;
import br.com.market.place.domain.customer.boundary.physical.UpdatePhysicalInputBoundary;
import br.com.market.place.domain.shared.boundary.ResponseBoundary;
import br.com.market.place.infrastructure.service.customer.PhysicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/customer/physical")
public class PhysicalController {
    private final PhysicalService service;

    @Autowired
    public PhysicalController(PhysicalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreatePhysicalInputBoundary data) {
        service.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public void update(@RequestBody UpdatePhysicalInputBoundary data) {
        service.update(data);
    }

    @GetMapping("/document/{document}/{type}")
    public ResponseEntity<ResponseBoundary<ReadPhysicalOutputBoundary>> findPhysicalByDocument(
            @PathVariable("document") String document,
            @PathVariable("type") String type
    ) {
        var response = service.findCustomerByDocument(document, type);
        return ResponseEntity.ok(new ResponseBoundary<>(response, true));
    }

    @GetMapping("/{email}")
    public ResponseEntity<ResponseBoundary<ReadPhysicalOutputBoundary>> findPhysicalByEmail(@PathVariable("email") String email) {
        var response = service.findCustomerByEmail(email);
        return ResponseEntity.ok(new ResponseBoundary<>(response, true));
    }

}