package br.com.market.place.infrastructure.service.product;

import br.com.market.place.domain.product.boundary.AddProductQuantityInputBoundary;
import br.com.market.place.domain.product.boundary.CreateProductInputBoundary;
import br.com.market.place.domain.product.boundary.ReadProductOutputBoundary;
import br.com.market.place.domain.product.boundary.UpdateProductInputBoundary;
import br.com.market.place.domain.product.entity.Product;
import br.com.market.place.domain.product.repository.ProductRepository;
import br.com.market.place.domain.product.service.ProductService;
import br.com.market.place.domain.product.value.ProductId;
import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.shared.exception.CreateException;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import br.com.market.place.domain.shared.exception.NotFoundException;
import br.com.market.place.domain.shared.exception.UpdateException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;


@DataJpaTest()
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import({ProductServiceImp.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductServiceImpTest {
    @SpyBean
    private ProductService service;

    @SpyBean
    private ProductRepository repository;

    private CreateProductInputBoundary createProduct;
    private Product product;

    @BeforeEach
    void setUp() {
        createProduct = new CreateProductInputBoundary(
                "Any Title", "Any Description",
                "Any Tag", 50, "50.00",
                "BRL"
        );
        product = createProduct.toEntity();
    }

    @Test
    void shouldCreateProductWithSuccess() {
        service.create(createProduct);

        ArgumentCaptor<Product> argument = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(repository).saveAndFlush(argument.capture());

        Product product = argument.getValue();

        assertThat(product.getId(), Matchers.notNullValue());
        assertThat(product.getCreateAt(), Matchers.notNullValue());
    }

    @Test
    void shouldThrowsCreateExceptionWhenCannotBeCreateProduct() {
        doThrow(new RuntimeException("Any message")).when(repository).saveAndFlush(Mockito.any(Product.class));

        var exception = assertThrows(CreateException.class, () -> service.create(createProduct));
        assertThat(exception.getMessage(), Matchers.is("Cannot be possible to create product record!"));
    }

    @Test
    void shouldUpdateCustomerWhenFoundById() {
        Product product = createProduct.toEntity();
        repository.saveAndFlush(product);
        var input = new UpdateProductInputBoundary(product.getId().toString(), "Any changed title", "Any changed description", "Any changed tag", "12.34", "BRL");

        service.update(input);
        var response = repository.findById(product.getId()).orElseThrow();
        assertThat(response.getTitle().toString(), Matchers.is("Any changed title, Any changed description"));
        assertThat(response.getTag(), Matchers.is("Any changed tag"));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNotFindProductById() {
        var input = new UpdateProductInputBoundary("f243f8d7-cbc8-41a3-9a15-eae3472bd2e6", "Any changed title", "Any changed description", "Any changed tag", "12.34", "BRL");

        var exception = assertThrows(NotFoundException.class, () -> service.update(input));
        assertThat(exception.getMessage(), Matchers.is("Product not found by id!"));
    }


    @Test
    void shouldThrowUpdateExceptionWhenRepositoryUpdateThrowsException() {
        doReturn(Optional.of(product)).when(repository).findById(Mockito.any());

        doThrow(new RuntimeException("Any message")).when(repository).saveAndFlush(Mockito.any());
        var input = new UpdateProductInputBoundary("271ad5d9-65fa-4c67-a374-6c2e7757e1ad", "Any changed title", "Any changed description", "Any changed tag", "12.34", "BRL");

        var exception = assertThrows(UpdateException.class, () -> service.update(input));
        assertThat(exception.getMessage(), Matchers.is("Cannot be possible to update product record!"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenReceiveInvalidPrice() {
        doReturn(Optional.of(product)).when(repository).findById(Mockito.any());

        var input = new UpdateProductInputBoundary("271ad5d9-65fa-4c67-a374-6c2e7757e1ad",
                "Any changed title", "Any changed description",
                "Any changed tag", "12.34", null);

        var exception = assertThrows(InvalidDataException.class, () -> service.update(input));
        assertThat(exception.getMessage(), Matchers.is("Attribute currencyType is required!"));
    }

    @Test
    void shouldSumQuantityOfProductWhenFindByProductId() {
        repository.saveAndFlush(product);
        service.addQuantity(new AddProductQuantityInputBoundary(product.getId().toString(), 10));
        assertThat(product.getQuantity(), Matchers.is(new Quantity(60)));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenAddQuantityNotFindProductByProductId() {
        var input = new AddProductQuantityInputBoundary("7a2c3626-61bc-405c-9ebe-36e1701cfd5a", 10);
        var exception = assertThrows(NotFoundException.class, () -> service.addQuantity(input));
        assertThat(exception.getMessage(), Matchers.is("Product not found by id!"));
    }

    @Test
    void shouldThrowUpdateExceptionWhenCannotBeUpdateProductQuantity() {
        var input = new AddProductQuantityInputBoundary("7a2c3626-61bc-405c-9ebe-36e1701cfd5a", 10);

        doReturn(Optional.of(product)).when(repository).findById(Mockito.any());
        doThrow(new RuntimeException("Any message")).when(repository).saveAndFlush(Mockito.any());

        var exception = assertThrows(UpdateException.class, () -> service.addQuantity(input));
        assertThat(exception.getMessage(), Matchers.is("Cannot be possible to update product record!"));
    }

    @Test
    void shouldReturnAllProduct() {
        repository.saveAndFlush(product);

        var response = service.findAllProducts();
        assertThat(response, Matchers.hasSize(1));
        assertThat(response, Matchers.hasItem(new ReadProductOutputBoundary(
                product.getId().toString(),
                product.getTitle().title(),
                product.getTitle().description(),
                product.getTag(),
                product.getQuantity().value(),
                product.getPrice().formatted(),
                product.getCreateAt().dateFormatted()
        )));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindAllProductsReturnEmpty() {
        var exception = assertThrows(NotFoundException.class, () -> service.findAllProducts());
        assertThat(exception.getMessage(), Matchers.is("Products not found!"));
    }

    @Test
    void shouldReturnProductWhenFindByProductId() {
        repository.saveAndFlush(product);
        var response = service.findProductById(product.getId());
        assertThat(response, Matchers.is(new ReadProductOutputBoundary(
                product.getId().toString(),
                product.getTitle().title(),
                product.getTitle().description(),
                product.getTag(),
                product.getQuantity().value(),
                product.getPrice().formatted(),
                product.getCreateAt().dateFormatted()
        )));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNotFindProductByProductId() {
        var id = new ProductId("9b5c6ae3-0a81-4d25-9c9e-3ff5cdf6d5bc");
        var exception = assertThrows(NotFoundException.class, () -> service.findProductById(id));
        assertThat(exception.getMessage(), Matchers.is("Product not found by id!"));
    }


}