/**
 * 
 */
package edu.arizona.sirls.biosemantics.ontologies;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import edu.arizona.sirls.biosemantics.parsing.ApplicationUtilities;

/**
 * @author Hong Cui
 * 
 * turn a csv file (part, parent) to a serialized Hashtable<String, ArrayList<String>>
 * 
 *
 */
public class PartOfSerialization {
	Hashtable<String, ArrayList<String>> parts = new Hashtable<String, ArrayList<String>>();
	/**
	 * 
	 */
	public PartOfSerialization(String input, String output) {
		try{

			FileInputStream fstream = new FileInputStream(input);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null)   {
				line = line.trim();
				if(line.length()==0) continue;
				String[] partof = line.split("\\s*,\\s*");
				ArrayList<String> parents = parts.get(partof[0]);
				if(parents == null){
					parents = new ArrayList<String>();
				}
				parents.add(partof[1]);
				parts.put(partof[0], parents);
			}
			br.close();
			//serialization
			File file = new File(output);
			ObjectOutput out = new ObjectOutputStream(
					new FileOutputStream(file));
			out.writeObject(parts);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String input = "Lactarius_part_of_preferredtermsonly.txt";
		String output = "Lactarius_part_of_preferredtermsonly.bin";
		String folder = ApplicationUtilities.getProperty("ontology.dir");
		PartOfSerialization pos = new PartOfSerialization(folder+input, folder+output);

	}

}
