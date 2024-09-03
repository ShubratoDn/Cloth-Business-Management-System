package com.cloth.business.servicesImple;

import com.cloth.business.DTO.StakeHolderDTO;
import com.cloth.business.entities.StakeHolder;
import com.cloth.business.entities.Store;
import com.cloth.business.entities.enums.StakeHolderType;
import com.cloth.business.exceptions.ResourceAlreadyExistsException;
import com.cloth.business.repositories.StakeHolderRepository;
import com.cloth.business.services.FileServices;
import com.cloth.business.services.StakeHolderService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StakeHolderServiceImpl implements StakeHolderService {

    @Autowired
    private StakeHolderRepository stakeHolderRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private FileServices fileServices;
    
    
    @Override
    public StakeHolder createStakeHolder(StakeHolderDTO stakeHolderDTO) {
    	
    	StakeHolder byPhoneOrEmail = stakeHolderRepository.findByPhoneOrEmail(stakeHolderDTO.getPhone(), stakeHolderDTO.getEmail());
    	if(byPhoneOrEmail != null) {
    		throw new ResourceAlreadyExistsException("Phone or email already exist");
    	}
    	
    	StakeHolder stakeHolder = modelMapper.map(stakeHolderDTO, StakeHolder.class);
    	
    	stakeHolder.setEmail(!stakeHolderDTO.getEmail().isBlank() ? stakeHolderDTO.getEmail() : null);
    	stakeHolder.setCreatedAt(new Date());
    	if(stakeHolderDTO.getStakeHolderImage() != null) {    		
    		stakeHolder.setImage(fileServices.uploadStakeholderImage(stakeHolderDTO.getStakeHolderImage()));
    	}
    	
    	
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

    @Override
    public List<StakeHolder> getStakeHoldersByTypeAndStore(String type, Long storeId) {
        // Convert String to StakeHolderType enum
        StakeHolderType stakeHolderType;
        try {
            stakeHolderType = StakeHolderType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle invalid type string
            throw new IllegalArgumentException("Invalid StakeHolderType: " + type);
        }

        // Create Store object with the provided storeId
        Store store = new Store();
        store.setId(storeId);

        // Retrieve stakeholders from the repository
        return stakeHolderRepository.findByStakeHolderTypeAndStore(stakeHolderType, store);
    }
}
