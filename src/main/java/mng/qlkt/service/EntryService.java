package mng.qlkt.service;

import mng.qlkt.dto.dto.EntryDto;
import mng.qlkt.dto.dtos.EntryFilter;
import mng.qlkt.dto.dtos.SaleEntryFilter;
import mng.qlkt.dto.request.EntryForm;
import mng.qlkt.entities.SaleEntry;
import mng.qlkt.model.Entry;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.Optional;

public interface EntryService {

    Entry save(EntryForm entryForm) throws Exception;

    Optional<EntryDto> findById(Long id) ;

    Page<EntryDto> searchEntry(EntryFilter entryFilter) throws Exception;

    void approveEntry(Integer isActive, Long id) throws Exception;

    SaleEntry saleEntry(SaleEntryFilter saleEntryFilter) throws ParseException;
}
