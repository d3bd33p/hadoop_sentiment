import java.io.BufferedReader;                                                                              
import java.io.FileInputStream;                                                                             
import java.io.IOException;                                                                                 
import java.io.InputStreamReader;                                                                           
public class Punctuators                                                                                    
                                                                                                            
                                                                                                            
                                                                                                            
{                                                                                                           
	public static void main(String[] args) throws IOException                                               
	{                                                                                                       
		FileInputStream fstream = new FileInputStream("/home/cloudera/textfile.txt");                       
		BufferedReader br= new BufferedReader(new InputStreamReader(fstream));                              
		String str;                                                                                         
        try{                                                                                                
		while((str = br.readLine())!=null)                                                                  
		{                                                                                                   
		System.out.println(str.replaceAll("\\p{P}", " "));                                                  
		System.out.println(str.replaceAll("[^a-zA-Z ]", ""));                                               
		}                                                                                                   
		 }                                                                                                  
		catch(Exception e)                                                                                  
        {                                                                                                   
        		                                                                                            
        	System.out.println("Exception occured while removal.");                                         
	}       	                                                                                            
                                                                                                            
	}	                                                                                                    
			                                                                                                
		                                                                                                    
}	                                                                                                        
                                                                                                            
                                                                                                            
                                                                                                            
