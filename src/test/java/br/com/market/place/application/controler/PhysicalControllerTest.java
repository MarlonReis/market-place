package br.com.market.place.application.controler;

import br.com.market.place.MarketPlaceApplication;
import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.customer.entity.Physical;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.customer.value.BirthDate;
import br.com.market.place.domain.customer.value.Email;
import br.com.market.place.domain.customer.value.Name;
import br.com.market.place.domain.customer.value.Telephone;
import br.com.market.place.domain.shared.value.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.market.place.domain.customer.constant.DocumentType.CPF;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(classes = MarketPlaceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PhysicalControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private CustomerRepository repository;
    private Physical physical;

    @BeforeEach
    void setUp() {
        physical = Physical.Builder.build()
                .withName(new Name("Benedito Caio Araújo"))
                .withDocument("536.271.871-13", CPF)
                .withEmail(new Email("benedito-araujo91@gmnail.com"))
                .withBirthDate(new BirthDate("23/02/2001"))
                .withTelephone(new Telephone("11999982343"))
                .withAddress(Address.Builder.build().withCity("London")
                        .withStreet("Baker Street").withNumber("221")
                        .withComponent("B").withZipCode("37540232").now())
                .now();
    }

    @AfterEach
    void setDown() {
        repository.delete(physical);
    }

    @Test
    void shouldReturnStatusCode201WhenCreatePhysicalWithSuccess() throws Exception {
        mockMvc.perform(post("/v1/api/customer/physical")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Marlon Reis",
                                  "email": "marlon-reis@email.com",
                                  "telephone": "35999902134",
                                  "document": "41953736033",
                                  "documentType": "CPF",
                                  "birthDate": "31/12/1991",
                                  "address": {
                                    "city": "Santa Rita do Sapucai",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnStatusCode422WhenTelephoneIsInvalid() throws Exception {
        mockMvc.perform(post("/v1/api/customer/physical")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Marlon Reis",
                                  "email": "marlon-reis@email.com",
                                  "telephone": "999902134",
                                  "document": "41953736033",
                                  "documentType": "CPF",
                                  "birthDate": "31/12/1991",
                                  "address": {
                                    "city": "Santa Rita do Sapucai",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """))
                .andExpect(status().isUnprocessableEntity()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Attribute telephone is invalid!")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturnStatusCode200WhenUpdatePhysicalWithSuccess() throws Exception {
        Customer response = repository.save(physical);
        mockMvc.perform(put("/v1/api/customer/physical")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "email": "exemple@email.com",
                                  "telephone": "34999902135",
                                  "address": {
                                    "city": "Rio de Janeiro",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """.formatted(response.getId().getId())))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusCode202WhenNotFoundCustomerToUpdate() throws Exception {
        mockMvc.perform(put("/v1/api/customer/physical")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "33711d74-ef1f-4b5a-882f-d1a8634463f2",
                                  "email": "exemple@email.com",
                                  "telephone": "34999902135",
                                  "address": {
                                    "city": "Rio de Janeiro",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """))
                .andExpect(status().isNotFound()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Cannot found customer by id!")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturnStatusCode200WhenFoundPhysicalByDocument() throws Exception {
        var response = repository.save(physical);
        mockMvc.perform(get("/v1/api/customer/physical/document/{document}/{type}", "53627187113", "CPF")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.customerId").value(response.getId().toString())).
                andExpect(jsonPath("$.data.address.address").value("Baker Street, 221, B, London, 37540232")).
                andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldReturnStatusCode202WhenNotFoundFoundPhysicalByDocument() throws Exception {
        mockMvc.perform(get("/v1/api/customer/physical/document/{document}/{type}", "53627187113", "CPF")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Customer not found by document!")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturnStatusCode200WhenFoundPhysicalByEmail() throws Exception {
        var response = repository.save(physical);
        mockMvc.perform(get("/v1/api/customer/physical/{email}", "benedito-araujo91@gmnail.com")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.customerId").value(response.getId().toString())).
                andExpect(jsonPath("$.data.address.address").value("Baker Street, 221, B, London, 37540232")).
                andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldReturnStatusCode202WhenNotFoundFoundPhysicalByEmail() throws Exception {
        mockMvc.perform(get("/v1/api/customer/physical/{email}", "benedito-araujo91@gmnail.com")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Customer not found by email!")).
                andExpect(jsonPath("$.success").value(false));
    }

}