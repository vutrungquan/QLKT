package mng.qlkt.dto.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
@Getter
@Setter
public class WareHouseExportDto {

    private Long id;
    private Set<ProductDto> product;
    private Date expiry;
    private Date manufactureDate;
    private Date intime;
    private Integer isActive;
    private Integer quantity;
    private Long id_wareHouse;

    public WareHouseExportDto() {
    }

    public WareHouseExportDto(Long id, Set<ProductDto> product, Date expiry, Date manufactureDate, Date intime, Integer isActive, Integer quantity) {
        this.id = id;
        this.product = product;
        this.expiry = expiry;
        this.manufactureDate = manufactureDate;
        this.intime = intime;
        this.isActive = isActive;
        this.quantity = quantity;
    }
}
