package mng.qlkt.dto.dtos;

import mng.qlkt.ultis.NumberUtils;

public record ProducerFilter(String producerName,
                             String idProducer,
                             Integer page,
                             Integer size) {
    @Override
    public Integer page() {
        return NumberUtils.isNull(page) ? 0 : (page - 1);
    }
}