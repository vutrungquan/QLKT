package mng.qlkt.controller;

import mng.qlkt.dto.dtos.ExportFilter;
import mng.qlkt.dto.dtos.SaleExportFilter;
import mng.qlkt.dto.request.EntryForm;
import mng.qlkt.entities.ErrorCode;
import mng.qlkt.entities.MyResponse;
import mng.qlkt.service.Impl.ExportServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@Log4j2
@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    ExportServiceImpl exportService;


    @PostMapping("/created")
    public MyResponse<?> save(@Valid @RequestBody EntryForm entryForm) throws Exception {
        try {
            exportService.save(entryForm);
            return MyResponse.response(ErrorCode.CREATED_OK.getCode(), ErrorCode.CREATED_OK.getMsgError());
        }
        catch (Exception ex) {
            log.info(ex);
            return MyResponse.response(ErrorCode.CREATED_FAIL.getCode(), ErrorCode.CREATED_FAIL.getMsgError());
        }
    }

    @GetMapping("/search")
    public MyResponse<?> search(ExportFilter exportFilter) throws Exception {
        var list = exportService.searchExport(exportFilter);
        return MyResponse.response(list);

    }

    @GetMapping("/find/{id}")
    public MyResponse<?> findbyid(@PathVariable Long id) throws Exception {
        var warehouser = exportService.findById(id);
        return MyResponse.response(warehouser);
    }

    @PutMapping("/approve/{id}")
    public MyResponse<?> approveExport(@PathVariable Long id ,@RequestParam Integer isActive) throws Exception {
        try {
            exportService.approveExport(id, isActive);
            return MyResponse.response(ErrorCode.APPROVE_OK.getCode(), ErrorCode.APPROVE_OK.getMsgError());
        }
        catch (Exception ex) {
            log.info(ex);
            return MyResponse.response(ErrorCode.APPROVE_FAIL.getCode(), ErrorCode.APPROVE_FAIL.getMsgError());
        }
    }

    @GetMapping("/sale")
    public MyResponse<?> saleExportUser(SaleExportFilter saleExportFilter) throws ParseException {
        return MyResponse.response(exportService.saleExport(saleExportFilter));
    }
}
