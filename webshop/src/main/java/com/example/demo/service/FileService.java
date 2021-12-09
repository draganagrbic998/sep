package com.example.demo.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FileService {

	public String store(MultipartFile file) throws IOException {
		log.info("FileService - store: file=" + file.getOriginalFilename());
		String fileName = "image_" + new Date().getTime() + "_" + file.getOriginalFilename();
		FileOutputStream fout = new FileOutputStream(ResourceUtils.getFile("classpath:images/" + fileName));
		fout.write(file.getBytes());
		fout.close();
		return fileName;
	}

}
