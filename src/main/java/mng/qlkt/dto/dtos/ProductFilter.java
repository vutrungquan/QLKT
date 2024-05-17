package mng.qlkt.dto.dtos;

import mng.qlkt.ultis.NumberUtils;

public record ProductFilter(
        String productName,
        String productId,
        String producers,
        Integer page,
        Integer size) {
    @Override
    public Integer page() {
        return NumberUtils.isNull(page) ? 0 : (page - 1);
    }
}
