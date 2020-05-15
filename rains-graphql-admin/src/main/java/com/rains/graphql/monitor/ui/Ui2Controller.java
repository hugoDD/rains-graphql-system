package com.rains.graphql.monitor.ui;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class Ui2Controller {

	@GetMapping(path = "/monitor", produces = MediaType.TEXT_HTML_VALUE)
	public String index() {
		return "redirect:monitor/index.html";
	}

	@ResponseBody
	@GetMapping(path = "/monitor/context")
	public String context(HttpServletRequest request) {
		String uri = request.getContextPath();

		if (!uri.endsWith("/")) {
			uri = uri + "/";
		}
		return uri;
	}
}
