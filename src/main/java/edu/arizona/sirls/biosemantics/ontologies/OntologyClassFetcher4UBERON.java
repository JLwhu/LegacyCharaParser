/**
 * 
 */
package  edu.arizona.sirls.biosemantics.ontologies;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;

import  edu.arizona.sirls.biosemantics.parsing.ApplicationUtilities;

/**
 * @author Hong Cui
 *
 */
public class OntologyClassFetcher4UBERON extends OntologyClassFetcher {
	private boolean debug = false;

	/**
	 * @param ontopath
	 * @param conn
	 * @param table
	 */
	public OntologyClassFetcher4UBERON(String ontopath, Connection conn,
			String table) {
		super(ontopath, conn, table);
		Statement stmt = null;
		try{
			//create table if not exist
			stmt = conn.createStatement();
			stmt.execute("drop table if exists "+table);
			//underscored terms (e.g, anal_fin) are added to glossaryfixed as "structure".
			stmt.execute("create table if not exists "+table+"(id MEDIUMINT NOT NULL AUTO_INCREMENT Primary Key, ontoid varchar(200), term varchar(200), category varchar(100), head_noun varchar(50), underscored varchar(200), remark text)");		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null) stmt.close();
			}catch(Exception e){
				StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);
				LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+
						System.getProperty("line.separator")
						+sw.toString());
			}
		}
	}

	/* (non-Javadoc)
	 * @see biosemantics.ontologies.OntologyClassFetcher#selectClasses()
	 */
	@Override
	public void selectClasses() {
		for(OWLClass aclass: allclasses){
			for(OWLOntology onto : super.onts){//each onto in the closure
				//get and save class id
				String classid = aclass.getIRI().toString().replaceAll(".*?(?=[A-Z]\\S+_[\\d]{7,7})", "");
				
				if(debug) System.out.println(classid);
				//get and save the label for the class
				String label = "";
				for (OWLAnnotation labelannotation : aclass
						.getAnnotations(onto, df.getRDFSLabel())) {
						if (labelannotation.getValue() instanceof OWLLiteral) {
							OWLLiteral val = (OWLLiteral) labelannotation.getValue();
							label = val.getLiteral();
							break;
						}
				}
				if(label.length()>0){
					super.selectedClassIds.add(classid);
					super.selectedClassLabels.add(label);
					//save category
					super.selectedClassCategories.add("structure");
				}
				
				//gather all types of synonyms (except synonyms in other languages)
				ArrayList<String> synonyms = getSynonymLabels(aclass);
				for(String synonym : synonyms){
					if(!synonym.matches(".*?\\([A-Z]\\w+\\)$")){ //(Japanese)
						super.selectedClassIds.add(classid+"_syn");
						super.selectedClassLabels.add(synonym);
						super.selectedClassCategories.add("structure");
					}
				}
				//get relational adjective
				Set<OWLAnnotation> annotations = aclass.getAnnotations(onto);
				for(OWLAnnotation anno : annotations){
					//System.out.println(anno.toString());
					//adjectiveorgans//adj => classID#label
					if(anno.toString().contains("UBPROP_0000007") ){//has_relational_adjective
						String adj = anno.getValue().toString();//"zeugopodial"^^xsd:string
						adj = adj.substring(0, adj.indexOf("^^")).replace("\"", "");
						super.selectedClassIds.add(classid+"_adj");
						super.selectedClassLabels.add(adj);
						super.selectedClassCategories.add("structure");
					}
				}
			}
		}
	}

	private void recordHeadNouns() {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select id, term from "+table);
			while(rs.next()){
				String id = rs.getString("id");
				String term = rs.getString("term");
				String headnoun = term.indexOf(" ") > 0? term.substring(term.lastIndexOf(" ")).trim() : term;
				pstmt = conn.prepareStatement("update "+table+" set head_noun= ? where id= ? ");
				pstmt.setString(1, headnoun);
				pstmt.setString(2,  id);
				pstmt.executeUpdate();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt != null) stmt.close();
				if(pstmt != null) pstmt.close();
				if(rs!=null) rs.close();
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> suffix = new ArrayList<String>();
		suffix.add("");
		suffix.add("_38484");
		suffix.add("_40674");
		suffix.add("_40676");
		suffix.add("_40716");
		suffix.add("_40717");
		suffix.add("_40718");
		suffix.add("_best");
		for(String suf: suffix){

			String termsobject = "C:/Users/updates/CharaParserTest/Ontologies/charaparser_eval/Ontologies/ext"+suf+"_terms.bin";
			String psobject = "C:/Users/updates/CharaParserTest/Ontologies/charaparser_eval/Ontologies/ext"+suf+"_terms.p2s.bin";
			String ontoURL = "C:/Users/updates/CharaParserTest/Ontologies/charaparser_eval/Ontologies/ext"+suf+".owl";
			String table = "ext"+suf+"_onto_structures";


			Connection conn = null;
			//String termsobject = ApplicationUtilities.getProperty("ontophrases.bin");
			//String table = ApplicationUtilities.getProperty("ontophrases.table");
			//String table = "uberon"+ApplicationUtilities.getProperty("ontophrases.table.suffix");
			//String table = "uberon_structures";
			String database="charaparsereval2013";
			String username="biocreative";
			String password="biocreative";
			try{
				if(conn == null){
					Class.forName("com.mysql.jdbc.Driver");
					String URL = "jdbc:mysql://localhost/"+database+"?user="+username+"&password="+password+"&connectTimeout=0&socketTimeout=0&autoReconnect=true";
					conn = DriverManager.getConnection(URL);
				}
			}catch(Exception e){
				e.printStackTrace();
				//StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
			}
			OntologyClassFetcher4UBERON ocf = new OntologyClassFetcher4UBERON(ontoURL, conn, table);
			ocf.selectClasses();
			ocf.saveSelectedClass("UBERON|CL");
			ocf.recordHeadNouns();
			ocf.serializeTermArrayList(termsobject);
			ocf.serializePSArrayList(psobject);
		}
	}

}
