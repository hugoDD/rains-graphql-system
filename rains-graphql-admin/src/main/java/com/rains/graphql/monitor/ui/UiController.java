package com.rains.graphql.monitor.ui;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UiController {

	@GetMapping(path = "/monitor1", produces = MediaType.TEXT_HTML_VALUE)
	public String index() {
		return "redirect:monitor1/index.html";
	}

	@ResponseBody
	@GetMapping(path = "/monitor1/context")
	public String context(HttpServletRequest request) {
		String uri = request.getContextPath();

		if (!uri.endsWith("/")) {
			uri = uri + "/";
		}
		return uri;
	}
}
