package restService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	 @RequestMapping("/rest")
	 public @ResponseBody String root() {
		 System.out.println("CALLED!!!!");
		 return "Test";
	 }
	 
	 @RequestMapping("/rest/bla")
	 public @ResponseBody String test2() {
		 System.out.println("CALLED!!!!");
		 return "Bla";
	 }
}
