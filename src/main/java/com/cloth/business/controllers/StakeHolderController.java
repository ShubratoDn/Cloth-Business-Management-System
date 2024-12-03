package com.cloth.business.controllers;

import com.cloth.business.DTO.StakeHolderDTO;
import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.StakeHolder;
import com.cloth.business.services.StakeHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/stakeholders")
public class StakeHolderController {

    @Autowired
    private StakeHolderService stakeHolderService;

    @PostMapping
    @CheckRoles({"ROLE_ADMIN", "ROLE_STAKEHOLDER_CREATE"})
    public ResponseEntity<?> createStakeHolder(@ModelAttribute StakeHolderDTO stakeHolderDto) {
        StakeHolder createdStakeHolder = stakeHolderService.createStakeHolder(stakeHolderDto);
        return new ResponseEntity<>(createdStakeHolder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StakeHolder> getStakeHolderById(@PathVariable Long id) {
        Optional<StakeHolder> stakeHolder = stakeHolderService.getStakeHolderById(id);
        return stakeHolder.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StakeHolder> updateStakeHolder(@PathVariable Long id, @RequestBody StakeHolder stakeHolder) {
        StakeHolder updatedStakeHolder = stakeHolderService.updateStakeHolder(id, stakeHolder);
        return ResponseEntity.ok(updatedStakeHolder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStakeHolder(@PathVariable Long id) {
        stakeHolderService.deleteStakeHolder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<StakeHolder>> getAllStakeHolders(Pageable pageable) {
        Page<StakeHolder> stakeHolders = stakeHolderService.getAllStakeHolders(pageable);
        return ResponseEntity.ok(stakeHolders);
    }


    @GetMapping("/type/{type}/store/{storeId}")
    public ResponseEntity<?> getStakeHolderByTypeAndStore(@PathVariable String type, @PathVariable Long storeId) {
        List<StakeHolder> stakeHolders = stakeHolderService.getStakeHoldersByTypeAndStore(type, storeId);
        return ResponseEntity.ok(stakeHolders);
    }
    
    
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> getStakeHolderByStore(@PathVariable Long storeId) {
        List<StakeHolder> stakeHolders = stakeHolderService.getStakeHoldersByStore(storeId);
        return ResponseEntity.ok(stakeHolders);
    }
}
