package br.com.market.place.application.controler;

import br.com.market.place.MarketPlaceApplication;
import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.factory.CustomerEntityMockFactory;
import br.com.market.place.factory.PaymentEntityMockFactory;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.payment.entity.Billet;
import br.com.market.place.domain.payment.entity.CredCard;
import br.com.market.place.domain.payment.repository.PaymentRepository;
import br.com.market.place.domain.payment.service.PaymentService;
import br.com.market.place.domain.payment.value.PaymentId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(classes = MarketPlaceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private CustomerRepository customerRepository;
    @SpyBean
    private PaymentRepository paymentRepository;
    @SpyBean
    private PaymentService paymentService;
    private Customer customer;
    private Billet billet;
    private CredCard credCard;

    @BeforeEach
    void setUp() {
        var factory = new PaymentEntityMockFactory();
        customer = new CustomerEntityMockFactory().makePhysicalFactory().now();
        billet = factory.billetFactory(customer);
        credCard = factory.credCardFactory(customer);
    }

    @AfterEach
    void setDown() {
        paymentRepository.deleteAll(paymentRepository.findAll());
        customerRepository.deleteAll(customerRepository.findAll());
    }

    @Test
    void shouldReturn201WhenCreateBilletPayment() throws Exception {
        customerRepository.saveAndFlush(customer);
        mockMvc.perform(post("/v1/api/payment/billets")
                        .header("x-customer-id", customer.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": "500.00",
                                  "currencyType": "GBP",
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
    void shouldReturn202WhenNotFoundCustomer() throws Exception {
        mockMvc.perform(post("/v1/api/payment/billets")
                        .header("x-customer-id", new CustomerId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": "500.00",
                                  "currencyType": "GBP",
                                  "address": {
                                    "city": "Santa Rita do Sapucai",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """))
                .andExpect(status().isNoContent()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Customer not found!")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturn400WhenTryCreateBilletWithoutXCustomerIdInTheHeader() throws Exception {
        mockMvc.perform(post("/v1/api/payment/billets")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": "500.00",
                                  "currencyType": "GBP",
                                  "address": {
                                    "city": "Santa Rita do Sapucai",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """))
                .andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Required header 'x-customer-id' is not present.")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturn400WhenTryCreateBilletWithoutBody() throws Exception {
        mockMvc.perform(post("/v1/api/payment/billets")
                        .header("x-customer-id", new CustomerId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Invalid request content.")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturn200AndBilletDataWhenFoundBilletByCustomerId() throws Exception {
        paymentRepository.saveAndFlush(billet);
        mockMvc.perform(get("/v1/api/payment/billets/{customerId}", customer.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.[*].paymentId").isNotEmpty()).
                andExpect(jsonPath("$.data.[*].dueDate").isNotEmpty()).
                andExpect(jsonPath("$.data.[*].document").value("53627187113")).
                andExpect(jsonPath("$.data.[*].documentType").value("CPF")).
                andExpect(jsonPath("$.data.[*].payLine").isNotEmpty()).
                andExpect(jsonPath("$.data.[*].amount").value("R$ 10,00")).
                andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldReturn202WhenNotFoundCustomerById() throws Exception {
        mockMvc.perform(get("/v1/api/payment/billets/{customerId}", "39c40b3b-e72e-4487-a538-d70376da258c")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print()).
                andExpect(status().isNoContent()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Cannot be possible to find billets payment by customer!")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturn422WhenSetInvalidCustomerId() throws Exception {
        mockMvc.perform(get("/v1/api/payment/billets/{customerId}", "2434323234234")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)).andDo(print()).
                andExpect(status().isUnprocessableEntity()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Id 2434323234234 is invalid!")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturn201WhenCreateCredCardPayment() throws Exception {
        customerRepository.saveAndFlush(customer);
        mockMvc.perform(post("/v1/api/payment/cred-card")
                        .header("x-customer-id", customer.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "cardPan": "5273548390118161",
                                  "amount": "500.00",
                                  "currencyType": "GBP",
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
    void shouldReturn204WhenTryCreateCredCardPaymentAndNotFoundCustomerById() throws Exception {
        mockMvc.perform(post("/v1/api/payment/cred-card")
                        .header("x-customer-id", new CustomerId())
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "cardPan": "5273548390118161",
                                  "amount": "500.00",
                                  "currencyType": "GBP",
                                  "address": {
                                    "city": "Santa Rita do Sapucai",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """))
                .andExpect(status().isNoContent()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Cannot be possible to find customer by id")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturn400WhenNotSetCustomerIdInTheHeader() throws Exception {
        mockMvc.perform(post("/v1/api/payment/cred-card")
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                        .content("""
                                {
                                  "cardPan": "5273548390118161",
                                  "amount": "500.00",
                                  "currencyType": "GBP",
                                  "address": {
                                    "city": "Santa Rita do Sapucai",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Required header 'x-customer-id' is not present.")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturn400TryCreateCredCardPaymentWithoutBody() throws Exception {
        mockMvc.perform(post("/v1/api/payment/cred-card")
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                        .header("x-customer-id", new CustomerId())).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Invalid request content.")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturn400WhenTryCreateCredCardPaymentWithFieldNull() throws Exception {
        mockMvc.perform(post("/v1/api/payment/cred-card")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .header("x-customer-id", new CustomerId())
                        .content("""
                                 {
                                  "amount": "500.00",
                                  "currencyType": "GBP",
                                  "address": {
                                    "city": "Santa Rita do Sapucai",
                                    "street": "Joaquim Teles de Souza",
                                    "number": "80",
                                    "component": "APT 102",
                                    "zipCode": "37540000"
                                  }
                                }
                                """)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON)).
                andExpect(jsonPath("$.data.message").value("Attribute cardPan is required!")).
                andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldReturn200WhenFindCredCardPaymentsByCustomerId() throws Exception {
        paymentRepository.saveAndFlush(credCard);

        mockMvc.perform(get("/v1/api/payment/cred-card/{customerId}", customer.getId())
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                .andDo(print()).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.data").isArray()).
                andExpect(jsonPath("$.data[*].paymentId").isNotEmpty()).
                andExpect(jsonPath("$.data[*].cardPan").value("2720********7456")).
                andExpect(jsonPath("$.data[*].fullName").value("Benedito Caio Araújo")).
                andExpect(jsonPath("$.data[*].document").value("53627187113")).
                andExpect(jsonPath("$.data[*].documentType").value("CPF")).
                andExpect(jsonPath("$.data[*].amount").value("R$ 10,00")).
                andExpect(jsonPath("$.data[*].status").value("PENDING")).
                andExpect(jsonPath("$.data[*].createAt").isNotEmpty()).
                andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldReturn204WhenNotFindCredCardPaymentsByCustomerId() throws Exception {
        mockMvc.perform(get("/v1/api/payment/cred-card/{customerId}", new CustomerId())
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                .andDo(print()).
                andExpect(status().isNoContent()).
                andExpect(jsonPath("$.success").value(false)).
                andExpect(jsonPath("$.data.message").value("Cannot be possible to find cred card payment by customer!"));
    }

    @Test
    void shouldReturn200WhenRunPaymentByPaymentIdWithSuccess() throws Exception {
        paymentRepository.saveAndFlush(credCard);
        mockMvc.perform(post("/v1/api/payment/pay")
                .header("x-payment-id", credCard.getId())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)).andExpect(status().isOk());

        ArgumentCaptor<PaymentId> argument = ArgumentCaptor.forClass(PaymentId.class);
        Mockito.verify(paymentService).pay(argument.capture());
        assertThat(argument.getValue(), Matchers.is(credCard.getId()));
    }

    @Test
    void shouldReturn400WhenRunCredCardPaymentWithoutPaymentIdInTheHeader() throws Exception {
        mockMvc.perform(post("/v1/api/payment/pay").contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)).andDo(print()).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.success").value(false)).
                andExpect(jsonPath("$.data.message")
                        .value("Required header 'x-payment-id' is not present."));
    }

    @Test
    void shouldReturn422WhenRunCredCardPaymentIdInvalid() throws Exception {
        mockMvc.perform(post("/v1/api/payment/pay").contentType(APPLICATION_JSON)
                        .header("x-payment-id", "3423424234")
                        .accept(APPLICATION_JSON)).andDo(print()).
                andExpect(status().isUnprocessableEntity()).
                andExpect(jsonPath("$.success").value(false)).
                andExpect(jsonPath("$.data.message").value("Id 3423424234 is invalid!"));
    }

    @Test
    void shouldReturn200WhenCancelPaymentByPaymentId() throws Exception {
        paymentRepository.saveAndFlush(billet);
        mockMvc.perform(post("/v1/api/payment/cancel")
                        .header("x-payment-id", billet.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status()
                        .isOk()).andDo(print());

        ArgumentCaptor<PaymentId> argument = ArgumentCaptor.forClass(PaymentId.class);
        Mockito.verify(paymentService).cancel(argument.capture());
        assertThat(argument.getValue(), Matchers.is(billet.getId()));
    }

    @Test
    void shouldReturn204WhenNotFoundPaymentToCancel() throws Exception {
        mockMvc.perform(post("/v1/api/payment/pay")
                        .header("x-payment-id", new PaymentId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent()).
                andExpect(jsonPath("$.success").value(false)).
                andExpect(jsonPath("$.data.message").value("Cannot be found payment by id"));
    }

    @Test
    void shouldReturn400WhenNotSetPaymentIdInTheHeaderToCancelPayment() throws Exception {
        mockMvc.perform(post("/v1/api/payment/pay")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.success").value(false)).
                andExpect(jsonPath("$.data.message").value("Required header 'x-payment-id' is not present."));
    }


}