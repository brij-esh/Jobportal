package com.jobportal.system.jwtconfig;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.system.exceptionhandler.UserBadCredentialsException;
import com.jobportal.system.exceptionhandler.UserDisabledException;

import lombok.extern.log4j.Log4j2;





@RestController
@CrossOrigin

@Log4j2
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;



	@PostMapping("/auth/usertoken")
	public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws UserDisabledException, UserBadCredentialsException {
                
                
        
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());
		//util is giving token
		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	



	
	
				
	
	private void authenticate(String username, String password) throws UserDisabledException,UserBadCredentialsException {
        try {
           
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			//for disabled user
			throw new UserDisabledException("USER_DISABLED");
		} catch (BadCredentialsException e) {
			// for INVALID_CREDENTIALS
			throw new UserBadCredentialsException("INVALID_CREDENTIALS");
		}
	}


	}

