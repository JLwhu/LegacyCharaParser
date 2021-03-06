/**
 * 
 */
package edu.arizona.sirls.biosemantics.parsing;


/**
 * @author hong cui, prasad
 *
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

import edu.arizona.sirls.biosemantics.beans.CharacterGroupBean;
import edu.arizona.sirls.biosemantics.beans.CoOccurrenceBean;
import edu.arizona.sirls.biosemantics.beans.ContextBean;
import edu.arizona.sirls.biosemantics.beans.TermBean;
import edu.arizona.sirls.biosemantics.beans.TermsDataBean;
import edu.arizona.sirls.biosemantics.charactermarkup.Utilities;
import edu.arizona.sirls.biosemantics.db.CharacterStateDBAccess;
import edu.arizona.sirls.biosemantics.db.MainFormDbAccessor;
import edu.arizona.sirls.biosemantics.db.VolumeMarkupDbAccessor;
import edu.arizona.sirls.biosemantics.parsing.character.CoOccurrenceGraph;
import edu.arizona.sirls.biosemantics.parsing.character.GraphNode;
import edu.arizona.sirls.biosemantics.parsing.character.LearnedTermsReport;
import edu.arizona.sirls.biosemantics.parsing.character.ManipulateGraphML;
import edu.arizona.sirls.biosemantics.parsing.state.GraphMLOutputter;
import edu.arizona.sirls.biosemantics.parsing.state.StateCollectorTest;


@SuppressWarnings("unchecked")
public class MainForm {


	@SuppressWarnings("unused")
	private Hashtable<String, String> typos = new Hashtable<String, String>();
	private Hashtable<String, String> categorizedtermsS = new Hashtable<String, String>();
	private Hashtable<String, String> categorizedtermsC = new Hashtable<String, String>();
	private Hashtable<String, String> categorizedtermsO = new Hashtable<String, String>();
	private ArrayList<String> inistructureterms = new ArrayList<String>();
	private ArrayList<String> inicharacterterms = new ArrayList<String>();	
	public static String type4xml;
	private Combo combo_1_1_1;
	private ProgressBar markupProgressBar;
	private Table findStructureTable;
	private Table findDescriptorTable;
	static {
		//Set the Log File path
		try {
			//ApplicationUtilities.setLogFilePath();
		} catch (Exception exe) {
	        exe.printStackTrace();	
		}

	}
	private Combo combo;
	
	public static Connection conn;
	private Combo modifierListCombo;
	private Table finalizerTable;
	private Table transformationTable;
	
	private Table verificationTable;
	private Table extractionTable;
	private Table tagTable;
	private Text targetText;
	private Text sourceText;
	private Text configurationText;
	private TabItem generalTabItem;
	private StyledText contextStyledText;
	private ProgressBar extractionProgressBar;
	private ProgressBar verificationProgressBar;
	private ProgressBar transformationProgressBar;
	//private ProgressBar finalizerProgressBar;
	private ProgressBar popupBar;
	
	private Combo tagListCombo;
	public static Combo dataPrefixCombo;
	public static Combo glossaryPrefixCombo;
	public static Button uploadTerm;
	public static Button generateMatrix;
	public static Button containIndexedParts;
	public static boolean upload2OTO; 
	public static boolean createMatrix;
	public static boolean containindexedparts;
	/* This Group belongs to Markup Tab -> Others tab*/
	//private Group termRoleGroup;
	//private Composite termRoleGroup;
	/* This ScrolledComposite to MarkUpTab -> Others tab*/
	//private ScrolledComposite scrolledComposite;
	/* This rectangle will hold the latest coordinates of the Markup -Others checkbox*/
	//private static Rectangle otherChkbx = new Rectangle(10, 20, 93, 16);
	/* This rectangle will hold the latest coordinates of the Markup -Others tab term*/
	//private static Rectangle otherTerm = new Rectangle(109, 20, 144, 21);
	/* This rectangle will hold the latest coordinates of the Markup - Others tab combo*/
	//private static Rectangle otherCombo = new Rectangle (265, 23, 90, 16);
	/* This String array holds all the roles for the Markup/Others tab */
	//private static String [] otherRoles = {"Other","Structure", "Descriptor", "Verb"};
	/* This ArrayList will hold all the group info of removed terms*/
	//private static ArrayList <TermRoleBean> markUpTermRoles = new ArrayList<TermRoleBean>();
	
	private StyledText glossaryStyledText;
	public Shell shell;
	/*In Unknown removal this variable is used to remember the last tab selected*/
	private static int hashCodeOfItem = 0;
	private boolean [] statusOfMarkUp = {false, false, false, false, false, false, false, false, false, false, false};
	private static boolean saveFlag = false;
	private static final Logger LOGGER = Logger.getLogger(MainForm.class);
	/* document type: This will determine how many tabs to show */
	public static String type = "";
	
	private MainFormDbAccessor mainDb = new MainFormDbAccessor();
	private CharacterStateDBAccess charDb = null;
	public static Text markUpPerlLog;
	/*Character Tab variables-----------------------------------------------------------------------------------*/
	/* This combo is the decision combo in Character tab */
	private static Combo comboDecision;
	/* This combo is the groups list on the Character Tab*/
	public static Combo groupsCombo;
	/* This Scrolled composite holds the termsGroup */
	private static Group termsGroup;
	/* This Scrolled Composite will hold the terms group */	
	private static ScrolledComposite termsScrolledComposite;
	/* This Group will hold all the removed terms */
	private static Group removedTermsGroup;
	/* This Scrolled Composite will hold the removed terms group */	
	private static ScrolledComposite removedScrolledComposite ;
	/* This is the standard increment for every terms row*/
	private static int standardIncrement = 30;
	/* These are the initial coordinates of term 1 group - this will keep on changing and hold the latest group
	 * Once a new group is loaded, this will be reset to initial values
	 * Initial y =
	 * */
	private static Rectangle term1 = new Rectangle(40, 10, 130, 35);
	
	/* These are the initial coordinates of term 2 group - this will keep on changing and hold the latest group
	 * Once a new group is loaded, this will be reset to initial values
	 * Initial y =
	 * */
	private static Rectangle term2 = new Rectangle(210, 10, 130, 35);
	
	/* These are the initial coordinates of deleted term 2 group - this will keep on changing and hold the latest group
	 * Once a new group is loaded, this will be reset to initial values
	 * Initial y =
	 * */
	private static Rectangle contextRadio = new Rectangle(10, 20, 20, 15);
	
	/* These are the initial coordinates of frequency label - this will keep on changing and hold the latest group
	 * Once a new group is loaded, this will be reset to initial values
	 * Initial y =
	 * */
	private static Rectangle frequencyLabel = new Rectangle(370, 20, 35, 15);
	/* This HashMap will hold all the group info temporarily*/
	private static HashMap <String, CharacterGroupBean> groupInfo = new HashMap <String, CharacterGroupBean> ();
	/* This HashMap will hold all processed groups information */
	private static TreeMap <String, String> processedGroups = new TreeMap<String, String> ();	
	/* This table is for showing contextual sentences */
	private Table contextTable;
	/* This table holed currently processed groups */
	private Table processedGroupsTable;
	/* This will hold sorted order of each group of terms */
	private boolean [] sortedBy ;
	/* This is the sort label picture */
	private Label sortLabel;
	/*this is the co-occur frequency label*/
	private Label label; 
	/* This will all the groups removed edges from the graph */
	private static HashMap<String, ArrayList<String>> removedEdges 
		= new HashMap<String, ArrayList<String>>();
	/* This variable stores the number of groups produced initially. This is needed to check whether
	 * remaining terms group can be generated.
	 */
	private static int noOfTermGroups = 0;
	/* Show information to the user after the first time pop up message while grouping the remaining terms */
	private static boolean charStatePopUp = true;
	

	//static String [] glossprefixes = null;
	//(1, 'Plant'),
	//(2, 'Hymenoptera'),
	//(3, 'Algea')
	//(4, 'Porifera')
	//(5, 'Fossil')
	//load glossaries
	static String [] glossprefixes = new String[]{"Plant", "Hymenoptera", "Algea", "Porifera", "Fossil", "Nematodes"};//in this order

	private Text projectDirectory;
	
	private CLabel StepsToBeCompletedLbl;
	
	private CLabel label_prjDir;
	private CLabel label_dsprefix;
	private CLabel label_glossary;
	private CLabel lblForProject;
	private CLabel lblWithDatasetPrefix;
	private CLabel lblAndGlossary;
	private CLabel lblStepsToBe;
	private Text tab5desc;
	private Text text_1;
	/*Display colordisplay = new Display();

	Color red = colordisplay.getSystemColor(SWT.COLOR_RED);
	Color green = colordisplay.getSystemColor(SWT.COLOR_GREEN);*/
	//Color red;
	//Color white;
	//Color green;
	public static Color grey;
	private Text step3_desc;
	private Text tab2desc; 	   
	private Text tab3desc;
	/*context for structure terms and descriptor terms for step 4/tab 5*/
	//private StyledText structureContextText;
	//private StyledText moreStructureContextText;
	//private StyledText descriptorContextText;
	//private StyledText moreDescriptorContextText;
	//private StyledText othersContextText;
	
	ArrayList<String> removedTags = new ArrayList<String>();// used to remove descriptors marked red
	//ArrayList<String> descriptorsToSaveList = new ArrayList<String>();// used to save the descriptors that are marked green
	//ArrayList<Integer> descriptorsToSaveIndexList = new ArrayList<Integer>();// used to save the descriptors index that are marked green
	
	private Text tab6desc;
	private Text step6_desc;
	private Text txtThisLastStep;
	private Composite currentTermRoleMatrix;
	private Composite termRoleMatrix4structures;
	private ScrolledComposite scrolledComposite4structures;
	private StyledText contextText4structures;
	private Composite termRoleMatrix4characters;
	private ScrolledComposite scrolledComposite4characters;
	private StyledText contextText4characters;
	private Composite termRoleMatrix4others;
	private ScrolledComposite scrolledComposite4others;
	private StyledText contextText4others;
	protected String[] categories; //used to populate decision list combo in step 6.
	private boolean unpaired;
	private Composite composite4structures;
	private Group group4structures;
	private Composite composite4characters;
	private Group group4characters;
	private Composite composite4others;
	private Group group4others;
	protected UUID lastSavedIdS; //= Utilities.getLastSavedId ("structure"); //UUID.randomUUID();
	protected UUID lastSavedIdC; //= Utilities.getLastSavedId ("character"); //UUID.randomUUID();
	protected UUID lastSavedIdO;// = Utilities.getLastSavedId ("other"); //UUID.randomUUID();
	private static boolean markupstarted = false;
	private static Group grpTermSets;
	
	/*the set of threads that would run off the mainform*/
	static VolumeExtractor ve = null;
	static VolumeVerifier vv = null;
	static VolumeTransformer vt = null;
	static Type3Transformer preMarkUp = null;
	static Type2Transformer transformer2 = null;
	static Type4Transformer transformer4 = null;
	static VolumeDehyphenizer vd = null;
	static VolumeFinalizer vf = null;
	
	/*the set of flag to control step 4*/
	private boolean runperl = false;
	private boolean fourdotoneload = false;
	private boolean fourdottwoload = false;
	private boolean fourdotthreeload = false; 
	private boolean fourdotonesave = false;
	private boolean fourdottwosave = false;
	private boolean fourdotthreesave = false;
	
	//snap shot of term categorization subtabs
	private Hashtable<String, String> categorizedTermsOnStructureTab = new Hashtable<String, String>();
	private Hashtable<String, String> categorizedTermsOnCharacterTab = new Hashtable<String, String>();
	private Hashtable<String, String> categorizedTermsOnOthersTab = new Hashtable<String, String>();
	/*variable to select a transformer for type1 doc*/
	private static String startupstring = null;
	//////////////////methods///////////////////////
	
	/////////////////display application window/////
	
	public static void main(String[] args) {
		launchMarker("");
	}
	
	public static void launchMarker(String type) {
		try {
			MainForm window = new MainForm();
			String[] info = type.split(":");
			window.type = info[0] ;
			if(window.type.compareTo("type4")==0 && info.length>1){
				window.setType4XML(info[1]);
			}
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
	}
	
	/**
	 * Open the window
	 */
	public void open() throws Exception {
		final Display display = Display.getDefault();
	   /* DeviceData data = new DeviceData();
	    data.tracking = true;
	    final Display display = new Display(data);
	    Sleak sleak = new Sleak();
	    sleak.open();*/
	    
		 //red = display.getSystemColor(SWT.COLOR_RED);
		 //green = display.getSystemColor(SWT.COLOR_GREEN);
		 grey = display.getSystemColor(SWT.COLOR_GRAY);
		
		
		createContents(display);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		if(shell.isDisposed()) {
			shell.dispose();
			if(VolumeMarkup.p != null)	VolumeMarkup.p.destroy(); //kill perl too.
			try{
				if(this.conn!=null) this.conn.close();
			}catch(Exception e){
				StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
			}
			System.exit(0);
		}
	}

	/**
	 * Create contents of the application window
	 */
	protected void createContents(Display display) throws Exception{
		shell = new Shell(display);
		shell.setImage(SWTResourceManager.getImage(MainForm.class, "/edu/arizona/sirls/biosemantics/parsing/garland_logo.gif"));
		shell.setSize(843, 614);
		shell.setLocation(200, 100);
		shell.setText(ApplicationUtilities.getProperty("application.name"));
		
		
		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 803, 543);
		
		tabFolder.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				System.out.println();
				
			}
			//Logic for tab access goes here
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				// chk if values were loaded
				StringBuffer messageText = new StringBuffer();
				String tabName = arg0.item.toString();
				tabName = tabName.substring(tabName.indexOf("{")+1, tabName.indexOf("}"));
	
				/****** Logic for tab access goes here*******/
				//if status is true  - u can go to the next tab, else don't even think! 
				// For general tab
				if (configurationText == null ) return;
				if(tabName.indexOf(
						ApplicationUtilities.getProperty("tab.one.name")) == -1 && statusOfMarkUp[0] 
						      && !saveFlag)  {
					// inform the user that he needs to load the information for starting mark up
					// focus back to the general tab
					checkFields(messageText, tabFolder);
					return;
				}
				if (combo.getText().equals("")) {
					checkFields(messageText, tabFolder);
					return;
				}
				//show pop up to inform the user
				if(statusOfMarkUp[0]) {
					if(!saveFlag) {
						ApplicationUtilities.showPopUpWindow(
								ApplicationUtilities.getProperty("popup.info.prefix.save"), 
								ApplicationUtilities.getProperty("popup.header.info"), SWT.ICON_INFORMATION);
						saveFlag = true;
					}

					try {
						int option_chosen = getType(type);
						mainDb.savePrefixData(dataPrefixCombo.getText().replaceAll("-", "_").trim(),getGlossary(glossaryPrefixCombo.getText().trim()),option_chosen);
						
					} catch (Exception e) {
						StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					}
				 }
				/* type="" means type 1, which has segmentation and verification steps*/
				if (type.equals("")){
					//segmentation
					if (!statusOfMarkUp[1]) {
						if(!tabName.equals(ApplicationUtilities.getProperty("tab.one.name"))
								&& !tabName.equals(ApplicationUtilities.getProperty("tab.two.name"))) {
							ApplicationUtilities.showPopUpWindow( 
									ApplicationUtilities.getProperty("popup.error.tab")+ " " +
									ApplicationUtilities.getProperty("tab.two.name"), 
									ApplicationUtilities.getProperty("popup.header.error"),
									SWT.ICON_ERROR);
							tabFolder.setSelection(1);
							tabFolder.setFocus();
							return;
						}
					}
					//verification
					if (!statusOfMarkUp[2]) {
						if(!tabName.equals(ApplicationUtilities.getProperty("tab.one.name"))
								&& !tabName.equals(ApplicationUtilities.getProperty("tab.two.name"))
								&& !tabName.equals(ApplicationUtilities.getProperty("tab.three.name"))) {
							ApplicationUtilities.showPopUpWindow(								
									ApplicationUtilities.getProperty("popup.error.tab")+ " " +
									ApplicationUtilities.getProperty("tab.three.name"), 
									ApplicationUtilities.getProperty("popup.header.error"),
									SWT.ICON_ERROR);
							tabFolder.setSelection(2);
							tabFolder.setFocus();
							return;
							
						}

					}
				}
                /*the following applies for all types*/
				//Transformation
				if (!statusOfMarkUp[3]) {
					if(!tabName.equals(ApplicationUtilities.getProperty("tab.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.three.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.four.name"))) {
						ApplicationUtilities.showPopUpWindow(								
								ApplicationUtilities.getProperty("popup.error.tab")+ " " +
								ApplicationUtilities.getProperty("tab.four.name"), 
								ApplicationUtilities.getProperty("popup.header.error"),
								SWT.ICON_ERROR);
						if (type.equals("")){
							tabFolder.setSelection(3);
						} else {
							tabFolder.setSelection(1);
						}
						
						tabFolder.setFocus();
						return;							
					}

				}
				// Mark Up, including 4 substeps
				if (!statusOfMarkUp[4] || !statusOfMarkUp[5] || !statusOfMarkUp[6] || !statusOfMarkUp[7]) {
					if(!tabName.equals(ApplicationUtilities.getProperty("tab.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.three.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.four.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.name"))) {
						ApplicationUtilities.showPopUpWindow(								
								ApplicationUtilities.getProperty("popup.error.tab")+ " " +
								ApplicationUtilities.getProperty("tab.five.name") + " and review all the terms. ", 
								ApplicationUtilities.getProperty("popup.header.error"),
								SWT.ICON_ERROR);
						if (type.equals("")){
							tabFolder.setSelection(4);
						} else {
							tabFolder.setSelection(2);
						}
						tabFolder.setFocus();
						return;
					}
					

				}
				/*
				// Mark Up
				if (!statusOfMarkUp[4]) {
					if(!tabName.equals(ApplicationUtilities.getProperty("tab.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.three.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.four.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.perl.name"))) {
						ApplicationUtilities.showPopUpWindow(								
								ApplicationUtilities.getProperty("popup.error.tab")+ " " +
								ApplicationUtilities.getProperty("tab.five.name"), 
								ApplicationUtilities.getProperty("popup.header.error"),
								SWT.ICON_ERROR);
						if (type.equals("")){
							tabFolder.setSelection(4);
						} else {
							tabFolder.setSelection(2);
						}
						tabFolder.setFocus();
						return;
					}


				}
				
				// Mark Up
				if (!statusOfMarkUp[5]) {
					if(!tabName.equals(ApplicationUtilities.getProperty("tab.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.three.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.four.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.perl.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.one.name"))) {
						ApplicationUtilities.showPopUpWindow(								
								ApplicationUtilities.getProperty("popup.error.tab")+ " " +
								ApplicationUtilities.getProperty("tab.five.one.name"), 
								ApplicationUtilities.getProperty("popup.header.error"),
								SWT.ICON_ERROR);
						if (type.equals("")){
							tabFolder.setSelection(4);
						} else {
							tabFolder.setSelection(2);
						}
						tabFolder.setFocus();
						return;
					}
					

				}
				
				// Mark Up
				if (!statusOfMarkUp[6]) {
					if(!tabName.equals(ApplicationUtilities.getProperty("tab.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.three.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.four.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.perl.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.two.name"))) {
						ApplicationUtilities.showPopUpWindow(								
								ApplicationUtilities.getProperty("popup.error.tab")+ " " +
								ApplicationUtilities.getProperty("tab.five.two.name"), 
								ApplicationUtilities.getProperty("popup.header.error"),
								SWT.ICON_ERROR);
						if (type.equals("")){
							tabFolder.setSelection(4);
						} else {
							tabFolder.setSelection(2);
						}
						tabFolder.setFocus();
						return;
					}
					

				}
				
				// Mark Up
				if (!statusOfMarkUp[7]) {
					if(!tabName.equals(ApplicationUtilities.getProperty("tab.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.three.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.four.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.perl.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.three.name"))) {
						ApplicationUtilities.showPopUpWindow(								
								ApplicationUtilities.getProperty("popup.error.tab")+ " " +
								ApplicationUtilities.getProperty("tab.five.three.name"), 
								ApplicationUtilities.getProperty("popup.header.error"),
								SWT.ICON_ERROR);
						if (type.equals("")){
							tabFolder.setSelection(4);
						} else {
							tabFolder.setSelection(2);
						}
						tabFolder.setFocus();
						return;
					}
					

				}
				
				
				*/
				
				
				
				
				
				
				
				
				
				
				
				//Unknown Removal
				//if (!statusOfMarkUp[5]) {
				if (!statusOfMarkUp[8]) {
					if(!tabName.equals(ApplicationUtilities.getProperty("tab.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.three.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.four.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.perl.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.three.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.six.name"))) {
						ApplicationUtilities.showPopUpWindow(								
								ApplicationUtilities.getProperty("popup.error.tab")+ " " +
								ApplicationUtilities.getProperty("tab.six.name"), 
								ApplicationUtilities.getProperty("popup.header.error"),
								SWT.ICON_ERROR);
						if (type.equals("")){
							tabFolder.setSelection(5);
						} else {
							tabFolder.setSelection(3);
						}
						tabFolder.setFocus();						
						return;
					}

				}
				//Finalizer//character
				//if (!statusOfMarkUp[6]) {
				if (!statusOfMarkUp[9]) {	
					if(!tabName.equals(ApplicationUtilities.getProperty("tab.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.three.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.four.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.perl.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.one.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.two.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.five.three.name"))
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.six.name"))
							/*&& !tabName.equals(ApplicationUtilities.getProperty("tab.seven.name"))*/
							&& !tabName.equals(ApplicationUtilities.getProperty("tab.character"))
					) {
						ApplicationUtilities.showPopUpWindow(								
								ApplicationUtilities.getProperty("popup.error.tab")+ " " +
								ApplicationUtilities.getProperty("tab.character"), 
								ApplicationUtilities.getProperty("popup.header.error"),
								SWT.ICON_ERROR);
						if (type.equals("")){
							tabFolder.setSelection(6);
						} else {
							tabFolder.setSelection(4);
							loadCharacterTab();
						}
						tabFolder.setFocus();
						return;
					}

				}

     			//changed from '6' to '5'
				//if (statusOfMarkUp[5]) {//passed tab 6 (step5),landed on tab 7 (step6)
				if (statusOfMarkUp[8]) {
					if(tabName.equals(ApplicationUtilities.getProperty("tab.character"))) loadCharacterTab();
				}

			}
			

			private void loadCharacterTab() {
					charDb = new CharacterStateDBAccess(dataPrefixCombo.getText().replaceAll("-", "_").trim(), getGlossary(glossaryPrefixCombo.getText().trim()));
					// set the decisions combo
					categories = setCharacterTabDecisions();
					if(categories.length>0){
						comboDecision.setItems(categories);
						comboDecision.setText(categories[0]);
					}else{//Use ant glossary to populate the table in step 6.
						categories = setDefaultCharacterTabDecisions();
						comboDecision.setItems(categories);
						comboDecision.setText(categories.length==0? "" : categories[0]);
						//the fixed string[]
					}
					//reset the table to clear previous grouping decisions
					Statement stmt = null;
					try{
						if(conn == null){
							Class.forName(ApplicationUtilities.getProperty("database.driverPath"));
							conn = DriverManager.getConnection(ApplicationUtilities.getProperty("database.url"));
						}
						stmt = conn.createStatement();
						stmt.execute("drop table if exists "+dataPrefixCombo.getText().replaceAll("-", "_").trim()+"_group_decisions");
						stmt.execute("create table if not exists "+dataPrefixCombo.getText().replaceAll("-", "_").trim()+"_group_decisions (groupId int, category varchar(200), primary key(groupId))");
					}catch (Exception e){
						StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
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
					
					processedGroups.clear();
					// set the groups list
					setCharactertabGroups();
					// show the terms that co-occured in the first group
					loadTerms();
					//Clear context table;
					contextTable.removeAll();
					//load processed groups table;
					loadProcessedGroups();
					//createRemainingTermsGroup();
			}
			
		});
		
		
		
		/////////////////////////////////////////////////
		/////////////general tab (tab1) /////////////////
		/////////////////////////////////////////////////
		
		generalTabItem = new TabItem(tabFolder, SWT.NONE);
		generalTabItem.setText(ApplicationUtilities.getProperty("tab.one.name"));

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		generalTabItem.setControl(composite);

		targetText = new Text(composite, SWT.BORDER);
		configurationText = new Text(composite, SWT.BORDER);
		sourceText = new Text(composite, SWT.BORDER);
		
		//final Group configurationDirectoryGroup_1_1_1 = new Group(composite, SWT.NONE);
		//configurationDirectoryGroup_1_1_1.setEnabled(false);
		//configurationDirectoryGroup_1_1_1.setBounds(20, 117, 748, 70);
		//configurationDirectoryGroup_1_1_1.setText(
		//		ApplicationUtilities.getProperty("datasetprefix"));
		//configurationDirectoryGroup_1_1_1.setVisible(false);
		// get value from the project conf and set it here
		List <String> datasetPrefixes = new ArrayList <String> (); 
		mainDb.datasetPrefixRetriever(datasetPrefixes);
		String [] prefixes = new String [datasetPrefixes.size()];
		int loopCount = 0;
		for (String s : datasetPrefixes) {
			prefixes [loopCount] = s;
			loopCount++;
		}
		
		//(1, 'Plant'),
		//(2, 'Hymenoptera'),
		//(3, 'Algea')
		//(4, 'Porifera')
		//(5, 'Fossil')
		//load glossaries
		//glossprefixes = new String[]{"Plant", "Hymenoptera", "Algea", "Porifera", "Fossil"};//in this order
		
		/*List <String> glossaryPrefixes = new ArrayList <String> (); 
		
		mainDb.glossaryPrefixRetriever(glossaryPrefixes);
		glossprefixes = new String [glossaryPrefixes.size()];
		int glossCount = 0;
		for (String s : glossaryPrefixes) {
			glossprefixes [glossCount] = s;
			glossCount++;
		}*/
		
		CLabel lblSelectA = new CLabel(composite, SWT.NONE);
		lblSelectA.setBounds(20, 32, 288, 21);
		lblSelectA.setText(ApplicationUtilities.getProperty("labelSelectProject"));
		
		projectDirectory = new Text(composite, SWT.BORDER);
		projectDirectory.setToolTipText(ApplicationUtilities.getProperty("chooseDirectoryTooltip"));
		projectDirectory.setBounds(33, 60, 586, 21);
		
		final Button browseConfigurationButton = new Button(composite, SWT.NONE);
		browseConfigurationButton.setBounds(624, 60, 100, 23);
		browseConfigurationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				browseConfigurationDirectory(); // browse the configuration directory
			}
		});
		browseConfigurationButton.setText(ApplicationUtilities.getProperty("browseProjectBtn"));
		browseConfigurationButton.setToolTipText(ApplicationUtilities.getProperty("browseProjectTTT"));
				
		Group grpCreateANew = new Group(composite, SWT.NONE);
		grpCreateANew.setText("Create a new project :");
		grpCreateANew.setBounds(10, 10, 773, 250);
		
		grpTermSets = new Group(composite, SWT.NONE);
		grpTermSets.setText("Term sets ready for use :");
		grpTermSets.setBounds(10, 270, 773, 200);	
				
		final Button saveProjectButton = new Button(grpCreateANew, SWT.NONE);
		saveProjectButton.setBounds(655, 210, 100, 23);
		saveProjectButton.setText(ApplicationUtilities.getProperty("saveProjectBtn"));
		saveProjectButton.setToolTipText(ApplicationUtilities.getProperty("saveProjectTTT"));
		saveProjectButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (checkFields(new StringBuffer(), tabFolder)) {
					return;
				}
				saveProject(); 
				saveFlag = false;
				if(uploadTerm.getSelection()) upload2OTO = true;
				if(generateMatrix.getSelection()) createMatrix = true;
				if(containIndexedParts.getSelection()) containindexedparts = true;
				try {
					int option_chosen =getType(type); 
					mainDb.savePrefixData(dataPrefixCombo.getText().replaceAll("-", "_").trim(),getGlossary(glossaryPrefixCombo.getText().trim()),option_chosen);
					mainDb.loadStatusOfMarkUp(statusOfMarkUp, combo.getText());
					//mainDb.createNonEQTable();
					//see if there is reviewed term set for downloading
					//clean up existing messages
					Control[] controls = grpTermSets.getChildren();
					for(Control control : controls){
						control.dispose();
					}			
					//display new messages
					downloadConfirmedTermsFromOTO(dataPrefixCombo.getText().replaceAll("-", "_").trim());	
				} catch (Exception exe) {
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);
					exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					
				}
				//String messageHeader = ApplicationUtilities.getProperty("popup.header.info");
				//String message = ApplicationUtilities.getProperty("popup.info.saved");				
				//ApplicationUtilities.showPopUpWindow(message, messageHeader, SWT.ICON_INFORMATION);								
			}
		});

		//dataPrefix combo										
		combo = new Combo(grpCreateANew, SWT.NONE);
		combo.setBounds(23, 109, 138, 23);
		combo.setToolTipText(ApplicationUtilities.getProperty("application.dataset.instruction"));
		dataPrefixCombo = combo;
		combo.setItems(prefixes);
														
		CLabel label = new CLabel(grpCreateANew, SWT.NONE);
		label.setBounds(23, 80, 760, 21);
		label.setText(ApplicationUtilities.getProperty("datasetprefix"));
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent me) {
				 if (combo.getText().trim().equals("")) {
					saveFlag = false;
				}
			}
		});
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				try {
					mainDb.loadStatusOfMarkUp(statusOfMarkUp, combo.getText());
				} catch (Exception exe) {
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);
					exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
				/* remove the deleted edges graph if a new prefix is selected*/
				removedEdges.clear();
			}
		});

		//glossary combo
		Combo glossaryCombo = new Combo(grpCreateANew, SWT.NONE);
		glossaryCombo.setBounds(199, 109, 138, 23);
		glossaryCombo.setItems(glossprefixes);
		glossaryPrefixCombo = glossaryCombo;

		//contain indexed part: rib 5 => the fifth rib
		containIndexedParts = new Button(grpCreateANew, SWT.CHECK);
		containIndexedParts.setBounds(23, 140, 730, 23);
		containIndexedParts.setText(ApplicationUtilities.getProperty("ContainIndexedParts"));
		containIndexedParts.setSelection(false);
		
		/*Label part2 = new Label(grpCreateANew, SWT.NONE);
		part2.setBounds(50, 225, 700, 23);
		part2.setText(ApplicationUtilities.getProperty("ContainIndexedParts2"));*/
		
	
		//upload terms to OTO checkbox
		uploadTerm = new Button(grpCreateANew, SWT.CHECK);
		uploadTerm.setBounds(23, 170, 700, 23);
		uploadTerm.setText(ApplicationUtilities.getProperty("upload2OTO"));
		uploadTerm.setSelection(false);
		
		//upload terms to OTO checkbox
		generateMatrix = new Button(grpCreateANew, SWT.CHECK);
		generateMatrix.setBounds(23, 200, 600, 23);
		generateMatrix.setText(ApplicationUtilities.getProperty("generateMatrix"));
		generateMatrix.setSelection(false);
		generateMatrix.setEnabled(false);
	
		/*controls for reloading and resuming the last project */
/*		Group grpContinueWithThe = new Group(composite, SWT.NONE);
		grpContinueWithThe.setToolTipText("Continue with the last project");
		grpContinueWithThe.setText("Continue with the last project");
		grpContinueWithThe.setBounds(20, 264, 763, 144);
						
		final Button loadProjectButton = new Button(grpContinueWithThe, SWT.NONE);
		loadProjectButton.setBounds(634, 99, 120, 23);
		loadProjectButton.setToolTipText(ApplicationUtilities.getProperty("loadLastProjectTTT"));
		loadProjectButton.setText(ApplicationUtilities.getProperty("loadLastProjectBtn"));
		loadProjectButton.addSelectionListener(new SelectionAdapter() {
			private TabFolder markupNReviewTabFolder;

			public void widgetSelected(final SelectionEvent e){
				//make all labels in this group "grpContinueWithThe" visible
				lblForProject.setVisible(true);
				lblWithDatasetPrefix.setVisible(true);
				lblAndGlossary.setVisible(true);
				label_prjDir.setVisible(true);
				label_dsprefix.setVisible(true);
				label_glossary.setVisible(true);
				lblStepsToBe.setVisible(true);
		
				loadProject();
				//step1 description
				if(tab2desc!=null){
					tab2desc.setText(ApplicationUtilities.getProperty("step1DescpPart1")+Registry.TargetDirectory.concat(ApplicationUtilities.getProperty("EXTRACTED"))+ ApplicationUtilities.getProperty("step1DescpPart2") +Registry.SourceDirectory+ApplicationUtilities.getProperty("step1DescpPart3"));
				}
				if(tab3desc!=null){
					tab3desc.setText(ApplicationUtilities.getProperty("step2DescpPart1")+Registry.TargetDirectory.concat(ApplicationUtilities.getProperty("EXTRACTED"))+ApplicationUtilities.getProperty("step2DescpPart2"));
				}
				// code for setting the text of the combo to the last accessed goes here 
				try {
					int option_chosen = getType(type);
					String t = mainDb.getLastAccessedDataSet(option_chosen);
					String prefix = t==null? "" : t;
					int index= t.indexOf("|");
					String glossaryName="";
					if(index != -1)
					{
						prefix=t.substring(0,index);
						glossaryName=t.substring(index+1);
						
					}
					MainForm.dataPrefixCombo.setText(prefix);
					label_dsprefix.setText(prefix);
					
					mainDb.loadStatusOfMarkUp(statusOfMarkUp, combo.getText());
					//mainDb.saveStatus("general", combo.getText(), true);
					statusOfMarkUp[0] = true;
					//display all remaining steps:
					StringBuffer remainingSteps= new StringBuffer();
					int i=3;
					if(type.trim().equalsIgnoreCase(""))
						i=1;
					boolean notComplete=false;
					for(;i<statusOfMarkUp.length;i++)
					{
						if(statusOfMarkUp[i])
						{
							notComplete=true;
							remainingSteps.append("Step "+i+",");
							// try loading all those steps..
							
								if(!StepsToBeCompletedLbl.getVisible())
									StepsToBeCompletedLbl.setVisible(true);
						}
						else{
						if(i==1)
							loadFileInfo(extractionTable, Registry.TargetDirectory + 
									ApplicationUtilities.getProperty("EXTRACTED"));
							if(i==2)
							loadFileInfo(verificationTable, Registry.TargetDirectory + 
									ApplicationUtilities.getProperty("EXTRACTED"));
							if(i==3)
							loadFileInfo(transformationTable, Registry.TargetDirectory + 
									ApplicationUtilities.getProperty("TRANSFORMED"));
						}		
					}
					if(!notComplete)
					{
						remainingSteps.append(ApplicationUtilities.getProperty("stepsComplete"));
						if(!StepsToBeCompletedLbl.getVisible())
							StepsToBeCompletedLbl.setVisible(true);
					}
					if(remainingSteps!=null && remainingSteps.length()>0){
					
						String remainingStepsLb =	remainingSteps.substring(0,remainingSteps.length()-1);
						StepsToBeCompletedLbl.setText(remainingStepsLb);
						
					}
					//else
					//{
					//	StepsToBeCompletedLbl.setText("All steps are complete from this project.");
					//}
					
					
					if(glossprefixes!=null && glossprefixes.length>0)
					MainForm.glossaryPrefixCombo.setText(glossaryName);
					label_glossary.setText(glossaryName);
					
				} catch (Exception ex) {
					LOGGER.error("Error Setting focus", ex);
					ex.printStackTrace();
				} 
				
				//load step4 here
				if(!statusOfMarkUp[4]){
					createSubtab(markupNReviewTabFolder, "structures",composite4structures,group4structures, scrolledComposite4structures, termRoleMatrix4structures, contextText4structures);
					createSubtab(markupNReviewTabFolder, "characters", composite4characters,group4characters, scrolledComposite4characters, termRoleMatrix4characters, contextText4characters);
					createSubtab(markupNReviewTabFolder, "others", composite4others,group4others, scrolledComposite4others, termRoleMatrix4others, contextText4others);
					//loadFindStructureTable();
					//loadFindDescriptorTable();
					//loadFindMoreStructureTable();
					//loadFindMoreDescriptorTable();
					//loadOthersTable();
					
				}
				
				if(!statusOfMarkUp[5])//unknown removal
				{
					loadTags(tabFolder);
					//should not rerun character grouping, should load results from terms table. Hong TODO 5/23/11
					//set the decisions combo
					//setCharacterTabDecisions();
					processedGroups.clear();
					//set the groups list
					setCharactertabGroups();
					// show the terms that co-occured in the first group
					loadTerms();
					//Clear context table;
					contextTable.removeAll();
					//load processed groups table;
					loadProcessedGroups();
				}
				if(!statusOfMarkUp[6])
				{
					loadFileInfo(finalizerTable, Registry.TargetDirectory + 
							ApplicationUtilities.getProperty("FINAL"));
				}
				
			}

			//protected void loadFindStructureTable() {
				
			//	ArrayList <String> structures = new ArrayList<String>();
			//	try {
				//	VolumeMarkupDbAccessor vmdb = new VolumeMarkupDbAccessor(dataPrefixCombo.getText().replaceAll("-", "_").trim(),glossaryPrefixCombo.getText().trim());
					//words = vmdb.getDescriptorWords();
					//vmdb.structureTags4Curation(structures);
					//int count = 1;
					//if (structures != null) {
					//	for (String word : structures){
					//		TableItem item = new TableItem(findStructureTable, SWT.NONE);
//							item.setText(new String [] {count+"", word});
//							count++;
//						}
//					}
//					
//				} catch (Exception exe){
//					LOGGER.error("unable to load descriptor tab in Markup : MainForm", exe);
//					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
//				}
//			
//			
//			}

//			protected void loadOthersTable() {
//				// TODO Auto-generated method stub
//				//showOtherTerms();
//				showOtherTermsTable();
//			}

//			protected void loadFindDescriptorTable() {
//				// TODO Auto-generated method stub
//				ArrayList <String> words = null;
//				try {
//					VolumeMarkupDbAccessor vmdb = new VolumeMarkupDbAccessor(dataPrefixCombo.getText().replaceAll("-", "_").trim(),glossaryPrefixCombo.getText().trim());
//					words = vmdb.getSavedDescriptorWords();
//					//words = vmdb.getDescriptorWords();
//					
//					int count = 1;
//					if (words != null) {
//						for (String word : words){
//							TableItem item = new TableItem(findDescriptorTable, SWT.NONE);
//							item.setText(new String [] {count+"", word});
//							count++;
//						}
//					}
//					
//				} catch (Exception exe){
//					LOGGER.error("unable to load descriptor tab in Markup : MainForm", exe);
//					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
//				}
//			
//			}

			}
		);
		
		this.lblStepsToBe = new CLabel(grpContinueWithThe, SWT.NONE);
		lblStepsToBe.setVisible(false);
		lblStepsToBe.setBounds(10, 84, 162, 21);
		lblStepsToBe.setText(ApplicationUtilities.getProperty("stepsTobeCompletedLbl"));
						
		this.StepsToBeCompletedLbl = new CLabel(grpContinueWithThe, SWT.NONE);
		this.StepsToBeCompletedLbl.setBounds(198, 84, 416, 21);
		this.StepsToBeCompletedLbl.setText("");
		this.StepsToBeCompletedLbl.setVisible(false);
		
		this.lblForProject= new CLabel(grpContinueWithThe, SWT.NONE);
		lblForProject.setBounds(10, 25, 224, 21);
		lblForProject.setText(ApplicationUtilities.getProperty("loadPrjLbl"));
		lblForProject.setVisible(false);
		
		this.label_prjDir= new CLabel(grpContinueWithThe, SWT.NONE);
		label_prjDir.setBounds(240, 25, 247, 21);
		label_prjDir.setText("");
		
		this.lblWithDatasetPrefix= new CLabel(grpContinueWithThe, SWT.NONE);
		lblWithDatasetPrefix.setBounds(10, 52, 113, 21);
		lblWithDatasetPrefix.setText(ApplicationUtilities.getProperty("datasetLbl"));
		lblWithDatasetPrefix.setVisible(false);
		
		this.label_dsprefix = new CLabel(grpContinueWithThe, SWT.NONE);
		label_dsprefix.setBounds(129, 52, 128, 21);
		label_dsprefix.setText("");
		
		this.lblAndGlossary = new CLabel(grpContinueWithThe, SWT.NONE);
		lblAndGlossary.setBounds(263, 52, 79, 21);
		lblAndGlossary.setText(ApplicationUtilities.getProperty("glossaryLbl"));
		lblAndGlossary.setVisible(false);
		
		this.label_glossary= new CLabel(grpContinueWithThe, SWT.NONE);
		label_glossary.setBounds(348, 52, 137, 21);
		label_glossary.setText("");
		//initialized variables to hold three filepaths when save project				
		targetText = new Text(composite, SWT.BORDER);
		configurationText = new Text(composite, SWT.BORDER);
		sourceText = new Text(composite, SWT.BORDER);
		//targetText.setBounds(20, 220, 618, 23);
		//targetText.setEditable(false);
		//targetText.setVisible(false);
								
		//final Group configurationDirectoryGroup_1 = new Group(composite, SWT.NONE);
		//configurationDirectoryGroup_1.setBounds(20, 216, 763, 70);
		//configurationDirectoryGroup_1.setText(ApplicationUtilities.getProperty("source"));
		//configurationDirectoryGroup_1.setVisible(false);
										
		//final Group configurationDirectoryGroup = new Group(configurationDirectoryGroup_1, SWT.NONE);
		//configurationDirectoryGroup.setBounds(-10, 46, 763, 70);
		//configurationDirectoryGroup.setText(ApplicationUtilities.getProperty("config"));
		//configurationDirectoryGroup.setVisible(false);
												
		//configurationText = new Text(configurationDirectoryGroup, SWT.BORDER);
		//configurationText.setEditable(false);
		//configurationText.setBounds(10, 25, 618, 23);
		//configurationText.setVisible(false);
														
		//sourceText = new Text(configurationDirectoryGroup, SWT.NONE);
		//sourceText.setBounds(20, 28, 618, 23);
		//sourceText.setEditable(false);
		
		//final Group configurationDirectoryGroup_1_1 = new Group(configurationDirectoryGroup_1, SWT.NONE);
		//configurationDirectoryGroup_1_1.setBounds(10, 10, 763, 70);
		//configurationDirectoryGroup_1_1.setVisible(false);
		//configurationDirectoryGroup_1_1.setText(
		//		ApplicationUtilities.getProperty("target"));
				
*/		
		//////////////////////////////////////////////////////////////////////
		if (type.equals("")){//type 1
			/* Segmentation Tab step 1*/
			final TabItem segmentationTabItem = new TabItem(tabFolder, SWT.NONE);
			segmentationTabItem.setText(ApplicationUtilities.getProperty("tab.two.name"));
			
			final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
			segmentationTabItem.setControl(composite_1);
			
			//label giving the description for this step is::
			this.tab2desc = new Text(composite_1, SWT.READ_ONLY | SWT.WRAP);
			tab2desc.setBounds(10,10,784,60);
			tab2desc.setText(ApplicationUtilities.getProperty("step1DescpPart1")+ApplicationUtilities.getProperty("step1DescpPart2")+ApplicationUtilities.getProperty("step1DescpPart3"));

			extractionTable = new Table(composite_1, SWT.FULL_SELECTION | SWT.BORDER );
			extractionTable.setLinesVisible(true);
			extractionTable.setHeaderVisible(true);
			extractionTable.setBounds(10, 75, 784, 358);

			final TableColumn extractionNumberColumnTableColumn = new TableColumn(extractionTable, SWT.NONE);
			extractionNumberColumnTableColumn.setWidth(100);
			extractionNumberColumnTableColumn.setText(
					ApplicationUtilities.getProperty("count"));

			final TableColumn extractionFileColumnTableColumn = new TableColumn(extractionTable, SWT.NONE);
			extractionFileColumnTableColumn.setWidth(254);
			extractionFileColumnTableColumn.setText(
					ApplicationUtilities.getProperty("file"));

			extractionProgressBar = new ProgressBar(composite_1, SWT.NONE);
			extractionProgressBar.setVisible(false);
			extractionProgressBar.setBounds(10, 443, 400, 17);
			
			
			final Button startExtractionButton = new Button(composite_1, SWT.NONE);
			startExtractionButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					try {
						extractionTable.removeAll();
						startExtraction(); // start the extraction process
					} catch (Exception exe) {
						StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					}
					 
					// Saving the status of markup
					
					try {
						mainDb.saveStatus(ApplicationUtilities.getProperty("tab.two.name"), combo.getText(), true);
						statusOfMarkUp[1] = true;
					} catch (Exception exe) {
						StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					}
				}
			});
			startExtractionButton.setText(ApplicationUtilities.getProperty("step1RunBtn"));
			startExtractionButton.setToolTipText(ApplicationUtilities.getProperty("step1RunTTT"));
			startExtractionButton.setBounds(420, 440, 83, 23);
		
			/*Hyperlinking the files */
			extractionTable.addMouseListener(new MouseListener () {
				public void mouseDoubleClick(MouseEvent event) {
					String filePath = Registry.TargetDirectory + 
					ApplicationUtilities.getProperty("EXTRACTED")+"/"+
					extractionTable.getSelection()[0].getText(1).trim();
					
					try {
						Runtime.getRuntime().exec(ApplicationUtilities.getProperty("notepad") 
								+ " \"" + filePath + "\"");

					} catch (Exception e){
						ApplicationUtilities.showPopUpWindow(ApplicationUtilities.getProperty("popup.error.msg") +
								ApplicationUtilities.getProperty("popup.editor.msg"),
								ApplicationUtilities.getProperty("popup.header.error"), 
								SWT.ERROR);
					} 
				}			
				public void mouseDown(MouseEvent event) {}
				public void mouseUp(MouseEvent event) {}
			});
			
			Button btnLoad = new Button(composite_1, SWT.NONE);
			btnLoad.setBounds(510, 440, 173, 23);
			btnLoad.setText(ApplicationUtilities.getProperty("step1LoadBtn"));
			btnLoad.setToolTipText(ApplicationUtilities.getProperty("step1LoadTTT"));
			btnLoad.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					loadFileInfo(extractionTable, Registry.TargetDirectory + 
							ApplicationUtilities.getProperty("EXTRACTED"));
				}
			});
			
			Button btnClear = new Button(composite_1, SWT.NONE);
			btnClear.setBounds(695, 440, 100, 23);
			btnClear.setText(ApplicationUtilities.getProperty("ClearRerunBtn"));
			btnClear.setToolTipText(ApplicationUtilities.getProperty("ClearRerunTTT"));
			
			btnClear.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					extractionTable.removeAll();
					statusOfMarkUp[1] = false;
					try {
						startExtraction();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						mainDb.saveStatus(ApplicationUtilities.getProperty("tab.two.name"), combo.getText(), false);
						statusOfMarkUp[1] = true;
					} catch (Exception exe) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					}
				}
			});
						
			/* Verification Tab: step 2*/			
			
			final TabItem verificationTabItem = new TabItem(tabFolder, SWT.NONE);
			verificationTabItem.setText(ApplicationUtilities.getProperty("tab.three.name"));

			final Composite composite_2 = new Composite(tabFolder, SWT.NONE);
			verificationTabItem.setControl(composite_2);

			this.tab3desc = new Text(composite_2, SWT.READ_ONLY | SWT.WRAP);
			tab3desc.setBounds(10,10,744,60);
			tab3desc.setText(ApplicationUtilities.getProperty("step2DescpPart1")+ApplicationUtilities.getProperty("step2DescpPart2"));
			
			
			verificationTable = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
			verificationTable.setBounds(10, 75, 744, 369);
			verificationTable.setLinesVisible(true);
			verificationTable.setHeaderVisible(true);

			final TableColumn verificationStageColumnTableColumn = new TableColumn(verificationTable, SWT.NONE);
			verificationStageColumnTableColumn.setWidth(168);
			verificationStageColumnTableColumn.setText("Tasks");

			final TableColumn verificationFileColumnTableColumn = new TableColumn(verificationTable, SWT.NONE);
			verificationFileColumnTableColumn.setWidth(172);
			verificationFileColumnTableColumn.setText(ApplicationUtilities.getProperty("file"));

			final TableColumn verificationErrorColumnTableColumn = new TableColumn(verificationTable, SWT.NONE);
			verificationErrorColumnTableColumn.setWidth(376);
			verificationErrorColumnTableColumn.setText("Errors to be corrected");

			
			verificationTable.addMouseListener(new MouseListener () {
				public void mouseDoubleClick(MouseEvent event) {
					String filePath = Registry.TargetDirectory + 
					ApplicationUtilities.getProperty("EXTRACTED")+ "/" +
					verificationTable.getSelection()[0].getText(1).trim();
					if (filePath.indexOf("xml") != -1) {
						try {
							Runtime.getRuntime().exec(ApplicationUtilities.getProperty("notepad") 
									+ " \"" + filePath + "\"");
						} catch (Exception e){
							ApplicationUtilities.showPopUpWindow(ApplicationUtilities.getProperty("popup.error.msg") +
									ApplicationUtilities.getProperty("popup.editor.msg"),
									ApplicationUtilities.getProperty("popup.header.error"), 
									SWT.ERROR);
						}
					} 
				}			
				public void mouseDown(MouseEvent event) {}
				public void mouseUp(MouseEvent event) {}
			});

			
			verificationProgressBar = new ProgressBar(composite_2, SWT.NONE);
			verificationProgressBar.setVisible(false);
			verificationProgressBar.setBounds(10, 450, 515, 17);

			final Button startVerificationButton = new Button(composite_2, SWT.NONE);
			startVerificationButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					System.out.println("Starting!!");
					verificationTable.removeAll();
					startVerification(); // start the verification process

					try {
						mainDb.saveStatus(ApplicationUtilities.getProperty("tab.three.name"), combo.getText(), true);
						statusOfMarkUp[2] = true;
					} catch (Exception exe) {
						StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					}
				}
			});
			startVerificationButton.setBounds(543, 450, 95, 23);
			startVerificationButton.setText(ApplicationUtilities.getProperty("step2RunBtn"));
			startVerificationButton.setToolTipText(ApplicationUtilities.getProperty("step2RunTTT"));
			final Button clearVerificationButton = new Button(composite_2, SWT.NONE);
			clearVerificationButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					clearVerification();
					//statusOfMarkUp[2] = false;					
					try {
						startVerification(); // start the verification process						
						mainDb.saveStatus(ApplicationUtilities.getProperty("tab.three.name"), combo.getText(), false);
						statusOfMarkUp[2] = true;
					} catch (Exception exe) {
						StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					}
				}
			});
			clearVerificationButton.setBounds(648, 450, 110, 23);
			clearVerificationButton.setText(ApplicationUtilities.getProperty("ClearRerunBtn"));
			clearVerificationButton.setToolTipText(ApplicationUtilities.getProperty("ClearRerunTTT"));

			/*Button btnLoad_1 = new Button(composite_2, SWT.NONE);
			btnLoad_1.setBounds(543, 385, 75, 23);
			btnLoad_1.setText("Load");
			btnLoad_1.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					loadFileInfo(verificationTable, Registry.TargetDirectory + 
							ApplicationUtilities.getProperty("EXTRACTED"));
				}
			});*/
			
		}
		/*****end of type1-specific tabs****/
		/*****start all type shared tabs****/

		/* Transformation Tab: step 3*/
		final TabItem transformationTabItem = new TabItem(tabFolder, SWT.NONE);
		transformationTabItem.setText(ApplicationUtilities.getProperty("tab.four.name"));
		final Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		transformationTabItem.setControl(composite_3);
		
		step3_desc = new Text(composite_3, SWT.READ_ONLY | SWT.WRAP);
		step3_desc.setText(ApplicationUtilities.getProperty("step3DescpText"));
		step3_desc.setBounds(20, 10, 744, 62);
		
		transformationTable = new Table(composite_3, SWT.FULL_SELECTION | SWT.BORDER);
		transformationTable.setBounds(20, 89, 744, 369);
		transformationTable.setLinesVisible(true);
		transformationTable.setHeaderVisible(true);
		transformationTable.addMouseListener(new MouseListener () {
			public void mouseDoubleClick(MouseEvent event) {
				String filePath = Registry.TargetDirectory + 
				ApplicationUtilities.getProperty("TRANSFORMED")+ "/" +
				transformationTable.getSelection()[0].getText(1).trim();
				if (filePath.indexOf("xml") != -1) {
					try {
						Runtime.getRuntime().exec(ApplicationUtilities.getProperty("notepad") 
								+ " \"" + filePath + "\"");
					} catch (Exception e){
						ApplicationUtilities.showPopUpWindow(ApplicationUtilities.getProperty("popup.error.msg") +
								ApplicationUtilities.getProperty("popup.editor.msg"),
								ApplicationUtilities.getProperty("popup.header.error"), 
								SWT.ERROR);
					}
				} 
			}			
			public void mouseDown(MouseEvent event) {}
			public void mouseUp(MouseEvent event) {}
		});


		final TableColumn transformationNumberColumnTableColumn_1 = new TableColumn(transformationTable, SWT.NONE);
		transformationNumberColumnTableColumn_1.setWidth(168);
		transformationNumberColumnTableColumn_1.setText("File Count");

		final TableColumn transformationNameColumnTableColumn_1 = new TableColumn(transformationTable, SWT.NONE);
		transformationNameColumnTableColumn_1.setWidth(172);
		transformationNameColumnTableColumn_1.setText("File Links");

		final Button startTransformationButton = new Button(composite_3, SWT.NONE);
		startTransformationButton.setToolTipText("Run step 3");
		startTransformationButton.setBounds(547, 464, 90, 23);
		startTransformationButton.setText(ApplicationUtilities.getProperty("step3RunBtn"));
		startTransformationButton.setToolTipText(ApplicationUtilities.getProperty("step3RunTTT"));
		startTransformationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				transformationTable.removeAll();
				
				if(type.equals("")) {
					startTransformation(); // start the transformation process
				} 
				else if (type.equals("type2")) {
					startType2Transformation(); // When the doc selected is type 2
				}				
				else if (type.equals("type3")) {
					startType3Transformation(); // start pre-mark up process
				} else if (type.equals("type4")) {
					startType4Transformation(); // When the doc selected is type 4
				}
				
				try {
					mainDb.saveStatus(ApplicationUtilities.getProperty("tab.four.name"), combo.getText(), true);
					statusOfMarkUp[3] = true;
				} catch (Exception exe) {
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);
					exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
			}
		});
		/* Type 4 Transformation doesn't do anything other than listing source files : Doubtful*/
		//if (type.equals("type4")){
		//	startTransformationButton.setVisible(false);
		//}

		final Button clearTransformationButton = new Button(composite_3, SWT.NONE);
		clearTransformationButton.setToolTipText(ApplicationUtilities.getProperty("ClearRerunTTT"));
		clearTransformationButton.setBounds(647, 464, 110, 23);
		clearTransformationButton.setText(ApplicationUtilities.getProperty("ClearRerunBtn"));
		clearTransformationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {/*
				clearTransformation();				
				try {
					mainDb.saveStatus(ApplicationUtilities.getProperty("tab.four.name"), combo.getText(), false);
					statusOfMarkUp[3] = false;
				} catch (Exception exe) {
					LOGGER.error("Couldnt save status - transform" , exe);
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
			*/
				//commented above code to make it re-run

				transformationTable.removeAll();
				if(type.equals("")) {
					startTransformation(); // start the transformation process
				} 
				else if (type.equals("type2")) {
					startType2Transformation(); // When the doc selected is type 4
				}				
				else if (type.equals("type3")) {
					startType3Transformation(); // start pre-mark up process
				} else if (type.equals("type4")) {
					startType4Transformation(); // When the doc selected is type 4
				}
				try {
					mainDb.saveStatus(ApplicationUtilities.getProperty("tab.four.name"), combo.getText(), true);
					statusOfMarkUp[3] = true;
				} catch (Exception exe) {
					LOGGER.error("Couldnt save status - transform" , exe);
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
			}
		});

		transformationProgressBar = new ProgressBar(composite_3, SWT.NONE);
		transformationProgressBar.setVisible(false);
		transformationProgressBar.setBounds(10, 464, 524, 17);
		
		/*Button button = new Button(composite_3, SWT.NONE);
		button.setBounds(577, 464, 60, 23);
		button.setText("Load");*/

		
		/*CLabel label_2 = new CLabel(composite_3, SWT.NONE);
		label_2.setBounds(105, 38, 61, 21);
		label_2.setText("New Label");
		
		CLabel label_3 = new CLabel(composite_3, SWT.NONE);
		label_3.setBounds(105, 38, 238, 23);
		label_3.setText("New Label");*/
		/*button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				loadFileInfo(transformationTable, Registry.TargetDirectory + 
						ApplicationUtilities.getProperty("TRANSFORMED"));
			}
		});
		 */
		
		/* Mark Up Tab: this is fifth tab but is step 4 in annotation*/
		/*contains 5 subtabs: 1. Run unsupervised learning perl code */
		/*2-5: term curation subtabs, filtered by the glossarytable*/
		/*2: "Find Structures" from sentence tags, wordpos (p,s) table; save to WordRole */
		/*3: "Find Descriptors" from wordpos (b), save to WordRole*/
		/*4: "Find More Structures" from allwords (dhword), save to WordRole*/
		/*5: "Find More Descriptors" from unknownwords(dhword), save to WordRole*/
		final TabItem markupTabItem = new TabItem(tabFolder, SWT.NONE);
		markupTabItem.setText(ApplicationUtilities.getProperty("tab.five.name"));

		final Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		markupTabItem.setControl(composite_4);

		//final TabFolder markupNReviewTabFolder = new TabFolder(composite_4, SWT.NONE);
		final TabFolder markupNReviewTabFolder = new TabFolder(composite_4, SWT.NONE);
		markupNReviewTabFolder.setBounds(0, 0, 795, 515);
		
		markupNReviewTabFolder.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				System.out.println();
				
			}
			//Logic for subtab access goes here
			public void widgetSelected(SelectionEvent arg0) {
				String tabName = arg0.item.toString();
				tabName = tabName.substring(tabName.indexOf("{")+1, tabName.indexOf("}")).trim();
				
				if (tabName.length()>0 && !statusOfMarkUp[4] ) { //if perl has not been run, the only subtab allowed to access is perl among the four subtabs
					if(tabName.compareTo(ApplicationUtilities.getProperty("tab.five.perl.title"))!=0) {
						ApplicationUtilities.showPopUpWindow(								
								ApplicationUtilities.getProperty("popup.error.tab")+ " " +
								ApplicationUtilities.getProperty("tab.five.perl.title"), 
								ApplicationUtilities.getProperty("popup.header.error"),
								SWT.ICON_ERROR);
						markupNReviewTabFolder.setSelection(0);
						markupNReviewTabFolder.setFocus();
						return;
					}
				}
				//status records activities completed before.
				//if(tabName.length()>0 && !fourdotonesave && !fourdottwosave && !fourdotthreesave && !fourdotoneload && !fourdottwoload && !fourdotthreeload){ //create these tables before terms are being reviewed
				if(tabName.length()>0 && !statusOfMarkUp[5]&& !statusOfMarkUp[6]&& !statusOfMarkUp[7]){ //create these tables before terms are being reviewed
					mainDb.createWordRoleTable();//roles are: op for plural organ names, os for singular, c for character, v for verb
					mainDb.createNonEQTable();
					mainDb.createTyposTable();
				}
				
				//manage termRoleMatrix
				if(currentTermRoleMatrix!=null){
					//dispose current
					Control[] children = currentTermRoleMatrix.getChildren();
					for(Control child: children) child.dispose();
					//load termRoleMatrix for the new selection
					if(tabName.compareTo(ApplicationUtilities.getProperty("tab.five.one.name"))==0){
						reLoadTermArea(termRoleMatrix4structures, scrolledComposite4structures, contextText4structures, "structures");
					}else if(tabName.compareTo(ApplicationUtilities.getProperty("tab.five.two.name"))==0){
						reLoadTermArea(termRoleMatrix4characters, scrolledComposite4characters, contextText4characters, "characters");
					}else if(tabName.compareTo(ApplicationUtilities.getProperty("tab.five.three.name"))==0){
						reLoadTermArea(termRoleMatrix4others, scrolledComposite4others, contextText4characters, "others");
					}
					
				}
				if(tabName.compareTo(ApplicationUtilities.getProperty("tab.five.one.name"))==0) currentTermRoleMatrix = termRoleMatrix4structures;
				if(tabName.compareTo(ApplicationUtilities.getProperty("tab.five.two.name"))==0) currentTermRoleMatrix = termRoleMatrix4characters;
				if(tabName.compareTo(ApplicationUtilities.getProperty("tab.five.three.name"))==0) currentTermRoleMatrix = termRoleMatrix4others;

			}
		});
		
		TabItem tbtmPerlProgram = new TabItem(markupNReviewTabFolder, SWT.NONE);
		tbtmPerlProgram.setText(ApplicationUtilities.getProperty("tab.five.perl.title"));
		
		Composite composite_9 = new Composite(markupNReviewTabFolder, SWT.NONE);
		tbtmPerlProgram.setControl(composite_9);
		
		tab5desc = new Text(composite_9, SWT.READ_ONLY | SWT.WRAP);
		tab5desc.setToolTipText(ApplicationUtilities.getProperty("step4Descp"));
		tab5desc.setText(ApplicationUtilities.getProperty("step4Descp"));
		tab5desc.setEditable(false);
		tab5desc.setBounds(10, 10, 744, 99);
		
		/*"run perl" subtab*/
		markUpPerlLog = new Text(composite_9, SWT.WRAP | SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
		markUpPerlLog.setBounds(10, 113, 744, 250);
		markUpPerlLog.setEnabled(true);

		markupProgressBar = new ProgressBar(composite_9, SWT.NONE);
		markupProgressBar.setBounds(7, 434, 432, 17);
		markupProgressBar.setVisible(false);

		final Button startMarkupButton_1 = new Button(composite_9, SWT.NONE);
		startMarkupButton_1.setToolTipText(ApplicationUtilities.getProperty("step4RunTTT"));
		startMarkupButton_1.setBounds(545, 434, 91, 23);
		startMarkupButton_1.setText(ApplicationUtilities.getProperty("step4RunBtn"));
		startMarkupButton_1.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				if(!termRoleMatrix4structures.isDisposed()){
					termRoleMatrix4structures.setVisible(false);
					termRoleMatrix4characters.setVisible(false);
					termRoleMatrix4others.setVisible(false);
				}
				startMarkup();
				//runperl = true; //set to true by VolumeDehyphenizer.java
				fourdotoneload = false;
				fourdottwoload = false;
				fourdotthreeload = false; 
				fourdotonesave = false; 
				fourdottwosave = false; 
				fourdotthreesave = false;			
			}
		});
		
		/*3 subtabs*/
		composite4structures = new Composite(markupNReviewTabFolder, SWT.NONE);
		group4structures = new Group(composite4structures, SWT.NONE);
		scrolledComposite4structures = new ScrolledComposite(group4structures, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		termRoleMatrix4structures = new Composite(scrolledComposite4structures, SWT.NONE);
		contextText4structures = new StyledText(composite4structures, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL|SWT.H_SCROLL);
		
		composite4characters = new Composite(markupNReviewTabFolder, SWT.NONE);
		group4characters = new Group(composite4characters, SWT.NONE);
		scrolledComposite4characters = new ScrolledComposite(group4characters, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		termRoleMatrix4characters = new Composite(scrolledComposite4characters, SWT.NONE);
		contextText4characters = new StyledText(composite4characters, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL|SWT.H_SCROLL);

		composite4others = new Composite(markupNReviewTabFolder, SWT.NONE);
		group4others = new Group(composite4others, SWT.NONE);
		scrolledComposite4others = new ScrolledComposite(group4others, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		termRoleMatrix4others = new Composite(scrolledComposite4others, SWT.NONE);
		contextText4others = new StyledText(composite4others, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL|SWT.H_SCROLL);
		
		createSubtab(markupNReviewTabFolder, "structures",composite4structures,group4structures, scrolledComposite4structures, termRoleMatrix4structures, contextText4structures);
		createSubtab(markupNReviewTabFolder, "characters", composite4characters,group4characters, scrolledComposite4characters, termRoleMatrix4characters, contextText4characters);
		createSubtab(markupNReviewTabFolder, "others", composite4others,group4others, scrolledComposite4others, termRoleMatrix4others, contextText4others);

		/*structure subtab*/
		/*final TabItem tbtmFindStructureNames = new TabItem(markupNReviewTabFolder, SWT.NONE);
		tbtmFindStructureNames.setText("Remove Non-Structure Terms");

		final Composite composite_2 = new Composite(markupNReviewTabFolder, SWT.NONE);
		tbtmFindStructureNames.setControl(composite_2);

		tab5desc = new Text(composite_2, SWT.READ_ONLY | SWT.WRAP);
		tab5desc.setToolTipText(ApplicationUtilities.getProperty("step4Descp1"));
		tab5desc.setText(ApplicationUtilities.getProperty("step4Descp1"));
		tab5desc.setEditable(false);
		tab5desc.setBounds(10, 10, 744, 39);
		
		findStructureTable = new Table(composite_2, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION );
		final TableColumn findStructureTableColumn_Count = new TableColumn(findStructureTable, SWT.NONE);
		findStructureTableColumn_Count.setWidth(81);
		findStructureTableColumn_Count.setText("Count");
		final TableColumn findStructureTableColumn_Words = new TableColumn(findStructureTable, SWT.NONE);
		findStructureTableColumn_Words.setWidth(658);
		findStructureTableColumn_Words.setText("Structure Name");
		
		findStructureTable.setBounds(10, 74, 744, 209);
		findStructureTable.setLinesVisible(true);
		findStructureTable.setHeaderVisible(true);
		findStructureTable.addListener(SWT.Selection, new Listener() {//display context for selected structure term
		      public void handleEvent(Event e) {		    	 
		    	 TableItem[] selItem= findStructureTable.getSelection();
		    	 for (TableItem item : selItem) {
			  				String str = item.getText(1);
			  				try {
			  					structureContextText.setText("");
								mainDb.getContextData(str, structureContextText);
							} catch (ParsingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			  			
		    	 }
		      }
		    });

		structureContextText = new StyledText(composite_2, SWT.BORDER | SWT.V_SCROLL|SWT.H_SCROLL);
		structureContextText.setEditable(false);
		structureContextText.setDoubleClickEnabled(false);
		structureContextText.setBounds(10, 299, 744, 114);
		*/

	    /*Load button*/
		/*Button tab5_findstructure_loadFromLastTimeButton = new Button(composite_2, SWT.NONE);
		tab5_findstructure_loadFromLastTimeButton.setToolTipText(ApplicationUtilities.getProperty("termCurationLoadTTT"));
		tab5_findstructure_loadFromLastTimeButton.setBounds(171, 433, 155, 25);
		tab5_findstructure_loadFromLastTimeButton.setText(ApplicationUtilities.getProperty("termCurationLoad"));
		tab5_findstructure_loadFromLastTimeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//call functions to load values for all tables in order			
				int c = loadFindStructureTable(); //use tag in sentence table
				if(c==0){
					String messageHeader = ApplicationUtilities.getProperty("popup.header.info");
					String message = ApplicationUtilities.getProperty("popup.load.nodata");				
					ApplicationUtilities.showPopUpWindow(message, messageHeader, SWT.ICON_INFORMATION);
				}
			}			
		});*/
	    
		/*"mark as bad" button*/
		/*Button tab5_findstructure_MarkAsGoodButton = new Button(composite_2, SWT.NONE);
		tab5_findstructure_MarkAsGoodButton.setToolTipText(ApplicationUtilities.getProperty("termCurationMarkBadTTT"));
		tab5_findstructure_MarkAsGoodButton.setText(ApplicationUtilities.getProperty("termCurationMarkBad"));
		tab5_findstructure_MarkAsGoodButton.setBounds(342, 433, 132, 25);
		tab5_findstructure_MarkAsGoodButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i=0;
				for (TableItem item : findStructureTable.getItems()) {
					if (item.getChecked()) {	
						if(item.getBackground(1).equals(red)){
							item.setBackground(0,white);
							item.setBackground(1,white);
						}else{
							item.setBackground(0,red);
							item.setBackground(1,red);
						}
						item.setChecked(false);
					}
					i+=1;
				}
				
			}
		});*/
		
		/*"mark others as good" button*/
		/*final Button tab5_findstructure_MarkAsBadButton = new Button(composite_2, SWT.NONE);
		tab5_findstructure_MarkAsBadButton.setToolTipText(ApplicationUtilities.getProperty("termCurationMarkOtherGoodTTT"));
		tab5_findstructure_MarkAsBadButton.setBounds(479, 433, 140, 25);
		tab5_findstructure_MarkAsBadButton.setText(ApplicationUtilities.getProperty("termCurationMarkOthersGood"));
	    tab5_findstructure_MarkAsBadButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				int i=0;
				for (TableItem item : findStructureTable.getItems()) {
					if (!item.getBackground(1).equals(red)) {				
						findStructureTable.getItem(i).setBackground(0,green);
						findStructureTable.getItem(i).setBackground(1,green);
						item.setChecked(false);
					}
					i+=1;
				}
				//removeBadStructuresFromTable(findStructureTable);
				//try { You don't need to run markup again ater removal!
				//	mainDb.saveStatus(ApplicationUtilities.getProperty("tab.five.name"), combo.getText(), false);
				//	statusOfMarkUp[4] = false;
				//} catch (Exception exe) {
				//	LOGGER.error("Couldnt save status - markup" , exe);
				//} 				
			}
		});*/
		
		
	    /*"Save" button*/
	    /*final Button tab5_findstructure_SaveButton = new Button(composite_2, SWT.NONE);
		tab5_findstructure_SaveButton.setText(ApplicationUtilities.getProperty("termCurationSave"));// save good structure names here.
		tab5_findstructure_SaveButton.setBounds(622, 433, 132, 25);
		tab5_findstructure_SaveButton.setToolTipText(ApplicationUtilities.getProperty("termCurationSaveTTT"));
		tab5_findstructure_SaveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				//saveStructureTerms(markupTable_Descriptor, Registry.MARKUP_ROLE_O);
				saveStructureTerms(findStructureTable, Registry.MARKUP_ROLE_O);
				structureContextText.setText("");
			}
		});*/

		/*"find descriptors" subtab*/
		/*final TabItem tbtmFindDescriptors = new TabItem(markupNReviewTabFolder, SWT.NONE);
		tbtmFindDescriptors.setText("Remove Non-Descriptor terms");

		final Composite composite_7 = new Composite(markupNReviewTabFolder, SWT.NONE);
		tbtmFindDescriptors.setControl(composite_7);

		tab5desc = new Text(composite_7, SWT.READ_ONLY | SWT.WRAP);
		tab5desc.setToolTipText(ApplicationUtilities.getProperty("step4Descp2"));
		tab5desc.setText(ApplicationUtilities.getProperty("step4Descp2"));
		tab5desc.setEditable(false);
		tab5desc.setBounds(10, 10, 744, 39);
		
		findDescriptorTable = new Table(composite_7, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		findDescriptorTable.setBounds(10, 74, 744, 209);
		findDescriptorTable.setLinesVisible(true);
		findDescriptorTable.setHeaderVisible(true);
		final TableColumn findDescriptorTableColumn_Count = new TableColumn(findDescriptorTable, SWT.NONE);
		findDescriptorTableColumn_Count.setWidth(81);
		findDescriptorTableColumn_Count.setText("Count");
		final TableColumn findDescriptorTableColumn_Words = new TableColumn(findDescriptorTable, SWT.LEFT);
		findDescriptorTableColumn_Words.setWidth(659);
		findDescriptorTableColumn_Words.setText("Descriptors");
		findDescriptorTable.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	 TableItem[] selItem= findDescriptorTable.getSelection();
		    	 for (TableItem item : selItem) {
			  		String str = item.getText(1);
	  				try {
	  					descriptorContextText.setText("");
						mainDb.getContextData(str, descriptorContextText);
					} catch (ParsingException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
			  			
		    	 }
		      }
		    });
		
		descriptorContextText = new StyledText(composite_7, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
		descriptorContextText.setEditable(false);
		descriptorContextText.setDoubleClickEnabled(false);
		descriptorContextText.setBounds(10, 299, 744, 114);
		*/
		/*"load results from last time" button*/
		/*Button tab5_findDescriptor_loadFromLastTimeButton = new Button(composite_7, SWT.NONE);
		tab5_findDescriptor_loadFromLastTimeButton.setBounds(171, 433, 155, 25);
		tab5_findDescriptor_loadFromLastTimeButton.setText(ApplicationUtilities.getProperty("termCurationLoad"));
		tab5_findDescriptor_loadFromLastTimeButton.setToolTipText(ApplicationUtilities.getProperty("termCurationLoadTTT"));
		tab5_findDescriptor_loadFromLastTimeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//call functions to load values for all tables in order			
				int c = loadFindDescriptorTable(); //use wordpos table
				if(c==0){
					String messageHeader = ApplicationUtilities.getProperty("popup.header.info");
					String message = ApplicationUtilities.getProperty("popup.load.nodata");				
					ApplicationUtilities.showPopUpWindow(message, messageHeader, SWT.ICON_INFORMATION);
				}

			}			
		});*/
		/*"mark as bad" button */
		/*final Button tab5_findDescriptor_MarkAsGoodButton = new Button(composite_7, SWT.NONE);//this button is on the markup-descriptor tab
		tab5_findDescriptor_MarkAsGoodButton.setText(ApplicationUtilities.getProperty("termCurationMarkBad"));
		tab5_findDescriptor_MarkAsGoodButton.setBounds(342, 433, 132, 25);
		tab5_findDescriptor_MarkAsGoodButton.setToolTipText(ApplicationUtilities.getProperty("termCurationMarkBadTTT"));
		tab5_findDescriptor_MarkAsGoodButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TableItem [] items = findDescriptorTable.getItems();
				for (TableItem item : items) {
					if (item.getChecked()) {
						if(item.getBackground().equals(red)){
							item.setBackground(0,white);
							item.setBackground(1,white);
						}else{
							item.setBackground(0, red);
							item.setBackground(1, red);	
						}
						item.setChecked(false);
						
					}
				}
			}
		});*/

	
		/*"mark others as good" button*/
		/*final Button tab5_findDescriptor_MarkAsBadButton = new Button(composite_7, SWT.NONE);
		tab5_findDescriptor_MarkAsBadButton.setText(ApplicationUtilities.getProperty("termCurationMarkOthersGood"));
		tab5_findDescriptor_MarkAsBadButton.setBounds(479, 433, 140, 25);
		tab5_findDescriptor_MarkAsBadButton.setToolTipText(ApplicationUtilities.getProperty("termCurationMarkOthersGoodTTT"));
		tab5_findDescriptor_MarkAsBadButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TableItem [] items = findDescriptorTable.getItems();
				for (TableItem item : items) {
					if (!item.getBackground(1).equals(red)) {
						item.setBackground(0, green);
						item.setBackground(1, green);	
						item.setChecked(false);
					}
				}
				//removeDescriptorFromTable(findDescriptorTable);
			}
		});*/
		
		/*save button*/
		/*Button tab5_findDescriptor_SaveButton = new Button(composite_7, SWT.NONE);
		tab5_findDescriptor_SaveButton.setBounds(622, 433, 132, 25);
		tab5_findDescriptor_SaveButton.setText(ApplicationUtilities.getProperty("termCurationSave"));
		tab5_findDescriptor_SaveButton.setToolTipText(ApplicationUtilities.getProperty("termCurationSaveTTT"));
		tab5_findDescriptor_SaveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveDescriptorTerms(findDescriptorTable, Registry.MARKUP_ROLE_B);
				descriptorContextText.setText("");
				//saveTermRole(descriptorsToSaveList, Registry.MARKUP_ROLE_B);
				
				//for display only show the descriptors that are red and undecided
				
				//markupTable_1.removeAll();//removed temporarily, should be removed from database
				
				//findDescriptorTable.removeAll();
				//reLoadTable();				
			}
		});*/

		/*** "Find more Structure" subtab ***/
		/*
		TabItem findMoreStructure = new TabItem(markupNReviewTabFolder, SWT.NONE);
		findMoreStructure.setText("Find More Structures");
		
		Composite composite_10 = new Composite(markupNReviewTabFolder, SWT.NONE);
		findMoreStructure.setControl(composite_10);
		
		tab5desc = new Text(composite_10, SWT.READ_ONLY | SWT.WRAP);
		tab5desc.setToolTipText(ApplicationUtilities.getProperty("step4Descp3"));
		tab5desc.setText(ApplicationUtilities.getProperty("step4Descp3"));
		tab5desc.setEditable(false);
		tab5desc.setBounds(10, 10, 744, 39);
		
		findMoreStructureTable = new Table(composite_10, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		//findMoreStructureTable.setBounds(35, 94, 722, 216);
		findMoreStructureTable.setBounds(10, 74, 744, 209);
		findMoreStructureTable.setHeaderVisible(true);
		findMoreStructureTable.setLinesVisible(true);
		TableColumn findMoreStructTalbeColumn_Count = new TableColumn(findMoreStructureTable, SWT.NONE);
		findMoreStructTalbeColumn_Count.setWidth(81);
		findMoreStructTalbeColumn_Count.setText("Count");		
		TableColumn findMoreStructTableColumn_Words = new TableColumn(findMoreStructureTable, SWT.NONE);
		findMoreStructTableColumn_Words.setWidth(658);
		findMoreStructTableColumn_Words.setText("StructureName");
		findMoreStructureTable.addListener(SWT.Selection, new Listener() {//display context for selected structure term
		      public void handleEvent(Event e) {		    	 
		    	 TableItem[] selItem= findMoreStructureTable.getSelection();
		    	 for (TableItem item : selItem) {
	  				String str = item.getText(1);
	  				try {
	  					moreStructureContextText.setText("");
						mainDb.getContextData(str, moreStructureContextText);
					} catch (ParsingException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}			  			
		    	 }
		     }
		});
		
		moreStructureContextText = new StyledText(composite_10, SWT.BORDER | SWT.V_SCROLL|SWT.H_SCROLL);
		//moreStructureContextText.setBounds(35, 334, 722, 69);
		moreStructureContextText.setEditable(false);
		moreStructureContextText.setDoubleClickEnabled(false);
		moreStructureContextText.setBounds(10, 299, 744, 114);
		*/
		/*"load from last time" button*/
		/*Button tab5_findMoreStructure_loadFromLastTimeButton = new Button(composite_10, SWT.NONE);
		tab5_findMoreStructure_loadFromLastTimeButton.setBounds(171, 433, 155, 25);
		tab5_findMoreStructure_loadFromLastTimeButton.setText(ApplicationUtilities.getProperty("termCurationLoad"));
		tab5_findMoreStructure_loadFromLastTimeButton.setToolTipText(ApplicationUtilities.getProperty("termCurationLoadTTT"));
		tab5_findMoreStructure_loadFromLastTimeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//call functions to load values for all tables in order			
				int c = loadFindMoreStructureTable(); //use unknownwords table
				if(c==0){
					String messageHeader = ApplicationUtilities.getProperty("popup.header.info");
					String message = ApplicationUtilities.getProperty("popup.load.nodata");				
					ApplicationUtilities.showPopUpWindow(message, messageHeader, SWT.ICON_INFORMATION);
				}
			}			
		});
		*/
		/*"mark as good*/
		/*Button tab5_findMoreStructure_MarkAsGoodButton = new Button(composite_10, SWT.NONE);
		tab5_findMoreStructure_MarkAsGoodButton.setBounds(342, 433, 132, 25);
		tab5_findMoreStructure_MarkAsGoodButton.setText(ApplicationUtilities.getProperty("termCurationMarkGood"));
		tab5_findMoreStructure_MarkAsGoodButton.setToolTipText(ApplicationUtilities.getProperty("termCurationMarkGoodTTT"));
		tab5_findMoreStructure_MarkAsGoodButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i=0;
				for (TableItem item : findMoreStructureTable.getItems()) {
					if (item.getChecked()) {
						if(item.getBackground().equals(green)){
							item.setBackground(0,white);
							item.setBackground(1,white);
						}else{
							item.setBackground(0,green);
							item.setBackground(1,green);
						}
						item.setChecked(false);
					}
					i+=1;
				}				
			}
		});
		*/
		/*mark others as bad*/
		/*Button tab5_findMoreStructure_MarkAsBadButton = new Button(composite_10, SWT.NONE);
		tab5_findMoreStructure_MarkAsBadButton.setBounds(479, 433, 132, 25);
		tab5_findMoreStructure_MarkAsGoodButton.setToolTipText(ApplicationUtilities.getProperty("termCurationMarkOthersBadTTT"));
		tab5_findMoreStructure_MarkAsBadButton.setText(ApplicationUtilities.getProperty("termCurationMarkOthersBad"));
	    tab5_findMoreStructure_MarkAsBadButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				int i=0;
				for (TableItem item : findMoreStructureTable.getItems()) {
					if (!item.getBackground(1).equals(green)) {				
						findMoreStructureTable.getItem(i).setBackground(0,red);
						findMoreStructureTable.getItem(i).setBackground(1,red);
						item.setChecked(false);
					}
					i+=1;
				}			
			}
		});

		Button tab5_findMoreStructure_SaveButton = new Button(composite_10, SWT.NONE);
		tab5_findMoreStructure_SaveButton.setBounds(622, 433, 132, 25);
		tab5_findMoreStructure_SaveButton.setText(ApplicationUtilities.getProperty("termCurationSave"));
		tab5_findMoreStructure_SaveButton.setToolTipText(ApplicationUtilities.getProperty("termCurationSaveTTT"));
		tab5_findMoreStructure_SaveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				saveStructureTerms(findMoreStructureTable, Registry.MARKUP_ROLE_O);
				moreStructureContextText.setText("");
			}
		});*/
		
		/* "Find More Descriptors" subtab */
		/*TabItem descriptor2Tab = new TabItem(markupNReviewTabFolder, SWT.NONE);
		descriptor2Tab.setText("Find More Descriptors");
		Composite composite_11 = new Composite(markupNReviewTabFolder, SWT.NONE);
		descriptor2Tab.setControl(composite_11);

		tab5desc = new Text(composite_11, SWT.READ_ONLY | SWT.WRAP);
		tab5desc.setToolTipText(ApplicationUtilities.getProperty("step4Descp4"));
		tab5desc.setText(ApplicationUtilities.getProperty("step4Descp4"));
		tab5desc.setEditable(false);
		tab5desc.setBounds(10, 10, 744, 39);
		
		findMoreDescriptorTable = new Table(composite_11, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		findMoreDescriptorTable.setBounds(10, 74, 744, 209);
		findMoreDescriptorTable.setHeaderVisible(true);
		findMoreDescriptorTable.setLinesVisible(true);

		moreDescriptorContextText = new StyledText(composite_11, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
		moreDescriptorContextText.setEditable(false);
		moreDescriptorContextText.setDoubleClickEnabled(false);	
		moreDescriptorContextText.setBounds(10, 299, 744, 114);

		TableColumn findMoreDescriptorTableColumn_Count= new TableColumn(findMoreDescriptorTable, SWT.NONE);
		findMoreDescriptorTableColumn_Count.setWidth(81);
		findMoreDescriptorTableColumn_Count.setText("Count");
		TableColumn findMoreDescriptorTableColumn_Words= new TableColumn(findMoreDescriptorTable, SWT.NONE);
		findMoreDescriptorTableColumn_Words.setWidth(658);
		findMoreDescriptorTableColumn_Words.setText("Descriptor Name");
		findMoreDescriptorTable.addListener(SWT.Selection, new Listener() {//display context for selected structure term
		      public void handleEvent(Event e) {		    	 
		    	 TableItem[] selItem= findMoreDescriptorTable.getSelection();
		    	 for (TableItem item : selItem) {
	  				String str = item.getText(1);
	  				try {
	  					moreDescriptorContextText.setText("");
						mainDb.getContextData(str, moreDescriptorContextText);
					} catch (ParsingException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}			  			
		    	 }
		     }
		});*/
		/*load button*/
		/*Button tab5_findMoreDescriptor_loadFromLastTimeButton = new Button(composite_11, SWT.NONE);
		tab5_findMoreDescriptor_loadFromLastTimeButton.setBounds(171, 433, 155, 25);
		tab5_findMoreDescriptor_loadFromLastTimeButton.setText(ApplicationUtilities.getProperty("termCurationLoad"));
		tab5_findMoreDescriptor_loadFromLastTimeButton.setToolTipText(ApplicationUtilities.getProperty("termCurationLoadTTT"));
		tab5_findMoreDescriptor_loadFromLastTimeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//call functions to load values for all tables in order			
				int c = loadFindMoreDescriptorTable();
				if(c==0){
					String messageHeader = ApplicationUtilities.getProperty("popup.header.info");
					String message = ApplicationUtilities.getProperty("popup.load.nodata");				
					ApplicationUtilities.showPopUpWindow(message, messageHeader, SWT.ICON_INFORMATION);
				}
			}			
		});*/

		/*mark as good*/
		/*Button tab5_findMoreDescriptor_MarkAsGoodButton = new Button(composite_11, SWT.NONE);
		tab5_findMoreDescriptor_MarkAsGoodButton.setBounds(342, 433, 132, 25);
		tab5_findMoreDescriptor_MarkAsGoodButton.setText(ApplicationUtilities.getProperty("termCurationMarkGood"));
		tab5_findMoreDescriptor_MarkAsGoodButton.setToolTipText(ApplicationUtilities.getProperty("termCurationMarkGoodTTT"));
		tab5_findMoreDescriptor_MarkAsGoodButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TableItem [] items = findMoreDescriptorTable.getItems();
				for (TableItem item : items) {
					if (item.getChecked()) {
						if(item.getBackground().equals(green)){
							item.setBackground(0,white);
							item.setBackground(1,white);
						}else{
							item.setBackground(0, green);
							item.setBackground(1, green);
						}
						item.setChecked(false);
					}
				}
			}
		});*/
		/*mark others as bad*/
		/*Button tab5_findMoreDescriptor_MarkAsBadButton = new Button(composite_11, SWT.NONE);
		tab5_findMoreDescriptor_MarkAsBadButton.setBounds(479, 433, 140, 25);
		tab5_findMoreDescriptor_MarkAsBadButton.setText(ApplicationUtilities.getProperty("termCurationMarkOthersBad"));
		tab5_findMoreDescriptor_MarkAsBadButton.setToolTipText(ApplicationUtilities.getProperty("termCurationMarkOthersBadTTT"));
		tab5_findMoreDescriptor_MarkAsBadButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TableItem [] items = findMoreDescriptorTable.getItems();
				for (TableItem item : items) {
					if (!item.getBackground(1).equals(green)) {
						item.setBackground(0, red);
						item.setBackground(1, red);
						item.setChecked(false);
					}
				}
				//removeDescriptorFromTable(findMoreDescriptorTable);
			}
		});

		Button tab5_findMoreDescriptor_SaveButton = new Button(composite_11, SWT.NONE);
		tab5_findMoreDescriptor_SaveButton.setBounds(622, 433, 132, 25);
		tab5_findMoreDescriptor_SaveButton.setText(ApplicationUtilities.getProperty("termCurationSave"));
		tab5_findMoreDescriptor_SaveButton.setToolTipText(ApplicationUtilities.getProperty("termCurationSaveTTT"));
		tab5_findMoreDescriptor_SaveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveDescriptorTerms(findMoreDescriptorTable, Registry.MARKUP_ROLE_B);
				moreDescriptorContextText.setText("");
				//saveTermRole(descriptorsToSaveList, Registry.MARKUP_ROLE_B);
				
				//for display only show the descriptors that are red and undecided
				
				//markupTable_1.removeAll();//removed temporarily, should be removed from database
				
				//findMoreDescriptorTable.removeAll();
				//reLoadTable();				
			}
		});*/
		/********************************/
		/*"unknown removal" tab: step 5 */
		/********************************/
		final TabItem tagTabItem = new TabItem(tabFolder, SWT.NONE);
		tagTabItem.setText(ApplicationUtilities.getProperty("tab.six.name"));

		final Composite composite_6 = new Composite(tabFolder, SWT.NONE);
		tagTabItem.setControl(composite_6);

		tab6desc = new Text(composite_6, SWT.READ_ONLY|SWT.WRAP);
		tab6desc.setText(ApplicationUtilities.getProperty("step5Descp"));
		tab6desc.setEditable(false);
		tab6desc.setBounds(10, 10, 741, 41);
		/*
		tagTable = new Table(composite_6, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION);
		//tagTable = new Table(composite_6,  SWT.BORDER | SWT.FULL_SELECTION);
		tagTable.setLinesVisible(true);
		tagTable.setHeaderVisible(true);
		tagTable.setBounds(10, 57, 744, 203);
		
	    final TableColumn newColumnTableColumn = new TableColumn(tagTable, SWT.NONE);
	    newColumnTableColumn.setWidth(81);
	    newColumnTableColumn.setText("Check one");

		final TableColumn numberColumnTableColumn = new TableColumn(tagTable, SWT.NONE);
		numberColumnTableColumn.setWidth(78);
		numberColumnTableColumn.setText("Sentence Id");

	    final TableColumn modifierColumnTableColumn = new TableColumn(tagTable, SWT.NONE);
	    modifierColumnTableColumn.setWidth(65);
	    modifierColumnTableColumn.setText("Modifier");
	    
		final TableColumn tagColumnTableColumn = new TableColumn(tagTable, SWT.NONE);
		tagColumnTableColumn.setWidth(78);
		tagColumnTableColumn.setText("Tag");

		final TableColumn sentenceColumnTableColumn = new TableColumn(tagTable, SWT.NONE);
		sentenceColumnTableColumn.setWidth(515);
		sentenceColumnTableColumn.setText("Sentence");
		//added to check on focus,to display any message
		//if(tabFolder.getSelectionIndex()==6){
		//	tabFolder.setSelection(1);
		//	tabFolder.setFocus();
		//}
		
	    tagTable.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	        	TableItem item = (TableItem) event.item;
	        	item.setChecked(true);
	        	//tagTable.getItem(hashCodeOfItem).setChecked(false);
	        	for (TableItem tempItem : tagTable.getItems()) {
	        		if (tempItem.hashCode() == hashCodeOfItem) {
	        			tempItem.setChecked(false);
	        		}
	        	} 
	        	int sentid = Integer.parseInt(item.getText(1));
	        	updateContext(sentid);
	        	if (hashCodeOfItem != item
	        			.hashCode()) {
	        		hashCodeOfItem = item.hashCode();
	        	} else {
	        		hashCodeOfItem = 0;
	        	}
	        	
	        }
	      });


		//controls for marking up a sentence
		final Label modifierLabel = new Label(composite_6, SWT.NONE);
		modifierLabel.setText("Modifier:");
		modifierLabel.setBounds(10, 275, 64, 15);

		modifierListCombo = new Combo(composite_6, SWT.NONE);
		modifierListCombo.setBounds(80, 270, 210, 21);				
		
		final Label tagLabel = new Label(composite_6, SWT.NONE);
		tagLabel.setText("Tag:");
		tagLabel.setBounds(300, 275, 64, 24);
		
		tagListCombo = new Combo(composite_6, SWT.NONE);
		tagListCombo.setBounds(370, 270, 210, 21);
		
		final Button applyToAllButton = new Button(composite_6, SWT.NONE);
		applyToAllButton.setText(ApplicationUtilities.getProperty("Apply2Checked"));
		applyToAllButton.setToolTipText(ApplicationUtilities.getProperty("Apply2CheckedTTT"));
		applyToAllButton.setBounds(626, 270, 130, 23);
		applyToAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				applyTagToAll();//also check the next box automatically								
			}
		});

	    //context
	    final Label contextLabel = new Label(composite_6, SWT.NONE);
		contextLabel.setText("Context:");
		contextLabel.setBounds(10, 310, 88, 15);
		*/
		contextStyledText = new StyledText(composite_6, SWT.READ_ONLY| SWT.WRAP | SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
		//contextStyledText = new StyledText(composite_6, SWT.V_SCROLL | SWT.READ_ONLY | SWT.H_SCROLL | SWT.BORDER);
		contextStyledText.setBounds(10, 330, 744, 114);		
		/*
		//load button
		final Button loadTagButton = new Button(composite_6, SWT.NONE);
		loadTagButton.setBounds(392, 450, 168, 23);
		loadTagButton.setText(ApplicationUtilities.getProperty("sentCurationLoad"));
		loadTagButton.setToolTipText(ApplicationUtilities.getProperty("sentCurationLoadTTT"));
		loadTagButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				ApplicationUtilities.showProgressPopup(popupBar);
				loadTags(tabFolder);
				groupInfo.clear();
				try {
					mainDb.saveStatus(ApplicationUtilities.getProperty("tab.six.name"), combo.getText(), true);
					statusOfMarkUp[5] = true;
					//if(tagTable.getItemCount()==0)
					//{
					//	String messageHeader = ApplicationUtilities.getProperty("popup.header.info");
					//	String message = ApplicationUtilities.getProperty("popup.load.nodata");				
					//	ApplicationUtilities.showPopUpWindow(message, messageHeader, SWT.ICON_INFORMATION);
					//}
					
				} catch (Exception exe) {
					LOGGER.error("Couldnt save status - unknown" , exe);
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
				
			}
		});
		

		//save button
		final Button saveTagButton = new Button(composite_6, SWT.NONE);
		saveTagButton.setBounds(580, 450, 174, 23);
		saveTagButton.setText("Save Tagged Sentences");
		saveTagButton.setToolTipText("Save tagged sentences");
		saveTagButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				saveTag(tabFolder);
			}
		});

		*/
		final Button runTagButton = new Button(composite_6, SWT.NONE);
		runTagButton.setBounds(580, 450, 174, 23);
		runTagButton.setText("Run Step 5");
		runTagButton.setToolTipText("Prepare for the next step");
		runTagButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				contextStyledText.setText("Preparing for the next step. Please proceed to the next step when \"Done\" is displayed in this box.\n");
				//read in typos from database
				if(typos.size()==0){
					try{
						mainDb.readInTypos(typos);						
					}catch(Exception ex){
						StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);ex.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					}
				}
				if(typos.size()>0) {
					contextStyledText.setText("Correcting typos identified in source and in database\n");
					correctTypos();
				}
				//saveTag(tabFolder);
				loadTagTable(tabFolder);
				
				try {
					mainDb.saveStatus(ApplicationUtilities.getProperty("tab.six.name"), combo.getText(), true);
					//statusOfMarkUp[5] = true;
					statusOfMarkUp[8] = true;
				} catch (Exception exe) {
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
				
			}
		});

		

		
		/*TableViewer tableViewer = new TableViewer(composite_6, SWT.BORDER | SWT.FULL_SELECTION);
		table_1 = tableViewer.getTable();
		table_1.setBounds(13, 147, 85, 85);
		formToolkit.paintBordersFor(table_1);
		String[] myList = {"A","B","C"};
		CellEditor[] editors = new CellEditor[3];
		editors[0] = new CheckboxCellEditor(table_1);
		editors[2]=new ComboBoxCellEditor(table_1,myList );
		tableViewer.setCellEditors(editors);*/
		
		
		/********************************/
		/*"Character State" tab: step 6 */
		/*make changes in prefix_grouped_terms, save decisions to prefix_group_decisions */
		/*result in term_category table that may be loaded to OTO: upload2OTO*/
		/********************************/
		TabItem tbtmCharacterStates = new TabItem(tabFolder, SWT.NONE);
		tbtmCharacterStates.setText(ApplicationUtilities.getProperty("tab.character"));
		
		Composite composite_8 = new Composite(tabFolder, SWT.NONE);
		tbtmCharacterStates.setControl(composite_8);
		
		Group grpContextTable = new Group(composite_8, SWT.NONE);
		grpContextTable.setText("Context Table");
		grpContextTable.setBounds(0, 356, 635, 149);
		// Add the context table here
		contextTable = new Table(grpContextTable, SWT.FULL_SELECTION | SWT.BORDER);
		contextTable.setBounds(10, 20, 615, 119);
		contextTable.setHeaderVisible(true);
		contextTable.setLinesVisible(true);
		contextTable.addMouseListener(new MouseListener () {
			public void mouseDoubleClick(MouseEvent event) {
				try {
					String filePath = Registry.TargetDirectory + 
					ApplicationUtilities.getProperty("DEHYPHENED")+ "/";
					String fileName = contextTable.getSelection()[0].getText(0).trim();
					fileName = fileName.substring(0, fileName.indexOf("-"));
					filePath += fileName;
					if (filePath.indexOf("txt") != -1) {
						try {
							Runtime.getRuntime().exec(ApplicationUtilities.getProperty("notepad") 
									+ " \"" + filePath + "\"");
						} catch (Exception e){
							ApplicationUtilities.showPopUpWindow(ApplicationUtilities.getProperty("popup.error.msg") +
									ApplicationUtilities.getProperty("popup.editor.msg"),
									ApplicationUtilities.getProperty("popup.header.error"), 
									SWT.ERROR);
						}
					}
				} catch (Exception exe) {
					LOGGER.error("Error in displaying the file in context table", exe);
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
 
			}			
			public void mouseDown(MouseEvent event) {}
			public void mouseUp(MouseEvent event) {}
		});
		
		final TableColumn contextTablecolumn_1 = new TableColumn(contextTable, SWT.NONE);
		contextTablecolumn_1.setWidth(100);
		contextTablecolumn_1.setText("Source");
		
		final TableColumn contextTablecolumn_2 = new TableColumn(contextTable, SWT.NONE);
		contextTablecolumn_2.setWidth(512);
		contextTablecolumn_2.setText("Sentence");
		
		Group group_2 = new Group(composite_8, SWT.NONE);
		group_2.setBounds(641, 314, 144, 191);
		
		processedGroupsTable = new Table(group_2, SWT.BORDER | SWT.FULL_SELECTION);
		processedGroupsTable.setBounds(10, 10, 124, 171);
		processedGroupsTable.setLinesVisible(true);
		processedGroupsTable.setHeaderVisible(true);
		
		TableColumn tableColumn = new TableColumn(processedGroupsTable, SWT.NONE);
		tableColumn.setWidth(120);
		tableColumn.setText("Processed Groups");
		
		Group group_3 = new Group(composite_8, SWT.NONE);
		group_3.setBounds(0, 295, 635, 36);
		
		Label lblGroup = new Label(group_3, SWT.NONE);
		lblGroup.setBounds(7, 13, 45, 15);
		lblGroup.setText("Groups");
		
		groupsCombo = new Combo(group_3, SWT.NONE);
		groupsCombo.setBounds(56, 10, 161, 23);
		groupsCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				//Clear context table as the sentences from the previous group needn't be shown for another group;
				contextTable.removeAll();
				//dispose handlers used for the current group
				//dispose termsScrolledComposite or termGroups?
				loadTerms();
			}
		});
		
		Label lblDecision = new Label(group_3, SWT.NONE);
		lblDecision.setBounds(270, 13, 120, 17);
		lblDecision.setText("Category :");
		
		comboDecision = new Combo(group_3, SWT.NONE);
		comboDecision.setBounds(392, 10, 145, 23);
		
		Button btnSave = new Button(group_3, SWT.NONE);
		btnSave.setBounds(550, 8, 75, 25);
		btnSave.setText(ApplicationUtilities.getProperty("SaveCategoryBtn"));
		btnSave.setToolTipText(ApplicationUtilities.getProperty("SaveCategoryTTT"));
		//added March 1st
		/*if(groupsCombo==null || groupsCombo.getItemCount()==0 ||groupsCombo.getText().trim()=="")
		{
			btnSave.setEnabled(false);
		}
		else{*/
		btnSave.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				/*if(unpaired){ //use a different machanism to save the unpaired term/decisions 
					Control[] children = termsGroup.getChildren();
					//loop through children for Group (hold term1) and Text (holds category)
					//save to the final term-category table
					for(int i = 1; i < children.length; i++){
						if(children[i] instanceof Text && children[i-1] instanceof Group){
							String term = children[i-1].getToolTipText();
							String decision = ((Text)(children[i])).getText();
							charDb.saveTermCategory(groupsCombo.getText().replace("Group_", ""),term, decision);
						}
					}
					//update processed group
					String savedGroupName = groupsCombo.getText();
					processedGroups.put(savedGroupName, savedGroupName);				
					Set <String> processed = processedGroups.keySet();
					processedGroupsTable.removeAll();
					for (String groupName : processed) {
						TableItem item = new TableItem(processedGroupsTable, SWT.NONE);
						item.setText(groupName);
					}
					//set "saved" in groupInfo
					((CharacterGroupBean)groupInfo.get(groupsCombo.getText())).setSaved(true);
					((CharacterGroupBean)groupInfo.get(groupsCombo.getText())).setDecision("done");

				}else{*/
				
				try {
					String decision4group = comboDecision.getText();
					if(unpaired){
						Control[] children = termsGroup.getChildren();
						//loop through children for Group (hold term1) and Text (holds category)
						//save to the final term-category table
						boolean ignore = false;
						for(int i = 1; i < children.length; i++){
							if(children[i] instanceof Text && children[i-1] instanceof Group){
								String term = children[i-1].getToolTipText();
								String decision = ((Text)(children[i])).getText();
								int choice = -1;
								if(!ignore && decision.trim().length()<1){
									choice = ApplicationUtilities.showPopUpWindow(
											ApplicationUtilities.getProperty("popup.char.missing"),
											ApplicationUtilities.getProperty("popup.header.warning"), 
											SWT.YES | SWT.NO);
								}
								if(choice == SWT.NO) {
									return; //continue to categorize terms
								}
								else if(choice == SWT.YES){
									ignore = true; //so the popup message will not show again.
								}
								charDb.saveTermCategory(groupsCombo.getText().replace("Group_", ""),term, decision); //unpaired terms are saved now. paired terms are saved through groups and decisions on groups.
							}
						}	
						decision4group = "done";
					}
					ArrayList<TermsDataBean> terms = new ArrayList<TermsDataBean>();				
					ArrayList <CoOccurrenceBean> cooccurrences = groupInfo.get(groupsCombo.getText()).getCooccurrences();								

					((CharacterGroupBean)groupInfo.get(groupsCombo.getText())).setSaved(true);
					((CharacterGroupBean)groupInfo.get(groupsCombo.getText())).setDecision(decision4group);

					/*Save the decision first */
					charDb.saveDecision(cooccurrences.get(0).getGroupNo(), decision4group);
					
					/*Save the terms remain in the group. In case of unpaired, cooccurrences hold all the unpaired terms in the group */
					for (CoOccurrenceBean cbean : cooccurrences) {
						TermsDataBean tbean = new TermsDataBean();
						tbean.setFrequency(Integer.parseInt(cbean.getFrequency().getText()));
						tbean.setGroupId(cbean.getGroupNo());
						tbean.setKeep(cbean.getKeep());
						tbean.setSourceFiles(cbean.getSourceFiles());
						
						/* The fun starts here! try and save the terms that are there in parentTermGroup*/
						if(cbean.getTerm1() != null && cbean.getTerm1().isTogglePosition()) {
							tbean.setTerm1(cbean.getTerm1().getTermText().getText());
						} else {
							tbean.setTerm1("");
						}
						
						if(cbean.getTerm2() != null && cbean.getTerm2().isTogglePosition()){
							tbean.setTerm2(cbean.getTerm2().getTermText().getText());
						} else {
							tbean.setTerm2("");
						}
						/* Add the termdatabean to the arraylist*/
						terms.add(tbean);
					}
					
					/* update terms table, keeping on the terms remains in the group */
					charDb.saveTerms(terms);
					
				} catch (Exception exe) {
					LOGGER.error("Couldnt save the character tab details in MainForm" , exe);
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
				
				String savedGroupName = groupsCombo.getText();
				processedGroups.put(savedGroupName, savedGroupName);				
				Set <String> processed = processedGroups.keySet();
				processedGroupsTable.removeAll();
				for (String groupName : processed) {
					TableItem item = new TableItem(processedGroupsTable, SWT.NONE);
					item.setText(groupName);
				}		
				//}//if(unpaired)

				/** Logic for the terms removed from the groups goes here */
				/* noOfTermGroups=the number of original groups; getNumberofGroupsSaved() can be greater when new groups are generated*/
				if (noOfTermGroups <= getNumberOfGroupsSaved() && isTermsNotGrouped()) {
					int choice = 0;
					if(charStatePopUp){
						choice = ApplicationUtilities.showPopUpWindow(
								ApplicationUtilities.getProperty("popup.charstates.info"),
								ApplicationUtilities.getProperty("popup.header.info") + " : " +
								ApplicationUtilities.getProperty("popup.header.character"), 
								SWT.YES | SWT.NO | SWT.CANCEL);
						charStatePopUp = false;
					} else {
						choice = ApplicationUtilities.showPopUpWindow(
								ApplicationUtilities.getProperty("popup.char.cont"),
								ApplicationUtilities.getProperty("popup.header.info") + " : " +
								ApplicationUtilities.getProperty("popup.header.character"), 
								SWT.YES | SWT.NO | SWT.CANCEL);
					}

					
				    switch (choice) {
				    case SWT.OK:
				    case SWT.YES:
				    	showRemainingTerms();
				    	break;
				    case SWT.CANCEL:
				    case SWT.NO:
				    	int count = mainDb.finalizeTermCategoryTable();
						if(MainForm.upload2OTO){
							if(count>0){
								UploadTerms2OTO ud = new UploadTerms2OTO(dataPrefixCombo.getText().replaceAll("-", "_").trim(), getGlossaryType(glossaryPrefixCombo.getText().trim()));
								boolean uploaded = ud.upload();
								if(uploaded){
								ApplicationUtilities.showPopUpWindow(
									count+ " "+
									ApplicationUtilities.getProperty("popup.char.uploadterms2OTO"),
									ApplicationUtilities.getProperty("popup.header.info"), 
									SWT.OK);
								}else{
									ApplicationUtilities.showPopUpWindow(
											ApplicationUtilities.getProperty("popup.char.uploadterms2OTOSad"),
											ApplicationUtilities.getProperty("popup.header.info"), 
											SWT.OK);
								}
							}else{
								ApplicationUtilities.showPopUpWindow(
										"no new terms are available to upload to OTO ",
										ApplicationUtilities.getProperty("popup.header.info"), 
										SWT.OK);
							}
						}						
						break;
				    case SWT.RETRY:
				    case SWT.ABORT:
				    case SWT.IGNORE:
				    default : //Do Nothing! :-)
				    }

				}else if(noOfTermGroups <= getNumberOfGroupsSaved() && !isTermsNotGrouped()){ //no terms left
					int count = mainDb.finalizeTermCategoryTable();
					if(count>0 && MainForm.upload2OTO){
						UploadTerms2OTO ud = new UploadTerms2OTO(dataPrefixCombo.getText().replaceAll("-", "_").trim(), getGlossaryType(glossaryPrefixCombo.getText().trim()));
						boolean uploaded = ud.upload();
						if(uploaded){
						ApplicationUtilities.showPopUpWindow(
							count+ " "+
							ApplicationUtilities.getProperty("popup.char.uploadterms2OTO"),
							ApplicationUtilities.getProperty("popup.header.info"), 
							SWT.OK);
						}else{
							ApplicationUtilities.showPopUpWindow(
									ApplicationUtilities.getProperty("popup.char.uploadterms2OTOSad"),
									ApplicationUtilities.getProperty("popup.header.info"), 
									SWT.OK);
						}
					}
					
				}
				try {
					mainDb.saveStatus(ApplicationUtilities.getProperty("tab.character"), combo.getText(), true);
					//statusOfMarkUp[6] = true;
					statusOfMarkUp[9] = true;
				} catch (Exception exe) {
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
			}


		});
	//	}
		
		Label label_1 = new Label(composite_8, SWT.SEPARATOR | SWT.VERTICAL);
		label_1.setBounds(510, 240, -6, 191);
		
		Group grpRemovedTerms = new Group(composite_8, SWT.NONE);
		grpRemovedTerms.setText("Removed Terms");
		grpRemovedTerms.setBounds(457, 81, 328, 208);
		
		removedScrolledComposite = new ScrolledComposite(grpRemovedTerms, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		removedScrolledComposite.setBounds(10, 24, 308, 174);
		removedScrolledComposite.setExpandHorizontal(true);
		removedScrolledComposite.setExpandVertical(true);
		
		removedTermsGroup = new Group(removedScrolledComposite, SWT.NONE);
		removedTermsGroup.setLayoutData(new RowData());
		
		removedScrolledComposite.setContent(removedTermsGroup);
		removedScrolledComposite.setMinSize(removedTermsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		Group grpDeleteAnyTerm = new Group(composite_8, SWT.NONE);
		grpDeleteAnyTerm.setText("Categorize the terms by their character categories.");
		grpDeleteAnyTerm.setBounds(0, 55, 451, 234);
		
		termsScrolledComposite = new ScrolledComposite(grpDeleteAnyTerm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		termsScrolledComposite.setBounds(10, 41, 429, 183);
		termsScrolledComposite.setExpandHorizontal(true);
		termsScrolledComposite.setExpandVertical(true);
		
		termsGroup = new Group(termsScrolledComposite, SWT.NONE);
		termsScrolledComposite.setContent(termsGroup);
		termsScrolledComposite.setMinSize(termsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		Label lblContext = new Label(grpDeleteAnyTerm, SWT.NONE);
		lblContext.setBounds(10, 20, 55, 15);
		lblContext.setText("Context");
		
		Label lblTerm = new Label(grpDeleteAnyTerm, SWT.NONE);
		lblTerm.setBounds(72, 20, 55, 15);
		lblTerm.setText("Term");
		
		Label lblTerm_1 = new Label(grpDeleteAnyTerm, SWT.NONE);
		lblTerm_1.setBounds(170, 20, 120, 15);
		lblTerm_1.setText("Co-occurred Term");
		
		Label lblFrequency = new Label(grpDeleteAnyTerm, SWT.NONE);
		lblFrequency.setBounds(353, 20, 55, 15);
		lblFrequency.setText("Frequency");
		
		sortLabel = new Label(grpDeleteAnyTerm, SWT.NONE);
		sortLabel.setImage(SWTResourceManager.getImage(MainForm.class, "/edu/arizona/sirls/biosemantics/parsing/down.jpg"));		
		sortLabel.setBounds(408, 10, 31, 25);
		sortLabel.setToolTipText("Sort by frequency");
		sortLabel.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent me){
				ArrayList <CoOccurrenceBean> cooccurences = null;
				Rectangle tempCoordinates = null;
					cooccurences = 
						((CharacterGroupBean)groupInfo.get(groupsCombo.getText())).getCooccurrences();
					int selectionIndex = groupsCombo.getSelectionIndex();
					int size = cooccurences.size();
					CoOccurrenceBean [] cbeans = new CoOccurrenceBean[size];
					int i = 0, j = 0, k = 0;
					for (CoOccurrenceBean cbean: cooccurences) {
						cbeans[i++] = cbean;
					}
					
					boolean toSort = true;
					int firstFrequency = 0; 
						if(cbeans!=null && cbeans.length>0)
							firstFrequency = cbeans[0].getFrequency().getBounds().y;
					int lastFrequency = 0;
					if(cbeans!=null && cbeans.length>0)
						lastFrequency = cbeans[size-1].getFrequency().getBounds().y;
					if(firstFrequency<lastFrequency) {
						toSort = true;
					} else if (firstFrequency>lastFrequency){
						toSort = false;
					}
					
					for (i = 0, j = size-1, k = size-1; i < size/2; i++, j--, k-=2){
						CoOccurrenceBean beanFirst = cbeans[i];
						CoOccurrenceBean beanLast = cbeans[j];
						if(beanFirst.getTerm1() != null 
								&& beanFirst.getTerm2() != null
								&& beanLast.getTerm1() != null
								&& beanLast.getTerm2() != null) {
							/* Swap coordinates of radio button */
							tempCoordinates = beanFirst.getContextButton().getBounds();
							beanFirst.getContextButton().setBounds(beanLast.getContextButton().getBounds());
							beanLast.getContextButton().setBounds(tempCoordinates);
							
							/* Swap Frequencies */						
							tempCoordinates = null;
							tempCoordinates = beanFirst.getFrequency().getBounds();
							beanFirst.getFrequency().setBounds(beanLast.getFrequency().getBounds());
							beanLast.getFrequency().setBounds(tempCoordinates);
							
							
							if (toSort) {
								/*Sort Ascending*/
									/* Swap Group 1 */
									tempCoordinates = beanFirst.getTerm1().getTermGroup().getBounds();
									tempCoordinates.y += k * standardIncrement;
									beanFirst.getTerm1().getTermGroup().setBounds(tempCoordinates);
									
									tempCoordinates = beanLast.getTerm1().getTermGroup().getBounds();
									tempCoordinates.y -= k * standardIncrement;						
									beanLast.getTerm1().getTermGroup().setBounds(tempCoordinates);
									
									/* Swap Group 2 */
									tempCoordinates = beanFirst.getTerm2().getTermGroup().getBounds();
									tempCoordinates.y += k * standardIncrement;
									beanFirst.getTerm2().getTermGroup().setBounds(tempCoordinates);
									
									tempCoordinates = beanLast.getTerm2().getTermGroup().getBounds();
									tempCoordinates.y -= k * standardIncrement;						
									beanLast.getTerm2().getTermGroup().setBounds(tempCoordinates);

								
							} else {
								/* Sort Descending*/
								/* Swap Group 1 */
									tempCoordinates = beanFirst.getTerm1().getTermGroup().getBounds();
									tempCoordinates.y -= k * standardIncrement;
									beanFirst.getTerm1().getTermGroup().setBounds(tempCoordinates);
									
									tempCoordinates = beanLast.getTerm1().getTermGroup().getBounds();
									tempCoordinates.y += k * standardIncrement;						
									beanLast.getTerm1().getTermGroup().setBounds(tempCoordinates);
									
									/* Swap Group 2 */
									tempCoordinates = beanFirst.getTerm2().getTermGroup().getBounds();
									tempCoordinates.y -= k * standardIncrement;
									beanFirst.getTerm2().getTermGroup().setBounds(tempCoordinates);
									
									tempCoordinates = beanLast.getTerm2().getTermGroup().getBounds();
									tempCoordinates.y += k * standardIncrement;						
									beanLast.getTerm2().getTermGroup().setBounds(tempCoordinates);


							}
						}
						
					}
						
					if(sortedBy!=null && sortedBy.length>0 && sortedBy[selectionIndex]) {
						/*Sort Ascending*/
						sortedBy[selectionIndex] = false;
						sortLabel.setImage(SWTResourceManager.getImage(MainForm.class, "/edu/arizona/sirls/biosemantics/parsing/up.jpg"));
						
					} else if(sortedBy!=null && sortedBy.length>0 && !sortedBy[selectionIndex]){
						/* Sort Descending*/
						sortedBy[selectionIndex] = true;
						sortLabel.setImage(SWTResourceManager.getImage(MainForm.class, "/edu/arizona/sirls/biosemantics/parsing/down.jpg"));	
						
					}
					
					termsScrolledComposite.setMinSize(termsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					removedScrolledComposite.setMinSize(removedTermsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					
			}
			public void mouseUp(MouseEvent me){	}
			public void mouseDoubleClick(MouseEvent me){	}
		});
		Button btnViewGraphVisualization = new Button(composite_8, SWT.NONE);
		btnViewGraphVisualization.setBounds(457, 55, 159, 25);
		btnViewGraphVisualization.setText("View Graph Visualization");
		btnViewGraphVisualization.setToolTipText("Click to view the graph visualization of the terms that have co-occurred");
		
		step6_desc = new Text(composite_8, SWT.READ_ONLY | SWT.WRAP);
		step6_desc.setText(ApplicationUtilities.getProperty("step6DescpText"));
		step6_desc.setEditable(false);
		step6_desc.setBounds(10, 10, 762, 39);
	
		
		/*Removed terms test*/
/*		Button btnRemovedTerms = new Button(composite_8, SWT.NONE);
		btnRemovedTerms.setBounds(622, 0, 105, 25);
		btnRemovedTerms.setText("Removed Terms");
		btnRemovedTerms.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				showRemainingTerms();
			}
		});*/
		
		btnViewGraphVisualization.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(groupsCombo.getItemCount()>0 && groupsCombo.getText().trim()!="")
				{
				CoOccurrenceGraph.viewGraph(Registry.TargetDirectory+
				ApplicationUtilities.getProperty("CHARACTER-STATES") + "/" + groupsCombo.getText()+".xml", groupsCombo.getText());
				}
				else
				{
					String messageHeader = ApplicationUtilities.getProperty("popup.header.info");
					ApplicationUtilities.showPopUpWindow("No graph to display", messageHeader, SWT.ICON_WARNING);
					//when no graph to display.. land him on next tab
				}
			}
		});
		
		/*************** Finalizer tab ***********************/

		final TabItem finalizerTabItem = new TabItem(tabFolder, SWT.NONE);
		finalizerTabItem.setText(ApplicationUtilities.getProperty("tab.seven.name"));

		final Composite composite_5 = new Composite(tabFolder, SWT.NONE);
		finalizerTabItem.setControl(composite_5);

		txtThisLastStep = new Text(composite_5, SWT.READ_ONLY | SWT.WRAP);
		txtThisLastStep.setText(ApplicationUtilities.getProperty("step7DescpText"));
		txtThisLastStep.setBounds(10, 10, 744, 38);
	
		finalizerTable = new Table(composite_5, SWT.FULL_SELECTION | SWT.BORDER);
		finalizerTable.setBounds(10, 54, 744, 250);
		finalizerTable.setLinesVisible(true);
		finalizerTable.setHeaderVisible(true);
		finalizerTable.addMouseListener(new MouseListener () {
			public void mouseDoubleClick(MouseEvent event) {
				String filePath = Registry.TargetDirectory + 
				ApplicationUtilities.getProperty("FINAL")+ "/" +
				finalizerTable.getSelection()[0].getText(1).trim();				
				
				if (filePath.indexOf("xml") != -1) {
					try {
						Runtime.getRuntime().exec(ApplicationUtilities.getProperty("notepad") 
								+ " \"" + filePath + "\"");
					} catch (Exception e){
						ApplicationUtilities.showPopUpWindow(ApplicationUtilities.getProperty("popup.error.msg") +
								ApplicationUtilities.getProperty("popup.editor.msg"),
								ApplicationUtilities.getProperty("popup.header.error"), 
								SWT.ERROR);
					}
				} 
			}			
			public void mouseDown(MouseEvent event) {}
			public void mouseUp(MouseEvent event) {}
		});

		final TableColumn transformationNumberColumnTableColumn_1_2 = new TableColumn(finalizerTable, SWT.NONE);
		transformationNumberColumnTableColumn_1_2.setWidth(168);
		transformationNumberColumnTableColumn_1_2.setText("Count");

		final TableColumn transformationNameColumnTableColumn_1_2 = new TableColumn(finalizerTable, SWT.NONE);
		transformationNameColumnTableColumn_1_2.setWidth(359);
		transformationNameColumnTableColumn_1_2.setText(ApplicationUtilities.getProperty("file"));
		
		final Text finalLog = new Text(composite_5, SWT.WRAP | SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
		finalLog.setBounds(10, 310, 744, 150);
		finalLog.setEnabled(true);

		/*finalizerProgressBar = new ProgressBar(composite_5, SWT.NONE);
		finalizerProgressBar.setVisible(false);
		finalizerProgressBar.setBounds(10, 436, 322, 17);*/

		final Button startFinalizerButton = new Button(composite_5, SWT.NONE);
		startFinalizerButton.setToolTipText("Run step 7");
		startFinalizerButton.setBounds(364, 470, 85, 23);
		startFinalizerButton.setText(ApplicationUtilities.getProperty("step7RunBtn"));
		startFinalizerButton.setToolTipText(ApplicationUtilities.getProperty("step7RunTTT"));
		startFinalizerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e){
				finalizerTable.removeAll();
				boolean completed = startFinalize(finalLog);
				try {
					mainDb.saveStatus(ApplicationUtilities.getProperty("tab.seven.name"), combo.getText(), true);
					//statusOfMarkUp[7] = true;
					statusOfMarkUp[10] = true;
					//if(completed){
					/*File fileList= new File(Registry.TargetDirectory+"/final/");
					if(fileList.list().length==0)
					{
						//show error popup
						statusOfMarkUp[6] = false;
						ApplicationUtilities.showPopUpWindow("Error executing step 7", "Error",SWT.ERROR);
					}*/
					//}
					
				} catch (Exception exe) {
					LOGGER.error("Couldnt save status - markup" , exe);
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
			}
		});
		
		Button btnLoad_2 = new Button(composite_5, SWT.NONE);
		btnLoad_2.setToolTipText(ApplicationUtilities.getProperty("step7LoadTTT"));
		btnLoad_2.setBounds(455, 470, 192, 23);
		btnLoad_2.setText(ApplicationUtilities.getProperty("step7LoadBtn"));
		btnLoad_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				loadFileInfo(finalizerTable, Registry.TargetDirectory + 
						ApplicationUtilities.getProperty("FINAL"));
			}
		});
		
		final Button clearFinalizerButton = new Button(composite_5, SWT.NONE);
		clearFinalizerButton.setToolTipText(ApplicationUtilities.getProperty("ClearRerunTTT"));
		clearFinalizerButton.setBounds(653, 470, 96, 23);
		clearFinalizerButton.setText(ApplicationUtilities.getProperty("ClearRerunBtn"));
		clearFinalizerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				finalizerTable.removeAll();
				startFinalize(finalLog);
				try {
					mainDb.saveStatus(ApplicationUtilities.getProperty("tab.seven.name"), combo.getText(), true);
					//statusOfMarkUp[7] = true;
					statusOfMarkUp[10] = true;
					//check if finalized final contains files--this should be done after finalize step is completed.
					/*File fileList= new File(Registry.TargetDirectory+"/final/");
					if(fileList.list().length==0)
					{
						//show error popup
						statusOfMarkUp[6] = false;
						ApplicationUtilities.showPopUpWindow("Error executing step 7", "Error",SWT.ERROR);
					}*/
				} catch (Exception exe) {
					LOGGER.error("Couldnt save status - markup" , exe);
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}							
			}
		});


/*		final TabItem glossaryTabItem = new TabItem(tabFolder, SWT.NONE);
		glossaryTabItem.setText(ApplicationUtilities.getProperty("tab.eight.name"));

		final Composite composite_7 = new Composite(tabFolder, SWT.NONE);
		glossaryTabItem.setControl(composite_7);

		glossaryStyledText = new StyledText(composite_7, SWT.V_SCROLL | SWT.READ_ONLY | SWT.H_SCROLL | SWT.BORDER);
		glossaryStyledText.setBounds(10, 10, 744, 369);

		final Button reportGlossaryButton = new Button(composite_7, SWT.NONE);
		reportGlossaryButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				reportGlossary();
				try {
					mainDb.saveStatus(ApplicationUtilities.getProperty("tab.eight.name"), combo.getText(), true);
					statusOfMarkUp[7] = true;
				} catch (Exception exe) {
					LOGGER.error("Couldnt save status - glossary" , exe);
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
				
			}
		});
		reportGlossaryButton.setBounds(654, 385, 100, 23);
		reportGlossaryButton.setText("Report");*/

		/* final Label logoLabel = new Label(shell, SWT.NONE);
		logoLabel.setText(ApplicationUtilities.getProperty("application.instructions"));
		logoLabel.setBounds(10, 485, 530, 83);

		final Label label = new Label(shell, SWT.NONE);
		label.setBackgroundImage(SWTResourceManager.getImage(MainForm.class, 
				ApplicationUtilities.getProperty("application.logo")));
		label.setBounds(569, 485, 253, 71);*/

	}

	/**
	 * 
	 */
	protected void correctTypos() {
		Hashtable<String, TreeSet<String>> sources = mainDb.correctTyposInDB(typos);	
		correctTyposInSource(sources);
	}
	
	/**
	 * correct typos corresponding source files
	 * @param sources: typo=>list of sources
	 */
	private void correctTyposInSource(Hashtable<String, TreeSet<String>> sources) {
		String descriptionfolder = Registry.TargetDirectory+"descriptions";
		String descriptiondfolder = Registry.TargetDirectory+"descriptions-dehyphened";
		Enumeration<String> en = sources.keys();
		while(en.hasMoreElements()){
			String typo = en.nextElement();
			String correction = typos.get(typo);
			TreeSet<String> sourcefiles = sources.get(typo);
			for(String file : sourcefiles){
				try{
					File f = new File(descriptionfolder, file);
					String content = IOUtils.toString(new FileInputStream(f));
					String correctioncp = correction;
					Pattern p = Pattern.compile("(.*?)\\b("+typo+")\\b(.*)", Pattern.CASE_INSENSITIVE);
					//need be case insenstive, but keep the original case
					Matcher m = p.matcher(content);
					while(m.matches()){
						content =m.group(1);
						String w = m.group(2);
						if(w.matches("^[A-Z].*")){
							correction = correction.substring(0,1).toUpperCase()+correction.substring(1); 
						}else{
							correction = correctioncp;
						}
						content+=correction;
						content+=m.group(3);
						m = p.matcher(content);
					}
					IOUtils.write(content, new FileOutputStream(f));
					
					f = new File(descriptiondfolder, file);
					content = IOUtils.toString(new FileInputStream(f));
					//need be case insenstive, but keep the original case
					m = p.matcher(content);
					while(m.matches()){
						content =m.group(1);
						String w = m.group(2);
						if(w.matches("^[A-Z].*")){
							correction = correction.substring(0,1).toUpperCase()+correction.substring(1); 
						}else{
							correction = correctioncp;
						}
						content+=correction;
						content+=m.group(3);
						m = p.matcher(content);
					}
					IOUtils.write(content, new FileOutputStream(f));					
				}catch(Exception e){
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
			}
		}
	}
		
	
	/**
	 * if there is a termset ready for download for the dataprefix on OTO, 
	 * update local dataset with the reviewed termset.
	 * @param trim
	 */
	private void downloadConfirmedTermsFromOTO(String dataprefixx) {
		// TODO Auto-generated method stub
		final String dataprefix = dataprefixx;
		//final Boolean success = null;
		//boolean outcome = true;
		Label text = new Label(MainForm.grpTermSets, SWT.NONE);
		text.setText("Checking OTO for availability of the "+dataprefix +" term set(s) ...");
		text.setBounds(23, 30, 700, 23);
		
		boolean downloadable = false;
		ArrayList<String> result = UploadTerms2OTO.execute(
				"ls "+ApplicationUtilities.getProperty("OTO.downloadable.dir")+ 
				" | grep "+dataprefix+"_groupterms.sql$");
		if(result.size()> 1) downloadable = true; //the last element in result is the return status
		//final boolean ddble = downloadable;
		if(!downloadable){
			text = new Label(MainForm.grpTermSets, SWT.NONE);
			text.setText("No "+dataprefix +" term set(s) is(are) available. Please proceed to the next step. ");
			text.setBounds(23, 60, 700, 23);
		}else{
			int i = 0;
			final int position = 60+(result.size()+1)*30;			
			//MainForm.grpTermSets
			text = new Label(MainForm.grpTermSets, SWT.NONE);
			text.setText("The "+dataprefix +" term set(s) is(are) ready for use from OTO. Select the term set you would like to download:");
			text.setBounds(23, 60, 700, 23);	
			Button choice = null;
			for(; i<result.size()-1; i++){ 
				choice =  new Button(MainForm.grpTermSets, SWT.RADIO);
				choice.setText(result.get(i));
				choice.setBounds(30, 60+(i+1)*30, 500, 23);
				choice.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						Button button = (Button) e.widget;
						button.setSelection(true);
						String filename = button.getText();
						downloadTermSet(dataprefix, filename, position);
					}
				});
			}
			
						
			choice = new Button(MainForm.grpTermSets, SWT.RADIO);
			choice.setText("no selection");
			choice.setBounds(30, 60+(i+1)*30, 200, 23);
			choice.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					Button button = (Button) e.widget;
					button.setSelection(true);
					String filename = button.getText();
					downloadTermSet(dataprefix, filename, position);
				}
			});

		}
		
		//return outcome;
	}

	private void downloadTermSet(String dataprefix, String file, /*Boolean success, boolean downloadable,*/ int position) {
		//String timestamp = file.replaceFirst(dataprefix+"_", "").replaceFirst("_groupterms.*", "");
		if(file.equals("no selection")){
			Label text = new Label(MainForm.grpTermSets, SWT.NONE);
			text.setText("You chose not to use any "+dataprefix +" term set(s) that is(are) available. Please proceed to the next step. ");
			text.setBounds(23, position, 700, 23);
		}else{				
			UploadTerms2OTO.scpFrom(ApplicationUtilities.getProperty("OTO.downloadable.dir")+file, Registry.TargetDirectory+file);
			//restore sql dump 
			Statement stmt = null;
			try{
				if(conn == null){
					Class.forName(ApplicationUtilities.getProperty("database.driverPath"));
					conn = DriverManager.getConnection(ApplicationUtilities.getProperty("database.url"));
				}
				stmt = conn.createStatement();
				//update dataprefix_term_category dataprefix_syns
				//save local version of term_category table
				String local = (new Timestamp(new Date().getTime())+"").replaceAll("[^\\d]", "");
				stmt.execute("drop table if exists "+dataprefix+"_"+ApplicationUtilities.getProperty("TERMCATEGORY")+"_"+local);
				stmt.execute("drop table if exists "+dataprefix+"_syns");
				try{
					stmt.execute("alter table "+dataprefix+"_"+ApplicationUtilities.getProperty("TERMCATEGORY")+" RENAME TO "+dataprefix+"_"+ApplicationUtilities.getProperty("TERMCATEGORY")+"_"+local);
				}catch(Exception e){
					//ignore the error _term_category not exist
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
				//create new version of term_category table
				String mysqlrestore = "mysql -u"+ApplicationUtilities.getProperty("database.username")+" -p"+ApplicationUtilities.getProperty("database.password")+" "+ApplicationUtilities.getProperty("database.name")+" < \""+Registry.TargetDirectory+file+"\""+" 2> \""+Registry.TargetDirectory+dataprefix+"_download_sqllog.txt\"";//write output to log file
				System.out.println(mysqlrestore);
				String[] cmd = new String [] {"cmd", "/C", mysqlrestore}; //to hid redirect <
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p
						.getInputStream()));
				
				BufferedReader errInput = new BufferedReader(new InputStreamReader(p
						.getErrorStream()));
				
				// read the output from the command
				boolean trouble = false;
				String s = "";
				while ((s = stdInput.readLine()) != null) {
					// listener.info(String.valueOf(i), s);
					System.out.println(s);
					trouble = true;
				}					
				// read the errors from the command
				String e = "";
				while ((e = errInput.readLine()) != null) {
					System.out.println(e);
					trouble = true;
				}
				if(!trouble){
					/*try{
						//stmt.execute("alter table "+dataprefix+"_"+timestamp+"_"+ApplicationUtilities.getProperty("TERMCATEGORY")+" RENAME TO "+dataprefix+"_"+ApplicationUtilities.getProperty("TERMCATEGORY"));
						//stmt.execute("alter table "+dataprefix+"_"+timestamp+"_syns RENAME TO "+dataprefix+"_syns");
						stmt.execute("alter table "+dataprefix+"_"+ApplicationUtilities.getProperty("TERMCATEGORY")+" RENAME TO "+dataprefix+"_"+ApplicationUtilities.getProperty("TERMCATEGORY"));
						stmt.execute("alter table "+dataprefix+"_syns RENAME TO "+dataprefix+"_syns");
					}catch(Exception e1){
						//ignore the error _term_category not exist
						StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);
						e1.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					}*/
					//add "structure" terms from term_category_local to term_category
					stmt.execute("insert into "+dataprefix+"_"+ApplicationUtilities.getProperty("TERMCATEGORY")+" (term, category) " +
						"select term, category from "+dataprefix+"_"+ApplicationUtilities.getProperty("TERMCATEGORY")+"_"+local+" where category in ('structure', 'character')");	
					
					//make a copy of wordrolestable
					stmt.execute("create table "+dataprefix+"_"+ApplicationUtilities.getProperty("WORDROLESTABLE")+"_"+local+
							"(select * from "+dataprefix+"_"+ApplicationUtilities.getProperty("WORDROLESTABLE")+")");					
					
					//update wordroles table: structures from downloaded TERMCATEGORY should be updated with 'op'
					stmt.execute("update "+dataprefix+"_"+ApplicationUtilities.getProperty("WORDROLESTABLE")+" set semanticrole='op' "
							+ "where word in (select distinct term from "+dataprefix+"_"+ApplicationUtilities.getProperty("TERMCATEGORY")+ " "
									+ "where category='structure')");
					
					//update wordroles table: non-structure/non-taxonname from downloaded TERMCATEGORY should be updated with 'c'
					stmt.execute("update "+dataprefix+"_"+ApplicationUtilities.getProperty("WORDROLESTABLE")+" set semanticrole='c' "
							+ "where word in (select distinct term from "+dataprefix+"_"+ApplicationUtilities.getProperty("TERMCATEGORY")+ " "
									+ "where category not in ('structure', 'taxon_name'))");
					
					Label text = new Label(MainForm.grpTermSets, SWT.NONE);
					text.setText("CharaParser term set has been updated for term set "+dataprefix +". You can now proceed directly to step 7.");
					text.setBounds(23, position, 700, 23);								
				}else{
					Label text = new Label(MainForm.grpTermSets, SWT.NONE);
					text.setText("Encountered technical problems while downloading the term set "+dataprefix +". Please try again later or report the problem.");
					text.setBounds(23, position, 700, 23);								
				}
			}catch(Exception e){
				StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				ApplicationUtilities.showPopUpWindow(
						 "Encountered technical problems while downloading the term set "+dataprefix +". Please try again later or report the problem.", 
							ApplicationUtilities.getProperty("popup.header.info"), SWT.OK);

			}finally{
				try{
					if(stmt!=null) stmt.close();
				}catch(Exception exe){
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					ApplicationUtilities.showPopUpWindow(
							 "Encountered technical problems while downloading the term set "+dataprefix +". Please try again later or report the problem.", 
								ApplicationUtilities.getProperty("popup.header.info"), SWT.OK);

				}
			}
			

			
			
		}
	}

	private void createSubtab(final TabFolder markupNReviewTabFolder, final String type, Composite composite_1, Group group, final ScrolledComposite scrolledComposite, final Composite termRoleMatrix, final StyledText contextText) {
		String subtabTitle ="";
		String subtabInstruction = "";
		if(type.compareToIgnoreCase("others")==0){
			subtabTitle = ApplicationUtilities.getProperty("tab.five.three.name");
			subtabInstruction = "step4Descp3";
		}else if(type.compareToIgnoreCase("structures")==0){
			subtabTitle = ApplicationUtilities.getProperty("tab.five.one.name");
			subtabInstruction = "step4Descp1";
		}else if(type.compareToIgnoreCase("characters")==0){
			subtabTitle = ApplicationUtilities.getProperty("tab.five.two.name");
			subtabInstruction = "step4Descp2";
		}		
		
		TabItem tbtmCategorizeOthers = new TabItem(markupNReviewTabFolder, SWT.NONE);
		tbtmCategorizeOthers.setText(subtabTitle);
		//final Composite composite_1 = new Composite(markupNReviewTabFolder, SWT.NONE);
		
		tbtmCategorizeOthers.setControl(composite_1);
	
		//subtab instruction
		Text text_1 = new Text(composite_1, SWT.READ_ONLY | SWT.WRAP);
		text_1.setToolTipText(ApplicationUtilities.getProperty(subtabInstruction));
		text_1.setText(ApplicationUtilities.getProperty(subtabInstruction));
		text_1.setEditable(false);
		text_1.setBounds(5, 0, 744, 39);
		//add URL to full instruction
		Link url = new Link(composite_1, SWT.NONE);
		url.setText(ApplicationUtilities.getProperty("tab.term.instruction"));
		url.setBounds(5, 40, 744, 15);
		url.addSelectionListener(new SelectionAdapter(){
	        @Override
	        public void widgetSelected(SelectionEvent e) {
	               //System.out.println("You have selected: "+e.text);
	               try {
	            	org.eclipse.swt.program.Program.launch(e.text);  
	              } 
	             catch (Exception ex) {
	                 ex.printStackTrace();
	            } 
	        }
	    });
		
		//final Group group = new Group(composite_1, SWT.NONE);
		group.setBounds(10, 62, 744, 250);

		//"table" header
		Label lblCount = new Label(group, SWT.NONE);
		lblCount.setText("Count");
		lblCount.setBounds(15, 10, 93, 15);
		
		Label lblTerm = new Label(group, SWT.NONE);
		lblTerm.setText("Term");
		lblTerm.setBounds(125, 10, 93, 15);

		Label lblRole1 = new Label(group, SWT.NONE);
		lblRole1.setText("Is Structure?");
		lblRole1.setBounds(325, 10, 93, 15);
		
		Label lblRole2 = new Label(group, SWT.NONE);
		lblRole2.setText("Is Descriptor?");
		lblRole2.setBounds(425, 10, 93, 15);

		Label lblRole3 = new Label(group, SWT.NONE);
		lblRole3.setText("Neither");
		lblRole3.setBounds(525, 10, 93, 15);
		
		//final ScrolledComposite scrolledComposite = new ScrolledComposite(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(0, 30,744, 200);
		scrolledComposite.setLayout(new RowLayout(SWT.VERTICAL));

		
		/*context area: event handler in loadTermArea */
		contextText.setEditable(false);
		contextText.setDoubleClickEnabled(false);
		contextText.setBounds(10, 310, 744, 120);
		
		/*"load" button*/
		//final Composite termRoleMatrix = new Composite(scrolledComposite, SWT.NONE);
		Button tab5_others_loadFromLastTimeButton = new Button(composite_1, SWT.NONE);
		tab5_others_loadFromLastTimeButton.setBounds(459, 433, 155, 25);
		tab5_others_loadFromLastTimeButton.setText(ApplicationUtilities.getProperty("termCurationLoad"));
		tab5_others_loadFromLastTimeButton.setToolTipText(ApplicationUtilities.getProperty("termCurationLoadTTT"));
		tab5_others_loadFromLastTimeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	
				//popup a message if Perl thread is still alive
				if(MainForm.markupstarted && MainForm.vd.isAlive()){
					ApplicationUtilities.showPopUpWindow(
						ApplicationUtilities.getProperty("popup.wait"), 
						ApplicationUtilities.getProperty("popup.header.info"), SWT.ICON_INFORMATION);
					return;
				}
				
				//set access flags
				int subtabindex = markupNReviewTabFolder.getSelectionIndex(); //0: perl 1: 4.1 2: 4.2 3:4.3				
				if(subtabindex == 1) fourdotoneload = true;
				if(subtabindex == 2) fourdottwoload = true;
				if(subtabindex == 3) fourdotthreeload = true;
						
				ArrayList<String> words = null;
				if(type.compareTo("others")==0){
					words = fetchContentTerms(contextText);
				}else if(type.compareTo("structures")==0){
					if(inistructureterms==null || inistructureterms.size()==0){
						words = fetchStructureTerms(contextText);
						inistructureterms = (ArrayList<String>) words.clone();
					}else{
						words = (ArrayList<String>) inistructureterms.clone();
					}
				}else if(type.compareTo("characters")==0){
					if(inicharacterterms==null || inicharacterterms.size()==0){
						words = fetchCharacterTerms(contextText);
						inicharacterterms = (ArrayList<String>) words.clone();
					}else{
						words = (ArrayList<String>) inicharacterterms.clone();
					}
				}
				if(words.size()==0){
					String messageHeader = ApplicationUtilities.getProperty("popup.header.info");
					String message = ApplicationUtilities.getProperty("popup.load.nodata");				
					ApplicationUtilities.showPopUpWindow(message, messageHeader, SWT.ICON_INFORMATION);
				}else{
					loadTermArea(termRoleMatrix, scrolledComposite, words, contextText, type);
				}				
			}			
		});
		
		/* "Save" button */ 
		final Button saveButton = new Button(composite_1, SWT.NONE);
		saveButton.setText("Save");
		saveButton.setBounds(622, 433, 132, 25);//(650, 427, 98, 25);
		saveButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				//int choice = ApplicationUtilities.showPopUpWindow(
				//	 "After the terms are saved, you will not be able to redo this step. Do you want to save now?", 
				//		ApplicationUtilities.getProperty("popup.header.info"), SWT.YES | SWT.NO);
				//if(choice == SWT.YES) {
					int subtabindex = markupNReviewTabFolder.getSelectionIndex(); //0: perl 1: 4.1 2: 4.2 3:4.3
					if(subtabindex == 1) fourdotonesave = true;
					if(subtabindex == 2) fourdottwosave = true;
					if(subtabindex == 3) fourdotthreesave = true;
			
					/*if(runperl && fourdotoneload && fourdottwoload && fourdotthreeload && fourdotonesave && fourdottwosave && fourdotthreesave){						
						try {
							mainDb.saveStatus(ApplicationUtilities.getProperty("tab.five.name"), combo.getText(), true);
							statusOfMarkUp[4] = true;
						} catch (Exception exe) {
							StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
						}
					}*/
					
					if(runperl){						
						try {
							mainDb.saveStatus(ApplicationUtilities.getProperty("tab.five.perl.name"), combo.getText(), true);
							statusOfMarkUp[4] = true; //need work here
						} catch (Exception exe) {
							StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
						}
					}
					
					if(fourdotoneload && fourdotonesave){						
						try {
							mainDb.saveStatus(ApplicationUtilities.getProperty("tab.five.one.name"), combo.getText(), true);
							statusOfMarkUp[5] = true;
						} catch (Exception exe) {
							StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
						}
					}
					
					if(fourdottwoload && fourdottwosave){						
						try {
							mainDb.saveStatus(ApplicationUtilities.getProperty("tab.five.two.name"), combo.getText(), true);
							statusOfMarkUp[6] = true;
						} catch (Exception exe) {
							StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
						}
					}
					
					if(fourdotthreeload && fourdotthreesave){						
						try {
							mainDb.saveStatus(ApplicationUtilities.getProperty("tab.five.three.name"), combo.getText(), true);
							statusOfMarkUp[7] = true;
						} catch (Exception exe) {
							StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
						}
					}
					
					if(recordTermReviewResults(termRoleMatrix))
						contextText.setText("Terms saved!");
					else
						contextText.setText("Failed to save the terms. Check the log for details.");
				//}else{
				//	return;
				//}
			}

			private boolean recordTermReviewResults(Composite termRoleMatrix) {
				try{
					//save to db
					ArrayList<String> noneqs = new ArrayList<String>();
					ArrayList<String> structures = new ArrayList<String>();
					ArrayList<String> characters = new ArrayList<String>();
					Hashtable<String, String> categorizedterms = null;
					UUID lastSavedId = null;
					if(type.compareToIgnoreCase("structures") ==0){
						categorizedterms = categorizedtermsS;
						if(lastSavedIdS == null){
							lastSavedIdS = mainDb.getLastSavedId(type);
						}
						lastSavedId = lastSavedIdS;
					}
					if(type.compareToIgnoreCase("characters") ==0){
						categorizedterms = categorizedtermsC;
						if(lastSavedIdC == null){
							lastSavedIdC = mainDb.getLastSavedId(type);
						}
						lastSavedId = lastSavedIdC;
					}
					if(type.compareToIgnoreCase("others") ==0){
						categorizedterms = categorizedtermsO;
						if(lastSavedIdO == null){
							lastSavedIdO = mainDb.getLastSavedId(type);
						}
						lastSavedId = lastSavedIdO;
					}
					Enumeration<String> en = categorizedterms.keys();
					while(en.hasMoreElements()){
						String t = en.nextElement();
						String type = categorizedterms.get(t);
						if(type.compareToIgnoreCase("others")==0) noneqs.add(t);
						if(type.compareToIgnoreCase("structures")==0) structures.add(t);
						if(type.compareToIgnoreCase("characters")==0) characters.add(t);
					}
					inistructureterms = null;
					inicharacterterms = null;		
					//categorizedterms = null;
					

					UUID currentSavedId = UUID.randomUUID();
					boolean success = mainDb.recordNonEQTerms(noneqs, lastSavedId, currentSavedId);//noneq, need not use type info because lastSavedId is found from wordroles table
					if(!success) return false;
					success = mainDb.saveTermRole(structures, Registry.MARKUP_ROLE_O, lastSavedId, currentSavedId, type); //structures, record into wordroles table
					if(!success) return false;
					success = mainDb.saveTermRole(characters, Registry.MARKUP_ROLE_B, lastSavedId, currentSavedId, type); //descriptors, record into wordroles table
					if(!success) return false;
					if(type.compareToIgnoreCase("structures") ==0){
						lastSavedIdS = currentSavedId;
					}
					if(type.compareToIgnoreCase("characters") ==0){
						lastSavedIdC = currentSavedId;
					}
					if(type.compareToIgnoreCase("others") ==0){
						lastSavedIdO = currentSavedId;
					}
					//set sentences to unknown
					ArrayList<String> nonStructureTerms = new ArrayList<String>();
					nonStructureTerms.addAll(noneqs);
					nonStructureTerms.addAll(characters);
					success = mainDb.setUnknownTags(nonStructureTerms);
					if(!success) return false;
				}catch(Exception e){
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
					return false;
				}
				//termRoleMatrix.setVisible(false);
				//termRoleMatrix4others.dispose();
				//termRoleMatrix4structures.dispose();
				//termRoleMatrix4characters.dispose();
				contextText.setText("");
				/*Control[] controls = termRoleMatrix.getChildren();
				Hashtable<String, String> structures = new Hashtable<String, String>();
				Hashtable<String, String> characters = new Hashtable<String, String>();
				Hashtable<String, String> noneqs = new Hashtable<String, String>();
				for(Control control: controls){
					Composite termRoleGroup = (Composite) control;
					Control[] row = termRoleGroup.getChildren();
					String word = ((Label)row[1]).getText();
					if(((Button)row[2]).getSelection()){//structure
						structures.put(word, word);
					}
					if(((Button)row[3]).getSelection()){//structure
						characters.put(word, word);
					}
					if(((Button)row[4]).getSelection()){//structure
						noneqs.put(word, word);
					}					
				}
				
				try{
					//save to db
					ArrayList<String> terms = new ArrayList<String>();
					terms.addAll(noneqs.values());
					mainDb.recordNonEQTerms(terms);//noneq
					terms = new ArrayList<String>();
					terms.addAll(characters.values());					
					mainDb.saveTermRole(terms, Registry.MARKUP_ROLE_B); //descriptor
					terms = new ArrayList<String>();
					terms.addAll(structures.values());					
					mainDb.saveTermRole(terms, Registry.MARKUP_ROLE_O); //descriptor
					
					//set sentences to unknown
					ArrayList<String> nonStructureTerms = new ArrayList<String>();
					nonStructureTerms.addAll(noneqs.values());
					nonStructureTerms.addAll(characters.values());
					mainDb.setUnknownTags(nonStructureTerms);
				}catch(Exception e){
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
				//remove
				//termRoleMatrix.setVisible(false);
				termRoleMatrix.dispose();
				contextText.setText("");*/
				return true;
			}
		});
	}
	
	

	/* This function saves the Other terms from the markup tab 
	 * to database after user assigns a role to each one of them*/
	/*private void saveOtherTerms() {
		
		HashMap<String, String> otherTerms = new HashMap<String, String> ();
		for (TermRoleBean  tbean : markUpTermRoles) {
			String word = tbean.getTermLabel().getText();
			String role = tbean.getRoleCombo().getText();
			if (!role.equalsIgnoreCase("Other")) {
				if(role.equalsIgnoreCase("Structure")) {
					otherTerms.put(word, Registry.MARKUP_ROLE_O);
				}
				
				if(role.equalsIgnoreCase("Descriptor")) {
					otherTerms.put(word, Registry.MARKUP_ROLE_B);
				}
				
				if(role.equalsIgnoreCase("Verb")) {
					otherTerms.put(word, Registry.MARKUP_ROLE_VERB);
				}
			}
		}
		
		try {
			mainDb.saveOtherTerms(otherTerms);
		} catch (Exception exe){
			LOGGER.error("Error in saving other terms from Markup-Others to database", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
	}
	*/
	/* This function is called after the Markup is run to load the Others tab;*/ 
	/*
	public void showOtherTerms() {
		ArrayList<String> otherTerms = null;
		try {
			otherTerms = mainDb.getUnknownWords();
		} catch (Exception exe) {
			LOGGER.error("Exception in getting unknown words", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		
		if (otherTerms != null) {
			int counter =1;
	        for (String term : otherTerms) {
	        	addOtherTermsRow(term,counter);
	        	counter+=1;
	        	System.out.println("addother term called for "+term);
	        }
	        
			RowData rowdata = (RowData)termRoleGroup.getLayoutData();
			rowdata.height += 40;
			termRoleGroup.setLayoutData(new RowData(rowdata.width, rowdata.height));
	        Rectangle rect = termRoleGroup.getBounds();
	        rect.height += 40;
	        termRoleGroup.setBounds(rect); 
	       scrolledComposite.setMinSize(termRoleGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}

	}*/
	/*
	public void showOtherTermsTable() {
		//Populate Others Table Hong TODO 5/23/11
		ArrayList<String> otherTerms = null;
		try {
			otherTerms = mainDb.getUnknownWords();
		} catch (Exception exe) {
			LOGGER.error("Exception in getting unknown words", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		
		if (otherTerms != null) {
			int counter =1;
	        for (String term : otherTerms) {
	        	//addOtherTermsRow(term,counter);
	        	
	        
	        //  Thanks Prasad Others tab removed this method used to load data in the Others table.
	         	
	        //	TableItem item = new TableItem(table_Others,SWT.HORIZONTAL);
	        //	item.setText(new String[] {counter+"",term});
	        	
	        	        	
	        	//table_Others.set
	        	counter+=1;
	        	System.out.println("addother term called for "+term);
	        }
	        
			
		}

	}*/
	
	
	
	/* This function adds a row to the Markup - Others tab*/
	/*private void addOtherTermsRow(String term,int counter){
		if (markUpTermRoles.size() > 7) {
			RowData rowdata = (RowData)termRoleGroup.getLayoutData();
			rowdata.height += 40;
			termRoleGroup.setLayoutData(new RowData(rowdata.width, rowdata.height));
	        Rectangle rect = termRoleGroup.getBounds();
	        rect.height += 40;
	        termRoleGroup.setBounds(rect);
		}
		
		Button button = new Button(termRoleGroup, SWT.CHECK);
		button.setBounds(otherChkbx.x, otherChkbx.y, otherChkbx.width, otherChkbx.height);
		button.setText(String.valueOf(counter));
		otherChkbx.y+=40;
		
		//instead of a combo box we need radio buttons.
		Button radiobutton_otherRoles =null;
		
		for(int i=0;i<otherRoles.length;i++){
			
			radiobutton_otherRoles= new Button(termRoleGroup, SWT.RADIO);
			radiobutton_otherRoles.setBounds(otherCombo.x +(i*100) , otherCombo.y, otherCombo.width, otherCombo.height);
		
		radiobutton_otherRoles.setText(otherRoles[i]);
		if(i==0){
			radiobutton_otherRoles.setSelection(true);
			
		}
		
		}
		otherCombo.y+=40;
		//radiobutton_otherRoles= new Button(termRoleGroup, SWT.RADIO);
		//radiobutton_otherRoles.setBounds(x,y,width,height);
		//radiobutton_otherRoles.setText("Other");
		
		//164,30   264,30    364,30  464,30
		//164,70   264,70         
		//Combo tempCombo = new Combo(termRoleGroup, SWT.NONE);	
		//tempCombo.setItems(otherRoles);
		//tempCombo.select(0);
		//tempCombo.setBounds(otherCombo.x , otherCombo.y, otherCombo.width, otherCombo.height);
		//otherCombo.y += 40;
	    
	    Label tempLabel = new Label(termRoleGroup, SWT.NONE);
	    tempLabel.setText(term);
	    tempLabel.setBounds(otherTerm.x, otherTerm.y, otherTerm.width, otherTerm.height);
	    otherTerm.y += 40;
	    
	    TermRoleBean tbean = new TermRoleBean(tempLabel, radiobutton_otherRoles);
	  //  markUpTermRoles.add(tbean);
	}*/
	
	/**
	 * This function saves the terms from the Structure tab 
	 * under markup tab - to the wordroles table
	 * method declaration changed to accept a list as the descriptors that are marked green
	 * are stored separately and passed here.Changed by Prasad in May 2011
	 */
	//private void saveTermRole(ArrayList <String>  terms, String role) {
		/*ArrayList <String> structureTerms = new ArrayList<String>();
		TableItem [] items = table.getItems();
		for (TableItem item : items) {
			structureTerms.add(item.getText(1));
		}*/
		/*try {
			mainDb.saveTermRole(terms, role);
			//now display only those that are yet to be decided.
			//so get descriptors from 
			
			
			/*markupTable_1.removeAll();//removed temporarily, should be removed from database
			int count = 1;
			for (String word : descriptorsToSaveList) {
				TableItem item = new TableItem(markupTable_1, SWT.NONE);
				item.setText(new String [] {count+"", word});
				count++;
			}*/
			
			
		/*} catch (Exception exe) {
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		
	}*/
	
	/**
	 * This function saves the terms from the Find(More)Structure subtab 
	 * under markup tab - to the wordroles table
	 * 
	 */
	/*private void saveStructureTerms(Table table, String role) {
		//save content of the table in order to assign correct color codes after green ones are saved
		Hashtable<String, Color> content = new Hashtable<String, Color>();
		ArrayList <String> structureTerms = new ArrayList<String>();
		ArrayList <String> nonStructureTerms = new ArrayList<String>();
		TableItem [] items = table.getItems();
		//collect decisions
		for (TableItem item : items) {
			Color color = item.getBackground(1); //bgcolor for text column
			content.put(item.getText(1), color);
			if(color!=null && color.equals(green)){
				structureTerms.add(item.getText(1));
			}else if(color!=null && color.equals(red)){
				nonStructureTerms.add(item.getText(1));
			}
		}
		//act on nonStructureTerms: 
		//set sentences tagged with nonStructureTerms to "unknown"
		//change pos from p/s to b
		try {
			mainDb.setUnknownTags(nonStructureTerms);
			mainDb.changePOStoB(nonStructureTerms);
		} catch (Exception exe) {
			LOGGER.error("Exception encountered in removing structures from database in MainForm:removeBadStructuresFromTable", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		//act on structureTerms: save them to wordrole table
		try {
			mainDb.saveTermRole(structureTerms, role);			
		} catch (Exception exe) {
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		//refresh table
		table.removeAll();
		List<String> terms = Arrays.asList(content.keySet().toArray(new String[]{}));
		Collections.sort(terms);
		int count = 1;
		Iterator<String> it = terms.iterator();
		while(it.hasNext()){
			String term = it.next();
			Color color = content.get(term);
			if(!color.equals(red) && !color.equals(green)){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[] {count+"", term});
				count++;
			}
		}		
	}*/
	
	/**
	 * This function saves the terms from the Find(More)Descriptor subtab 
	 * under markup tab - to the wordroles table
	 * 
	 */
	/*private void saveDescriptorTerms(Table table, String role) {
		//save content of the table in order to assign correct color codes after green ones are saved
		Hashtable<String, Color> content = new Hashtable<String, Color>();
		ArrayList <String> descriptorTerms = new ArrayList<String>();
		ArrayList <String> nonDescriptorTerms = new ArrayList<String>();
		TableItem [] items = table.getItems();
		//collect decisions
		for (TableItem item : items) {
			Color color = item.getBackground(1); //bgcolor for the word column
			content.put(item.getText(1), color);
			if(color!=null && color.equals(green)){
				descriptorTerms.add(item.getText(1));
			}else if(color!=null && color.equals(red)){
				nonDescriptorTerms.add(item.getText(1));
			}
		}
		//act on nonDescriptorTerms: set save_flag in wordroles to "red",
		//so next time these terms will not be curated again
		try {
			mainDb.recordNonEQTerms(nonDescriptorTerms);
		} catch (Exception exe) {
			LOGGER.error("Exception encountered in removing structures from database in MainForm:removeBadStructuresFromTable", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		//act on descriptorTerms: save them to wordrole table
		try {
			mainDb.saveTermRole(descriptorTerms, role);			
		} catch (Exception exe) {
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		//refresh table
		table.removeAll();
		List<String> terms = Arrays.asList(content.keySet().toArray(new String[]{}));
		Collections.sort(terms);
		int count = 1;
		Iterator<String> it = terms.iterator();
		while(it.hasNext()){
			String term = it.next();
			Color color = content.get(term);
			if(!color.equals(red) && !color.equals(green)){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[] {count+"", term}); 
				count++;
			}
		}		
	}
	*/
	
/**
 * In the Markup - Descriptor Tab, this function 
 * is used to remove any term selected by the user 
 */
	/*private void removeDescriptorFromTable(Table table){
		@SuppressWarnings("unused")
		boolean toRemove = false;
		TableItem [] items = table.getItems();
		for (TableItem item : items) {
			if (item.getChecked()) {
				removedTags.add(item.getText(1));
				//item.setBackground(0,red);
				//item.setBackground(1,red);
				//item.setBackground(2,red);
				item.setBackground(red);				
				toRemove = true;
				item.setChecked(false);
			} else {
				//descriptorsToSaveList.add(item.getText(1));
			}
		}
		
		// remove the tag from the database (No need to remove from database now!)
		if(toRemove) {
			try {
				mainDb.recordNonEQTerms(removedTags);
			} catch (Exception exe) {
				LOGGER.error("Exception encountered in removing tags from database in MainForm:removeMarkup", exe);
				StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
			}
		} else {
			ApplicationUtilities.showPopUpWindow("You have not selected anything for removal. " +
					"\nPlease select atleast one row.", 
					"Nothing Selected!", SWT.ICON_ERROR);
		}
	//	markupTable_1.removeAll();//removed temporarily, should be removed from database
		

	}*/

	private void browseConfigurationDirectory() {
        DirectoryDialog directoryDialog = new DirectoryDialog(shell);
        directoryDialog.setMessage("Please select a directory and click OK");
        
        String directory = directoryDialog.open();
        if(directory != null && !directory.equals("")) {
        	String dirsep = System.getProperty("file.separator");
        	if(!directory.endsWith(dirsep)){
        		directory =directory+dirsep;
        	}
        	
        String path = 	directory;
        
        projectDirectory.setText(directory);
        makeReqDirectories(path);
        
       
        }
	}
	
	private void makeReqDirectories(String path) {
		File confFldr = new File(path+"/conf/");
		File srcFldr = new File(path+"/source/");
		File targetFldr = new File(path+"/target/");
	        
	      
        if(!confFldr.exists())
        	confFldr.mkdir();
        if(!srcFldr.exists())
        	srcFldr.mkdir();
        if(!targetFldr.exists())
        	targetFldr.mkdir();
        
        /*should not reset folders. when resume a previous run, reset folder will make all folders empty
        fna.parsing.Utilities.resetFolder(targetFldr,"co-occurrence");
        fna.parsing.Utilities.resetFolder(targetFldr,"descriptions");
        fna.parsing.Utilities.resetFolder(targetFldr,"descriptions-dehyphened");
        fna.parsing.Utilities.resetFolder(targetFldr,"extracted");
        fna.parsing.Utilities.resetFolder(targetFldr,"extractedword");
        fna.parsing.Utilities.resetFolder(targetFldr,"final");
        fna.parsing.Utilities.resetFolder(targetFldr,"habitats");
        fna.parsing.Utilities.resetFolder(targetFldr,"transformed");*/
        String targetPath= targetFldr.getAbsolutePath();
	        
        File cooccur = new File(targetPath+"/co-occurrence");
        File descriptions = new File(targetPath+"/descriptions");
        File desc_dehyphened = new File(targetPath+"/descriptions-dehyphened");
        File extracted = new File(targetPath+"/extracted");
        File extractedword = new File(targetPath+"/extractedword");
        File final_dir = new File(targetPath+"/final");
        File habitats = new File(targetPath+"/habitats");
        //File markedup = new File(targetPath+"/markedup"); //not used
        File transformed = new File(targetPath+"/transformed");
	        
        
        cooccur.mkdir();
        descriptions.mkdir();
        desc_dehyphened.mkdir();
        extracted.mkdir();
        extractedword.mkdir();
        final_dir.mkdir();
        habitats.mkdir();
        //markedup.mkdir();
        transformed.mkdir();

        configurationText.setText(confFldr.getAbsolutePath());
        sourceText.setText(srcFldr.getAbsolutePath());
        targetText.setText(targetFldr.getAbsolutePath());
          
        Registry.ConfigurationDirectory = confFldr.getAbsolutePath()+System.getProperty("file.separator");
        Registry.SourceDirectory=srcFldr.getAbsolutePath()+System.getProperty("file.separator");
        Registry.TargetDirectory=targetFldr.getAbsolutePath()+System.getProperty("file.separator");
	}

	private void startExtraction() throws Exception {
		if(ve==null || !ve.isAlive()){
			ProcessListener listener = new ProcessListener(extractionTable, extractionProgressBar, shell.getDisplay());
			ve = new VolumeExtractor(Registry.SourceDirectory, Registry.TargetDirectory, listener);
			//VolumeExtractor ve = new WordDocSegmenter(Registry.SourceDirectory, Registry.TargetDirectory, listener);
			ve.start();
		}
	}
	
	
	private void startVerification() {
		if(vv==null || !vv.isAlive()){
			ProcessListener listener = new ProcessListener(verificationTable, verificationProgressBar, shell.getDisplay());
			vv = new VolumeVerifier(listener);
			vv.start();
		}
	}
	
	private void clearVerification() {		
		verificationTable.removeAll();
	}
	
	private void startTransformation() {
		if(vt==null || !vt.isAlive()){
			ProcessListener listener = new ProcessListener(transformationTable, transformationProgressBar, shell.getDisplay());
			if(MainForm.startupstring.contains("Heading")){
				vt = new VolumeTransformerFoC(listener, dataPrefixCombo.getText().replaceAll("-", "_").trim(), getGlossary(this.glossaryPrefixCombo.getText().replaceAll("-", "_").trim()), shell.getDisplay());
			}else{
				vt = new VolumeTransformer(listener, dataPrefixCombo.getText().replaceAll("-", "_").trim(), getGlossary(this.glossaryPrefixCombo.getText().replaceAll("-", "_").trim()), shell.getDisplay());
			}
			vt.start();
		}
	}
	
	private void startType3Transformation() {
		if(preMarkUp==null || !preMarkUp.isAlive()){
			ProcessListener listener = 
				new ProcessListener(transformationTable, transformationProgressBar, 
						shell.getDisplay());
			/* Need to clarify perlLog, and seeds new arraylist from Dr Hong*/ 
			preMarkUp = 
				new Type3Transformer(listener, shell.getDisplay(), 
						null, dataPrefixCombo.getText().replaceAll("-", "_").trim(),MainForm.glossaryPrefixCombo.getText().trim(), new ArrayList());
			preMarkUp.start();
		}
	}
	
	private void startType2Transformation () {
		if(transformer2 == null || !transformer2.isAlive()){
			ProcessListener listener = 
				new ProcessListener(transformationTable, transformationProgressBar, 
						shell.getDisplay());
			transformer2 = new Type2Transformer(listener, dataPrefixCombo.getText().replaceAll("-", "_").trim());
			transformer2.start();
		}
	}
	
	
	private void startType4Transformation () {
		if(transformer4 == null || !transformer4.isAlive()){
			ProcessListener listener = 
				new ProcessListener(transformationTable, transformationProgressBar, 
						shell.getDisplay());
			if(MainForm.type4xml.compareToIgnoreCase("taxonx") ==0){
				transformer4 = new Type4Transformer4TaxonX(listener, dataPrefixCombo.getText().replaceAll("-", "_").trim());
			}else if(MainForm.type4xml.compareToIgnoreCase("goldengate-noschema") ==0){
				transformer4 = new Type4Transformer4GoldenGATEnoSchema(listener, dataPrefixCombo.getText().replaceAll("-", "_").trim());
			}
			transformer4.start();
		}
	}
	private void clearTransformation() {
		transformationTable.removeAll();
	}
	
	/*private void loadProject() {
		File project = null;
		try{
			if(type.trim().equals(""))
				project= new File("type1project.conf");//fna/foc doc 
			else
				if(type.trim().equals("type2"))
					project= new File("type2project.conf"); //treatise and paragraph-level fna
				else
			if(type.trim().equals("type3"))
				project= new File("type3project.conf"); //bhl
				else 
			if(type.trim().equals("type4"))
				project= new File("type4project.conf");//taxonX and phenoscape
			
			//	 project= new File("project.conf");			
		BufferedReader in = new BufferedReader(new FileReader(project));
		String conf = in.readLine();
		conf = conf == null ? "" : conf;
		configurationText.setText(conf);
        Registry.ConfigurationDirectory = conf;

        String source = in.readLine();
        source = source == null ? "" : source;
        sourceText.setText(source);
        Registry.SourceDirectory = source;
        
        String target = in.readLine();
        target = target == null ? "" : target;
        targetText.setText(target);
        Registry.TargetDirectory = target;
        step3_desc.append(Registry.TargetDirectory+ApplicationUtilities.getProperty("TRANSFORMED"));
        
        String projDir = in.readLine();
        projDir = projDir==null?"":projDir;
        projectDirectory.setText(projDir);
        label_prjDir.setText(projDir);
        
		
		}catch(Exception e){
			LOGGER.error("couldn't load the configuration file", e);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
	}
	*/
	private void saveProject() {

		StringBuffer sb = new StringBuffer();
		this.dataPrefixCombo.setText(this.dataPrefixCombo.getText().toLowerCase());
		sb.append(configurationText.getText()).append("/\n");
		sb.append(sourceText.getText()).append("/\n");
		sb.append(targetText.getText());
		//save the main directory also
		sb.append("/\n").append(projectDirectory.getText());
		
		File project =null;
		try{
			System.out.println(type.equalsIgnoreCase(""));
			if(type.trim().equals(""))//that means fna is selected.. so save it to type1project.conf
				project = new File(System.getProperty("user.dir")+"/type1project.conf");
				else
					if(type.trim().equals("type2"))
						project = new File(System.getProperty("user.dir")+"/type2project.conf");
						
				else
					if(type.trim().equals("type3"))
						
						project = new File(System.getProperty("user.dir")+"/type3project.conf");
				else 
					if(type.trim().equals("type4"))
						project = new File(System.getProperty("user.dir")+"/type4project.conf");
						
				
				
				//project = new File(System.getProperty("user.dir")+"/project.conf");
			
			if(!project.exists()){
				project.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(project));
			out.write(sb.toString());
			out.close();
		}catch(Exception e){
			LOGGER.error("couldn't save project", e);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
	
	}
	
	private void startMarkup() {
		if(vd == null || !vd.isAlive()){
			MainForm.markupstarted  = true;
			//mainDb.createWordRoleTable();//roles are: op for plural organ names, os for singular, c for character, v for verb
			//mainDb.createNonEQTable();
			//mainDb.createTyposTable();
			String workdir = Registry.TargetDirectory;
			//if there is a characters folder,add the files in characters folder to descriptions folder
			mergeCharDescFolders(new File(workdir));
			String todofoldername = ApplicationUtilities.getProperty("DESCRIPTIONS");
			String databasename = ApplicationUtilities.getProperty("database.name");
			ProcessListener listener = new ProcessListener(findStructureTable, markupProgressBar, shell.getDisplay());
			
			vd = new VolumeDehyphenizer(listener, workdir, todofoldername,
					databasename, shell.getDisplay(), markUpPerlLog, 
					dataPrefixCombo.getText().replaceAll("-", "_").trim(), /*findDescriptorTable,*/ this);
			vd.start();
			
		}
	}
	
	private void mergeCharDescFolders(File parentfolder) {
		File charas = new File(parentfolder, ApplicationUtilities.getProperty("CHARACTERS"));
		if(charas.exists()){
			//add its files to Descriptions folder
			File descs = new File(parentfolder, ApplicationUtilities.getProperty("DESCRIPTIONS"));
			File[] cfiles = charas.listFiles();
			boolean nooverlap = true;
			for(File cfile: cfiles){
				//any risk of overwriting files?
				File target = new File(descs, cfile.getName());
				if(target.exists()){
					nooverlap = false;
					String messageHeader = ApplicationUtilities.getProperty("popup.header.warning");
					String message = ApplicationUtilities.getProperty("popup.warning.copyfiles");				
					ApplicationUtilities.showPopUpWindow(message, messageHeader, SWT.ICON_WARNING);
					break;
				}
			}
			if(nooverlap){
				for(File cfile: cfiles){
					//copy cfile to descs
					edu.arizona.sirls.biosemantics.parsing.Utilities.copyFile(cfile.getName(), charas, descs);
				}
			}
		}		
	}

	private boolean startFinalize(Text finalLog) {
		if(vf==null || !vf.isAlive()){
			//ProcessListener listener = new ProcessListener(finalizerTable, finalizerProgressBar, shell.getDisplay());
			ProcessListener listener = new ProcessListener(finalizerTable, null, shell.getDisplay());
	
			//Connection conn = null;
			try{
				if(conn == null){
					Class.forName(ApplicationUtilities.getProperty("database.driverPath"));
					conn = DriverManager.getConnection(ApplicationUtilities.getProperty("database.url"));
				}
				//this.mainDb.finalizeTermCategoryTable(); //moved to the end of step 6
				finalLog.setText("");
				vf = new VolumeFinalizer(listener, finalLog, 
						dataPrefixCombo.getText().replaceAll("-", "_").trim(), conn,getGlossary(MainForm.glossaryPrefixCombo.getText().trim()), shell.getDisplay());
				vf.start();
				//vf.join();
				System.out.println();
				return true;
			}catch(Exception e){
				StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
			}
		}
		return false;
	}
	
	/*private void removeBadStructuresFromTable(Table table) {
		// gather removed tag
		List<String> badStructures = new ArrayList<String>();
		int i=0;
		for (TableItem item : table.getItems()) {
			if (item.getChecked()) {
				badStructures.add(item.getText(1));
				table.getItem(i).setBackground(i,red);
			}
			i+=1;
		}
		// remove the tag from the database
		try {
			mainDb.setUnknownTags(badStructures);
		} catch (Exception exe) {
			LOGGER.error("Exception encountered in removing structures from database in MainForm:removeBadStructuresFromTable", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}

	}*/	
	
	/*private void loadTags(TabFolder tabFolder) {
		int XMLFileCount = loadTagTable(tabFolder);
		tagListCombo.add("PART OF LAST SENTENCE"); //part of the last sentence
		
		try {
			 mainDb.loadTagsData(tagListCombo, modifierListCombo);
			 if(XMLFileCount==0)
			 {
				@SuppressWarnings("unused")
				String messageHeader = ApplicationUtilities.getProperty("popup.header.info");
				@SuppressWarnings("unused")
				String message = ApplicationUtilities.getProperty("popup.load.nodata");				
					
			//	 ApplicationUtilities.showPopUpWindow(message, messageHeader,SWT.ICON_INFORMATION );
			 }
			} catch (Exception exe) {
				LOGGER.error("Exception encountered in loading tags from database in MainForm:loadTags", exe);
				StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		    }

	}*/
	
	/**
	 * simplified step 5
	 * @param tabFolder
	 * @return
	 */
	private int loadTagTable(TabFolder tabFolder) {
		//tagTable.removeAll();
		int XMLFileCount =0;
		try {
			 //if(mainDb.loadTagsTableData(tagTable)==0){
				//ApplicationUtilities.showPopUpWindow(
				//			ApplicationUtilities.getProperty("popup.info.unknownremoval"), 
				//			ApplicationUtilities.getProperty("popup.header.info"), SWT.ICON_INFORMATION);
				//this.tagListCombo.setText("");
				//this.modifierListCombo.setText("");
				try{
					if(conn == null){
						Class.forName("com.mysql.jdbc.Driver");
						String URL = ApplicationUtilities.getProperty("database.url");
						conn = DriverManager.getConnection(URL);
					}
				}catch(Exception e){
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
				String dataPrefix = MainForm.dataPrefixCombo.getText().replaceAll("-", "_").trim();
				String glosstable = getGlossary(MainForm.glossaryPrefixCombo.getText().trim());
				StateCollectorTest sct = new StateCollectorTest(conn, dataPrefix,true,glosstable, shell.getDisplay(), contextStyledText); /*using learned semanticroles only*/
				sct.collect();//markedsentence created in there.
				sct.saveStates();
				XMLFileCount = sct.grouping4GraphML();
				contextStyledText.append("Done! Ready to move to the next step.");
				//tabFolder.setSelection(4); //[general, step3, 4, 5, 6, 7] index starts at 0
				//tabFolder.setFocus();
			 //}
		} catch (Exception exe) {
				LOGGER.error("Exception encountered in loading tags from database in MainForm:loadTags", exe);
				StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		return XMLFileCount;
	}
	
	
	/*old step 5*/
	/*private int loadTagTable(TabFolder tabFolder) {
		tagTable.removeAll();
		int XMLFileCount =0;
		try {
			 if(mainDb.loadTagsTableData(tagTable)==0){
				ApplicationUtilities.showPopUpWindow(
							ApplicationUtilities.getProperty("popup.info.unknownremoval"), 
							ApplicationUtilities.getProperty("popup.header.info"), SWT.ICON_INFORMATION);
				this.tagListCombo.setText("");
				this.modifierListCombo.setText("");
				contextStyledText.setText("Preparing for the next step. ");
				contextStyledText.append("Please proceed to the next step when \"Done\" is displayed in this box.\n");
				try{
					if(conn == null){
						Class.forName("com.mysql.jdbc.Driver");
						String URL = ApplicationUtilities.getProperty("database.url");
						conn = DriverManager.getConnection(URL);
					}
				}catch(Exception e){
					StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
				}
				String dataPrefix = MainForm.dataPrefixCombo.getText().replaceAll("-", "_").trim();
				String glosstable = MainForm.glossaryPrefixCombo.getText().trim();
				StateCollectorTest sct = new StateCollectorTest(conn, dataPrefix,true,glosstable, shell.getDisplay(), contextStyledText); //using learned semanticroles only
				sct.collect();
				sct.saveStates();
				XMLFileCount = sct.grouping4GraphML();
				contextStyledText.append("Done! Ready to move to the next step.");
				//tabFolder.setSelection(4); //[general, step3, 4, 5, 6, 7] index starts at 0
				//tabFolder.setFocus();
			 }
		} catch (Exception exe) {
				LOGGER.error("Exception encountered in loading tags from database in MainForm:loadTags", exe);
				StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		return XMLFileCount;
	}*/
	
	private void updateContext(int sentid) throws ParsingException {
		contextStyledText.setText("");
		//tagListCombo.setText("");		
		try {
			mainDb.updateContextData(sentid, contextStyledText);
		} catch (Exception e) {
			LOGGER.error("Exception encountered in loading tags from database in MainForm:updateContext", e);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
			throw new ParsingException("Failed to execute the statement.", e);			
		}
	}
	
	private void applyTagToAll(){
		String tag = tagListCombo.getText();
		String modifier = this.modifierListCombo.getText();
		
		if (tag == null || tag.equals(""))
			return;
		
		TableItem[] items = tagTable.getItems();
		int i = 0;
		for (; i<items.length; i++) {
			//if (item.hashCode() == hashCodeOfItem) {			
			if (items[i].getChecked()) {
				items[i].setText(2, modifier);
				items[i].setText(3, tag);
				break;
			}
		}
		//auto forward to the next item
		
		if(i+1<items.length){
			i++;
			//now check the next item
			items[i].setChecked(true);
			items[i-1].setChecked(false);
			//show the context for the next item
        	updateContext(Integer.parseInt(items[i].getText(1)));
		}
	}
	
	/**
	 * This is used when Save is clicked on Step5.
	 * @param tagTable
	 * @throws ParsingException
	 * @throws SQLException
	 */
	/*private void saveTag(TabFolder tabFolder) {

		try {
			mainDb.saveTagData(tagTable);
			
		} catch (Exception exe) {
			LOGGER.error("Exception encountered in loading tags from database in MainForm:saveTag", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		loadTagTable(tabFolder);
		//reset context box
		//contextStyledText.setText("");
	}*/
	
	@SuppressWarnings("unused")
	private void reportGlossary() {
		
		LearnedTermsReport ltr = new LearnedTermsReport(ApplicationUtilities.getProperty("database.name") + "_corpus");
		glossaryStyledText.append(ltr.report());
	}
	
	private boolean checkFields(StringBuffer messageText, TabFolder tabFolder) {
		
		boolean errorFlag = false;
		
		if ( configurationText != null && configurationText.getText().equals("")) {
			messageText.append(ApplicationUtilities.getProperty("popup.error.config"));
		}  			
		if ( targetText != null && targetText.getText().equals("")) {
			messageText.append(ApplicationUtilities.getProperty("popup.error.target"));
		} 					
		if ( sourceText != null && sourceText.getText().equals("")) {
			messageText.append(ApplicationUtilities.getProperty("popup.error.source"));
		} 
		
		if (dataPrefixCombo != null && dataPrefixCombo.getText().replaceAll("-", "_").trim().equals("")) {
			
			messageText.append(ApplicationUtilities.getProperty("popup.error.dataset"));
			
		}
		
		if (messageText.length() != 0) {
			messageText.append(ApplicationUtilities.getProperty("popup.error.info"));
			ApplicationUtilities.showPopUpWindow(messageText.toString(), 
					ApplicationUtilities.getProperty("popup.header.missing"), SWT.ICON_WARNING);						
			tabFolder.setSelection(0);
			tabFolder.setFocus();
			errorFlag = true;
		} else {
			if(configurationText != null && !saveFlag) {
				errorFlag = false;
			}

		}
		
		return errorFlag;
	}
	
	/**
	 * This function will set the decisions for the character tab, step 5.
	 */
	private String[] setCharacterTabDecisions() {
		ArrayList<String> decisions = new ArrayList<String> ();
		
		try {
			charDb.getDecisionCategory(decisions);
		} catch (Exception exe) {
			LOGGER.error("Couldnt retrieve decision names" , exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		int count = 0;
		String [] strDecisions = new String[decisions.size()];
		for (String decision : decisions) {
			strDecisions[count++] = decision;
		}
		return strDecisions;
		//comboDecision.setItems(strDecisions);
		//comboDecision.setText(strDecisions[0]);
	}
	/**
	 * This function will set the default decisions for the character tab, step 5 when we use an empty glossary.
	 */
	private String[] setDefaultCharacterTabDecisions() {
		ArrayList<String> decisions = new ArrayList<String> ();
		
		try {
			charDb.getDefaultDecisionCategory(decisions);
		} catch (Exception exe) {
			LOGGER.error("Couldnt retrieve decision names" , exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		int count = 0;
		String [] strDecisions = new String[decisions.size()];
		for (String decision : decisions) {
			strDecisions[count++] = decision;
		}
		return strDecisions;
		//comboDecision.setItems(strDecisions);
		//comboDecision.setText(strDecisions[0]);
	}
	/**
	 * This function will prepare the character tab for display of co-occured terms
	 */
	private void setCharactertabGroups() {
		File directory = new File(Registry.TargetDirectory+"/"+
				ApplicationUtilities.getProperty("CHARACTER-STATES"));
		File [] files = directory.listFiles();
		/**Update the global variable with number of groups**/
		noOfTermGroups = files.length;
		
		String [] fileNames = new String[files.length];
		int count = 0, removedEdgesSize = removedEdges.size();
		sortedBy = new boolean [fileNames.length];
		for (File group : files) {
			sortedBy[count] = true;
			fileNames[count] = group.getName().substring(0, group.getName().indexOf(".xml"));
			if (removedEdgesSize == 0){
				/* RemovedEdges HashMap is intialized to store removed edges when
				 *  the user interacts with the terms*/
				removedEdges.put(fileNames[count], new ArrayList<String>());
			}
			
			count++;
		}
		
		groupsCombo.setItems(fileNames);	
		//add check for filename
		if(fileNames!=null && fileNames.length>0)
			{
			groupsCombo.setText(fileNames[0]);
			}
		else
		{
			groupsCombo.setText("");
			//print alert
			
			ApplicationUtilities.showPopUpWindow(
					ApplicationUtilities.getProperty("popup.load.nodata"), 
					ApplicationUtilities.getProperty("popup.header.info"), SWT.ICON_INFORMATION);
			/* no new terms, no need to upload the terms
			 * if(MainForm.upload2OTO){
				//UploadData ud = new UploadData(dataPrefixCombo.getText().replaceAll("-", "_").trim());
				ApplicationUtilities.showPopUpWindow(
						ApplicationUtilities.getProperty("popup.char.uploadterms2OTO"),
						ApplicationUtilities.getProperty("popup.header.info"), 
						SWT.OK);
			}*/		
		}			
		groupsCombo.select(0);
		
	}
	
	/***
	 * This function loads the character tab with the co-occurred terms
	 */
	private void loadTerms() {
		String groupName = groupsCombo.getText();
		CharacterGroupBean charGrpBean = groupInfo.get(groupName);
		int selectionIndex = groupsCombo.getSelectionIndex();
		if(charGrpBean == null || !charGrpBean.isSaved()){
			showTerms();
			//restore edges if they were removed but the group was not processed (saved)
			restoreUnsavedEdges();
			sortLabel.setImage(SWTResourceManager.getImage(MainForm.class, "/edu/arizona/sirls/biosemantics/parsing/down.jpg"));
		} else {
			/* Load a reviewd group from memory! */
			termsGroup = null; //should dispose handlers for termsGroup and removedTermsGroup, figure out how to do this later --when user revisit a previous group.
			//Control[] children = termsGroup.getChildren();
			//for(Control child: children) child.dispose();
			termsGroup = new Group(termsScrolledComposite, SWT.NONE);
			termsGroup.setLayoutData(new RowData());
			termsScrolledComposite.setContent(termsGroup);
			termsScrolledComposite.setMinSize(termsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
			removedTermsGroup = null;
			removedTermsGroup = new Group(removedScrolledComposite, SWT.NONE);
			removedTermsGroup.setLayoutData(new RowData());
			removedScrolledComposite.setContent(removedTermsGroup);
			removedScrolledComposite.setMinSize(removedTermsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
			ArrayList<CoOccurrenceBean> cooccurrences = (ArrayList<CoOccurrenceBean>)charGrpBean.getCooccurrences();
			
			if(cooccurrences.size() > 5) {
				
				/* If the number of rows is more than what is displayed, resize the group*/
				RowData rowdata = (RowData)termsGroup.getLayoutData();
				rowdata.height = cooccurrences.size() * 36;
				termsGroup.setLayoutData(new RowData(rowdata.width, rowdata.height));
		        Rectangle rect = termsGroup.getBounds();
		        rect.height = cooccurrences.size() * 36;
		        termsGroup.setBounds(rect);
				
				
				rowdata = (RowData)removedTermsGroup.getLayoutData();
				rowdata.height = cooccurrences.size() * 36;
				removedTermsGroup.setLayoutData(new RowData(rowdata.width, rowdata.height));
		        rect = removedTermsGroup.getBounds();
		        rect.height = cooccurrences.size() * 36;
		        removedTermsGroup.setBounds(rect);
			}
			
			/*Set the decision if it was saved*/
			comboDecision.setText(charGrpBean.getDecision());
			
			if (cooccurrences.size() != 0) {
				for (CoOccurrenceBean cbean : cooccurrences) {
					cbean.getContextButton().setParent(termsGroup);
					cbean.getContextButton().setSelection(false);
					cbean.getFrequency().setParent(termsGroup);
					if (cbean.getTerm1() != null){
						if (cbean.getTerm1().isTogglePosition()) {
							cbean.getTerm1().getTermGroup().setParent(termsGroup);
							cbean.getTerm1().setParentGroup(termsGroup);
							cbean.getTerm1().setDeletedGroup(removedTermsGroup);
						} else {
							cbean.getTerm1().getTermGroup().setParent(removedTermsGroup);
							cbean.getTerm1().setParentGroup(termsGroup);
							cbean.getTerm1().setDeletedGroup(removedTermsGroup);
						}
					}

					if (cbean.getTerm2() != null) {
						if (cbean.getTerm2().isTogglePosition()) {
							cbean.getTerm2().getTermGroup().setParent(termsGroup);
							cbean.getTerm2().setParentGroup(termsGroup);
							cbean.getTerm2().setDeletedGroup(removedTermsGroup);
						} else {
							cbean.getTerm2().getTermGroup().setParent(removedTermsGroup);
							cbean.getTerm2().setParentGroup(termsGroup);
							cbean.getTerm2().setDeletedGroup(removedTermsGroup);
						}
					}

				}
			}
			
			/*Resize the groups*/
			termsScrolledComposite.setMinSize(termsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			removedScrolledComposite.setMinSize(removedTermsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
				/** Show the correct sort order image */		
				if(sortedBy[selectionIndex]) {
					sortLabel.setImage(SWTResourceManager.getImage(MainForm.class, "/edu/arizona/sirls/biosemantics/parsing/down.jpg"));			
				} else {
					sortLabel.setImage(SWTResourceManager.getImage(MainForm.class, "/edu/arizona/sirls/biosemantics/parsing/up.jpg"));	
				
			}
			
		}
		
	}
	
	private void loadProcessedGroups() {
		try {
			ArrayList<String> processedGroupsList = charDb.getProcessedGroups();
			processedGroupsTable.removeAll();
			for (String groupName : processedGroupsList){
				processedGroups.put(groupName, groupName);
				TableItem item = new TableItem(processedGroupsTable, SWT.NONE);
				item.setText(groupName);
				
			}
		} catch (Exception exe) {
			LOGGER.error("Couldnt retrieve processedGroups terms" , exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
	}
	
	private void showTerms() {
		ArrayList<TermsDataBean> terms = null;
		boolean saved = false;
		term1.y = 10;
		term2.y = 10;
		contextRadio.y = 20;
		frequencyLabel.y = 20;
		ArrayList<CoOccurrenceBean> cooccurrences = new ArrayList<CoOccurrenceBean>();
		String decision = "";

		termsGroup = null; //dispose controls
		//Control[] children = termsGroup.getChildren();
		//for(Control child: children) child.dispose();
		termsGroup = new Group(termsScrolledComposite, SWT.NONE);
		termsGroup.setLayoutData(new RowData());
		termsScrolledComposite.setContent(termsGroup);
		termsScrolledComposite.setMinSize(termsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		removedTermsGroup = null;
		removedTermsGroup = new Group(removedScrolledComposite, SWT.NONE);
		removedTermsGroup.setLayoutData(new RowData());
		removedScrolledComposite.setContent(removedTermsGroup);
		removedScrolledComposite.setMinSize(removedTermsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				
		try {
			terms = charDb.getTerms(groupsCombo.getText());
			if(terms!=null && terms.size() != 0) {
					int groupId = ((TermsDataBean)terms.get(0)).getGroupId();
					decision = charDb.getDecision(groupId);
			}
			
		} catch (Exception exe) {
			LOGGER.error("Couldnt retrieve co-occurring terms" , exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}

		if (terms!=null && terms.size() > 5) {
			
			RowData rowdata = (RowData)termsGroup.getLayoutData();
			rowdata.height = terms.size() * 36;
			termsGroup.setLayoutData(new RowData(rowdata.width, rowdata.height));
	        Rectangle rect = termsGroup.getBounds();
	        rect.height = terms.size() * 36;
	        termsGroup.setBounds(rect);
			
			
			rowdata = (RowData)removedTermsGroup.getLayoutData();
			rowdata.height = terms.size() * 36;
			removedTermsGroup.setLayoutData(new RowData(rowdata.width, rowdata.height));
	        rect = removedTermsGroup.getBounds();
	        rect.height = terms.size() * 36;
	        removedTermsGroup.setBounds(rect);
			
		}
		unpaired = false;
		if(unPairedTerms(terms)){
			unpaired = true;
		}
		
		if (terms!=null && terms.size() != 0) {
			int radio_button_count=0;
			for (final TermsDataBean tbean : terms) {
				radio_button_count+=1;
				CoOccurrenceBean cbean = new CoOccurrenceBean();
				if (!(tbean.getTerm1() == null) && !tbean.getTerm1().equals("")) {
					Group term1Group = new Group(termsGroup, SWT.NONE);
					term1Group.setToolTipText(tbean.getTerm1());
					term1Group.setBounds(term1.x, term1.y, term1.width, term1.height);
					cbean.setTerm1(new TermBean(term1Group, removedTermsGroup, true, tbean.getTerm1()));
				}
				
				if (!(tbean.getTerm2() == null) && !tbean.getTerm2().equals("")) {
					Group term2Group = new Group(termsGroup, SWT.NONE);	
					term2Group.setToolTipText(tbean.getTerm2());
					term2Group.setBounds(term2.x, term2.y, term2.width, term2.height);
					cbean.setTerm2(new TermBean(term2Group, removedTermsGroup, true, tbean.getTerm2()));
				}else if(Integer.parseInt(groupsCombo.getText().replaceFirst("Group_", "")) ==noOfTermGroups && unpaired){ //fill combo boxes in place of term2 for the last group of the terms
					Text term1decision = new Text(termsGroup, SWT.BORDER);
					term1decision.setBounds(term2.x, term2.y+5, term2.width, term2.height-10);
					term1decision.setToolTipText("select a category for the term");
					term1decision.setEditable(true);
					cbean.setText(term1decision);
				}
				
				cbean.setGroupNo(tbean.getGroupId());
				cbean.setSourceFiles(tbean.getSourceFiles());
				cbean.setKeep(tbean.getKeep());
				//step 6 context code:
				final Button button = new Button(termsGroup, SWT.RADIO);
				//button.setText("radio_"+radio_button_count);
				button.setBounds(contextRadio.x, contextRadio.y, contextRadio.width, contextRadio.height);
				button.setToolTipText("Select to see the context sentences");
				button.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if (button.getSelection()) {
							contextTable.removeAll();
							try {
								//first show glossary defintions for the two terms in a pair
								String t1 = tbean.getTerm1();
								String t2 = tbean.getTerm2();
								if(conn == null){
									Class.forName("com.mysql.jdbc.Driver");
									String URL = ApplicationUtilities.getProperty("database.url");
									conn = DriverManager.getConnection(URL);
								}								
								String[] chinfo1 = edu.arizona.sirls.biosemantics.charactermarkup.Utilities.lookupCharacter(t1, conn, edu.arizona.sirls.biosemantics.charactermarkup.ChunkedSentence.characterhash, getGlossary(glossaryPrefixCombo.getText().trim()), dataPrefixCombo.getText().replaceAll("-", "_"));
								String[] chinfo2 = edu.arizona.sirls.biosemantics.charactermarkup.Utilities.lookupCharacter(t2, conn, edu.arizona.sirls.biosemantics.charactermarkup.ChunkedSentence.characterhash, getGlossary(glossaryPrefixCombo.getText().trim()), dataPrefixCombo.getText().replaceAll("-", "_"));
								ArrayList<ContextBean> contexts = charDb.getContext(tbean.getSourceFiles());
								TableItem item = new TableItem(contextTable, SWT.NONE);
								item.setText(new String[]{t1+":", chinfo1==null? "" : chinfo1[0]});
								item = new TableItem(contextTable, SWT.NONE);
								item.setText(new String[]{t2+":", chinfo2==null? "" : chinfo2[0]});
								//then show source sentences for the two terms
								
								for (ContextBean cbean : contexts){
									item = new TableItem(contextTable, SWT.NONE);
									//@TODO: style text here:	not possible using tableItem
									item.setText(new String[]{cbean.getSourceText(), cbean.getSentence()});
								}
								
								
							} catch (Exception exe) {
								LOGGER.error("Couldnt retrieve sentences terms" , exe);
								StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
							}
							
						}

					}
				});

				cbean.setContextButton(button);
				if (decision != null && !decision.equals("")){
					comboDecision.setText(decision);
					saved = true;
				}
				
				label = new Label(termsGroup, SWT.NONE);
				label.setBounds(frequencyLabel.x, frequencyLabel.y, frequencyLabel.width, frequencyLabel.height);
				label.setText(tbean.getFrequency()+ "");
				label.setToolTipText("Frequency of co-occurrence");
				cbean.setFrequency(label);
				cooccurrences.add(cbean);
				
				term1.y += standardIncrement;
				term2.y += standardIncrement;
				contextRadio.y += standardIncrement;
				frequencyLabel.y += standardIncrement;
			}
		}
		
		termsScrolledComposite.setMinSize(termsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		removedScrolledComposite.setMinSize(removedTermsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		CharacterGroupBean charGrpBean = new CharacterGroupBean(cooccurrences, groupsCombo.getText(), saved);
		groupInfo.put(groupsCombo.getText(), charGrpBean);
	}
	
	/**
	 * check and see if Terms contains only term1s
	 * @param terms
	 * @return
	 */
	private boolean unPairedTerms(ArrayList<TermsDataBean> terms) {
		if(terms==null) return false;
		Iterator<TermsDataBean> it = terms.iterator();
		while(it.hasNext()){
			TermsDataBean tdb = it.next();
			if(!(tdb.getTerm2()==null || tdb.getTerm2().length()==0)){
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the groupInfo
	 */
	public static HashMap<String, CharacterGroupBean> getGroupInfo() {
		return groupInfo;
	}
	
	private void restoreUnsavedEdges(){
		String group = groupsCombo.getText();
		if (!groupInfo.get(group).isSaved()) {
			ArrayList <String> edges = removedEdges.get(group);
			if (edges != null) {
				for (String edgeNodes : edges){
					String [] nodes = edgeNodes.split(",");
					if(nodes[0] != null && !nodes[0].equals("") && nodes[1] != null && !nodes[1].equals("") ) {
						ManipulateGraphML.insertEdge(new GraphNode(nodes[0]), new GraphNode(nodes[1]), 
								Registry.TargetDirectory+
									ApplicationUtilities.getProperty("CHARACTER-STATES")+ "/"+ group + ".xml");
					}

				}
			}
	
		}
	}

	/**
	 * @return the removedEdges
	 */
	public static HashMap<String, ArrayList<String>> getRemovedEdges() {
		return removedEdges;
	}
	
	/* This function loads the files, if any, to the respective tabs*/
	private void loadFileInfo(Table table, String directoryPath){
		File directory = new File(directoryPath);
		File [] files = directory.listFiles();
		int count = 0;
		int [] fileNumbers = new int[files.length];
		/* Will need to change this logic if the filenames are no long numbers but strings */
		for (File file : files) {
			String fileName = file.getName();
			fileNumbers[count++] = Integer.parseInt(fileName.substring(0, fileName.indexOf(".xml")));
		}
		
		Arrays.sort(fileNumbers);
		
		for (int fileNumber : fileNumbers) {			
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String [] {fileNumber+"", fileNumber+".xml"});
		}
		
		
	}
	
	//removedTermsGroups
	//0. Copy and create a removed terms list
	//1. Check if there are terms remaining - a function that returns true/false
	//2. Create a new group with the number one more than the existing groups.
	//3. Create an xml for the group in 2
	//4. Prepare the GUI for displaying the terms : 
	//      a) Ignore repeated terms 
	//      b)Ignore if terms were reinserted in some existing groups
	//5. Save operation - call the same function as before but create a termsdatabean and pass it along.
	//6. Go to 1
	
	/**
	 * This function creates remaining terms group for the character tab,
	 * as a unpaired group
	 */
	private void showRemainingTerms() {
		
		ArrayList<TermsDataBean> terms = null;
		term1.y = 10;
		term2.y = 10;
		contextRadio.y = 20;
		frequencyLabel.y = 20;
		ArrayList<CoOccurrenceBean> cooccurrences = new ArrayList<CoOccurrenceBean>();
		String decision = "";

		termsGroup = null;
		termsGroup = new Group(termsScrolledComposite, SWT.NONE);
		termsGroup.setLayoutData(new RowData());
		termsScrolledComposite.setContent(termsGroup);
		termsScrolledComposite.setMinSize(termsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		removedTermsGroup = null;
		removedTermsGroup = new Group(removedScrolledComposite, SWT.NONE);
		removedTermsGroup.setLayoutData(new RowData());
		removedScrolledComposite.setContent(removedTermsGroup);
		removedScrolledComposite.setMinSize(removedTermsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		
		int newGroupNumber = groupsCombo.getItemCount()+1;
		String newGroup = "Group_" + newGroupNumber;
		groupsCombo.add(newGroup);
		groupsCombo.setText(newGroup);
		groupsCombo.select(groupsCombo.getItemCount()-1);
		/*Generate the graph XML*/
		ArrayList <ArrayList> groups = null;
		/* Create the arraylist to create new terms list*/
		terms = getRemovedTerms(newGroupNumber);	
		/* Create the arraylist for Graph Visualization*/
		groups = createGraphML(terms);
		/* Create the GraphML */
		new GraphMLOutputter(false).output(groups, newGroupNumber);
		/* Create an entry in the removedEdges hashmap for the new group*/
		removedEdges.put(newGroup, new ArrayList<String>());
		/* Add an entry to the sort order */
		boolean [] newSortedBy = new boolean [sortedBy.length+1];
		for(int i = 0 ; i< sortedBy.length; i++) {
			newSortedBy[i] = sortedBy[i];
		}
		sortedBy = newSortedBy;

		if (terms.size() > 5) {
			
			RowData rowdata = (RowData)termsGroup.getLayoutData();
			rowdata.height = terms.size() * 36;
			termsGroup.setLayoutData(new RowData(rowdata.width, rowdata.height));
	        Rectangle rect = termsGroup.getBounds();
	        rect.height = terms.size() * 36;
	        termsGroup.setBounds(rect);
			
			
			rowdata = (RowData)removedTermsGroup.getLayoutData();
			rowdata.height = terms.size() * 36;
			removedTermsGroup.setLayoutData(new RowData(rowdata.width, rowdata.height));
	        rect = removedTermsGroup.getBounds();
	        rect.height = terms.size() * 36;
	        removedTermsGroup.setBounds(rect);
			
		}
		
		unpaired = false;
		if(unPairedTerms(terms)){
			unpaired = true;
		}
		
		if (terms.size() != 0) {
			
			for (final TermsDataBean tbean : terms) {
				CoOccurrenceBean cbean = new CoOccurrenceBean();
				if (!(tbean.getTerm1() == null) && !tbean.getTerm1().equals("")) {
					
				Group term1Group = new Group(termsGroup, SWT.NONE);
				term1Group.setToolTipText(tbean.getTerm1());
				term1Group.setBounds(term1.x, term1.y, term1.width, term1.height);
				cbean.setTerm1(new TermBean(term1Group, removedTermsGroup, true, tbean.getTerm1()));

				}
				
				if (!(tbean.getTerm2() == null) && !tbean.getTerm2().equals("")) {
					Group term2Group = new Group(termsGroup, SWT.NONE);	
					term2Group.setToolTipText(tbean.getTerm2());
					term2Group.setBounds(term2.x, term2.y, term2.width, term2.height);
					cbean.setTerm2(new TermBean(term2Group, removedTermsGroup, true, tbean.getTerm2()));
				}else if(Integer.parseInt(groupsCombo.getText().replaceFirst("Group_", "")) >=noOfTermGroups && unpaired){ //fill combo boxes in place of term2 for the last group of the terms
					Text term1decision = new Text(termsGroup, SWT.BORDER);
					term1decision.setBounds(term2.x, term2.y+5, term2.width, term2.height-10);
					term1decision.setToolTipText("select a category for the term");
					term1decision.setEditable(true);
					cbean.setText(term1decision);
				}
				
				cbean.setGroupNo(tbean.getGroupId());
				cbean.setSourceFiles(tbean.getSourceFiles());
				cbean.setKeep(tbean.getKeep());
				
				final Button button = new Button(termsGroup, SWT.RADIO);
				button.setBounds(contextRadio.x, contextRadio.y, contextRadio.width, contextRadio.height);
				button.setToolTipText("Select to see the context sentences");
				button.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if (button.getSelection()) {
							contextTable.removeAll();
							try {
								//first show glossary defintions for the two terms in a pair
								String t1 = tbean.getTerm1();
								String t2 = tbean.getTerm2();
								if(conn == null){
									Class.forName("com.mysql.jdbc.Driver");
									String URL = ApplicationUtilities.getProperty("database.url");
									conn = DriverManager.getConnection(URL);
								}								
								String ch1 = (edu.arizona.sirls.biosemantics.charactermarkup.Utilities.lookupCharacter(t1, conn, edu.arizona.sirls.biosemantics.charactermarkup.ChunkedSentence.characterhash, getGlossary(glossaryPrefixCombo.getText().trim()), dataPrefixCombo.getText().replaceAll("-", "_"))[0].trim());
								String ch2 = (edu.arizona.sirls.biosemantics.charactermarkup.Utilities.lookupCharacter(t2, conn, edu.arizona.sirls.biosemantics.charactermarkup.ChunkedSentence.characterhash, getGlossary(glossaryPrefixCombo.getText().trim()), dataPrefixCombo.getText().replaceAll("-", "_"))[0].trim());
								ArrayList<ContextBean> contexts = charDb.getContext(tbean.getSourceFiles());
								TableItem item = new TableItem(contextTable, SWT.NONE);
								item.setText(new String[]{t1+":", ch1});
								item = new TableItem(contextTable, SWT.NONE);
								item.setText(new String[]{t2+":", ch2});
								//then show source sentences for the two terms
								for (ContextBean cbean : contexts){
									item = new TableItem(contextTable, SWT.NONE);
									item.setText(new String[]{cbean.getSourceText(), cbean.getSentence()});
								}								
								
							} catch (Exception exe) {
								LOGGER.error("Couldnt retrieve sentences terms" , exe);
								StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
							}
							
						}

					}
				});

				cbean.setContextButton(button);
				if (decision != null && !decision.equals("")){
					comboDecision.setText(decision);
				}
				
				Label label = new Label(termsGroup, SWT.NONE);
				label.setBounds(frequencyLabel.x, frequencyLabel.y, frequencyLabel.width, frequencyLabel.height);
				label.setText(tbean.getFrequency()+ "");
				label.setToolTipText("Frequency of co-occurrence");
				cbean.setFrequency(label);
				cooccurrences.add(cbean);
				
				term1.y += standardIncrement;
				term2.y += standardIncrement;
				contextRadio.y += standardIncrement;
				frequencyLabel.y += standardIncrement;
			}
		}
		
		termsScrolledComposite.setMinSize(termsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		removedScrolledComposite.setMinSize(removedTermsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		CharacterGroupBean charGrpBean = new CharacterGroupBean(cooccurrences, groupsCombo.getText(), false);
		groupInfo.put(groupsCombo.getText(), charGrpBean);
		
		/* Save the newly formed group to db*/
		try {
			charDb.saveTerms(terms);
		} catch(Exception exe){
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
	}
	
	/***
	 * This function creates the graph for the remaining character tab terms
	 * @param terms
	 * @return
	 */
	private ArrayList<ArrayList> createGraphML(ArrayList<TermsDataBean> terms) {
		ArrayList<ArrayList> group = new ArrayList<ArrayList>();
		ArrayList<ArrayList> groups = new ArrayList<ArrayList>();
		for (TermsDataBean tbean : terms){
			ArrayList coTerms = new ArrayList();
			if(tbean.getTerm1() != null) {
				coTerms.add(tbean.getTerm1());
			} else {
				coTerms.add("");
			}
			if(tbean.getTerm2() != null) {
				coTerms.add(tbean.getTerm2());
			} else {
				coTerms.add("");
			}
			if(coTerms.size() != 0){
				groups.add(coTerms);
			}
		}
		group.add(groups);
		return group;
	}
	
	/***
	 * This function will get all the removed terms and form the pool
	 * @param groupNo
	 * @return
	 */
	private ArrayList<TermsDataBean> getRemovedTerms(int groupNo) {
		ArrayList <TermsDataBean> terms = new ArrayList<TermsDataBean>();

			Set <String> keys = groupInfo.keySet();
			for (String key : keys){	
				CharacterGroupBean charBean = groupInfo.get(key);
				if (charBean.isSaved()){
					terms.addAll(getRemovedTermsInformation (charBean, groupNo));
				}
				
			}			

		
		return terms;
	}
	
	/**
	 * This is a helper internal function that gets the specific removed 
	 * terms information from the character tab's removed terms'
	 * Remove duplicate words, remove words already in glossary, and sort all words in one colume, i.e. term1
	 * @param charGroupBean
	 * @param groupNo
	 * @return
	 */
	private ArrayList <TermsDataBean> getRemovedTermsInformation (CharacterGroupBean charGroupBean, int groupNo){
		
		ArrayList <TermsDataBean> terms = new ArrayList<TermsDataBean>();	
		ArrayList <CoOccurrenceBean> cooccurrences = charGroupBean.getCooccurrences();
		String words = "";
		for (CoOccurrenceBean  bean : cooccurrences) {
			
			if(bean.getTerm1() != null) {
				if (!bean.getTerm1().isTogglePosition()) {
					String t1 = bean.getTerm1().getTermText().getText();
					words = words.replaceFirst("\\|$", "");
					if(!t1.matches("("+words+")") && !Utilities.inGlossary(t1, conn, getGlossary(this.glossaryPrefixCombo.getText()), this.dataPrefixCombo.getText())){
						words +=t1+"|";
						TermsDataBean tbean = new TermsDataBean();
						tbean.setFrequency(Integer.parseInt(bean.getFrequency().getText()));
						tbean.setSourceFiles(bean.getSourceFiles());
						tbean.setGroupId(groupNo);
						tbean.setTerm1(t1);
						tbean.setTerm2("");
						terms.add(tbean);
					}
					// Remove the term from the original group of removed terms
					bean.setTerm1(null);
				}
		    }

			if(bean.getTerm2() != null) {
				if (!bean.getTerm2().isTogglePosition()) {
					String t2 = bean.getTerm2().getTermText().getText();
					words = words.replaceFirst("\\|$", "");
					if(!t2.matches("("+words+")")&& !Utilities.inGlossary(t2, conn, getGlossary(this.glossaryPrefixCombo.getText()), this.dataPrefixCombo.getText())){
						words +=t2+"|";
						TermsDataBean tbean = new TermsDataBean();
						tbean.setFrequency(Integer.parseInt(bean.getFrequency().getText()));
						tbean.setSourceFiles(bean.getSourceFiles());
						tbean.setGroupId(groupNo);
						tbean.setTerm1(t2);
						tbean.setTerm2("");
						terms.add(tbean);
					}
					// Remove the term from the original group of removed terms
					bean.setTerm2(null);
				}
		    }
		}

		return terms;
	}
	
	/**
	 * This is a helper internal function that gets the specific removed 
	 * terms information from the character tab's removed terms'
	 * @param charGroupBean
	 * @param groupNo
	 * @return
	 */
	/*private ArrayList <TermsDataBean> getRemovedTermsInformation (CharacterGroupBean charGroupBean, int groupNo){
		
		ArrayList <TermsDataBean> terms = new ArrayList<TermsDataBean>();	
		ArrayList <CoOccurrenceBean> cooccurrences = charGroupBean.getCooccurrences();
		
		for (CoOccurrenceBean  bean : cooccurrences) {
			
			TermsDataBean tbean = new TermsDataBean();
			tbean.setFrequency(Integer.parseInt(bean.getFrequency().getText()));
			tbean.setSourceFiles(bean.getSourceFiles());
			tbean.setGroupId(groupNo);
			if(bean.getTerm1() != null) {
				if (!bean.getTerm1().isTogglePosition()) {
					tbean.setTerm1(bean.getTerm1().getTermText().getText());
					//Remove the term from the original group of removed terms
					bean.setTerm1(null);
					
			    } else {
			    	tbean.setTerm1("");
			    }
			} else {
		    	tbean.setTerm1("");
		    }

			if (bean.getTerm2() != null){
				if(!bean.getTerm2().isTogglePosition()){
					tbean.setTerm2(bean.getTerm2().getTermText().getText());
					// Remove the term from the original group of removed terms
					bean.setTerm2(null);
					
			    } else {
			    	tbean.setTerm2("");
			    }
			} else {
		    	tbean.setTerm2("");
		    }

			if (tbean.getTerm1() != null && tbean.getTerm2() != null) {
				if (!tbean.getTerm1().equals("")|| 
						!tbean.getTerm2().equals("")) {
					terms.add(tbean);
				}
			}
			
		}
		
		return terms;
	}*/
	
	/**
	 * This function checks how 
	 * many groups were saved
	 * @return
	 */  	
	private int getNumberOfGroupsSaved(){
		int returnValue = 0;
		Set <String>
		keys = groupInfo.keySet();
		for (String key : keys){
			CharacterGroupBean charGrpBean = groupInfo.get(key);
			if(charGrpBean.isSaved()) {
				returnValue ++;
			}
		}
		return returnValue;
	}
	
	/**
	 * This function checks if there are terms remaining that are not yet grouped.
	 * @return boolean
	 */
	
	private boolean isTermsNotGrouped(){
		boolean returnValue = false;
		Set <String>
		keys = groupInfo.keySet();
		for (String key : keys){
			CharacterGroupBean charGrpBean = groupInfo.get(key);
			ArrayList <CoOccurrenceBean> cooccurrences = charGrpBean.getCooccurrences();
			for (CoOccurrenceBean  bean : cooccurrences) {
				if((bean.getTerm1() != null && !bean.getTerm1().isTogglePosition()) ||
						(bean.getTerm2() != null && !bean.getTerm2().isTogglePosition())){
						returnValue = true;
						break;
				    } 
				}
			if(returnValue) {
				break;
			}
		}
		return returnValue;
	}
	
	private int getType(String type) {
		
		if(type.trim().equalsIgnoreCase(""))
			return 1;
		
		if(type.equals("type2"))
			return 2;
		
		if(type.equals("type3"))
			return 3;
		
		if(type.equals("type4"))
			return 4;
		else
			return 0;
	}
	

	
	/**
	 * this procedure seems to be slow and only a handful of terms are filtered.
	 * 1. search db for candidate structure terms
	 * 2. apply heuristic rules to filter the terms
	 * 		2.1 pos = v|adv
	 * 		2.2 does not ...
	 * 		2.3 by [means] of
	 * 3. filtered terms are not displayed and they will not be saved to wordroles table as "os" or "op".
	 * 4. terms filtered by 2.1.adv or 2.3 will be saved in NONEQTERMSTABLE
	 * 5. cache results to reduce cost
	 * 
	 * @return filtered candidate structure words
	 */
	private ArrayList<String> fetchStructureTerms(StyledText contextText){
		ArrayList <String> words = new ArrayList<String>();
		ArrayList <String> filteredwords = new ArrayList<String>();
		ArrayList <String> noneqwords = new ArrayList<String>();
		try{
			if(conn == null){
				Class.forName(ApplicationUtilities.getProperty("database.driverPath"));
				conn = DriverManager.getConnection(ApplicationUtilities.getProperty("database.url"));
			}
			String prefix = dataPrefixCombo.getText().replaceAll("-", "_").trim();
			VolumeMarkupDbAccessor vmdb = new VolumeMarkupDbAccessor(prefix,getGlossary(glossaryPrefixCombo.getText()).trim());
			words = vmdb.structureTags4Curation(words);
			for(String word: words){
				if(word.length()==0) continue;
				if(word.startsWith("[") && word.endsWith("]")) continue;
				//before structure terms are set, partOfPrepPhrases can not be reliability determined
				if(Utilities.mustBeVerb(word, MainForm.conn, prefix) || Utilities.mustBeAdv(word) /*|| Utilities.partOfPrepPhrase(word, this.conn, prefix)*/){
					//if(Utilities.mustBeAdv(word) /*|| Utilities.partOfPrepPhrase(word, this.conn, prefix)*/){
						noneqwords.add(word);
						contextText.append(word+" is excluded\n");
					//}					
					continue;
				}
				filteredwords.add(word);
			}
			mainDb.recordNonEQTerms(noneqwords, null, null);
			words = null;
		}catch(Exception e){
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		conn = null;
		return filteredwords;
	}

	/**
	 * 1. search db for candidate character terms
	 * 2. apply heuristic rules to filter the terms
	 * 		2.1 pos = adv
	 * 		2.2 by [means] of
	 * 3. filtered terms are not displayed and they will not be saved to wordroles table as "os" or "op".
	 * 4. terms filtered by 2.1.adv or 2.2 will be saved in NONEQTERMSTABLE
	 * 5. cache results to reduce cost
	 * @return filtered candidate character words
	 */
	private ArrayList<String> fetchCharacterTerms(StyledText contextText){
		ArrayList <String> words = new ArrayList<String>();;
		ArrayList <String> filteredwords = new ArrayList<String>();
		ArrayList <String> noneqwords = new ArrayList<String>();
		try{
			if(conn == null){
				Class.forName(ApplicationUtilities.getProperty("database.driverPath"));
				conn = DriverManager.getConnection(ApplicationUtilities.getProperty("database.url"));
			}
			String prefix = dataPrefixCombo.getText().replaceAll("-", "_").trim();
			VolumeMarkupDbAccessor vmdb = new VolumeMarkupDbAccessor(prefix, getGlossary(glossaryPrefixCombo.getText().trim()));
			words = (ArrayList<String>)vmdb.descriptorTerms4Curation();
			for(String word: words){
				if(Utilities.mustBeVerb(word, conn, prefix) || Utilities.mustBeAdv(word) /*|| Utilities.partOfPrepPhrase(word, this.conn, prefix)*/){
					noneqwords.add(word);
					//display filtered word in the context box
					contextText.append(word+" is excluded\n");
					continue;
				}
				filteredwords.add(word);
			}
			mainDb.recordNonEQTerms(noneqwords, null, null);
			words = null;
			
		}catch(Exception e){
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		conn = null;
		return filteredwords;	
		}
	
	private ArrayList<String> fetchContentTerms(StyledText contextText) {
		ArrayList<String> words = new ArrayList<String>();
		ArrayList <String> filteredwords = new ArrayList<String>();
		ArrayList <String> noneqwords = new ArrayList<String>();
		try{
			
			VolumeMarkupDbAccessor vmdb = new VolumeMarkupDbAccessor(dataPrefixCombo.getText().replaceAll("-", "_").trim(),getGlossary(glossaryPrefixCombo.getText().trim()));
			if(inistructureterms==null || inistructureterms.size()==0){
				inistructureterms = vmdb.structureTags4Curation(new ArrayList<String>());
			}
			if(inicharacterterms==null || inicharacterterms.size()==0){
				inicharacterterms = vmdb.descriptorTerms4Curation();
			}
			words=(ArrayList<String>)vmdb.contentTerms4Curation(words, inistructureterms, inicharacterterms);
			if(conn == null){
				Class.forName(ApplicationUtilities.getProperty("database.driverPath"));
				conn = DriverManager.getConnection(ApplicationUtilities.getProperty("database.url"));
			}
			String prefix = dataPrefixCombo.getText().replaceAll("-", "_").trim();
			for(String word: words){
				if(Utilities.mustBeVerb(word, conn, prefix) || Utilities.mustBeAdv(word) /*|| Utilities.partOfPrepPhrase(word, this.conn, prefix)*/){
					noneqwords.add(word);
					contextText.append(word+" is excluded\n");
					continue;
				}
				filteredwords.add(word);
			}
			mainDb.recordNonEQTerms(noneqwords, null, null);
			words = null;
		}catch(Exception e){
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);e.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		return filteredwords;
	}
	
	/**
	 * when user clicks a hiden tab in term review step.
	 * @param termRoleMatrix
	 * @param scrolledComposite
	 * @param contextText
	 * @param type
	 */
	protected void reLoadTermArea(Composite termRoleMatrix, ScrolledComposite scrolledComposite, final StyledText contextText, final String type){
		int count = 0;
		try {
			if(termRoleMatrix.isDisposed()){
				ApplicationUtilities.showPopUpWindow(
						"Term categorization has been saved and the process can not be redone.", 
						ApplicationUtilities.getProperty("popup.header.info"), SWT.ICON_INFORMATION);
				return;
			}
			final int y = 10; //height of a row
			int m = 1; //vertical margin
			Hashtable <String, String> words = null;
			Hashtable<String, String> categorizedterms = null;
			if(type.compareToIgnoreCase("structures") ==0){
				words = categorizedtermsS;
				categorizedterms = categorizedtermsS; //the global variable categorizedtermsS is populated when the local variable thiscategorizedterms is populated below
			}
			if(type.compareToIgnoreCase("characters") ==0){
				words = categorizedtermsC;
				categorizedterms = categorizedtermsC;
			}
			if(type.compareToIgnoreCase("others") ==0){
				words = categorizedtermsO;
				categorizedterms = categorizedtermsO;
			}
			termRoleMatrix.setSize(744, words.size()*y);
			scrolledComposite.setContent(termRoleMatrix);
			termRoleMatrix.setVisible(true);
			final Hashtable<String, String> thiscategorizedterms = categorizedterms;
			if (words != null) {
				ArrayList<Control> tabList = new ArrayList<Control>();
				Enumeration<String> en = words.keys();
				while(en.hasMoreElements()){
					String word = en.nextElement();
					String cat = words.get(word);
					thiscategorizedterms.put(word, type); //populate term list 
					count++;					
					final Composite termRoleGroup = new Composite(termRoleMatrix, SWT.NONE);
					termRoleGroup.setLayoutData(new RowLayout(SWT.HORIZONTAL));	
					if(count % 2 == 0){
						termRoleGroup.setBackground(grey);
					}
					termRoleGroup.setBounds(0, (count-1)*y, 744, y);
					//show context info				
					termRoleGroup.addMouseListener(new MouseListener(){
						@Override
						public void mouseDoubleClick(MouseEvent e) {}
						@Override
						public void mouseDown(MouseEvent e) {
							Control[] controls = termRoleGroup.getChildren();
							if(controls[1] instanceof Label){
								String term = ((Label)controls[1]).getText().trim();
				  				try {
				  					contextText.setText("");
				  					contextText.setTopMargin(2);
									mainDb.getContextData(term, contextText);
								} catch (ParsingException e1) {
									e1.printStackTrace();
								} catch (SQLException e1) {
									e1.printStackTrace();
								}	
							}
						}
						@Override
						public void mouseUp(MouseEvent e) {}
					});
					
					Label clabel = new Label(termRoleGroup, SWT.NONE);
					clabel.setText(" "+count);
					if(count%2 == 0) clabel.setBackground(grey);
					clabel.setBounds(15, (count-1)*y+m, 93, y-2*m);
					
					
					//replace label with text, so the reviewer may correct the typo in the terms
					/*Text ttext = new Text(termRoleGroup, SWT.NONE);
					ttext.setText(word);
					if(count%2 == 0) ttext.setBackground(grey);
					ttext.setBounds(125, (count-1)*y+m, 150, y-2*m);*/
					
					final Label tlabel = new Label(termRoleGroup, SWT.NONE);
					tlabel.setText(word);
					if(count%2 == 0) tlabel.setBackground(grey);
					tlabel.setBounds(125, (count-1)*y+m, 150, y-2*m);
					

					tlabel.addMouseListener(new MouseListener(){

						@Override
						public void mouseDoubleClick(MouseEvent arg0) {
							
							final Shell typodialog = new Shell(shell, SWT.DIALOG_TRIM|SWT.APPLICATION_MODAL);
							typodialog.setText("Correct Typo");
							Label label = new Label(typodialog, SWT.NULL);
							final String theword = tlabel.getText();
						    label.setText("Change "+theword);
						    label.setBounds(10, 10, 150, 23);
						    
						    label = new Label(typodialog, SWT.NULL);
						    label.setText("To " );
						    label.setBounds(10, 35, 20, 23);
						    
						    final Text text = new Text(typodialog, SWT.BORDER);
						    text.setBounds(30, 35, 100, 23);
						    
						    Button change = new Button(typodialog, SWT.PUSH);
						    change.setBounds(10, 70, 50, 23 );
						    change.setText("Correct");
						    change.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(final SelectionEvent e) {
									String term = text.getText();
									//rewrite the label
									tlabel.setText(term);
									//update term hashes
									if(type.compareToIgnoreCase("structures") ==0){
										categorizedtermsS.put(term, categorizedtermsS.get(theword));
										categorizedtermsS.remove(theword);
									}
									if(type.compareToIgnoreCase("characters") ==0){
										categorizedtermsC.put(term, categorizedtermsC.get(theword));
										categorizedtermsC.remove(theword);
									}
									if(type.compareToIgnoreCase("others") ==0){
										categorizedtermsO.put(term, categorizedtermsO.get(theword));
										categorizedtermsO.remove(theword);
									}
									
									//updata sentence table first because context box uses it and needs also be refreshed 
									mainDb.correctTypoInTableWordMatch("sentence", "originalsent",  theword, term, "sentid");
									//save corrections
									mainDb.insertTypo(theword, term); //to db table, can be read in in a scenario of stop and resume
									//if (term, theword) is in typos, remove the record, don't add
									if(typos.get(term)!=null && typos.get(term).compareToIgnoreCase(theword)==0) typos.remove(term);
									else typos.put(theword, term); //in-memoy, save the typos/corrections and make corrections in source text and tables at the beginning of step 5.
									typodialog.dispose();
								}
							});
						    
						    Button cancel = new Button(typodialog, SWT.PUSH);
						    cancel.setBounds(70, 70, 50, 23 );
						    cancel.setText("Cancel");
						    cancel.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(final SelectionEvent e) {
									typodialog.dispose();
								}
							});
							typodialog.pack();
							typodialog.open();
						}

						@Override
						public void mouseDown(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseUp(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
						
					});
					
					final Button button_1 = new Button(termRoleGroup, SWT.RADIO);
					button_1.setBounds(325, (count-1)*y+m, 90, y-2*m);			
					if(cat.compareToIgnoreCase("structures")==0) button_1.setSelection(true);
					if(count%2 == 0) button_1.setBackground(grey);
					tabList.add(button_1);
					button_1.addListener(SWT.Selection, new Listener() {
					      public void handleEvent(Event e) {
					    	  Control[] controls = button_1.getParent().getChildren();
					    	  if(controls[1] instanceof Label){
					    		 String term = ((Label)controls[1]).getText().trim();
					    		//String term = ((Text)controls[1]).getText().trim();
						    	 thiscategorizedterms.put(term, "structures");
					    	  }
					      }						
					});
					
					final Button button_2 = new Button(termRoleGroup, SWT.RADIO);
					button_2.setBounds(425, (count-1)*y+m, 90, y-2*m);
					//button_2.setSelection(true);//This can't be done. It will waste all the learning perl completed: For use cases where the person who runs charaparser needs another person to review the terms. Here mark all terms as "descriptor" by default so they will all be loaded to OTO for review
					if(cat.compareToIgnoreCase("characters")==0) button_2.setSelection(true);
					if(count%2 == 0) button_2.setBackground(grey);
					tabList.add(button_2);
					button_2.addListener(SWT.Selection, new Listener() {
					      public void handleEvent(Event e) {
					    	  Control[] controls = button_2.getParent().getChildren();
					    	  if(controls[1] instanceof Label){
					    		 String term = ((Label)controls[1]).getText().trim();
					    		 // String term = ((Text)controls[1]).getText().trim();
						    	 thiscategorizedterms.put(term, "characters");
					    	  }
					      }						
					});
					
					final Button button_3 = new Button(termRoleGroup, SWT.RADIO);
					button_3.setBounds(525, (count-1)*y+m, 90, y-2*m);
					if(cat.compareToIgnoreCase("others")==0) button_3.setSelection(true);
					if(count%2 == 0) button_3.setBackground(grey);
					tabList.add(button_3);
					button_3.addListener(SWT.Selection, new Listener() {
					      public void handleEvent(Event e) {
					    	  Control[] controls = button_3.getParent().getChildren();
					    	  if(controls[1] instanceof Label){
					    		 String term = ((Label)controls[1]).getText().trim();
					    		 //String term = ((Text)controls[1]).getText().trim();
						    	 thiscategorizedterms.put(term, "others");
					    	  }
					      }						
					});
					
					Label invisible = new Label(termRoleGroup, SWT.NONE);
					invisible.setBounds(720, (count-1)*y+m, 90, y-2*m);
					invisible.setText("invisible");
					invisible.setVisible(false);
					
					clabel.pack();
					tlabel.pack();
					//ttext.pack();
					button_1.pack();
					button_2.pack();
					button_3.pack();
					termRoleGroup.pack();
					termRoleGroup.redraw();
				}
				termRoleMatrix.pack();
				//termRoleMatrix.setTabList(tabList.toArray(new Control[]{}));
			}			
		} catch (Exception exe){
			LOGGER.error("unable to load subtab in Markup : MainForm", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
	}
	
	protected void loadTermArea(Composite termRoleMatrix, ScrolledComposite scrolledComposite, ArrayList <String> words, final StyledText contextText, final String type) {
		int count = 0;
		try {
			if(termRoleMatrix.isDisposed()){
				ApplicationUtilities.showPopUpWindow(
						"Term categorization has been saved and the process can not be redone.", 
						ApplicationUtilities.getProperty("popup.header.info"), SWT.ICON_INFORMATION);
				return;
			}
			final int y = 10; //height of a row
			int m = 1; //vertical margin
			termRoleMatrix.setSize(744, words.size()*y);
			scrolledComposite.setContent(termRoleMatrix);
			termRoleMatrix.setVisible(true);
			Hashtable<String, String> categorizedterms = null;
			if(type.compareToIgnoreCase("structures") ==0){
				categorizedterms = categorizedtermsS; //the global variable categorizedtermsS is populated when the local variable thiscategorizedterms is populated below
			}
			if(type.compareToIgnoreCase("characters") ==0){
				categorizedterms = categorizedtermsC;
			}
			if(type.compareToIgnoreCase("others") ==0){
				categorizedterms = categorizedtermsO;
			}
			final Hashtable<String, String> thiscategorizedterms = categorizedterms;
			if (words != null) {
				ArrayList<Control> tabList = new ArrayList<Control>();
				for (String word : words){
					thiscategorizedterms.put(word, type); //populate term list 
					count++;					
					final Composite termRoleGroup = new Composite(termRoleMatrix, SWT.NONE);
					termRoleGroup.setLayoutData(new RowLayout(SWT.HORIZONTAL));	
					if(count % 2 == 0){
						termRoleGroup.setBackground(grey);
					}
					termRoleGroup.setBounds(0, (count-1)*y, 744, y);
					//show context info				
					termRoleGroup.addMouseListener(new MouseListener(){
						@Override
						public void mouseDoubleClick(MouseEvent e) {}
						@Override
						public void mouseDown(MouseEvent e) {
							Control[] controls = termRoleGroup.getChildren();
							if(controls[1] instanceof Label){
								String term = ((Label)controls[1]).getText().trim();
				  				try {
				  					contextText.setText("");
				  					contextText.setTopMargin(2);
									mainDb.getContextData(term, contextText);
								} catch (ParsingException e1) {
									e1.printStackTrace();
								} catch (SQLException e1) {
									e1.printStackTrace();
								}	
							}
						}
						@Override
						public void mouseUp(MouseEvent e) {}
					});
					
					Label clabel = new Label(termRoleGroup, SWT.NONE);
					clabel.setText(" "+count);
					if(count%2 == 0) clabel.setBackground(grey);
					clabel.setBounds(15, (count-1)*y+m, 93, y-2*m);
					
					
					//replace label with text, so the reviewer may correct the typo in the terms
					/*Text ttext = new Text(termRoleGroup, SWT.NONE);
					ttext.setText(word);
					if(count%2 == 0) ttext.setBackground(grey);
					ttext.setBounds(125, (count-1)*y+m, 150, y-2*m);*/
					
					final Label tlabel = new Label(termRoleGroup, SWT.NONE);
					tlabel.setText(word);
					if(count%2 == 0) tlabel.setBackground(grey);
					tlabel.setBounds(125, (count-1)*y+m, 150, y-2*m);
					

					tlabel.addMouseListener(new MouseListener(){

						@Override
						public void mouseDoubleClick(MouseEvent arg0) {
							
							final Shell typodialog = new Shell(shell, SWT.DIALOG_TRIM|SWT.APPLICATION_MODAL);
							typodialog.setText("Correct Typo");
							Label label = new Label(typodialog, SWT.NULL);
							final String theword = tlabel.getText();
						    label.setText("Change "+theword);
						    label.setBounds(10, 10, 150, 23);
						    
						    label = new Label(typodialog, SWT.NULL);
						    label.setText("To " );
						    label.setBounds(10, 35, 20, 23);
						    
						    final Text text = new Text(typodialog, SWT.BORDER);
						    text.setBounds(30, 35, 100, 23);
						    
						    Button change = new Button(typodialog, SWT.PUSH);
						    change.setBounds(10, 70, 50, 23 );
						    change.setText("Correct");
						    change.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(final SelectionEvent e) {
									String term = text.getText();
									//rewrite the label
									tlabel.setText(term);
									//update term hashes with the correct spelling, keep the existing categorization
									if(type.compareToIgnoreCase("structures") ==0){
										categorizedtermsS.put(term, categorizedtermsS.get(theword));
										categorizedtermsS.remove(theword);
									}
									if(type.compareToIgnoreCase("characters") ==0){
										categorizedtermsC.put(term, categorizedtermsC.get(theword));
										categorizedtermsC.remove(theword);
									}
									if(type.compareToIgnoreCase("others") ==0){
										categorizedtermsO.put(term, categorizedtermsO.get(theword));
										categorizedtermsO.remove(theword);
									}
									
									//updata sentence table first because context box uses it and needs also be refreshed 
									mainDb.correctTypoInTableWordMatch("sentence", "originalsent",  theword, term, "sentid");
									//save corrections
									mainDb.insertTypo(theword, term); //to db table, can be read in in a scenario of stop and resume
									//if (term, theword) is in typos, remove the record, don't add
									if(typos.get(term)!=null && typos.get(term).compareToIgnoreCase(theword)==0) typos.remove(term);
									else typos.put(theword, term); //in-memoy, save the typos/corrections and make corrections in source text and tables at the beginning of step 5.
									typodialog.dispose();
								}
							});
						    
						    Button cancel = new Button(typodialog, SWT.PUSH);
						    cancel.setBounds(70, 70, 50, 23 );
						    cancel.setText("Cancel");
						    cancel.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(final SelectionEvent e) {
									typodialog.dispose();
								}
							});
							typodialog.pack();
							typodialog.open();
						}

						@Override
						public void mouseDown(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseUp(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
						
					});
					
					final Button button_1 = new Button(termRoleGroup, SWT.RADIO);
					button_1.setBounds(325, (count-1)*y+m, 90, y-2*m);			
					if(type.compareToIgnoreCase("structures")==0) button_1.setSelection(true);
					if(count%2 == 0) button_1.setBackground(grey);
					tabList.add(button_1);
					button_1.addListener(SWT.Selection, new Listener() {
					      public void handleEvent(Event e) {
					    	  Control[] controls = button_1.getParent().getChildren();
					    	  if(controls[1] instanceof Label){
					    		 String term = ((Label)controls[1]).getText().trim();
					    		//String term = ((Text)controls[1]).getText().trim();
						    	 thiscategorizedterms.put(term, "structures");
					    	  }
					      }						
					});
					
					final Button button_2 = new Button(termRoleGroup, SWT.RADIO);
					button_2.setBounds(425, (count-1)*y+m, 90, y-2*m);
					//button_2.setSelection(true);//This can't be done. It will waste all the learning perl completed: For use cases where the person who runs charaparser needs another person to review the terms. Here mark all terms as "descriptor" by default so they will all be loaded to OTO for review
					if(type.compareToIgnoreCase("characters")==0) button_2.setSelection(true);
					if(count%2 == 0) button_2.setBackground(grey);
					tabList.add(button_2);
					button_2.addListener(SWT.Selection, new Listener() {
					      public void handleEvent(Event e) {
					    	  Control[] controls = button_2.getParent().getChildren();
					    	  if(controls[1] instanceof Label){
					    		 String term = ((Label)controls[1]).getText().trim();
					    		 // String term = ((Text)controls[1]).getText().trim();
						    	 thiscategorizedterms.put(term, "characters");
					    	  }
					      }						
					});
					
					final Button button_3 = new Button(termRoleGroup, SWT.RADIO);
					button_3.setBounds(525, (count-1)*y+m, 90, y-2*m);
					if(type.compareToIgnoreCase("others")==0) button_3.setSelection(true);
					if(count%2 == 0) button_3.setBackground(grey);
					tabList.add(button_3);
					button_3.addListener(SWT.Selection, new Listener() {
					      public void handleEvent(Event e) {
					    	  Control[] controls = button_3.getParent().getChildren();
					    	  if(controls[1] instanceof Label){
					    		 String term = ((Label)controls[1]).getText().trim();
					    		 //String term = ((Text)controls[1]).getText().trim();
						    	 thiscategorizedterms.put(term, "others");
					    	  }
					      }						
					});
					
					Label invisible = new Label(termRoleGroup, SWT.NONE);
					invisible.setBounds(720, (count-1)*y+m, 90, y-2*m);
					invisible.setText("invisible");
					invisible.setVisible(false);
					
					clabel.pack();
					tlabel.pack();
					//ttext.pack();
					button_1.pack();
					button_2.pack();
					button_3.pack();
					termRoleGroup.pack();
					termRoleGroup.redraw();
				}
				termRoleMatrix.pack();
				//termRoleMatrix.setTabList(tabList.toArray(new Control[]{}));
			}			
		} catch (Exception exe){
			LOGGER.error("unable to load subtab in Markup : MainForm", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
	}


	
	/**loading structure/descriptor terms for curation**/

	 /*protected void loadTermCurationTabs(){
		//loadFindStructureTable();
		//loadFindDescriptorTable();
		//loadFindMoreStructureTable();
		//loadFindMoreDescriptorTable();
		createSubtab(markupNReviewTabFolder, "structures");
		createSubtab(markupNReviewTabFolder, "characters");
		createSubtab(markupNReviewTabFolder, "others");
	}*/
	/*protected int loadFindStructureTable() {
		//ArrayList <String> words = new ArrayList<String>();
		findStructureTable.removeAll();
		int count = 0;
		try {
			ArrayList<String> words = fetchStructureTerms();			

			if (words != null) {
				for (String word : words){
					count++;
					TableItem item = new TableItem(findStructureTable, SWT.NONE);
					item.setText(new String [] {count+"", word});
				}
			}					
		} catch (Exception exe){
			LOGGER.error("unable to load findStructure subtab in Markup : MainForm", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		return count;
	}*/
	/*protected void loadOthersTable() {
		showOtherTerms();
	}*/

	/*protected int loadFindDescriptorTable() {
		// TODO Auto-generated method stub
		//ArrayList <String> words = null;
		findDescriptorTable.removeAll();
		int count = 0;
		try {
			ArrayList<String> words = fetchCharacterTerms();
			if (words != null) {
				for (String word : words){
					count++;
					TableItem item = new TableItem(findDescriptorTable, SWT.NONE);
					item.setText(new String [] {count+"", word});
				}
			}
			
		} catch (Exception exe){
			LOGGER.error("unable to load findDescriptor subtab in Markup : MainForm", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		return count;
	
	}*/


	
	/*protected int loadFindMoreStructureTable() {
		ArrayList <String> words = new ArrayList<String>();
		findMoreStructureTable.removeAll();
		int count = 0;
		try {
			words = fetchContentTerms(words);
			if (words != null) {
				for (String word : words){
					count++;
					TableItem item = new TableItem(findMoreStructureTable, SWT.NONE);
					item.setText(new String [] {count+"", word});
				}
			}					
		} catch (Exception exe){
			LOGGER.error("unable to load findMoreStructure subtab in Markup : MainForm", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		return count;
	}*/
	
	/*protected int loadFindMoreDescriptorTable() {
		ArrayList <String> words = new ArrayList<String>();
		findMoreDescriptorTable.removeAll();
		int count = 0;
		try {
			words = fetchContentTerms(words);
			if (words != null) {
				for (String word : words){
					count++;
					TableItem item = new TableItem(findMoreDescriptorTable, SWT.NONE);
					item.setText(new String [] {count+"", word});
				}
			}					
		} catch (Exception exe){
			LOGGER.error("unable to load findMoreDescriptor subtab in Markup : MainForm", exe);
			StringWriter sw = new StringWriter();PrintWriter pw = new PrintWriter(sw);exe.printStackTrace(pw);LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
		}
		return count;
	}*/
	
	static void setStartUpString(String string){
		MainForm.startupstring = string;
	}
	
	private void setType4XML(String schema){
		this.type4xml = schema;
	}
	
	private String getType4XML(){
		return this.type4xml;
	}
	
	public void setStatus(int index){
		this.statusOfMarkUp[index] = true;
	}
	
	public void setRunPerl(boolean completed){
		this.runperl = completed;
	}
	
	/**
	 * mapping from gloss type string to the gloss to be used
	 */
	public static String getGlossary(String glosstype) {
		if(glosstype.compareToIgnoreCase("plant")==0){
			//return "gg_noschema_fnaglossaryfixed";
			return "fnaglossaryfixed";
		}else if(glosstype.compareToIgnoreCase("hymenoptera")==0){
			return "antglossaryfixed";
		}else if(glosstype.compareToIgnoreCase("fossil")==0){
			return "treatiseoglossaryfixed";
		}else if(glosstype.compareToIgnoreCase("porifera")==0){
			return "spongeglossaryfixed";
		}else if(glosstype.compareToIgnoreCase("nematodes")==0){
			return "nematodesglossaryfixed";
		}else  if(glosstype.compareToIgnoreCase("algea")==0){
			return "diatomglossaryfixed";
		}
		LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+glosstype+" glossary is not ready yet");
		return null;
	}
	
	/**
	 * mapping from gloss type string to the gloss to be used
	 */
	public static String getOntoStructureTable(String glosstype) {
		if(glosstype.compareToIgnoreCase("plant")==0){
			String suffix = ApplicationUtilities.getProperty("ontophrases.table.suffix");
			return suffix!=null? "po"+suffix : null;
		}else if(glosstype.compareToIgnoreCase("hymenoptera")==0){
			return null;
		}else if(glosstype.compareToIgnoreCase("fossil")==0){
			return null;
		}else if(glosstype.compareToIgnoreCase("porifera")==0){
			return null;
		}else if(glosstype.compareToIgnoreCase("nematodes")==0){
			return null;
		}else  if(glosstype.compareToIgnoreCase("algea")==0){
			return null;
		}
		//LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+glosstype+" glossary is not ready yet");
		return null;
	}
	/**
	 * YourGlossaryType must be integer from 1 to 5 meaning: 
	(1, 'Plant'),
	(2, 'Hymenoptera'),
	(3, 'Algea')
	(4, 'Porifera')
	(5, 'Fossil')
	 * @param glosstype
	 * @return
	 */
	public static int getGlossaryType(String glosstype) {
		List<String> types = Arrays.asList(glossprefixes);
		return types.indexOf(glosstype)+1;
	}
}
