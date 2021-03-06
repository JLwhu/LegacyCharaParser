 /* $Id: StreamGobbler.java 1248 2012-11-28 20:36:42Z hong1.cui $ */
/**
 * 
 */
package edu.arizona.sirls.biosemantics.charactermarkup;

/**
 * @author Hong Updates
 *
 */
import java.util.*;

import java.io.*;

import org.apache.log4j.Logger;

import edu.arizona.sirls.biosemantics.parsing.ApplicationUtilities;
@SuppressWarnings({ "unused" })
class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    OutputStream os;
    ArrayList<String> headings = new ArrayList<String>();
    ArrayList<String> trees = new ArrayList<String>();
    static int h = 0;
    static int t = 0;
    static boolean debug = false;
    private static final Logger LOGGER = Logger.getLogger(StreamGobbler.class);
    
    StreamGobbler(InputStream is, String type, ArrayList<String> headings, ArrayList<String> trees)
    {
        this.is = is;
        this.type = type;
        this.headings = headings;
        this.trees = trees;
    }
    
    public void run()
    {
        try
        {  	
        	PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);
                
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            StringBuffer sb = new StringBuffer();

            while ( (line = br.readLine()) != null)
            {	
            	sb = gobbleLine(line, sb);
            }
            if(sb.toString().trim().length()>0){
            	trees.add(sb.toString());
            }
        } catch (IOException ioe)
            {
            StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);ioe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());  
            }
    }

	protected StringBuffer gobbleLine(String line, StringBuffer sb) {
		if(debug) System.out.println(type+">"+line);
		if(line.startsWith("Parsing [sent.")){
			headings.add(line);
			if(debug) System.out.println(h+" add heading: "+line);
			h++;
		}else{
			if(line.startsWith("(ROOT") || line.startsWith("SENTENCE_SKIPPED_OR_UNPARSABLE")){
				if(sb.toString().trim().length()>0){
					trees.add(sb.toString());
					if(debug) System.out.println(t+" add tree: "+sb.toString());
					t++;
				}
				sb = new StringBuffer();
				sb.append(line+System.getProperty("line.separator"));
			}else if(line.matches("^\\s*\\(.*")){
				sb.append(line+System.getProperty("line.separator"));
			}
		}
		return sb;
	}
}

