package mng.qlkt.entities;

import mng.qlkt.dto.dto.ExportDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class SaleExport {
    private Page<ExportDto> exportDto;
    private Long totalAmount;
}
