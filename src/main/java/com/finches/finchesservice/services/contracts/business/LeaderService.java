package com.finches.finchesservice.services.contracts.business;

import com.finches.finchesservice.models.entitymodels.LeaderModel;
import com.finches.finchesservice.services.contracts.common.MustHaveCurdServiceOperations;

public interface LeaderService extends MustHaveCurdServiceOperations<LeaderModel> {
    Integer IncrementAssignedFinchesCountByOne(LeaderModel leaderModel);
}
