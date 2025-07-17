package com.shmashine.api.service.elevatorcollect;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.elevatorCollect.input.OperationElevatorCollectModule;
import com.shmashine.api.module.elevatorCollect.input.SearchElevatorCollectModule;

public interface BizElevatorCollectService {


    PageListResultEntity searchElevatorList(SearchElevatorCollectModule searchElevatorCollectModule);

    boolean searchUserCollectElevatorInfo(OperationElevatorCollectModule operationElevatorCollectModule);


}
