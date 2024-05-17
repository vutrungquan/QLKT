package mng.qlkt.service.Impl;

import mng.qlkt.model.WareHouseExport;
import mng.qlkt.repository.WareHouseExportRepository;
import mng.qlkt.service.WareHouseExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WareHouseExportServiceImpl implements WareHouseExportService {
    @Autowired
    WareHouseExportRepository wareHouseExportRepository;

    @Override
    public WareHouseExport save(WareHouseExport wareHouseExport) throws Exception {

        wareHouseExport.setIs_active(1);
        return wareHouseExportRepository.save(wareHouseExport);
    }
}
