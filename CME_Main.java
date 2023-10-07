

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CME_Main {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CME cme = new CME();
		//cme.extractRelationships("US_RMS.txt");
		cme.extractRelationships(args[0]);
		String filename = "cme_object.ser";
        // Serialization
        try
        {  
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(cme);
            out.close();
            file.close();
            System.out.println("Object has been serialized");
            
            new CMExcel().genCmeExcelOutput();
        }
        catch(IOException ex)
        {
            System.out.println(ex);
        }
	}
}
