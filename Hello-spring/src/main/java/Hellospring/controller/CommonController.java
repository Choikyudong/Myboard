package Hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

	@GetMapping("/")
	public String welcomeMyboard(Model model) {
		
		model.addAttribute("template", "common/index");
		
		return "index";
	}
	
}
