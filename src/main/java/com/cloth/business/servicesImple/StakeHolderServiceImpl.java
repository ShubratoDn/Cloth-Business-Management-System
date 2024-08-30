package com.cloth.business.servicesImple;

import com.cloth.business.entities.StakeHolder;
import com.cloth.business.repositories.StakeHolderRepository;
import com.cloth.business.services.StakeHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class StakeHolderServiceImpl implements StakeHolderService {

    @Autowired
    private StakeHolderRepository stakeHolderRepository;

    @Override
    public StakeHolder createStakeHolder(StakeHolder stakeHolder) {
        return stakeHolderRepository.save(stakeHolder);
    }

    @Override
    public Optional<StakeHolder> getStakeHolderById(Long id) {
        return stakeHolderRepository.findById(id);
    }

    @Override
    public StakeHolder updateStakeHolder(Long id, StakeHolder stakeHolder) {
        Optional<StakeHolder> existingStakeHolder = stakeHolderRepository.findById(id);
        if (existingStakeHolder.isPresent()) {
            StakeHolder updatedStakeHolder = existingStakeHolder.get();
            updatedStakeHolder.setStakeHolderType(stakeHolder.getStakeHolderType());
            updatedStakeHolder.setName(stakeHolder.getName());
            updatedStakeHolder.setPhone(stakeHolder.getPhone());
            updatedStakeHolder.setEmail(stakeHolder.getEmail());
            updatedStakeHolder.setAddress(stakeHolder.getAddress());
            updatedStakeHolder.setImage(stakeHolder.getImage());
            updatedStakeHolder.setIsActive(stakeHolder.getIsActive());
            updatedStakeHolder.setUpdatedAt(new Date());
            return stakeHolderRepository.save(updatedStakeHolder);
        } else {
            throw new RuntimeException("StakeHolder not found with id " + id);
        }
    }

    @Override
    public void deleteStakeHolder(Long id) {
        stakeHolderRepository.deleteById(id);
    }

    @Override
    public Page<StakeHolder> getAllStakeHolders(Pageable pageable) {
        return stakeHolderRepository.findAll(pageable);
    }
}
