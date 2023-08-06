package br.com.market.place.application.controler;

import br.com.market.place.MarketPlaceApplication;
import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.factory.CustomerEntityMockFactory;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(classes = MarketPlaceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LegalControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private CustomerRepository repository;
    private Legal legal;

    @BeforeEach
    void setUp() {
        legal = new CustomerEntityMockFactory().makeLegalFactory().now();
    }

    @AfterEach
    void setDown() {
        repository.delete(legal);
    }

    @Test
    void shouldReturnStatusCode201WhenCreateLegalWithSuccess() throws Exception {
        mockMvc.perform(post("/v1/api/customer/legal")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Alana e Guilherme",
                                  "telephone": "1138384351",
                                  "email": "qualidade@alanaeguilhermeconstrucoesltda.com.br",
                                  "cnpj": "77111888000164",
                                  "fantasyName": "Alana e Guilherme Construções Ltda",
                                  "municipalRegistration": "1512994",
                                  "stateRegistration": "151299495372",
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
    void shouldReturnStatusCode422WhenCNPJIsInvalid() throws Exception {
        mockMvc.perform(post("/v1/api/customer/legal")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                  {
                                   "name": "Alana e Guilherme",
                                   "telephone": "1138384351",
                                   "email": "qualidade@alanaeguilhermeconstrucoesltda.com.br",
                                   "cnpj": "7711188",
                                   "fantasyName": "Alana e Guilherme Construções Ltda",
                                   "municipalRegistration": "1512994",
                                   "stateRegistration": "151299495372",
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
                andExpect(jsonPath("$.data.message").value("Attribute document of type CNPJ is invalid!")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturnStatusCode200WhenUpdateLegalWithSuccess() throws Exception {
        repository.saveAndFlush(legal);
        mockMvc.perform(put("/v1/api/customer/legal")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "exemple@email.com",
                                  "telephone": "34999902135",
                                  "cnpj":"33.747.249/0001-14",
                                  "address": {
                                    "city": "Rio de Janeiro",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusCode202WhenNotFoundLegalCustomerToUpdate() throws Exception {
        mockMvc.perform(put("/v1/api/customer/legal")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                 {
                                  "email": "exemple@email.com",
                                  "telephone": "34999902135",
                                  "cnpj":"33.747.249/0001-14",
                                  "address": {
                                    "city": "Rio de Janeiro",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """))
                .andExpect(status().isNoContent()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Customer not found by CNPJ!")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturnStatusCode200WhenFoundLegalByDocument() throws Exception {
        var response = repository.save(legal);
        mockMvc.perform(get("/v1/api/customer/legal/document/{document}", "33747249000114")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.customerId").value(response.getId().toString())).
                andExpect(jsonPath("$.data.address.address").value("Baker Street, 221, B, London, 37540232")).
                andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldReturnStatusCode202WhenNotFoundFoundLegalByDocument() throws Exception {
        mockMvc.perform(get("/v1/api/customer/legal/document/{document}", "33747249000114")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Customer not found!")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturnStatusCode200WhenFoundLegalCustomerByEmail() throws Exception {
        var response = repository.save(legal);
        mockMvc.perform(get("/v1/api/customer/legal/{email}", "financeiro@exemple.com.br")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.customerId").value(response.getId().toString())).
                andExpect(jsonPath("$.data.address.address").value("Baker Street, 221, B, London, 37540232")).
                andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldReturnStatusCode202WhenNotFoundFoundLegalByEmail() throws Exception {
        mockMvc.perform(get("/v1/api/customer/legal/{email}", "not.found@email.com")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Customer not found!")).
                andExpect(jsonPath("$.success").value(false));
    }


}