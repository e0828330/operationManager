package restService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.TestService;

@Controller
public class MainController {

	@Autowired
	private TestService service;

	@RequestMapping("/rest")
	public @ResponseBody
	String root() {
		return service.getString();
	}

	@RequestMapping("/rest/bla")
	public @ResponseBody
	String test2() {
		System.out.println("CALLED!!!!");
		return "Bla";
	}
}
