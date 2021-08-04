package burp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyledDocument;


public class BurpExtender implements IBurpExtender, ITab{
	public String title = "BFAC";
	public String long_title = "Backup File Artifacts Checker";
    public boolean isRunning = false;
	PrintWriter stdout;
	PrintWriter stderr;
    IBurpExtenderCallbacks callbacks;
	JTextPane outputPane;
	JComboBox<String> levelsList;
	JButton butRun;
	JProgressBar progress;
	private IExtensionHelpers helpers;
    private JPanel panel;
    private IHttpRequestResponse[] sitemap;
	private JRadioButton butScopeOnly;
	private JTextField badExt;
	private Thread bfacThread;
	
	@Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        // set our extension name
        this.callbacks = callbacks;
        this.callbacks.setExtensionName(this.title);
        this.helpers = this.callbacks.getHelpers();

        // obtain our output and error streams
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        this.stderr = new PrintWriter(callbacks.getStderr(), true);
        
        this.setITab();
        this.callbacks.addSuiteTab(this);
    }

	private void setITab() {
		/* UI */
		this.panel = new JPanel();
        panel.setMaximumSize(new Dimension(800, Short.MAX_VALUE));
		JLabel title = new JLabel(this.long_title);

		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		title.setForeground(new Color(233, 84, 32));  // SEC-IT logo color

		JRadioButton butScopeOnly = new JRadioButton("In-scope only", true);
		JRadioButton butFull = new JRadioButton("Full site map", false);
		ButtonGroup butGrp1 = new ButtonGroup();
		butGrp1.add(butScopeOnly);
		butGrp1.add(butFull);
		this.butScopeOnly = butScopeOnly;

		JLabel labelBadExt = new JLabel();
        labelBadExt.setText("Ingore extension");
		this.badExt = new JTextField("gif,jpg,jpeg,png,css,js,ico,svg,eot,woff,woff2,ttf");
		badExt.setMaximumSize(new Dimension(200, 20));
		
        JButton butExtract = new JButton("Extract URL");
        butExtract.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				printURL();
			} 
    	});

        JButton butClear = new JButton("Clear Log");
        butClear.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				clearLog();
			} 
    	});

        this.butRun = new JButton("Run BFAC");
        butRun.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				runBFAC();
			} 
    	});
        
		ButtonGroup butGrp2 = new ButtonGroup();
		butGrp2.add(butExtract);
		butGrp2.add(butClear);
		butGrp2.add(butRun);
		
        String[] levels = {"Level 1","Level 2","Level 3","Level 4","Level 5"}; 
        this.levelsList = new JComboBox<>(levels);
        levelsList.setSelectedIndex(4); // Level 5 by default
        levelsList.setMaximumSize(new Dimension(100, 20));

        progress = new JProgressBar(0,100);  
        progress.setMaximumSize(new Dimension(800, 15));  
        //progress.setBounds(40,40,160,30);         
        progress.setValue(0);    
        progress.setStringPainted(true);

        JLabel titleOutput = new JLabel("Output");
        titleOutput.setFont(new Font("Tahoma", Font.BOLD, 14));
        JTextPane outputPane = new JTextPane();
        outputPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        outputPane.setEditable(false);
        DefaultCaret caret = (DefaultCaret)outputPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        this.outputPane = outputPane;
        JScrollPane scrollPane = new JScrollPane(outputPane);
        scrollPane.setMaximumSize(new Dimension(800, Short.MAX_VALUE));
        GroupLayout layout = new GroupLayout(this.panel);
        this.panel.setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(title)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10,10,10)
                        .addComponent(butScopeOnly)
                        .addGap(10,10,10)
                        .addComponent(butFull))
                    .addGap(10,10,10)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10,10,10)
                        .addComponent(labelBadExt)
                        .addGap(10,10,10)
                        .addComponent(badExt))
                    .addGap(10,10,10)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10,10,10)
                        .addComponent(butExtract)
                        .addGap(10,10,10)
                        .addComponent(butClear)
                        .addGap(10,10,10)
                        .addComponent(butRun)
                        .addGap(10,10,10)
                        .addComponent(levelsList))
                    .addGap(10,10,10)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10,10,10)
                        .addComponent(progress))
                    .addGap(15,15,15)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10,10,10)
                    	.addComponent(titleOutput))
                    .addGap(10,10,10)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10,10,10)
                    	.addComponent(scrollPane)))
                .addContainerGap(26, Short.MAX_VALUE)));
        
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10,10,10)
                .addComponent(title)
                .addGap(10,10,10)
                .addGroup(layout.createParallelGroup()
                    .addComponent(butScopeOnly)
                    .addComponent(butFull))
                .addGap(10,10,10)
                .addGroup(layout.createParallelGroup()
                    .addComponent(labelBadExt)
                    .addComponent(badExt))
                .addGap(10,10,10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(butExtract)
                        .addComponent(butClear)
                        .addComponent(butRun)
                        .addComponent(levelsList))
                .addGap(10,10,10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(progress))
                .addGap(15,15,15)
                .addGroup(layout.createParallelGroup()
	                .addComponent(titleOutput))
                .addGap(10,10,10)
                .addGroup(layout.createParallelGroup()
	                .addComponent(scrollPane))
                .addGap(20,20,20)));
	}

	private void printURL() {
		this.clearLog();
		StyledDocument doc = this.outputPane.getStyledDocument();
		for(String u : this.extractSitemap()) {
		    try {
				doc.insertString(doc.getLength(), u+"\n", null );
			} catch (BadLocationException e) {}
		}
	}

	private void runBFAC() {
		if (isRunning) {
			isRunning = false; 
	        progress.setValue(0);
			butRun.setText("Run BFAC");
		}else {
			isRunning = true;
	        progress.setValue(0);  
			this.clearLog();
			butRun.setText("Stop BFAC");
		    bfacThread = new Thread(new BFAC(this));
		    bfacThread.start();
		}
	}
	private void clearLog() {
        progress.setValue(0);
		outputPane.setText("");
	}

	public LinkedHashSet<String> extractSitemap(){
		this.sitemap = callbacks.getSiteMap(null);
		String badExtStr = badExt.getText();
		List<String> badExts = Arrays.asList(badExtStr.split("\\s*,\\s*"));

		LinkedHashSet<String> urls = new LinkedHashSet<String>();
        for (IHttpRequestResponse r : sitemap){ // For each element of sitemap
        	try {
        		byte[] tReq = r.getRequest();
        		byte[] tRep = r.getResponse();
        		if ((tReq == null) || (tRep == null)) {
        			continue;
        		}
        		IRequestInfo req = this.helpers.analyzeRequest(r);
        		IResponseInfo rep = this.helpers.analyzeResponse(tRep);
        		URL url = req.getUrl();
				if (req.getHeaders().size() <= 0){
					continue;
				}
				int code = rep.getStatusCode();
	        	if ((code >= 400) && (code < 500)){  // Avoid 400 error
	        		continue;
	        	}
	        	String cleanPath = url.toString().split("#")[0].split("\\?")[0];
	        	cleanPath = cleanPath.replaceAll("/+$", ""); // remove trailing /
            	// If path end with an ext
            	if (cleanPath.matches(".*([.])[a-z]+$")){
            		if (!(butScopeOnly.isSelected()) || callbacks.isInScope(url) ){
            			boolean addPath = true;
            			for (String ext: badExts) {
            				if(cleanPath.endsWith("."+ext)) {
            					addPath = false;
            				}
            			}
            			if (addPath) { // Does not end with a bad extension
                			urls.add(cleanPath);
            			}
            		}
            	}
        	} catch (Exception e) {
				// Request or response is malformated, continue
        	    stderr.println(e);  
        		continue; 
			}
        }
        return urls;
	}

	@Override
	public String getTabCaption() {
		return title;
	}

	@Override
	public Component getUiComponent() {
		return panel;
	}
}