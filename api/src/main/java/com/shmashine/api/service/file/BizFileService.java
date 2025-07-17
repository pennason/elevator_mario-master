package com.shmashine.api.service.file;

import java.util.List;
import java.util.Map;

public interface BizFileService {

    void insertWorkOrderBatch(List<String> fileNameList, String workOrderDetailId);

    void deleteElevatorDetailImages(String fileId, String elevatorId);

    void deleteSystemLogImages(String businessId);

    List<Map> getFileElevatorImg(String elevatorId);

    List<Map> getFileElevatorQRCodeImg(String elevatorId);
}
