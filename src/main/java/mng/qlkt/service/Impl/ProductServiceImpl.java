package mng.qlkt.service.Impl;

import mng.qlkt.dto.dtos.ProductFilter;
import mng.qlkt.dto.request.ProductForm;
import mng.qlkt.model.Producer;
import mng.qlkt.model.Product;
import mng.qlkt.repository.ProductRepository;
import mng.qlkt.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProducerServiceImpl producerService;
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<?> searchProduct(ProductFilter productFilter) throws Exception {
        log.info("--------- search product  -----------");
        Pageable pageable = PageRequest.of(productFilter.page(), productFilter.size());
        var result = productRepository.getAllProductList(pageable, productFilter.productId(), productFilter.productName(), productFilter.producers());
        return result;
    }

    @Override
    public Product updateProduct(ProductForm productForm, Long id) throws Exception {
        log.info("--------update user---------");
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(!optionalProduct.isPresent()) {
            throw new Exception("Product không được update");
        }
        Product product1 = optionalProduct.get();
        product1.setProductId(productForm.getProductId());
        product1.setProductName(productForm.getProductName());
        product1.setConcentration(productForm.getConcentration());
        product1.setPrice(productForm.getPrice());
        product1.setIngredients(productForm.getIngredients());
        product1.setRegulations(productForm.getRegulations());
        product1.setDosage(productForm.getDosage());
        product1.setMedicinesUseFor(productForm.getMedicinesUseFor());
        product1.setSideEffects(productForm.getSideEffects());
        product1.setUseMedicine(productForm.getUseMedicine());
        Set<Producer> producers = new HashSet<>();
        Producer producer = producerService.findByName(productForm.getProducer()).orElseThrow(
                ()-> new RuntimeException("Không tìm được NCC"));
        producers.add(producer);
        product1.setProducers(producers);
        return productRepository.save(product1);
    }

    @Override
    public Product save(Product product) throws Exception {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        int hour = now.getHour();
        product.setProductId("SP" + formattedDate +hour+minute);
        product.setIsActive(1);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) throws Exception {
        var count = productRepository.countProductWareHouse(id);
        if(count > 0) {
            throw new Exception("Đã tồn tại trong kho");
        }else {
            productRepository.deleteById(id);
        }
    }

}
