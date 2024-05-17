package mng.qlkt.dto.dtos;

import mng.qlkt.ultis.NumberUtils;


public record UserFilter(
        String name,
        String idUser,
        Integer page,
        Integer size
) {
    @Override
    public Integer page() {
        return NumberUtils.isNull(page) ? 0 : (page - 1);
    }
}
