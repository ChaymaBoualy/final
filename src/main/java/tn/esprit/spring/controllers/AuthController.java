package tn.esprit.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.Dto.MessageResponse;
import tn.esprit.spring.Dto.JwtResponse;
import tn.esprit.spring.entities.ERole;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.Dto.SignupRequest;
import tn.esprit.spring.Dto.JwtResponse;
import tn.esprit.spring.Dto.LoginRequest;
import tn.esprit.spring.Dto.MessageResponse;
import tn.esprit.spring.repositories.RoleRepository;
import tn.esprit.spring.repositories.UserRepository;
import tn.esprit.spring.security.JwtUtils;
import tn.esprit.spring.services.UserDetailsImpl;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RestController
	@RequestMapping("/api/auth")
	public class AuthController {
		@Autowired
		AuthenticationManager authenticationManager;
		@Autowired
		UserRepository userRepository;
		@Autowired
		RoleRepository roleRepository;
		@Autowired
		 PasswordEncoder encoder;

	@PostMapping("/signin")
		public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = JwtUtils.generateJwtToken(authentication);
			
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return ResponseEntity.ok(new JwtResponse(jwt,
													 userDetails.getId(), 
													 userDetails.getUsername(), 
													 userDetails.getEmail(), 
													 roles));

		}
		@PostMapping("/signup")
		public ResponseEntity<?> registerCOMPANY(@Valid @RequestBody SignupRequest signUpRequest) {
			if (userRepository.existsByUsername(signUpRequest.getUsername())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Username is already taken!"));
			}
			if (userRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Email is already in use!"));
			}

			// Create new user's account
			User user = new User(signUpRequest.getUsername(), 
								 signUpRequest.getEmail(),
								 encoder.encode  (signUpRequest.getPassword()));
			Set<Role> roles = new HashSet<>();
		
				Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			user.setRoles(roles);
			
			userRepository.save(user);
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		}
		@PostMapping("/signupe")
		public ResponseEntity<?> registerEMPOLYEE(@Valid @RequestBody SignupRequest signUpRequest) {
			if (userRepository.existsByUsername(signUpRequest.getUsername())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Username is already taken!"));
			}
			if (userRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Email is already in use!"));
			}
			// Create new user's account
			User user = new User(signUpRequest.getUsername(),
								 signUpRequest.getEmail(),
								 encoder.encode(signUpRequest.getPassword()));
			Set<Role> roles = new HashSet<>();
		
				Role userRole = roleRepository.findByName(ERole.ROLE_PARTNER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			user.setRoles(roles);
			userRepository.save(user);
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		}
		@PostMapping("/signupa")
		public ResponseEntity<?> registerADMINSTRATOR(@Valid @RequestBody SignupRequest signUpRequest) {
			if (userRepository.existsByUsername(signUpRequest.getUsername())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Username is already taken!"));
			}
			if (userRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Email is already in use!"));
			}
			// Create new user's account
			User user = new User(signUpRequest.getUsername(), 
								 signUpRequest.getEmail(),
								 encoder.encode(signUpRequest.getPassword()));
			Set<Role> roles = new HashSet<>();
		
				Role userRole = roleRepository.findByName(ERole.ROLE_ENTERPERISE)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			user.setRoles(roles);
			userRepository.save(user);
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		}
		
	}