package mng.qlkt.service.Impl;

import mng.qlkt.dto.dto.WareHouseDto;
import mng.qlkt.dto.dtos.WareHouseFilter;
import mng.qlkt.model.WareHouse;
import mng.qlkt.repository.WareHouseRepository;
import mng.qlkt.service.WareHouseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WareHouseServiceImpl implements WareHouseService {
    @Autowired
    WareHouseRepository wareHouseRepository;

    @Override
    public Optional<WareHouse> findById(Long id) {
        return wareHouseRepository.findById(id);
    }

    @Override
    public Page<WareHouseDto> searchWareHouse(WareHouseFilter wareHouseFilter) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        log.info("--------- search  -----------");
        var test = wareHouseFilter.isActive();
        Pageable pageable = PageRequest.of(wareHouseFilter.page(), wareHouseFilter.size());
        Page<WareHouse> wareHouses;
        List<Integer> numberList = Arrays.stream(test.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        var result = wareHouseRepository.getAllWareHouseList(pageable, wareHouseFilter.productName(), wareHouseFilter.productId(), numberList);
        return result.map(wareHouse -> modelMapper.map(wareHouse, WareHouseDto.class));
    }

    @Override
    public WareHouse updateWareHouse(WareHouse wareHouse, Long id) throws Exception {
        log.info("--------update warehouse---------");
        Optional<WareHouse> optionalWareHouse = wareHouseRepository.findById(id);
        if(!optionalWareHouse.isPresent()) {
            throw new Exception("warehouse không được update");
        }
        WareHouse wareHouse1 = new WareHouse();
        wareHouse1.setIs_active(wareHouse.getIs_active());
        wareHouse1.setProduct(wareHouse.getProduct());
        wareHouse1.setExpiry(wareHouse.getExpiry());
        wareHouse1.setManufactureDate(wareHouse.getManufactureDate());
        return wareHouseRepository.save(wareHouse1);
    }

    @Override
    public WareHouse save(WareHouse wareHouse) throws Exception {
        return wareHouseRepository.save(wareHouse);
    }

    @Scheduled(fixedRate = 1*60*1000)
    @Transactional
    public void updateStatusDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 5);

        Date currentDateMinus5Days = calendar.getTime();
        wareHouseRepository.updateStatusDateForExpiredWarehouses(currentDateMinus5Days);
    }




}
