package edu.arizona.sirls.biosemantics.parsing.character;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.arizona.sirls.biosemantics.parsing.ApplicationUtilities;
import edu.arizona.sirls.biosemantics.parsing.MainForm;

public class ManipulateGraphML {
	private static final Logger LOGGER = Logger.getLogger(ManipulateGraphML.class);
	/**
	 * @param args
	 */
	public static void main(String [] args) {
		String group = "D:\\FNA\\FNAV19\\target\\co-occurrence\\" + "Group_1.xml";
		
		//"D:\\FNA\\FNAV19\\target\\co-occurrence\\"+ groupName;
		//removeEdge(new GraphNode("stem"), new GraphNode("herb"), group);
		//GraphNode graphNode = deleteNode("rhizome", group);
		//restoreNode(graphNode, group);
		insertEdge(new GraphNode("stem"), new GraphNode("herb"), group);
		
	}
	 
	
	
	public static void removeEdge(GraphNode fromNode, GraphNode toNode, String groupPath, String group) {		
		
	    try{
		      File file = new File(groupPath);
		      String remElement = "node";
		      String idFrom =  "", idTo = "";
		      
		      if (file.exists()){
		    	  /*Look for existing nodes*/
			        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			        DocumentBuilder builder = factory.newDocumentBuilder();
			        Document doc = builder.parse(groupPath);
			        TransformerFactory tFactory = TransformerFactory.newInstance();
			        Transformer tFormer = tFactory.newTransformer();
			        NodeList nodes = doc.getElementsByTagName(remElement);
			        
			        for (int i = 0 ; i < nodes.getLength(); i++) {
			        	Element el = (Element)nodes.item(i);
			        	String nodeKeyValue =  el.getElementsByTagName("data").item(0).getFirstChild().getNodeValue();

			        	if (nodeKeyValue.equals(fromNode.getNodeName())) {
			        		idFrom = el.getAttribute("id");
			        	}
			        	
			        	if (nodeKeyValue.equals(toNode.getNodeName())) {
			        		idTo = el.getAttribute("id");
			        	}
			        	
			        }
			        /* Now remove the edge connecting the nodes */
			        NodeList nl = doc.getElementsByTagName("edge");
		            int size = nl.getLength();
		            for (int j=0; j< size; j++) {
		            	Element edgeElement = (Element) nl.item(j);
		            	String source = edgeElement.getAttribute("source");
		            	String target = edgeElement.getAttribute("target");
		            	if ((source.equals(idFrom) &&  target.equals(idTo)) 
		            			|| (source.equals(idTo) &&  target.equals(idFrom))) {
		            		edgeElement.getParentNode().removeChild(edgeElement);
		            		doc.normalize();
		            		MainForm.getRemovedEdges().get(group).add(fromNode.getNodeName()+","+toNode.getNodeName());
		            		break;
		            	}
		            	
		            }
			        
        	        Source source = new DOMSource(doc);
        	        Result dest = new StreamResult(new File(groupPath));
        	        tFormer.transform(source, dest); 
		      }
	    } catch(Exception e){
	    	 StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString()); 
	      }

	}
	
	
	
		
	public static void insertEdge(GraphNode fromNode, GraphNode toNode, String groupName) {

	      File file = new File(groupName);
	      String remElement = "node";
	      String idFrom =  "", idTo = "";
	      
	      try {
		      if (file.exists()){
			        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			        DocumentBuilder builder = factory.newDocumentBuilder();
			        Document doc = builder.parse(groupName);
			        TransformerFactory tFactory = TransformerFactory.newInstance();
			        Transformer tFormer = tFactory.newTransformer();
			        
			        NodeList nodes = doc.getElementsByTagName(remElement);
			        
			        for (int i = 0 ; i < nodes.getLength(); i++) {
			        	Element el = (Element)nodes.item(i);
			        	String nodeKeyValue =  el.getElementsByTagName("data").item(0).getFirstChild().getNodeValue();

			        	if (nodeKeyValue.equals(fromNode.getNodeName())) {
			        		idFrom = el.getAttribute("id");
			        	}
			        	
			        	if (nodeKeyValue.equals(toNode.getNodeName())) {
			        		idTo = el.getAttribute("id");
			        	}
			        	
			        }
			        /* Add the parent graph-node */
			        Node graph = doc.getElementsByTagName("graph").item(0);
			        
			        NodeList nl = doc.getElementsByTagName("edge");
		            int size = nl.getLength();
		            boolean present = false;
		            for (int j=0; j< size; j++) {
		            	Element edgeElement = (Element) nl.item(j);
		            	String source = edgeElement.getAttribute("source");
		            	String target = edgeElement.getAttribute("target");
		            	if ((source.equals(idFrom) &&  target.equals(idTo)) 
		            			|| (source.equals(idTo) &&  target.equals(idFrom))) {
		            		present = true;
		            	}
		            }
		            
		            if (!present) {
			        	Element edge = doc.createElement("edge");
			        	edge.setAttribute("source", idFrom);
			        	edge.setAttribute("target", idTo);
			        	graph.appendChild(edge);
			        	doc.normalize();
		            }
			        Source source = new DOMSource(doc);
			        Result dest = new StreamResult(new File(groupName));
			        tFormer.transform(source, dest);
		      }
	      } catch(Exception e){
	    	 StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString()); 
	      }
	}
	
}
