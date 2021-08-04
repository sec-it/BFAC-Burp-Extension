package burp;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class BFAC implements Runnable {
	
	private IExtensionHelpers helpers;
	private StyledDocument doc;
	private ArrayList<String> level1;
	private ArrayList<String> level2;
	private ArrayList<String> level3;
	private ArrayList<String> level4;
	private ArrayList<String> level5;
	private IBurpExtenderCallbacks callbacks;
	private BurpExtender extender;
	private SimpleAttributeSet kYellow;
	private SimpleAttributeSet kCyan;
	private SimpleAttributeSet kGreen;
	private SimpleAttributeSet kRed;
	private ArrayList<ArrayList<String>> levelsList;
	private LinkedList<ConcurrentHashMap<String, IResponseInfo>> repList;

    public BFAC(BurpExtender extender) {
       this.extender = extender;
       this.callbacks = extender.callbacks;
       this.helpers = callbacks.getHelpers();
       this.doc = extender.outputPane.getStyledDocument();
       this.initStyles();
    }

    private void initStyles() {
		this.kYellow = new SimpleAttributeSet();
		StyleConstants.setForeground(kYellow, Color.YELLOW);

		this.kCyan = new SimpleAttributeSet();
		StyleConstants.setForeground(kCyan, Color.CYAN);

		this.kGreen = new SimpleAttributeSet();
		StyleConstants.setForeground(kGreen, Color.GREEN);

		this.kRed = new SimpleAttributeSet();
		StyleConstants.setForeground(kRed, Color.RED);
    }

    public void run() {
    	this.printHeader();

		for(String urlString : this.extender.extractSitemap()) {
			if (this.extender.isRunning == false) {
				return; // equivalent to thread.stop();
			}
	    	String cleanPath = urlString.split("#")[0].split("\\?")[0];
	    	cleanPath = cleanPath.replaceAll("/+$", ""); // remove trailing /
			URL url = null;
			try {
				url = new URL(cleanPath);
			} catch (MalformedURLException e) { return; }
			
			try{
			    doc.insertString(doc.getLength(), "[i]", kYellow);
			    doc.insertString(doc.getLength(), " URL: "+cleanPath+"\n", null);
			}
			catch(Exception e){}
			
			loopLevel(url, cleanPath);
		}
		try{
		    doc.insertString(doc.getLength(), "\n[i]", kYellow);
		    doc.insertString(doc.getLength(), " Finished performing scan.\n", null);
		}
		catch(Exception e){}

		this.extender.isRunning = false;
		this.extender.butRun.setText("Run BFAC");
    }

    private void printHeader() {
		
		String l = "----------------------------------------------------------------------\n";
		String h = "";
    	h += "                    _____ _____ _____ _____\n";
		h += "                   | __  |   __|  _  |     |\n";
		h += "                   | __ -|   __|     |   --|\n";
		h += "                   |_____|__|  |__|__|_____|\n";
		h += "\n";
		h += "              -:::Backup File Artifacts Checker:::-\n";
		h += "                        Version: 1.4\n";
		h += "  Advanced Backup-File Artifacts Testing for Web-Applications\n";
		h += "Author: Mazin Ahmed | <mazin AT mazinahmed DOT net> | @mazen160\n";
		h += "Author: SEC-IT - Alex G. | @zeecka_\n";

		try{
		    doc.insertString(doc.getLength(), l, null);
		    doc.insertString(doc.getLength(), h, kCyan);
		    doc.insertString(doc.getLength(), l+"\n\n", null);
		}
		catch(Exception e){}
    }

    private void loopLevel(URL originalUrl, String cleanPath) {

		boolean isHttps = originalUrl.getProtocol().startsWith("https");
		String host = originalUrl.getHost();
		int portTmp = originalUrl.getPort();
		
		final int port = (portTmp != -1) ? portTmp : (isHttps ? 443 : 80);
		

		String dirPath = cleanPath.substring(0, cleanPath.lastIndexOf('/'))+"/";
		String fileName = "";
		int i = cleanPath.lastIndexOf("/");
		if (i > 0) {
			fileName = cleanPath.substring(i+1);
		}
		
		this.initLevels(fileName);

		int threadMax = 10;
		ArrayBlockingQueue<String> thList = new ArrayBlockingQueue<>(threadMax);
		repList = new LinkedList<ConcurrentHashMap<String,IResponseInfo>>();
		
		int runLevel = this.extender.levelsList.getSelectedIndex(); // Get selected level
		for (i=0; i<=runLevel; i++) {
			for (String file : levelsList.get(i)) {
				if (this.extender.isRunning == false) {
					return; // equivalent to thread.stop();
				}
				//this.extender.stdout.println("Thread : "+dirPath + file + " ==> "+Integer.toString(thList.size()));
				while(thList.size() >= threadMax) {
					//this.extender.stdout.println("Thread : "+dirPath + file + " ==> "+Integer.toString(thList.size()));
					try {
						Thread.sleep(100); // sleep 100 ms
					} catch (InterruptedException e) {}
				}

            	String urlString = dirPath + file;
            	thList.add(urlString);
				new Thread(() -> {
	        		URL url = null;
					try {
						url  = new URL(urlString);
					} catch (MalformedURLException e) { 
						return; 
					}
					byte[] response = callbacks.makeHttpRequest(host, port, isHttps, helpers.buildHttpRequest(url));
					IResponseInfo rep = helpers.analyzeResponse(response);
					int code = rep.getStatusCode();
					if ((code >= 400) && (code < 500)){  // Avoid 400 error
						thList.remove(urlString);
		        		return;
		        	}
					// Add response to HashMap
					ConcurrentHashMap<String, IResponseInfo> hRep = new ConcurrentHashMap<String,IResponseInfo>();
					hRep.put(urlString, rep);
					repList.offer(hRep);
		            thList.remove(urlString);
		        }).start();
				this.processRespList();
			}
		}
		while(thList.size() > 0) {  // Wait until all threads ends
			try {
				Thread.sleep(100); // sleep 100 ms
			} catch (InterruptedException e) {}
		}
		this.processRespList();
    }

    private void processRespList() {
    	// Unload Queue and print to doc. Doc is not thread safe, thats why we need a queue
		while ( !repList.isEmpty() ) {
			ConcurrentHashMap<String, IResponseInfo> repMap = repList.poll();
		    String key = "";
			if (repMap.isEmpty()) {
				this.extender.stderr.println("Empty error");
				continue; // Should not happen but repMap.keySet() is blocking without this line
			}
			//this.extender.stdout.println(repMap.keySet());
		    for (String k : repMap.keySet()) {
		    	key = k;
		    }
		    IResponseInfo rep = repMap.get(key);

			String respCode = Integer.toString(rep.getStatusCode());
			String contLen = Integer.toString(rep.toString().length());

			try {
				doc.insertString(doc.getLength(), "[$] ", kGreen);
				doc.insertString(doc.getLength(), "Discovered: -> {"+key+"} ", null);
				doc.insertString(doc.getLength(), "(Response-Code: "+respCode+" | Content-Length: "+contLen+")\n", null);
			} catch (BadLocationException e) {
				this.extender.stderr.println(e);
			}
		}
    }

    private void initLevels(String filename) {

		this.level1 = new ArrayList<String>();
		this.level2 = new ArrayList<String>();
		this.level3 = new ArrayList<String>();
		this.level4 = new ArrayList<String>();
		this.level5 = new ArrayList<String>();
		
		String filenameNoExt = filename.substring(0, filename.lastIndexOf('.'));
		String fileExt = "";
		int i = filename.lastIndexOf('.');
		if (i > 0) {
			fileExt = filename.substring(i+1);
		}

		// LEVEL 1
		level1.add(filename + "~");
	    level1.add(filename + "%23");
	    level1.add(filename + ".save");
	    level1.add(filename + ".swp");
	    level1.add(filename + ".swo");
	    level1.add("%23" + filename + "%23");
	    level1.add(filename + ".bak");

	    // LEVEL 2
	    level2.add(filename + "_");
	    level2.add(filename + "~~");
	    level2.add(filename + "_bak");
	    level2.add(filename + "-bak");
	    level2.add(filename + ".bk");
	    level2.add(filename + ".bkp");
	    level2.add(filename + ".bac");
	    level2.add(filename + ".old");
	    level2.add(filename + "_old");
	    level2.add(filename + ".copy");
	    level2.add(filename + ".original");
	    level2.add(filename + ".orig");
	    level2.add(filename + ".org");
	    level2.add(filename + ".txt");
	    level2.add(filename + ".default");
	    level2.add(filename + ".tpl");
	    level2.add(filename + ".tmp");
	    level2.add(filename + ".temp");
	    level2.add("." + filename + ".swp");
	    level2.add("." + filename + ".swo");
	    level2.add("_" + filename + ".swp");
	    level2.add("_" + filename + ".swo");
	    level2.add(filename + ".sav");
	    level2.add(filename + ".conf");
	    level2.add(filenameNoExt + "%20%28copy%29." + fileExt);
	    level2.add("Copy%20of%20" + filename);
	    level2.add("copy%20of%20" + filename);
	    level2.add("Copy_" + filename);
	    level2.add("Copy%20" + filename);
	    level2.add("Copy_of_" + filename);
	    level2.add("Copy_(1)_of_" + filename);
	    level2.add("Copy_(2)_of_" + filename);
	    level2.add(filenameNoExt + "%20-%20Copy." + fileExt);
	    level2.add(filenameNoExt + "%20copy." + fileExt);

	    // LEVEL 3
	    
	    level3.add(filenameNoExt + ".txt");
	    level3.add(filenameNoExt + ".backup");
	    level3.add(filenameNoExt + ".bak");
	    level3.add(filenameNoExt + ".bak1");
	    level3.add(filenameNoExt + ".bakup");
	    level3.add(filenameNoExt + ".bakup1");
	    level3.add(filenameNoExt + ".bkp");
	    level3.add(filenameNoExt + ".save");
	    level3.add(filenameNoExt + ".old");
	    level3.add(filenameNoExt + ".orig");
	    level3.add(filenameNoExt + ".original");
	    level3.add(filenameNoExt + ".sql");
	    level3.add(filename + "%00");
	    level3.add(filename + "%01");
	    level3.add("~" + filename);
	    level3.add(filenameNoExt + ".tpl");
	    level3.add(filenameNoExt + ".tmp");
	    level3.add(filenameNoExt + ".temp");
	    level3.add(filename + ".saved");
	    level3.add(filename + ".back");
	    level3.add(filename + ".backup");
	    level3.add(filename + ".bck");
	    level3.add(filename + ".bakup");
	    level3.add(filenameNoExt + ".saved");
	    level3.add(filenameNoExt + ".back");
	    level3.add(filenameNoExt + ".bck");
	    level3.add(filenameNoExt + ".bakup");
	    level3.add("_" + filename);
	    level3.add("%20" + filename);
	    level3.add(filename + ".nsx");
	    level3.add(filename + ".cs");
	    level3.add(filename + ".csproj");
	    level3.add(filename + ".vb");
	    level3.add(filename + ".0");
	    level3.add(filename + ".1");
	    level3.add(filename + ".2");
	    level3.add(filename + ".arc");
	    level3.add(filename + ".inc");
	    level3.add(filename + ".lst");
	    level3.add(".~lock." + filename + "%23");
	    level3.add(".~" + filename);
	    level3.add("~%24" + filename);

	    // LEVEL 4
	    level4.add(filename + ".tar");
	    level4.add(filename + ".rar");
	    level4.add(filename + ".zip");
	    level4.add("~" + filenameNoExt + ".tmp");
	    level4.add(filename + ".tar.gz");
	    level4.add("backup-" + filename);
	    level4.add(filenameNoExt + "-backup." + fileExt);
	    level4.add(filenameNoExt + "-bkp." + fileExt);
	    level4.add(filenameNoExt + ".tar");
	    level4.add(filenameNoExt + ".rar");
	    level4.add(filenameNoExt + ".zip");
	    level4.add(filenameNoExt + ".tar.gz");
	    level4.add(filenameNoExt + ".sql.gz");
	    level4.add(filenameNoExt + ".bak.sql");
	    level4.add(filenameNoExt + ".bak.sql.gz");
	    level4.add(filenameNoExt + ".bak.sql.bz2");
	    level4.add(filenameNoExt + ".bak.sql.tar.gz");
	    level4.add(filename + ".");  // CVE-2017-12616
	    level4.add(filename + "::$DATA");  // CVE-2017-12616

	    // LEVEL 5
	    level5.add(".git/HEAD");
	    level5.add(".git/index");
	    level5.add(".git/config");
	    level5.add(".gitignore");
	    level5.add(".git-credentials");
	    level5.add(".bzr/README");
	    level5.add(".bzr/checkout/dirstate");
	    level5.add(".hg/requires");
	    level5.add(".hg/store/fncache");
	    level5.add(".svn/entries");
	    level5.add(".svn/all-wcprops");
	    level5.add(".svn/wc.db");
	    level5.add(".svnignore");
	    level5.add("CVS/Entries");
	    level5.add(".cvsignore");
	    level5.add(".idea/misc.xml");
	    level5.add(".idea/workspace.xml");
	    level5.add(".DS_Store");
	    level5.add("composer.lock");


		this.levelsList = new ArrayList<>();
		levelsList.add(level1);
		levelsList.add(level2);
		levelsList.add(level3);
		levelsList.add(level4);
		levelsList.add(level5);
    }
    
 }