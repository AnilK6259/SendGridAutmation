package sendGridFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class SendGridFilesSerivice {
	
	private String fmdpeCta1Subject="Ford - Planning to Keep your Ford ";
	
	private String dealerCta1Subject="Important news about your";
	
	private String fmdpexCta1Subject="Ford - Planning to Keep your Ford?";
	
	private String fmdpexpCta1Subject="Ford - Important News About You";
	
	private String dataFilesPath="C:\\Users\\anilk\\JavaPractice\\SendGrid\\DataFiles\\";
	
	private String resultsFilePath="C:\\Users\\anilk\\JavaPractice\\SendGrid\\Result\\";
	
	private Map<String,Map<String,Integer>>emails=new LinkedHashMap<>();
	
	private List<String> emailSubjects=new ArrayList<>();
	
	private WriteCTAFiles wpcdFiles;
	
	//private int lastIndex=0;
	
	
	public void setWritePeCeDealersFiles(WriteCTAFiles wpcdFiles)
	{
		this.wpcdFiles=wpcdFiles;
	}
	
	
	/*public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}*/
	
	public void putDataIntoMaps(String email,String event1)
	{
		
		if(!emails.containsKey(email))
		{
			Map<String,Integer> event=new HashMap<>();
			
			event.put("bounce", 0);
			
			event.put("click", 0);
			
			event.put("delivered", 0);
			
			event.put("open", 0);
			
			event.put("processed", 0);
			
			event.put(event1, event.getOrDefault(event1, 0)+1);
			
			emails.put(email, event);
		}
		
		else
		{
			emails.get(email).put(event1, emails.get(email).getOrDefault(event1, 0)+1);
		}
	}
	
	public void getMails(String filePath)
	{
	
   	try {
			FileReader reader=new FileReader(filePath);
			
			CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT);
			
			for(CSVRecord record:csvParser)
			{
				
				if(isValidMailId(record.get(8)))
				{
					putDataIntoMaps(record.get(8),record.get(2));
					
				}
				
				if("bounce".equalsIgnoreCase(record.get(2))||"processed".equalsIgnoreCase(record.get(2)))
				{
					emailSubjects.add(record.get(6));
				}
				
			}
			
			String sheetName=ctaFinder();
			
			emailSubjects.clear();

			wpcdFiles.writeFiles(sheetName);
			
			
			reader.close();
			
		}
   	
   	
   	catch (IOException e) 
   	
   	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*public int getLastIndex() {
		return lastIndex;
	}*/

	public String getResultsFilePath() {
		return resultsFilePath;
	}

	public Map<String, Map<String, Integer>> getEmails() {
		return emails;
	}

	public boolean isValidMailId(String mailid)
	{
		boolean isTrue=false;
		
		if(mailid.contains("@"))
		{
			String[] mailidParts=mailid.split("@");
			
			if(mailidParts[1].equalsIgnoreCase("firstbaseit.net")||mailidParts[1].equalsIgnoreCase("ibaseit.com"))
{
				isTrue=false;
}
			if(mailid.contains("test")||mailid.contains("anil")||mailid.contains("ashok")||mailid.contains("ravi"))
			{
				isTrue=false;
			}
			else
			{
				isTrue=true;
			}
		}
		
		return isTrue;
	}
	
	public void fileDir()
	{

		File file=new File(dataFilesPath);
		
	    File[] files=file.listFiles();
	    
	    for(File file1:files)
	    {
	    	getMails(file1.getPath());
	    }
	    
	   
	}
	
	public String ctaFinder()
	{
		
		String ctaName=null;
		
		int[] subStringIndex= {24,43,29,47,67,69,50,61,21,34,35,25,31};
		
		//{23,42,28,46,66,68,50,60,20,33,34,24,31};
			//{33,46,51,49,24,21,28,50,23,42,28,31,46,66,68,34,61,66,59};
		
		int countSubject1=0;
		
		int countSubject2=0;
		
		for(int i=0;i<=emailSubjects.size();i++)
		{
			
			
			String emailSubject=null;
			
			String subjects=emailSubjects.get(i).trim();
			
			
		for(int j:subStringIndex)
		{
			boolean loopBreak=false;
			
			if(subjects.length()>j)
			{
			String subStringSubject=subjects.substring(0, j);

			switch(subStringSubject)
			{
			
			case "Saving you a minimum of ":
		    {
		    	ctaName="CTA-1-Disc"; 
		    	
		    	System.out.println(ctaName);
		    	
		    	loopBreak=true;
		    	
		    	break;
		    }
		    
			case "Don't Miss Out - Save on your policy today!":
			{
				ctaName="CTA-2-Disc";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Quotation Expiry Notification":
			{
				ctaName="CTA-3-Disc";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Ford - Planning to Keep your Ford?":
			{
				countSubject2++;
				
				emailSubject=subStringSubject;
				
				break;
			}
			
			case "Ford - Your Ford New Vehicle Extended Warranty is Expiring f":
			{

				ctaName="FMDPEX - CTA2";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Ford - Your Ford New Vehicle Extended Warranty is Expiring Soon for":
			{
				ctaName="FMDPEX - CTA3";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Ford - Your Ford New Vehicle Extended Warranty Expiry notice":
			{
				ctaName="FMDPEX - CTA4";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Ford - Important News About You":
			{
				countSubject2++;
				
				emailSubject=subStringSubject;
				
				break;
			}
			
			case "Ford - Extended Warranty Quote For Your Vehicle":
			{
				ctaName="FMDPEXP - CTA2";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Ford - Reminder About Your Extended Warranty Quote For Your Vehicle":
			{
				ctaName="FMDPEXP - CTA3";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Ford - Your Extended Warranty Quote Is Due To Expire For Your Vehicle":
			{
				ctaName="FMDPEXP - CTA4";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Important news about your":
			{
				countSubject1++;
				
				emailSubject=subStringSubject;
				
				break;
			}
			
			case "A reminder about your":
			{
				ctaName="F-D-CTA2";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Your warranty quote f":
			{
				ctaName="F-D-CTA3";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Time is running out for your warranty quote on you":
			{
				ctaName="F-D-CTA4";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Ford - Planning to Keep your Ford ":
			{
				countSubject1++;
				
				emailSubject=subStringSubject;
				
				break;
			}
			
			case "Ford - Your Ford Base Warranty is Expiring for ":
			{
				ctaName="P-CTA 2";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Ford - Your Ford Base Warranty is Expiring Soon fo":
			{
				ctaName="P-CTA 3";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
			
			case "Ford - Your Ford Base Warranty has Expired on your":
			{
				ctaName="C-CTA 5&6";
				
				System.out.println(ctaName);
				
				loopBreak=true;
				
				break;
			}
				
				
			}
			
			if(loopBreak)
			{
				break;
			}
			
			}
			
		}
		

		if(emailSubject==null)
		{
			break;
		}
		
		
		if(i==(emailSubjects.size()-1))
		{
		if(emailSubject.equalsIgnoreCase(fmdpeCta1Subject)||emailSubject.equalsIgnoreCase(fmdpexCta1Subject))
		{
			if(countSubject2>0&&countSubject1==0)
			{
				ctaName="FMDPEX - CTA1";
				System.out.println(ctaName);
			}
			
			else if(countSubject1>0&&countSubject2>=0)
			{
				ctaName="P&C-CTA 1";
				System.out.println(ctaName);
			}
			
			break;
		}
		
		if(emailSubject.equalsIgnoreCase(dealerCta1Subject)||emailSubject.equalsIgnoreCase(fmdpexpCta1Subject))
		{
			if(countSubject2>0&&countSubject1==0)
			{
				ctaName="FMDPEXP - CTA1";
				System.out.println(ctaName);
			}
			
			else if(countSubject1>0&&countSubject2>=0)
			{
				ctaName="F-D-CTA1";
				System.out.println(ctaName);
			}
			
			break;
		}
		}
		}
		
		return ctaName;
	}
	
	
	

}
