package com.cloth.business;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import com.cloth.business.configurations.constants.Constants;
import com.cloth.business.configurations.constants.UserRolesList;
import com.cloth.business.entities.UserRole;
import com.cloth.business.exceptions.ResourceAlreadyExistsException;
import com.cloth.business.repositories.UserRoleRepository;
import com.cloth.business.services.UserRoleServices;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import io.lettuce.core.models.role.RedisInstance.Role;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableCaching	
@Slf4j
public class ClothBusinessManagementSystemApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClothBusinessManagementSystemApplication.class, args);
    }

    @Bean
    ModelMapper mapper(){
        return  new ModelMapper();
    }

    
    @Autowired
    private UserRoleRepository userRoleRepository;
    

    @Override
    public void run(String... args) throws Exception {
//    	List<UserRole> userRoles = UserRolesList.userRoles;
//    	saveAllRoles(userRoles);


		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

			if (networkInterfaces == null) {
				System.out.println("No network interfaces found.");
				return;
			}

			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

				while (inetAddresses.hasMoreElements()) {
					InetAddress inetAddress = inetAddresses.nextElement();

					if (inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && !inetAddress.isMulticastAddress()) {
						if (inetAddress.getHostAddress().indexOf(':') == -1) { // Check if it's an IPv4 address
							System.out.println("Interface: " + networkInterface.getName() + ", IP Address: " + inetAddress.getHostAddress());
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
    }

//    @CacheEvict(value = "userRoles", key = "#userRoles")
    
	private List<UserRole> saveAllRoles(List<UserRole> userRoles) {
		List<UserRole> roleList = new ArrayList<>();
		
		for(UserRole role: userRoles) {
			UserRole dbRole = userRoleRepository.findByRole(role.getRole());
	        if(dbRole != null){
	        	log.warn("The role:{} is already exist",role.getRole());
	            continue;
	        }else {
	        	UserRole save = userRoleRepository.save(role);
	        	roleList.add(save);
	        }
		}
		
		return roleList;
		
	}
	
	public void generateQRcode() {
		String data ="https://scanqr.org/#scan";
		String filePath = Constants.RESOURCE_DIRECTORY + "qr.jpg";
		
		try {
			BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);
			MatrixToImageWriter.writeToPath(matrix, "jpg", Paths.get(filePath));
		} catch (WriterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
