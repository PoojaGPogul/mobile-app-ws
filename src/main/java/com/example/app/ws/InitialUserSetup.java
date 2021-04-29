package com.example.app.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.app.ws.io.entity.AuthorityEntity;
import com.example.app.ws.io.repositories.AuthorityRepository;

@Component
public class InitialUserSetup {
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	@EventListener
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("From Application ready event...");
		
		AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
		AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
		AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");
		
	}
	
	private AuthorityEntity createAuthority(String name) {
		
		AuthorityEntity authorityEntity = authorityRepository.findByName(name);
		
		if(authorityEntity == null) {
			authorityEntity = new AuthorityEntity(name);
			authorityRepository.save(authorityEntity);
		}
		return authorityEntity;
		
	}
	
}
