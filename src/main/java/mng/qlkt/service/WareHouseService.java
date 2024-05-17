package mng.qlkt.service;

import mng.qlkt.dto.dto.WareHouseDto;
import mng.qlkt.dto.dtos.WareHouseFilter;
import mng.qlkt.model.WareHouse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface WareHouseService {

    Optional<WareHouse> findById(Long id) ;

    Page<WareHouseDto> searchWareHouse(WareHouseFilter wareHouseFilter) throws Exception;

     WareHouse updateWareHouse(WareHouse wareHouse , Long id) throws Exception;

     WareHouse save(WareHouse wareHouse) throws Exception;


}
