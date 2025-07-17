package com.shmashine.api.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson2.JSON;
import com.shmashine.api.entity.IotCard;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.common.utils.SnowFlakeUtils;

/**
 * @AUTHOR jiangheng
 * @DATA 2021/3/29 - 14:25
 */
@Component
public class IotCardInfoDataListener extends AnalysisEventListener<IotCard> {

    @Autowired
    private BizElevatorService elevatorService;

    private static final Logger LOGGER = LoggerFactory.getLogger("IotCardExcelResolve");

    private static final int BATCH_COUNT = 100;
    List<IotCard> iotCardList = new ArrayList<>();

    @Override
    public void invoke(IotCard iotCard, AnalysisContext analysisContext) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(iotCard));
        //雪花算法生成主键id
        iotCard.setId(SnowFlakeUtils.nextStrId());
        iotCardList.add(iotCard);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (iotCardList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            iotCardList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        iotCardList.clear();
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", iotCardList.size());
        try {
            elevatorService.save(iotCardList);
        } finally {
            iotCardList.clear();
        }
        LOGGER.info("存储数据库成功！");
    }
}
