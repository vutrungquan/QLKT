package mng.qlkt.service.Impl;

import mng.qlkt.dto.dto.EntryDto;
import mng.qlkt.dto.dto.WareHouseDto;
import mng.qlkt.dto.dtos.EntryFilter;
import mng.qlkt.dto.dtos.SaleEntryFilter;
import mng.qlkt.dto.request.EntryForm;
import mng.qlkt.dto.request.WareHouseForm;
import mng.qlkt.entities.SaleEntry;
import mng.qlkt.model.Entry;
import mng.qlkt.model.Product;
import mng.qlkt.model.WareHouse;
import mng.qlkt.repository.EntryRepository;
import mng.qlkt.repository.ProductRepository;
import mng.qlkt.repository.WareHouseRepository;
import mng.qlkt.service.EntryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service

public class EntryServiceImpl implements EntryService {
    @Autowired
    EntryRepository entryRepository;

    @Autowired
    WareHouseServiceImpl wareHouseService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WareHouseRepository wareHouseRepository;

    @Override
    public Entry save(EntryForm entryForm) throws Exception {
        Entry entry = new Entry();
        entry.setNote(entryForm.getNote());
        entry.setCreator(entryForm.getCreator());
        entry.setMoneyTotal(entryForm.getMoneyTotal());
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        int hour = now.getHour();
        entry.setIdEntry("PNK_" + formattedDate + hour + minute);
        entry.setIsActive(0);
        entryRepository.save(entry);
        for (WareHouseForm wareHouseForm : entryForm.getWareHouseForm()) {
            WareHouse wareHouse = new WareHouse();
            Set<Product> products = new HashSet<>();
            Product product = productRepository.findByProductName(wareHouseForm.getProduct()).orElseThrow(
                    () -> new RuntimeException("Không tìm được Sản phẩm"));
            products.add(product);
            wareHouse.setProduct(products);
            wareHouse.setExpiry(wareHouseForm.getExpiry());
            wareHouse.setManufactureDate(wareHouseForm.getManufacture_date());
            wareHouse.setQuantity(wareHouseForm.getQuantity());
            wareHouse.setQuantityfix(wareHouseForm.getQuantity());
            wareHouse.setIs_active(0);
            wareHouse.setEntry(entry);
            wareHouseService.save(wareHouse);
        }
        return entry;
    }

    @Override
    public Optional<EntryDto> findById(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Optional<Entry> entry;
        entry = entryRepository.findById(id);
        return entry.map(entry1 -> modelMapper.map(entry1, EntryDto.class));
    }

    @Override
    public Page<EntryDto> searchEntry(EntryFilter entryFilter) throws Exception {
        ModelMapper modelMapper = new ModelMapper();

        log.info("--------- search eNTRY  -----------");
        Pageable pageable = PageRequest.of(entryFilter.page(), entryFilter.size());
        Page<Entry> entries;
        if (entryFilter.startDate() != null || entryFilter.endDate() != null) {
            entries = entryRepository.getAllEntryListNotDate(pageable, entryFilter.nameProduct(), entryFilter.idEntry(), entryFilter.nameProducer());

        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(entryFilter.startDate());
            Date endDate = dateFormat.parse(entryFilter.endDate());
            entries = entryRepository.getAllEntryList(pageable, entryFilter.nameProduct(), entryFilter.idEntry(), entryFilter.nameProducer(), startDate, endDate);
        }

        return entries.map(entry -> modelMapper.map(entry, EntryDto.class));
    }

    @Override
    @Transactional
    public void approveEntry(Integer isActive, Long id) throws Exception {
        var optEntry = this.findById(id);
        if (isActive == 1) {
            for (WareHouseDto wareHouseDto : optEntry.get().getWareHouse()) {
                var idWareHouse = wareHouseDto.getId();
                wareHouseRepository.updateEntryWareHouse(isActive, idWareHouse);
            }
            entryRepository.updateEntryActive(isActive, id);
        } else if (isActive == 2) {
            entryRepository.updateEntryActive(isActive, id);
        }
    }


    public Page<EntryDto> saleEntryUser(SaleEntryFilter saleEntryFilter) throws ParseException {
        ModelMapper modelMapper = new ModelMapper();
        log.info("--------- search eNTRY  -----------");
        Pageable pageable = PageRequest.of(saleEntryFilter.page(), saleEntryFilter.size());
        Page<Entry> entries;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(saleEntryFilter.startDate());
        Date endDate = dateFormat.parse(saleEntryFilter.endDate());
        entries = entryRepository.getAllEntrySaleList(pageable, saleEntryFilter.nameProduct(), saleEntryFilter.idEntry(), saleEntryFilter.nameProducer(), saleEntryFilter.creator(), startDate, endDate);
        return entries.map(entry -> modelMapper.map(entry, EntryDto.class));
    }
    @Override
    public SaleEntry saleEntry(SaleEntryFilter saleEntryFilter) throws ParseException {
        SaleEntry saleEntry = new SaleEntry();
        saleEntry.setEntryDto(this.saleEntryUser(saleEntryFilter));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(saleEntryFilter.startDate());
        Date endDate = dateFormat.parse(saleEntryFilter.endDate());
        saleEntry.setTotalAmount(entryRepository.calculateTotalMoney(saleEntryFilter.nameProduct(), saleEntryFilter.idEntry(), saleEntryFilter.nameProducer(), saleEntryFilter.creator(), startDate, endDate));
        return saleEntry;
    }


}
