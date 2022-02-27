package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Delete this later
 */
@Controller
@RequestMapping("/")
public class SomeController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello world!";
    }
}