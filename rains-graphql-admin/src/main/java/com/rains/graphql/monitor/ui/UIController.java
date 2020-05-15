package com.rains.graphql.monitor.ui;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UiController {

	@GetMapping(path = "/monitor", produces = MediaType.TEXT_HTML_VALUE)
	public String index() {
		return "redirect:index.html";
	}

	@GetMapping(path = "/altairui", produces = MediaType.TEXT_HTML_VALUE)
	public String altair() {
		return "redirect:/altair";
	}
}
