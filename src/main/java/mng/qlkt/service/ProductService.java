package mng.qlkt.service;

import mng.qlkt.dto.dtos.ProductFilter;
import mng.qlkt.dto.request.ProductForm;
import mng.qlkt.model.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductService {

    Optional<Product> findById(Long id) ;

    Page<?> searchProduct(ProductFilter productFilter) throws Exception;

    public Product updateProduct(ProductForm productForm , Long id) throws Exception;

    public Product save(Product product) throws Exception;

    public void deleteProduct(Long id) throws Exception;
}
