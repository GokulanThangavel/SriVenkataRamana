
package com.example.googlesheets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.googlesheets.model.User;
import com.example.googlesheets.model.getUsersByPhone;
import com.example.googlesheets.service.AddUserService;
import com.example.googlesheets.service.GetUsersService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

	@Autowired
	GetUsersService getUsersService;

	@Autowired
	AddUserService addUserService;

	@PostMapping("/get")
	public String getUser(@RequestBody getUsersByPhone request) {

		String jsonRespone = getUsersService.getUsersData(request);

		return jsonRespone;
	}

	@PostMapping("/add")
	public String addUser(@RequestBody User userData) {

		String jsonReponse = addUserService.addUsers(userData);

		return jsonReponse;
	}
	
	@GetMapping("/functionTypes")
	public String getFunctionTypes() {

		String jsonRespone = getUsersService.getFunctionTypes();

		return jsonRespone;
	}
}
