package mng.qlkt.entities;

import mng.qlkt.dto.dto.EntryDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class SaleEntry {

    private Page<EntryDto> entryDto;
    private Long totalAmount;
}
