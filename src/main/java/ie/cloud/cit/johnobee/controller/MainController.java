package ie.cloud.cit.johnobee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
	
	@RequestMapping
	public String getMainPage() {
		return "redirect:/monitor/event";
	}
}
