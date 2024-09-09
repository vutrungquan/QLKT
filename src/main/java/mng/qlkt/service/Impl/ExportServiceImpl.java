package mng.qlkt.service.Impl;

import mng.qlkt.dto.dto.ExportDto;
import mng.qlkt.dto.dto.WareHouseExportDto;
import mng.qlkt.dto.dtos.ExportFilter;
import mng.qlkt.dto.dtos.SaleExportFilter;
import mng.qlkt.dto.request.EntryForm;
import mng.qlkt.dto.request.WareHouseForm;
import mng.qlkt.entities.SaleExport;
import mng.qlkt.model.Export;
import mng.qlkt.model.Product;
import mng.qlkt.model.WareHouseExport;
import mng.qlkt.repository.*;
import mng.qlkt.service.ExportService;
import lombok.extern.log4j.Log4j2;
import mng.qlkt.repository.ExportRepository;
import mng.qlkt.repository.ProductRepository;
import mng.qlkt.repository.WareHouseRepository;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Service
@Log4j2
public class ExportServiceImpl implements ExportService {

    @Autowired
    ExportRepository exportRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WareHouseExportServiceImpl wareHouseExportService;

    @Autowired
    WareHouseRepository wareHouseRepository;

    @Override
    public void save(EntryForm entryForm) throws Exception {
        Export export = new Export();
        export.setNote(entryForm.getNote());
        export.setCreator(entryForm.getCreator());
        export.setMoneyTotal(entryForm.getMoneyTotal());
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        int hour = now.getHour();
        export.setIdExport("PXK_" + formattedDate +hour+minute);
        export.setIs_active(0);
        exportRepository.save(export);
        for(WareHouseForm wareHouseForm: entryForm.getWareHouseForm()){
            WareHouseExport wareHouseExport = new WareHouseExport();
            Set<Product> products = new HashSet<>();
            Product product = productRepository.findByProductName(wareHouseForm.getProduct()).orElseThrow(
                    ()-> new RuntimeException("Không tìm được Sản phẩm"));
            products.add(product);
            wareHouseExport.setProduct(products);
            wareHouseExport.setExpiry(wareHouseForm.getExpiry());
            wareHouseExport.setManufactureDate(wareHouseForm.getManufacture_date());
            wareHouseExport.setQuantity(wareHouseForm.getQuantity());
            wareHouseExport.setId_wareHouse(wareHouseForm.getId());
            var opt = wareHouseRepository.findById(wareHouseForm.getId());
            if (!opt.isPresent()) {
                throw new Exception("Sản phẩm không có trong kho");
            }
            if(wareHouseForm.getQuantity() > opt.get().getQuantity()){
                throw new Exception("Số lượng không đủ");
            }
            wareHouseExport.setIs_active(0);
            wareHouseExport.setExport(export);
            wareHouseExportService.save(wareHouseExport);
        }
    }



    @Override
    public Page<ExportDto> searchExport(ExportFilter exportFilter) throws Exception {
        ModelMapper modelMapper = new ModelMapper();

        log.info("--------- search export  -----------");
        Pageable pageable = PageRequest.of(exportFilter.page(), exportFilter.size());
        Page<Export> entries;
        if(exportFilter.startDate() != null || exportFilter.endDate() != null) {
            entries = exportRepository.getAllExportListNotDate(pageable, exportFilter.nameProduct(), exportFilter.idExport(), exportFilter.nameProducer());

        }
        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(exportFilter.startDate());
            Date endDate = dateFormat.parse(exportFilter.endDate());
            entries = exportRepository.getAllExportList(pageable, exportFilter.nameProduct(), exportFilter.idExport(), exportFilter.nameProducer(), startDate, endDate);
        }

        return entries.map(export -> modelMapper.map(export, ExportDto.class));
    }

    @Override
    public Optional<ExportDto> findById(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Optional<Export> export;
        export = exportRepository.findById(id);
        return export.map(export1 -> modelMapper.map(export1, ExportDto.class));
    }

    @Override
    @Transactional
    public void approveExport(Long id,Integer isActive) throws Exception {
        var optExport = this.findById(id);
        if(isActive == 1) {

            for (WareHouseExportDto wareHouseExport : optExport.get().getWareHouseExport()) {
                var opt = wareHouseRepository.findById(wareHouseExport.getId_wareHouse());
                var remaining = opt.get().getQuantity() - wareHouseExport.getQuantity();
                if (remaining == 0) {
                    wareHouseRepository.updateExportWareHouseifClean(remaining, wareHouseExport.getId_wareHouse());
                } else {
                    wareHouseRepository.updateExportWareHouse(remaining, wareHouseExport.getId_wareHouse());
                }
            }
            exportRepository.updateExportActive(isActive, id);
        }
        else if(isActive == 2) {
            exportRepository.updateExportActive(isActive, id);
        }


    }

    public Page<ExportDto> saleExportUser(SaleExportFilter saleExportFilter) throws ParseException {
        ModelMapper modelMapper = new ModelMapper();
        log.info("--------- search eNTRY  -----------");
        Pageable pageable = PageRequest.of(saleExportFilter.page(), saleExportFilter.size());
        Page<Export> entries;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(saleExportFilter.startDate());
        Date endDate = dateFormat.parse(saleExportFilter.endDate());
        entries = exportRepository.getAllSaleExportList(pageable, saleExportFilter.nameProduct(), saleExportFilter.idExport(), saleExportFilter.nameProducer(), saleExportFilter.creator(), startDate, endDate);
        return entries.map(export -> modelMapper.map(export, ExportDto.class));
    }

    @Override
    public SaleExport saleExport(SaleExportFilter saleExportFilter) throws ParseException {
        SaleExport saleExport = new SaleExport();
        saleExport.setExportDto(this.saleExportUser(saleExportFilter));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(saleExportFilter.startDate());
        Date endDate = dateFormat.parse(saleExportFilter.endDate());
        saleExport.setTotalAmount(exportRepository.calculateTotalMoney(saleExportFilter.nameProduct(), saleExportFilter.idExport(), saleExportFilter.nameProducer(), saleExportFilter.creator(), startDate, endDate));
        return saleExport;
    }

}
