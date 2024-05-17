package mng.qlkt.service;

import mng.qlkt.dto.dto.ExportDto;
import mng.qlkt.dto.dtos.ExportFilter;
import mng.qlkt.dto.dtos.SaleExportFilter;
import mng.qlkt.dto.request.EntryForm;
import mng.qlkt.entities.SaleExport;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.Optional;

public interface ExportService {
    void save(EntryForm entryForm) throws Exception;

    Page<ExportDto> searchExport(ExportFilter exportFilter) throws Exception;

    Optional<ExportDto> findById(Long id);

    void approveExport(Long id,Integer isActive) throws Exception;

    SaleExport saleExport(SaleExportFilter saleExportFilter) throws ParseException;
}
