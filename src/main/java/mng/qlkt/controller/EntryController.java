package mng.qlkt.controller;

import mng.qlkt.dto.dtos.EntryFilter;
import mng.qlkt.dto.dtos.SaleEntryFilter;
import mng.qlkt.dto.request.EntryForm;
import mng.qlkt.entities.ErrorCode;
import mng.qlkt.entities.MyResponse;
import mng.qlkt.service.Impl.EntryServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.text.ParseException;

@Log4j2
@RestController
@RequestMapping("/entry")
public class EntryController {
    @Autowired
    EntryServiceImpl entryService;

    @PostMapping("/created")
    public MyResponse<?> save(@Valid @RequestBody EntryForm entryForm) throws Exception {
        try {
            entryService.save(entryForm);
            return MyResponse.response(ErrorCode.CREATED_OK.getCode(), ErrorCode.CREATED_OK.getMsgError());
        }
        catch (Exception ex) {
            log.info(ex);
            return MyResponse.response(ErrorCode.CREATED_FAIL.getCode(), ErrorCode.CREATED_FAIL.getMsgError());
        }
    }

    @GetMapping("/search")
    public MyResponse<?> search(EntryFilter entryFilter) throws Exception {
            var list = entryService.searchEntry(entryFilter);
            return MyResponse.response(list);

    }

    @GetMapping("/find/{id}")
    public MyResponse<?> findbyid(@PathVariable Long id) throws Exception {
        var warehouser = entryService.findById(id);
        return MyResponse.response(warehouser);
    }

    @PutMapping("/approve/{id}")
    public MyResponse<?> approveExport(@PathVariable Long id ,@RequestParam Integer isActive) throws Exception {
        try {
            entryService.approveEntry(isActive, id);
            return MyResponse.response(ErrorCode.APPROVE_OK.getCode(), ErrorCode.APPROVE_OK.getMsgError());
        }
        catch (Exception ex) {
            log.info(ex);
            return MyResponse.response(ErrorCode.APPROVE_FAIL.getCode(), ErrorCode.APPROVE_FAIL.getMsgError());
        }
    }

    @GetMapping("/sale")
    public MyResponse<?> saleEntryUser(SaleEntryFilter saleEntryFilter) throws ParseException {
        return MyResponse.response(entryService.saleEntry(saleEntryFilter));
    }
}
