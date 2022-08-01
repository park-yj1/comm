package edu.kh.comm.main.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(Exception.class)
	public String ExceptionHandler(Exception e, Model model) {
		
		e.printStackTrace();
		model.addAttribute("errorMessage","서비스 이용중 문제가 발생했습니다. <br>에러 내용을 확인해주세요.");
		model.addAttribute("e",e);
		
		return "common/error";
	}
}
