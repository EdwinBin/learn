package com.org.edwin.annotation.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.edwin.annotation.customAnnotation.AppSignValid;

@Controller
@RequestMapping("/")
public class AnnotationController {

	@RequestMapping("testAppSignValid")
	@ResponseBody
	@AppSignValid
	public String testAppSignValid() {
		
		System.out.println("test:AppSignValid");
		return "AnnotationController";
	}
}
