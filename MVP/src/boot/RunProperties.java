package boot;

import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import presenter.Properties;

public class RunProperties {

	public static void main(String[] args) 
	{
		
		try 
		{
			XMLEncoder xmlE = new XMLEncoder(new FileOutputStream("./resources/properties.xml"));
			xmlE.writeObject(new Properties(10, "growing-last", "dfs" , 10,"cli"));
			xmlE.close();
			System.out.println("XML File create successfuly!");
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
	}

}
