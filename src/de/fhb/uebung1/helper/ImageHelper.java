/**
 * 
 */
package de.fhb.uebung1.helper;

import java.io.File;

/**
 * @author Max Gregor
 *
 */
public class ImageHelper {

    public static String getImagePath(long playerID, String stdPath){
	String path = stdPath+"/"+playerID+".png";
	File actProfilImage = new File(path);
	if(!actProfilImage.exists()){
	    path = "profilbilder/standard.png";
	}else{
	    path = "profilbilder/"+playerID+".png";
	}
	return path;
    }
}
