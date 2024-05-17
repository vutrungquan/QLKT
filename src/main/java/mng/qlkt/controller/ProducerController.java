package mng.qlkt.controller;

import mng.qlkt.dto.dtos.ProducerFilter;
import mng.qlkt.entities.ErrorCode;
import mng.qlkt.entities.MyResponse;
import mng.qlkt.model.Producer;
import mng.qlkt.repository.ProducerRespository;
import mng.qlkt.service.Impl.ProducerServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Autowired
    ProducerServiceImpl producerService;

    @Autowired
    ProducerRespository producerRespository;

    @GetMapping("/search")
    public MyResponse<?> searchProducer(ProducerFilter producerFilter) throws Exception {
        var page = producerService.searchProducer(producerFilter);
        return MyResponse.response(page);
    }


    @GetMapping("/list-producer")
    public MyResponse<?> listProducer() throws Exception{
       var list = producerService.getAllProducer();
        return MyResponse.response(list);
    }

    @PostMapping("/created")
    public MyResponse<?> createdProducer(@RequestBody Producer producer) throws Exception {
        try {

            producerService.save(producer);
            return MyResponse.response(ErrorCode.CREATED_OK.getCode(), ErrorCode.CREATED_OK.getMsgError());
        }
        catch (Exception ex) {
            log.info(ex);
            return MyResponse.response(ErrorCode.CREATED_FAIL.getCode(), ErrorCode.CREATED_FAIL.getMsgError());
        }
    }

    @DeleteMapping("/delete/{id}")
    public MyResponse<?> deleteProducer(@PathVariable Long id) throws Exception{
        try {
            producerRespository.deleteById(id);
            return MyResponse.response(ErrorCode.DELETED_OK.getCode(), ErrorCode.DELETED_OK.getMsgError());
        }
        catch (Exception ex) {
            log.info(ex);
            return MyResponse.response(ErrorCode.DELETED_FAIL.getCode(), ErrorCode.DELETED_FAIL.getMsgError());
        }
    }
}
