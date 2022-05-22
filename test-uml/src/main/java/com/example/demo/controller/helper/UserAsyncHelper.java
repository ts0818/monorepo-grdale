package com.example.demo.controller.helper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAsyncHelper implements Runnable {
	
	private SseEmitter sseEmitter;
	
	private List<User> userList;

	@Override
	public synchronized void run() {
		// 
		try {
		  BigInteger num = new BigInteger("100000000");
		  BigInteger result = BigInteger.valueOf(0);
		  log.info("■run() for文開始");
		  for (BigInteger index = BigInteger.ONE; index.compareTo(num) <= 0;   index = index.add(BigInteger.ONE)) {
			  //sseEmitter.send("success:" + i);
			  result = result.add(index);
		  }
		    log.info("■run() for文終了");
			sseEmitter.send("complete:" + result);
		} catch (IOException e) {
			// 
		}
		sseEmitter.complete();
	}
}
