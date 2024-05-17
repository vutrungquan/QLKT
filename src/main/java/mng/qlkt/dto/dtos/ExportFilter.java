package mng.qlkt.dto.dtos;

import mng.qlkt.ultis.NumberUtils;

public record ExportFilter(
        String idExport,
        String nameProduct,
        String nameProducer,
        String startDate,
        String endDate,
        Integer page,
        Integer size
) {
    @Override
    public Integer page() {
        return NumberUtils.isNull(page) ? 0 : (page - 1);
    }
}
