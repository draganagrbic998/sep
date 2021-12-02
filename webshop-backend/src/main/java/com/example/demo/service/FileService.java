package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class FileService {

	public static final String RESOURCES_PATH = "src/main/resources/images/";

	public String store(MultipartFile file) throws IOException {
		log.info("FileService - store: name=" + file.getOriginalFilename());
		String fileName = "image_" + new Date().getTime() + "_" + file.getOriginalFilename();
		FileOutputStream fout = new FileOutputStream(new File(RESOURCES_PATH + fileName));
		fout.write(file.getBytes());
		fout.close();
		return fileName;
	}

}
