package com.cloth.business.services;

import com.cloth.business.entities.StakeHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StakeHolderService {
    StakeHolder createStakeHolder(StakeHolder stakeHolder);
    Optional<StakeHolder> getStakeHolderById(Long id);
    StakeHolder updateStakeHolder(Long id, StakeHolder stakeHolder);
    void deleteStakeHolder(Long id);
    Page<StakeHolder> getAllStakeHolders(Pageable pageable);
}
