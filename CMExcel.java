
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CMExcel {
	
	public static void main( String[] args ) throws IOException
	{
		new CMExcel().genCmeExcelOutput();
	}

	public void genCmeExcelOutput() throws IOException
	{
		int originalRelSetSize = 0;
		int reducedRelSet1Size = 0;
		int reducedRelSet2Size = 0;
		double reduction = 0.0;
		
		ArrayList<Relationship> finalRelationshipSet = null;
		
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Relations");
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);

		Row header = sheet.createRow(0);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);
		headerStyle.setFont(font);

		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("S.No.");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(1);
		headerCell.setCellValue("User Story");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(2);
		headerCell.setCellValue("Relations");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(3);
		headerCell.setCellValue("#Relationships - Original Set");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(4);
		headerCell.setCellValue("Relationships - Reduced Set 1");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(5);
		headerCell.setCellValue("Relationships - Reduced Set 2");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(6);
		headerCell.setCellValue("Reduction");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(7);
		headerCell.setCellValue("# of tokens in user story");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(8);
		headerCell.setCellValue("# of tokens covered by relations (excluding stopword tokens)");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(9);
		headerCell.setCellValue("# of stopword tokens in user story");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(10);
		headerCell.setCellValue("Coverage");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(11);
		headerCell.setCellValue("Remarks");
		headerCell.setCellStyle(headerStyle);

		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

		Row row = null;
		Cell cell = null;

		//***********************************************************

		CME cme = null;

		// Deserialization
		try
		{  
			// Reading the object from a file
			FileInputStream file = new FileInputStream("cme_object.ser");
			ObjectInputStream in = new ObjectInputStream(file);

			// Method for deserialization of object
			cme = (CME)in.readObject();

			in.close();
			file.close();

			System.out.println("Object has been deserialized ");
			Path outputFileCME = Path.of("output_cme.txt");

			Set<Entry<String, ArrayList<Relationship>>> entries = cme.sourceRelationshipMap.entrySet();
			Files.writeString(outputFileCME, "Total Elements Extracted: "+entries.size());
			System.out.println("Total Elements Extracted: "+entries.size());
			//using for loop
			int count = 1;
			ArrayList<TagInfo> tagsList = null;
			ArrayList<TagInfo> tempTagsList = null;
			LinkedHashSet<String> tagsToWrite = null;

			LinkedHashSet<String> userStoryTokens = null;
			LinkedHashSet<String> extractedRelationshipsTokens = null;

			LinkedHashSet<String> stopWordsSet = new LinkedHashSet<String>();
			stopWordsSet.add("As");
			stopWordsSet.add("a");
			stopWordsSet.add("an");
			stopWordsSet.add(",");
			stopWordsSet.add("I");
			stopWordsSet.add("want");
			stopWordsSet.add("to");
			stopWordsSet.add("the");
			stopWordsSet.add("so");
			stopWordsSet.add("that");
			stopWordsSet.add(".");
			// [As, a, an ,, , I, want, to, the, so , that, .]
			String userStory = null;

			for(Entry<String, ArrayList<Relationship>> entry : entries){
				userStory = entry.getKey();
				userStoryTokens = new LinkedHashSet<String>();

				System.out.println("\nUser Story "+count+": "+userStory);


				row = sheet.createRow(count);
				cell = row.createCell(0);
				cell.setCellValue(count);
				cell.setCellStyle(style);

				cell = row.createCell(1);
				cell.setCellValue(userStory);
				cell.setCellStyle(style);



				Files.writeString(outputFileCME, "\n\nUser Story "+count+": "+userStory,StandardOpenOption.APPEND);

				tagsList = cme.sourceTagsMap.get(userStory);
				tempTagsList = cme.sourceTagsMap.get(userStory);

				for(TagInfo tag:tagsList)
					userStoryTokens.add(tag.getToken());
				
//				reducedRelSet1Size = entry.getValue().size();
				finalRelationshipSet = new CME().getFinalRelationshipSet(entry.getValue());
				reducedRelSet1Size = entry.getValue().size();
				originalRelSetSize = cme.sourceRelationshipSetSizeMap.get(userStory);
				
				System.out.println(entry.getValue());
				
				if(finalRelationshipSet!=null)
					reducedRelSet2Size = finalRelationshipSet.size();
				else
					reducedRelSet2Size=0;
				
				try 
				{
					extractedRelationshipsTokens = new LinkedHashSet<String>();
					
					cell = row.createCell(2);
					cell.setCellValue(finalRelationshipSet.toString());
					cell.setCellStyle(style);
					
					cell = row.createCell(3);
					cell.setCellValue(originalRelSetSize);
					cell.setCellStyle(style);
					
					cell = row.createCell(4);
					cell.setCellValue(reducedRelSet1Size);
					cell.setCellStyle(style);
					
					cell = row.createCell(5);
					cell.setCellValue(reducedRelSet2Size);
					cell.setCellStyle(style);					
													
					for(Relationship relationship:finalRelationshipSet)
					{
						tagsToWrite = new LinkedHashSet<String>();
						if(relationship!=null)
						{
							System.out.println(relationship);
							Files.writeString(outputFileCME, "\n\n"+relationship.toString()+"\n",StandardOpenOption.APPEND);

							String[] subjTokens = relationship.getSubject().split(" ");
							for(int i=0;i<subjTokens.length;i++)
							{
								extractedRelationshipsTokens.add(subjTokens[i]);
								for(TagInfo tag:tagsList)
								{
									if(subjTokens[i].equals(tag.getToken()))
									{
										tagsToWrite.add(tag.toString());
									}
								}
							}

							String[] relTokens = relationship.getRelation().split(" ");
							for(int i=0;i<relTokens.length;i++)
							{
								extractedRelationshipsTokens.add(relTokens[i]);
								for(TagInfo tag:tagsList)
								{
									if(relTokens[i].equals(tag.getToken()))
									{
										tagsToWrite.add(tag.toString());
									}
								}
							}

							String[] objTokens = relationship.getObject().split(" ");
							for(int i=0;i<objTokens.length;i++)
							{
								extractedRelationshipsTokens.add(objTokens[i]);
								for(TagInfo tag:tagsList)
								{
									if(objTokens[i].equals(tag.getToken()))
									{
										tagsToWrite.add(tag.toString());

									}
								}
							}
							for(String tagToWrite:tagsToWrite)
							{
								//									System.out.println(tagToWrite);
								Files.writeString(outputFileCME, "\n"+tagToWrite.toString(),StandardOpenOption.APPEND);
							}
						}
					}
					System.out.println(userStoryTokens);
					System.out.println(stopWordsSet);
					System.out.println(extractedRelationshipsTokens);

					int hit = 0;

					for(String userStoryToken: userStoryTokens)
					{
						for(String relToken:extractedRelationshipsTokens)
						{
							if(userStoryToken.equals(relToken))
							{
								hit++;
							}
						}
					}
					System.out.println("No. of tokens in User Story: "+userStoryTokens.size());
					System.out.println("Tokens in User Story: "+userStoryTokens);
					int countUserStoryTokens = userStoryTokens.size();
					System.out.println("Tokens in extracted relationships: "+extractedRelationshipsTokens.size());
					System.out.println("No. of user story tokens covered by extracted relationships: "+hit);
					
					LinkedHashSet<String> userStoryTokensTemp = new LinkedHashSet<String>();
					userStoryTokensTemp.addAll(userStoryTokens);
					extractedRelationshipsTokens.removeAll(stopWordsSet);
					userStoryTokensTemp.removeAll(extractedRelationshipsTokens);
					
					int countUserStoryTokensCoveredByRelations = countUserStoryTokens - userStoryTokensTemp.size();
					
					userStoryTokensTemp.clear();
					userStoryTokensTemp.addAll(userStoryTokens);
					userStoryTokensTemp.removeAll(stopWordsSet);
					int countStopWordsInUserStory = countUserStoryTokens - userStoryTokensTemp.size();
					
					double coverage = (((double)countUserStoryTokensCoveredByRelations/((double)countUserStoryTokens-(double)countStopWordsInUserStory))*100);
					// [As, a, ,, I, want, to, the, that, .]
					System.out.println(userStoryTokens+": "+countUserStoryTokensCoveredByRelations+" - > "+countUserStoryTokens+" -> "+countStopWordsInUserStory+" -> "+String.format("%1.2f", coverage)+"%");
					Files.writeString(outputFileCME, "\n"+userStoryTokens+" Coverage: "+String.format("%1.2f", coverage)+"%",StandardOpenOption.APPEND);

					Files.writeString(outputFileCME, "\n"+originalRelSetSize+"-->"+reducedRelSet1Size+"-->"+reducedRelSet2Size,StandardOpenOption.APPEND);
					System.out.println(originalRelSetSize+"-->"+reducedRelSet1Size+"-->"+reducedRelSet2Size);
					
					reduction = ((((double)originalRelSetSize)-((double)reducedRelSet2Size))/((double)originalRelSetSize))*100;
					
					Files.writeString(outputFileCME, "\nReduction: "+String.format("%1.2f", reduction)+"%",StandardOpenOption.APPEND);
					System.out.println("Reduction: "+String.format("%1.2f", reduction)+"%");
					
					cell = row.createCell(6);
					cell.setCellValue(String.format("%1.2f", reduction));
					cell.setCellStyle(style);
					
					cell = row.createCell(7);
					cell.setCellValue(countUserStoryTokens);
					cell.setCellStyle(style);
					
					cell = row.createCell(8);
					cell.setCellValue(countUserStoryTokensCoveredByRelations);
					cell.setCellStyle(style);
					
					cell = row.createCell(9);
					cell.setCellValue(countStopWordsInUserStory);
					cell.setCellStyle(style);
					
					cell = row.createCell(10);
					cell.setCellValue(String.format("%1.2f", coverage));
					cell.setCellStyle(style);

					cell = row.createCell(11);
					cell.setCellValue("");
					cell.setCellStyle(style);

				}
				catch(NullPointerException nPE)
				{
					System.out.println("No relationship filtered out of the original relationship set or the original relationship set itself was empty!!");
					Files.writeString(outputFileCME, "\nNo relationship filtered out of the original relationship set or the original relationship set itself was empty!!",StandardOpenOption.APPEND);
				}
				count++;
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}

		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length() - 1) + "output_cme.xlsx";

		FileOutputStream outputStream = new FileOutputStream(fileLocation);
		workbook.write(outputStream);
		workbook.close();
		System.out.println("Excel file created successfully !!");
	}
}