package mng.qlkt.service.Impl;

import mng.qlkt.dto.dtos.ProducerFilter;
import mng.qlkt.model.Producer;
import mng.qlkt.repository.ProducerRespository;
import mng.qlkt.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProducerServiceImpl implements ProducerService {
    @Autowired
    ProducerRespository producerRespository;
    @Override
    public Optional<Producer> findByName(String producerName) {
        return producerRespository.findByProducerName(producerName);
    }

    public Optional<Producer> findByAddress(String address) {
        return producerRespository.findByAddress(address);
    }

    public Producer save(Producer producer) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        int hour = now.getHour();
        producer.setIdProducer("NCC_" + formattedDate +hour+minute);
        producer.setIs_active(1);
        return  producerRespository.save(producer);
    }

    @Override
    public Page<?> searchProducer(ProducerFilter producerFilter) throws Exception {
        log.info("--------- search product  -----------");
        Pageable pageable = PageRequest.of(producerFilter.page(), producerFilter.size());
        var result = producerRespository.getAllProducer(pageable, producerFilter.producerName(), producerFilter.idProducer());
        return result;
    }

    public Producer[] getAllProducer() {
        List<Producer> producerList = producerRespository.findAll();
        Producer[] array = producerList.toArray(new Producer[0]);
        return array;
    }



}
