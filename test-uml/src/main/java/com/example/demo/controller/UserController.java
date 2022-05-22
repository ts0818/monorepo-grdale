package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.dto.user.UserFormDto;
import com.example.demo.service.azure.media.EncodingWithMESPredefinedPreset;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private EncodingWithMESPredefinedPreset encodingWithMESPredefinedPreset;
	
	@PostMapping("/save")
	public SseEmitter save(@ModelAttribute  @Validated UserFormDto userFormDto) throws IOException {
		log.info("■■■INPUT チェック");
		log.info(userFormDto);
		log.info(userFormDto.getId());
		log.info(userFormDto.getFile());
		
		long timeOutForSseEmitter = 2 * 60 * 60 * 1000L;
		SseEmitter sseEmitter = new SseEmitter(timeOutForSseEmitter);
//		UserAsyncHelper userAsyncHelper = UserAsyncHelper.builder()
//		.sseEmitter(sseEmitter)
//		.userList(null)
//		.build();
//		Thread th = new Thread(userAsyncHelper);
		log.info("■■■処理開始前");
		sseEmitter.send("処理開始");
//		th.start();
		//userAsyncHelpe.run();
		log.info("■■■処理開始後");
		String streamingUrl = encodingWithMESPredefinedPreset.runEncodingWithMESPredefinedPreset(userFormDto.getFile());
		sseEmitter.send(streamingUrl);
		sseEmitter.complete();
		return sseEmitter;
	}
}
