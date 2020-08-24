package com.trecapps.false_hood.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/Blank")
public class BlankController {

	@GetMapping
	public String blankMapMethod()
	{
		return "Hello World!";
	}
}
