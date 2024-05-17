package mng.qlkt.controller;

import mng.qlkt.dto.dtos.WareHouseFilter;
import mng.qlkt.dto.request.WareHouseForm;
import mng.qlkt.entities.ErrorCode;
import mng.qlkt.entities.MyResponse;
import mng.qlkt.model.Product;
import mng.qlkt.model.WareHouse;
import mng.qlkt.repository.ProductRepository;
import mng.qlkt.service.Impl.WareHouseServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@RestController
@RequestMapping("/warehouse")
public class WareHouseController {
    @Autowired
    WareHouseServiceImpl wareHouseService;
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/search")
    public MyResponse<?> searchWareHouse(WareHouseFilter wareHouseFilter) throws Exception {
        var page = wareHouseService.searchWareHouse(wareHouseFilter);
        return MyResponse.response(page);
    }

    @PutMapping("/update/{id}")
    public MyResponse<?> updateWareHouse(@RequestBody WareHouse wareHouse,Long id) throws Exception {
        try {
            wareHouseService.updateWareHouse(wareHouse, id);
            return MyResponse.response(ErrorCode.UPDATED_OK.getCode(), ErrorCode.UPDATED_OK.getMsgError());
        }catch (Exception ex) {
            return MyResponse.response(ErrorCode.UPDATED_FAIL.getCode(),ErrorCode.UPDATED_FAIL.getMsgError());
        }
    }

    @PostMapping("/created")
    public MyResponse<?> save(@Valid @RequestBody WareHouseForm wareHouseForm) throws Exception {
        try {
            WareHouse wareHouse = new WareHouse(wareHouseForm.getExpiry(),wareHouseForm.getManufacture_date(),wareHouseForm.getIs_active(),wareHouseForm.getQuantity());
            Set<Product> products = new HashSet<>();
            Product product = productRepository.findByProductName(wareHouseForm.getProduct()).orElseThrow(
                    ()-> new RuntimeException("Không tìm được Sản phẩm"));
            products.add(product);
            wareHouse.setProduct(products);
            wareHouseService.save(wareHouse);
            return MyResponse.response(ErrorCode.CREATED_OK.getCode(), ErrorCode.CREATED_OK.getMsgError());
        }
        catch (Exception ex) {
            log.info(ex);
            return MyResponse.response(ErrorCode.CREATED_FAIL.getCode(), ErrorCode.CREATED_FAIL.getMsgError());
        }
    }

    @GetMapping("/find/{id}")
    public MyResponse<?> findById(@PathVariable Long id) throws Exception {
        var warehouser = wareHouseService.findById(id);
        return MyResponse.response(warehouser);
    }

    @GetMapping("/test")
    public MyResponse<?> updateStatusDate(){
        wareHouseService.updateStatusDate();
        return MyResponse.response("ok");
    }
}
