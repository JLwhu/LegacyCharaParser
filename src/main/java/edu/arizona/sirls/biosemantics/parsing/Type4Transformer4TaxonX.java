/**
 * 
 */
package edu.arizona.sirls.biosemantics.parsing;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import edu.arizona.sirls.biosemantics.parsing.diagnosis.TaxonXDiagnosis;

/**
 * @author Hong Updates
 *
 */
public class Type4Transformer4TaxonX extends Type4Transformer {

	/**
	 * @param listener
	 * @param dataprefix
	 */
	public Type4Transformer4TaxonX(ProcessListener listener, String dataprefix) {
		super(listener, dataprefix);
		// TODO Auto-generated constructor stub
	}
	
	protected void transformXML(File[] files) {
		int number = 0;
		try{
			TaxonXDiagnosis txd = new TaxonXDiagnosis(files);
			txd.parseDiagnosis(); //add new patterns to database

			
			SAXBuilder builder = new SAXBuilder();
			for(int f = 0; f < files.length; f++) {
				int fn = f+1;
				//split by treatment
				Document doc = builder.build(files[f]);
				Element root = doc.getRootElement();
				List<Element> treatments = XPath.selectNodes(root,"/tax:taxonx/tax:taxonxBody/tax:treatment");
				//detach all but one treatments from doc
				ArrayList<Element> saved = new ArrayList<Element>();
				for(int t = 1; t<treatments.size(); t++){
					Element e = treatments.get(t);
					doc.removeContent(e);
					e.detach();
					saved.add(e);
				}
				//now doc is a template to create other treatment files
				//root.detach();
				//adding ids to description elements in treatment
				formatDescription((Element)XPath.selectSingleNode(root,"/tax:taxonx/tax:taxonxBody/tax:treatment"),".//tax:div[@type='description']", "./tax:p", fn+"", 0);
				root.detach();
				//output to target folder
				writeTreatment2Transformed(root, fn+"", 0);
				listener.info((number++)+"", fn+"_0.xml"); // list the file on GUI here
				//write out description text to description folder
		        getDescriptionFrom("/tax:taxonx/tax:taxonxBody/tax:treatment/tax:div[@type='description']",root,fn+"", 0);
				//replace treatement in doc with a new treatment in saved
				Iterator<Element> it = saved.iterator();
				int count = 1;
				while(it.hasNext()){
					Element e = it.next();
					Element body = root.getChild("taxonxBody", root.getNamespace());
					Element treatment = (Element) XPath.selectSingleNode(
						root,"/tax:taxonx/tax:taxonxBody/tax:treatment");	
					//in treatment/div[@type="description"], replace <tax:p> tag with <description pid="1.txtp436_1.txt">
					int index = body.indexOf(treatment);
					e = formatDescription(e, ".//tax:div[@type='description']", ".//tax:p",fn+"", count);
					body.setContent(index, e);
					//write each treatment as a file in the target/transfromed folder
					//write description text in the target/description folder
					root.detach();
					writeTreatment2Transformed(root, fn+"", count);
					listener.info((number++)+"", fn+"_"+count+".xml"); // list the file on GUI here
					getDescriptionFrom("/tax:taxonx/tax:taxonxBody/tax:treatment/tax:div[@type='description']", root, fn+"", count);
					count++;
				}				
				String transformeddir = Registry.TargetDirectory+"\\transformed\\";
				try{
					if(MainForm.conn == null){
						Class.forName(ApplicationUtilities.getProperty("database.driverPath"));
						MainForm.conn = DriverManager.getConnection(ApplicationUtilities.getProperty("database.url"));
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}

				//TaxonNameCollector4TaxonX tnc = new TaxonNameCollector4TaxonX(MainForm.conn, transformeddir, this.dataprefix+"_"+ApplicationUtilities.getProperty("TAXONNAMES"), this.dataprefix);
				//tnc.collect();

			}
		}catch(Exception e){
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
