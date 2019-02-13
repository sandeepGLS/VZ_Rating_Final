package com.vz.rating.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LoadProperty {
	
/*-------- Method loadProperty() --------------*/
	
	public Properties loadPropertyContact()
	{
		Properties fileprop=null;
		try 
			{
				File f = new File("property.properties");
				System.out.println(f);
				FileInputStream fis = new FileInputStream(f);
				fileprop = new Properties();
				fileprop.load(fis);
				fis.close();
			}
		catch (FileNotFoundException e) 
			{			
				e.printStackTrace();
			} 
		catch (IOException e) 
			{			
				e.printStackTrace();
			}
		return fileprop;
	}	// end of method loadProperty()

}
