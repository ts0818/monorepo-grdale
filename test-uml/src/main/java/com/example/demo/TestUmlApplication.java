package com.example.demo;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestUmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestUmlApplication.class, args);
		prosess();
	}

	private static void prosess() {
		
        try {
            ProcessBuilder builder = new ProcessBuilder("ps");
            Process process = builder.start();
            InputStream is = process.getInputStream();
            while (true) {
                int c = is.read();
                if (c == -1) {
                    is.close();
                    break;
                }
                System.out.print((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
