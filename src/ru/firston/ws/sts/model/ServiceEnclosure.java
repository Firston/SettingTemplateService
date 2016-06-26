package ru.firston.ws.sts.model;

import org.springframework.web.multipart.MultipartFile;

import ru.firston.ws.sts.utils.Enclosure;

public interface ServiceEnclosure {

	public Enclosure save(MultipartFile file);
	
	public String getFullPath();
	
	public String getError();
}
