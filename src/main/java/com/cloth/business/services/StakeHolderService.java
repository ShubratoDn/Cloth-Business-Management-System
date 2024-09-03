package com.cloth.business.services;

import com.cloth.business.DTO.StakeHolderDTO;
import com.cloth.business.entities.StakeHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StakeHolderService {
    StakeHolder createStakeHolder(StakeHolderDTO stakeHolder);
    Optional<StakeHolder> getStakeHolderById(Long id);
    StakeHolder updateStakeHolder(Long id, StakeHolder stakeHolder);
    void deleteStakeHolder(Long id);
    Page<StakeHolder> getAllStakeHolders(Pageable pageable);

    List<StakeHolder> getStakeHoldersByTypeAndStore(String type, Long storeId);
}
