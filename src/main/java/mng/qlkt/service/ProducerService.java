package mng.qlkt.service;

import mng.qlkt.dto.dtos.ProducerFilter;
import mng.qlkt.model.Producer;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProducerService {
    Optional<Producer> findByName(String name);

    public Producer save(Producer producer);

    Page<?> searchProducer(ProducerFilter producerFilter) throws Exception;

    Producer[] getAllProducer();
}
