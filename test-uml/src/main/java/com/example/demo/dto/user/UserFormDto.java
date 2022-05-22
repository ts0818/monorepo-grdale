package com.example.demo.dto.user;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserFormDto implements Serializable {
	//@JsonProperty("id")
	private String id;
	@JsonIgnore
	//@JsonProperty("file")
	private MultipartFile file;
}
