package ru.firston.ws.sts.model;

import java.io.FileNotFoundException;

import org.springframework.web.multipart.MultipartFile;

import ru.firston.ws.sts.utils.Enclosure;

/**
 * 
 * @author Anton Arefyev
 * @version 16.07.07 pre 16.06.25
 *
 */
public interface ServiceEnclosure {

	public Enclosure save(MultipartFile file);
	
	public Enclosure delete();
	
	public String getFullPath();
	
	public String getError();
	
	public void exists(String fileName) throws FileNotFoundException;
}
