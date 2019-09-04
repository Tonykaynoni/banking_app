package com.banking_app.controller;

import java.io.IOException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	 @PostMapping("/myApiText")
	    public String listUploadedFiles(Model model) throws IOException {

	       

	        return "success";
	    }

}
