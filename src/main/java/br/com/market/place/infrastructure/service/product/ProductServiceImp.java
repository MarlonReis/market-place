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
import br.com.market.place.domain.shared.exception.NotFoundException;
import br.com.market.place.domain.shared.exception.UpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);
    private final ProductRepository repository;

    @Autowired
    public ProductServiceImp(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(CreateProductInputBoundary product) {
        try {
            repository.saveAndFlush(product.toEntity());
            logger.info("Create product record: {}", product);
        } catch (Exception e) {
            logger.error("Error when create product: {}", e.getMessage());
            throw new CreateException("Cannot be possible to create product record!");
        }
    }

    @Override
    public void update(UpdateProductInputBoundary data) {
        logger.info("Update product record: {}", data);

        Product product = findOneByProductId(new ProductId(data.id()));

        product.changePrice(data.price(), data.currencyType());
        product.changeTitle(data.title(), data.description());
        product.changeTag(data.tag());

        try {
            repository.saveAndFlush(product);
            logger.info("Updated product record: {}", data);
        } catch (Exception ex) {
            logger.error("Error when update product: {}", ex.getMessage());
            throw new UpdateException("Cannot be possible to update product record!");
        }
    }

    @Override
    public void addQuantity(AddProductQuantityInputBoundary data) {
        logger.info("Add product quantity record: {}", data);
        var product = findOneByProductId(new ProductId(data.id()));
        product.add(new Quantity(data.quantity()));

        try {
            repository.saveAndFlush(product);
            logger.info("Updated product quantity record: {}", data);
        } catch (Exception ex) {
            logger.error("Error when add product quantity: {}", ex.getMessage());
            throw new UpdateException("Cannot be possible to update product record!");
        }
    }

    @Override
    public Set<ReadProductOutputBoundary> findAllProducts() {
        var response = repository.findAll().stream().map(Product::toOutputBoundary).collect(Collectors.toUnmodifiableSet());
        if (response.isEmpty()) throw new NotFoundException("Products not found!");
        return response;
    }

    @Override
    public ReadProductOutputBoundary findProductById(ProductId id) {
        return findOneByProductId(id).toOutputBoundary();
    }

    private Product findOneByProductId(ProductId id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Product not found by id!"));
    }
}
