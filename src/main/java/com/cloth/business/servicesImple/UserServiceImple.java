package com.cloth.business.servicesImple;

import com.cloth.business.DTO.UserDTO;
import com.cloth.business.entities.Store;
import com.cloth.business.entities.User;
import com.cloth.business.entities.UserRole;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.repositories.StoreRepository;
import com.cloth.business.repositories.UserRepository;
import com.cloth.business.services.FileServices;
import com.cloth.business.services.StoreServices;
import com.cloth.business.services.UserRoleServices;
import com.cloth.business.services.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImple implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleServices userRoleService;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private StoreServices storeService;
    
    @Autowired
    private FileServices fileServices;
    
    @Autowired
    private StoreRepository storeRepository;

    @Override
    public UserDTO findByPhoneOrEmail(String phone, String email) {    	
        User user = userRepository.findByPhoneOrEmail(phone, email);
        if(user != null){
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    @Override
    public UserDTO findByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        if(user != null){
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user != null){
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    @Override
    public UserDTO findById(Long id) {  
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User", id+""));
        if(user != null){
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        List<UserRole> roles = new ArrayList<>();
        for(UserRole userRole : userDTO.getRoles()) {
            roles.add(userRoleService.getRoleById(userRole.getId()));
        }

        userDTO.setRoles(roles);
        userDTO.setImage(fileServices.uploadUserImage(userDTO.getUserImage()));
        
        User user = modelMapper.map(userDTO, User.class);
        User save = userRepository.save(user);
        if(save != null) {
            return modelMapper.map(save, UserDTO.class);
        }
        return null;
    }
    
    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        List<UserRole> roles = new ArrayList<>();
        for(UserRole userRole : userDTO.getRoles()) {
            roles.add(userRoleService.getRoleById(userRole.getId()));
        }

        
        List<Store> stores = new ArrayList<>();
        for(Store store : userDTO.getAssignedStore()) {
            stores.add(storeService.getStoreById(store.getId()));
        }

        
        
        userDTO.setRoles(roles);
        userDTO.setAssignedStore(stores);
        
        User user = modelMapper.map(userDTO, User.class);
        User save = userRepository.save(user);
        if(save != null) {
            return modelMapper.map(save, UserDTO.class);
        }

        return null;
    }
    
    
    @Override
    public UserDTO updateUserRoles(UserDTO userDTO) {
    	
    	User dbUser = userRepository.findById(userDTO.getId()).get();
    	
    	List<UserRole> roles = new ArrayList<>();
        for(UserRole userRole : userDTO.getRoles()) {
            roles.add(userRoleService.getRoleById(userRole.getId()));
        }
        dbUser.setRoles(roles);
        dbUser.setLogoutRequired(true);
        
        User save = userRepository.save(dbUser);
        if(save != null) {
            return modelMapper.map(save, UserDTO.class);
        }
        return null;
    }
    
    
    @Override
    public List<UserDTO> getAllUsers() {
        List<User> all = userRepository.findAll();
        List<UserDTO> users = new ArrayList<>();
        for (User u : all) {
            users.add(modelMapper.map(u, UserDTO.class));
        }
        return users;
    }


    
    @Override
    public PageResponse searchUser(String query, int page, int size, String sortBy, String sortDirection) {
    	Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		
		Page<User> pageInfo;
		
		try {
			Pageable pageable = PageRequest.of(page, size, sort);
			pageInfo = userRepository.searchByFields(query, query, query, query, query, pageable);
		} catch (Exception e) {
			Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
			pageInfo = userRepository.searchByFields(query, query, query,query, query, pageable);
		}

		
		PageResponse pageToPageResponse = HelperUtils.pageToPageResponse(pageInfo);
		
    	return pageToPageResponse;
    }

    
    
    
    
    
    
    
    
    
    @Transactional
    public void updateUserAssignedStore(Long userId, Long storeId) {
        
        UserDTO user = this.findById(userId);
        
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId));

        if (user.getAssignedStore().contains(store)) {
            // Store is already assigned to the user, remove it
            user.getAssignedStore().remove(store);
        } else {
            // Store is not assigned to the user, add it
            user.getAssignedStore().add(store);
        }
        user.setLogoutRequired(true);
        userRepository.save(modelMapper.map(user, User.class));
    }
}
