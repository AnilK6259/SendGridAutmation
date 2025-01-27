package sendGridFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
//import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
//import org.apache.poi.ss.usermodel.IndexedColors;
//import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteCTAFiles {
	
	private SendGridFilesSerivice sgfs;
	
	private int peCeDesheetOrder=0;
	
	private int exExpSheetOrder=0;
	
	private int discoutSheetOrder=0;
	
	private int bounce=0;
	private int click=0;
	private int delivered=0;
	private int open=0;
	private int processed=0;
	
	private Map<String,Map<String,Integer>> PeCeDeMails=new HashMap<>();
	
	private Set<String> passengerEmails= new HashSet<>();
	
	private Set<String> dealerEmails=new HashSet<>();
	
	private Set<String> commercialEmails=new HashSet<>();
	
	private String[] sheetNames= {"Passenger","Dealer ","Commerical"};
	
	private Set<String> removedMails=new HashSet<>();
	
	public void setRemovedMails(String email)
	{
		removedMails.add(email);
	}
	
	
	public void setPeCeDeMails(Map<String,Map<String,Integer>> emails)
	{
		PeCeDeMails.putAll(emails);
	
	}
	
	
	public WriteCTAFiles(SendGridFilesSerivice sgfs)
	{
		this.sgfs=sgfs;
	}
	
	public void writeFiles(String ctaName)
	{
		String fileName=setFileName(ctaName);
		
		String filePath=sgfs.getResultsFilePath().concat(fileName);
		
		File file=new File(filePath);
		
		// System.out.println("Checking file at: " + file.getAbsolutePath());
		
		if(!file.exists())
		{
			try {
				
				file.createNewFile();
				
			} 
			catch (IOException e)
			{
				
				e.printStackTrace();
			}
		}
		
		
		try {
			
			int sheetOrder=0;
			
			if(ctaName=="F-D-CTA1"||ctaName=="F-D-CTA2"||ctaName=="F-D-CTA3"||ctaName=="F-D-CTA4"||ctaName=="P&C-CTA 1"||ctaName=="P-CTA 2"||ctaName=="P-CTA 3"||ctaName=="C-CTA 5&6")	
			{
				sheetOrder=peCeDesheetOrder;
			}
			
			else if(ctaName=="CTA-2-Disc"||ctaName=="CTA-1-Disc"||ctaName=="CTA-3-Disc")
			{
				sheetOrder=discoutSheetOrder;
			}
			
			else if(ctaName=="FMDPEX - CTA1"||ctaName=="FMDPEX - CTA2"||ctaName=="FMDPEX - CTA3"||ctaName=="FMDPEX - CTA4"||ctaName=="FMDPEXP - CTA1"||ctaName=="FMDPEXP - CTA2"||ctaName=="FMDPEXP - CTA3"||ctaName=="FMDPEXP - CTA4")
			{
				sheetOrder=exExpSheetOrder;
			}
			
			XSSFWorkbook workbook;
			
			FileInputStream fi ;
			
			if(sheetOrder>0)
			{
			fi = new FileInputStream(filePath);
			
			workbook=new XSSFWorkbook(fi);
		}
			
			else
			{
			workbook=new XSSFWorkbook();
			}
			//workbook.getNumberOfSheets()
			
			XSSFSheet sheet=workbook.createSheet(ctaName);
			
			workbook.setSheetOrder(ctaName, sheetOrder);
			
			//sheetOrder++;
			
			if(ctaName=="F-D-CTA1"||ctaName=="F-D-CTA2"||ctaName=="F-D-CTA3"||ctaName=="F-D-CTA4"||ctaName=="P&C-CTA 1"||ctaName=="P-CTA 2"||ctaName=="P-CTA 3"||ctaName=="C-CTA 5&6")	
			{
				peCeDesheetOrder++;
			}
			
			else if(ctaName=="CTA-2-Disc"||ctaName=="CTA-1-Disc"||ctaName=="CTA-3-Disc")
			{
				discoutSheetOrder++;
			}
			
			else if(ctaName=="FMDPEX - CTA1"||ctaName=="FMDPEX - CTA2"||ctaName=="FMDPEX - CTA3"||ctaName=="FMDPEX - CTA4"||ctaName=="FMDPEXP - CTA1"||ctaName=="FMDPEXP - CTA2"||ctaName=="FMDPEXP - CTA3"||ctaName=="FMDPEXP - CTA4")
			{
				exExpSheetOrder++;
			}
			
			sheet.setColumnWidth(0, 40 * 256);
			
			sheet.setDisplayGridlines(false);
			
			int sizeOfTheLoop=(sgfs.getEmails().size()+1);
			
			int mailIndex=0;
			
		        XSSFCellStyle cellStyle = workbook.createCellStyle();
		        cellStyle.setBorderTop(BorderStyle.THIN); 
		        cellStyle.setBorderBottom(BorderStyle.THIN); 
		        cellStyle.setBorderLeft(BorderStyle.THIN); 
		        cellStyle.setBorderRight(BorderStyle.THIN); 
		        
		        XSSFFont font = workbook.createFont();
		        
		        font.setBold(true);
		    
		        cellStyle.setFont(font);
		        
		        byte[] rgb = new byte[] {(byte) 217, (byte) 225, (byte) 242};  // RGB equivalent of #D9E1F2
		        XSSFColor color = new XSSFColor(rgb);

		        //XSSFCellStyle style = workbook.createCellStyle();
		        cellStyle.setFillForegroundColor(color);
		        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); 
		    
		        XSSFCellStyle cellStyle1 = workbook.createCellStyle();
		        cellStyle1.setBorderTop(BorderStyle.THIN); 
		        cellStyle1.setBorderBottom(BorderStyle.THIN); 
		        cellStyle1.setBorderLeft(BorderStyle.THIN); 
		        cellStyle1.setBorderRight(BorderStyle.THIN);
			
			for(int i=0;i<=sizeOfTheLoop;i++)
			{
				XSSFRow row=sheet.createRow(i);
				
				Object[] Mails=sgfs.getEmails().keySet().toArray();
				
				String mailId=(String) Mails[mailIndex];
				
				for(int j=0;j<=5;j++)
				{
				
					XSSFCell cell=row.createCell(j);
					
				if(i==0)
				{
					switch(j)
					{
					
					case 0:
					{
						cell.setCellValue((String)"Row Labels");
	
						cell.setCellStyle(cellStyle);
						
						break;
					}
					
					case 1:
					{
						cell.setCellValue((String)"bounce");
					
						cell.setCellStyle(cellStyle);
						
						break;
					}
					
					case 2:
					{
						cell.setCellValue((String)"click");
						
						cell.setCellStyle(cellStyle);
						break;
					}
					
					case 3:
					{
						cell.setCellValue((String)"delivered");
				
						cell.setCellStyle(cellStyle);
						
						break;
					}
					
					case 4:
					{
						cell.setCellValue((String)"open");
						
						cell.setCellStyle(cellStyle);
						
						break;
					}
					
					case 5:
					{
						cell.setCellValue((String)"processed");
				
						cell.setCellStyle(cellStyle);
						
						break;
					}
					
					}
				}
				
				else if(i==(sgfs.getEmails().size()+1))
				{
					switch(j)
					{
					
					case 0:
					{
						cell.setCellValue((String)"Grand Total");
						
						cell.setCellStyle(cellStyle);
						break;
					}
					
					case 1:
					{
						cell.setCellValue((int)bounce);
						
						cell.setCellStyle(cellStyle);
						break;
					}
					
					case 2:
					{
						cell.setCellValue((int)click);
						
						cell.setCellStyle(cellStyle);
						
						break;
					}
					
					case 3:
					{
						cell.setCellValue((int)delivered);
						
						cell.setCellStyle(cellStyle);
						break;
					}
					
					case 4:
					{
						cell.setCellValue((int)open);
					
						cell.setCellStyle(cellStyle);
						break;
					}
					
					case 5:
					{
						cell.setCellValue((int)processed);
				
						cell.setCellStyle(cellStyle);
						
						break;
					}
					
					}
				}
				
				else
				{
					//font.setBold(false);
					
					switch(j)
					{
					
					case 0:
					{
						cell.setCellValue((String)mailId);
						
						cell.setCellStyle(cellStyle1);
						
						//mailIndex++;
						
						break;
					}
					
					case 1:
					{
						
						if(sgfs.getEmails().get(mailId).get("bounce")==0)
						{
						cell.setCellValue((String)"");
						
						cell.setCellStyle(cellStyle1);
						}
						
						else
						{
						cell.setCellValue((int)sgfs.getEmails().get(mailId).get("bounce"));
							
						setBounce(sgfs.getEmails().get(mailId).get("bounce"));
						
						cell.setCellStyle(cellStyle1);
						}
						
						break;
					}
					
					case 2:
					{
						
						if(sgfs.getEmails().get(mailId).get("click")==0)
						{
						cell.setCellValue((String)"");
						
						cell.setCellStyle(cellStyle1);
						}
						
						else
						{
							cell.setCellValue((int)sgfs.getEmails().get(mailId).get("click"));
							
							setClick(sgfs.getEmails().get(mailId).get("click"));
							
						cell.setCellStyle(cellStyle1);
						}
						
						
						break;
					}
					
					case 3:
					{
						
						if(sgfs.getEmails().get(mailId).get("delivered")==0)
						{
						cell.setCellValue((String)"");
						
						cell.setCellStyle(cellStyle1);
						}
						
						else
						{
							cell.setCellValue((int)sgfs.getEmails().get(mailId).get("delivered"));
							
							setDelivered(sgfs.getEmails().get(mailId).get("delivered"));
							
						    cell.setCellStyle(cellStyle1);
						}
						break;
					}
					
					case 4:
					{
						if(sgfs.getEmails().get(mailId).get("open")==0)
						{
						cell.setCellValue((String)"");
						
						cell.setCellStyle(cellStyle1);
						}
						
						else
						{
							cell.setCellValue((int)sgfs.getEmails().get(mailId).get("open"));
							
							setOpen(sgfs.getEmails().get(mailId).get("open"));
							
						cell.setCellStyle(cellStyle1);
						}
						break;
					}
					
					case 5:
					{
						if(sgfs.getEmails().get(mailId).get("processed")==0)
						{
						cell.setCellValue((String)"");
						
						cell.setCellStyle(cellStyle1);
						}
						
						else
						{
							cell.setCellValue((int)sgfs.getEmails().get(mailId).get("processed"));
							
							setProcessed(sgfs.getEmails().get(mailId).get("processed"));
							
						cell.setCellStyle(cellStyle1);
						}
						break;
					}
					
					}
				}
				
				}
				if(i>0&&i<sgfs.getEmails().size())
				{
				mailIndex++;
				}
			}
			
			//sgfs.setLastIndex(sgfs.getEmails().size());
			
			System.out.println(ctaName+"  :FileCreated");
			
			setEventsCount();
			
			if(ctaName=="F-D-CTA1"||ctaName=="F-D-CTA2"||ctaName=="F-D-CTA3"||ctaName=="F-D-CTA4"||ctaName=="P&C-CTA 1"||ctaName=="P-CTA 2"||ctaName=="P-CTA 3"||ctaName=="C-CTA 5&6")	
			{
				setPeCeDeMails(sgfs.getEmails());
				
				System.out.println(PeCeDeMails.size()+" :Mails added to map");
			}
			
			sgfs.getEmails().clear();
			
			FileOutputStream fo=new FileOutputStream(filePath);
			
			workbook.write(fo);
			
			//fi.close();
			
			workbook.close();
			
			fo.close();
			
		} 
		
		
		catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} 
		
		
		catch (IOException e) {
			
			e.printStackTrace();
		}
		
	
		
	}
	
	public void setBounce(int eventCount)
	{
		this.bounce+=eventCount;
	}
	
	public void setClick(int eventCount)
	{
		this.click+=eventCount;
	}
	
	public void setDelivered(int eventCount)
	{
		this.delivered+=eventCount;
	}
	
	public void setOpen(int eventCount)
	{
		this.open+=eventCount;
	}
	
	public void setProcessed(int eventCount)
	{
		this.processed+=eventCount;
	}
	
	public void setEventsCount()
	{
		this.bounce=0;
		this.click=0;
		this.delivered=0;
		this.open=0;
		this.processed=0;
	}
	
	public String setFileName(String ctaName)
	{
		LocalDate dt=LocalDate.now();
		
		LocalDate dt1=dt.minusDays(1);
		
		DateTimeFormatter dtm=DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		//dtm.format(dt1);
		
		String FileName=null;
		
		if(ctaName=="F-D-CTA1"||ctaName=="F-D-CTA2"||ctaName=="F-D-CTA3"||ctaName=="F-D-CTA4"||ctaName=="P&C-CTA 1"||ctaName=="P-CTA 2"||ctaName=="P-CTA 3"||ctaName=="C-CTA 5&6")	
		{
			FileName=dtm.format(dt1)+".xlsx";
		}
		
		else if(ctaName=="CTA-2-Disc"||ctaName=="CTA-1-Disc"||ctaName=="CTA-3-Disc")
		{
			FileName="Discount-CTA-Stats "+dtm.format(dt1)+".xlsx";
		}
		
		else if(ctaName=="FMDPEX - CTA1"||ctaName=="FMDPEX - CTA2"||ctaName=="FMDPEX - CTA3"||ctaName=="FMDPEX - CTA4"||ctaName=="FMDPEXP - CTA1"||ctaName=="FMDPEXP - CTA2"||ctaName=="FMDPEXP - CTA3"||ctaName=="FMDPEXP - CTA4")
		{
			FileName="FMDPEX & FMDPEXP "+dtm.format(dt1)+".xlsx";
		}
		
		return FileName;
	}
	
	public void loadPeCeDeMails()
	{
		String filePath="C:\\Users\\anilk\\JavaPractice\\SendGrid\\PeCeDealerMails\\Testing Data.xlsx";
		
		try {
			FileInputStream fi=new FileInputStream(filePath);
			
			XSSFWorkbook workbook=new XSSFWorkbook(fi);
			
			
			
			for(String sheetName:sheetNames)
			{
				XSSFSheet sheet=workbook.getSheet(sheetName);
				
				int lastRow=sheet.getLastRowNum();
				
				for(int i=1;i<=lastRow;i++)
				{
				 XSSFRow row=sheet.getRow(i);
				 
				 XSSFCell cell=row.getCell(0);
				 
				 String email=cell.toString().toLowerCase();
				 
				switch(sheetName)
				{
				
				case "Passenger":passengerEmails.add(email);break;
				
				case "Dealer ":dealerEmails.add(email);break;
				
				case "Commerical":commercialEmails.add(email);break;
				}
				}
				
				System.out.println(sheetName+" :Mails Loaded");
			}
			workbook.close();
			fi.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writePeCeDeMails()
	{
		String FilePath=sgfs.getResultsFilePath()+setFileName("F-D-CTA1");
		
		
		try {
			FileInputStream fi=new FileInputStream(FilePath);
			
			XSSFWorkbook workbook=new XSSFWorkbook(fi);
			
			   XSSFCellStyle cellStyle = workbook.createCellStyle();
		        cellStyle.setBorderTop(BorderStyle.THIN); 
		        cellStyle.setBorderBottom(BorderStyle.THIN); 
		        cellStyle.setBorderLeft(BorderStyle.THIN); 
		        cellStyle.setBorderRight(BorderStyle.THIN); 
		        
		        XSSFFont font = workbook.createFont();
		        
		        font.setBold(true);
		        
		        cellStyle.setFont(font);
		        
		        byte[] rgb = new byte[] {(byte) 217, (byte) 225, (byte) 242};  // RGB equivalent of #D9E1F2
		        XSSFColor color = new XSSFColor(rgb);

		        //XSSFCellStyle style = workbook.createCellStyle();
		        cellStyle.setFillForegroundColor(color);
		        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); 
		    
		        XSSFCellStyle cellStyle1 = workbook.createCellStyle();
		        cellStyle1.setBorderTop(BorderStyle.THIN); 
		        cellStyle1.setBorderBottom(BorderStyle.THIN); 
		        cellStyle1.setBorderLeft(BorderStyle.THIN); 
		        cellStyle1.setBorderRight(BorderStyle.THIN);
			
			String[] sheetNames= {"Passenger_Emails","Commercial_Emails","Dealer_Emails"};
			
			for (String SOLName:sheetNames)
			{
				XSSFSheet sheet=workbook.createSheet(SOLName);
				
				workbook.setSheetOrder(SOLName, peCeDesheetOrder);
				
				sheet.setColumnWidth(0, 40 * 256);
				
				sheet.setDisplayGridlines(false);
				
				Set<String> SOLMails=getMapOfMails(SOLName);
				
				int rowIndex=0;
				
				 for(Entry<String, Map<String, Integer>> email:PeCeDeMails.entrySet())
				 {
					 if(rowIndex==0) 
						{

						 XSSFRow row=sheet.createRow(rowIndex);
						 
						 for(int i=0;i<=5;i++)
						 {
							XSSFCell cell=row.createCell(i);
							
							switch(i)
							{
							
							case 0:
							{
								cell.setCellValue((String)"Row Labels");
			
								cell.setCellStyle(cellStyle);
								
								break;
							}
							
							case 1:
							{
								cell.setCellValue((String)"bounce");
							
								cell.setCellStyle(cellStyle);
								
								break;
							}
							
							case 2:
							{
								cell.setCellValue((String)"click");
								
								cell.setCellStyle(cellStyle);
								break;
							}
							
							case 3:
							{
								cell.setCellValue((String)"delivered");
						
								cell.setCellStyle(cellStyle);
								
								break;
							}
							
							case 4:
							{
								cell.setCellValue((String)"open");
								
								cell.setCellStyle(cellStyle);
								
								break;
							}
							
							case 5:
							{
								cell.setCellValue((String)"processed");
						
								cell.setCellStyle(cellStyle);
								
								break;
							}
							
							}
						 }
						 
						 rowIndex++;
						}
					 
					 if(SOLMails.contains(email.getKey()))
					 {
						 XSSFRow row=sheet.createRow(rowIndex);
						 
						 for(int i=0;i<=5;i++)
						 {
							 XSSFCell cell=row.createCell(i);
							 
							 switch(i)
								{
								
								case 0:
								{
									cell.setCellValue((String)email.getKey());
									
									cell.setCellStyle(cellStyle1);
									
									//mailIndex++;
									
									break;
								}
								
								case 1:
								{
									
									
									if(email.getValue().get("bounce")==0)
									{
									cell.setCellValue((String)"");
									
									cell.setCellStyle(cellStyle1);
									}
									
									else
									{
									cell.setCellValue((int)email.getValue().get("bounce"));
										
									cell.setCellStyle(cellStyle1);
									}
									
									break;
								}
								
								case 2:
								{
									
									if(email.getValue().get("click")==0)
									{
									cell.setCellValue((String)"");
									
									cell.setCellStyle(cellStyle1);
									}
									
									else
									{
										cell.setCellValue((int)email.getValue().get("click"));
	
									cell.setCellStyle(cellStyle1);
									}
									
									
									break;
								}
								
								case 3:
								{
									
									if(email.getValue().get("delivered")==0)
									{
									cell.setCellValue((String)"");
									
									cell.setCellStyle(cellStyle1);
									}
									
									else
									{
										cell.setCellValue((int)email.getValue().get("delivered"));
									
									    cell.setCellStyle(cellStyle1);
									}
									break;
								}
								
								case 4:
								{
									if(email.getValue().get("open")==0)
									{
									cell.setCellValue((String)"");
									
									cell.setCellStyle(cellStyle1);
									}
									
									else
									{
										cell.setCellValue((int)email.getValue().get("open"));
										
									cell.setCellStyle(cellStyle1);
									}
									break;
								}
								
								case 5:
								{
									if(email.getValue().get("processed")==0)
									{
									cell.setCellValue((String)"");
									
									cell.setCellStyle(cellStyle1);
									}
									
									else
									{
										cell.setCellValue((int)email.getValue().get("processed"));
										
									cell.setCellStyle(cellStyle1);
									}
									break;
								}
								
								}
							 
						 }
						 
						 rowIndex++;
						 
						 //PeCeDeMails.remove(email.getKey(),email.getValue()) ;  
						 
						 //setRemovedMails(email.getKey());
					 }
				 }
				 
				 System.out.println(SOLName+" :File Created");
				 
				/*for(String MailId:removedMails)
				{
					PeCeDeMails.remove(MailId);
				}
				
				removedMails.clear();*/
			}
			
			FileOutputStream fo=new FileOutputStream(FilePath);
			
			workbook.write(fo);
			
			workbook.close();
			
			fi.close();
			
			fo.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Set<String> getMapOfMails(String SOLName)
	{
		Set<String> mails=null;
		
		switch(SOLName)
		{
		case "Passenger_Emails":mails= passengerEmails;break;
		
		case "Commercial_Emails":mails=commercialEmails;break;
		
		case "Dealer_Emails"	:mails=dealerEmails;break;
		
		}
		
		return mails;
	}

}
