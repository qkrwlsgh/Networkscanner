import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class PingOutline extends JFrame {
	
	char flag=0;
	int i=0,j=0,k=0,l=0;

	public PingOutline() {
		
	

	//menu begin
	JMenuBar menubar = new JMenuBar();
	setJMenuBar(menubar);
	
	JMenu scanmenu = new JMenu("Scan");
	JMenu gotomenu = new JMenu("Go to");
	JMenu commandsmenu = new JMenu("Commands");
	JMenu favoritesmenu = new JMenu("Favorite");
	JMenu toolsmenu = new JMenu("Tools");
	JMenu helpmenu = new JMenu("Help");

	scanmenu.add("Load from file..");
	scanmenu.add("Export All...");
	scanmenu.add("Export selection...");
	scanmenu.addSeparator();
	scanmenu.add("Quit");
	
	gotomenu.add("Next alive host");
	gotomenu.add("Next open host");
	gotomenu.add("Next dead host");
	gotomenu.addSeparator();
	gotomenu.add("Previous alive host");
	gotomenu.add("Previous open host");
	gotomenu.add("Previous dead host");
	gotomenu.addSeparator();
	gotomenu.add(new JMenuItem("Finds..."));
	
	commandsmenu.add("Show datails");
	commandsmenu.addSeparator();
	commandsmenu.add("Rescan IP(s)");
	commandsmenu.add("Delete IP(s)");
	commandsmenu.addSeparator();
	commandsmenu.add("Copy IP");
	commandsmenu.add("Copy details");
	commandsmenu.addSeparator();
	commandsmenu.add("Open");
	
	favoritesmenu.add("Add current...");
	favoritesmenu.add("Manage favorites...");
	
	toolsmenu.add("Preferences...");
	toolsmenu.add("Feachers...");
	toolsmenu.addSeparator();
	toolsmenu.add("Selection");
	toolsmenu.add("Scan statstics");

	helpmenu.add("Getting Started");
	helpmenu.addSeparator();
	helpmenu.add("Official Website");
	helpmenu.add("FAQ");
	helpmenu.add("Report an issue");
	helpmenu.add("Plugins");
	helpmenu.addSeparator();
	helpmenu.add("Command-line usage");
	helpmenu.addSeparator();
	helpmenu.add("Check for newer version...");
	helpmenu.addSeparator();
	helpmenu.add("About");
	
	menubar.add(scanmenu);
	menubar.add(gotomenu);
	menubar.add(commandsmenu);
	menubar.add(favoritesmenu);
	menubar.add(toolsmenu);
	menubar.add(helpmenu);
	
	
	//menu end
	
    //status bar begin
	
	JPanel statusPanel = new JPanel();
	statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
	add(statusPanel, BorderLayout.SOUTH);
	JLabel readyLabel = new JLabel("Ready");
	readyLabel.setPreferredSize(new Dimension(450,16));
	readyLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
	JLabel displayLabel = new JLabel("Display:All");
	displayLabel.setPreferredSize(new Dimension(100,16));
	displayLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
	JLabel threadLabel = new JLabel("Thread:0");
	threadLabel.setPreferredSize(new Dimension(100,16));
	threadLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));

	statusPanel.add(readyLabel);
	statusPanel.add(displayLabel);
	statusPanel.add(threadLabel);
	//status bar end
	
	//table begin
	
	String[] titles = new String[] { "IP", "Ping", "Hostname" , "TTL", "Port[0+]" };
	
	Object[][] stats = initTable();
	JTable jt = new JTable(stats,titles);
	
	add(jt);
	//table end
	
	//tool bar begin
	
	Font myFont  = new Font("Serif",Font.BOLD,16);
	JToolBar toolbar1 = new JToolBar();
	toolbar1.setLayout(new FlowLayout(FlowLayout.LEFT));
	JToolBar toolbar2 = new JToolBar();
	toolbar2.setLayout(new FlowLayout(FlowLayout.LEFT));
	
	JLabel rangeStartLabel = new JLabel("IP Range: ");
	JTextField rangeStartTextField = new JTextField(10);
	JLabel rangeEndLabel = new JLabel("to: ");
	JTextField rangeEndTextField = new JTextField(10);
	JComboBox optioncombobox = new JComboBox();
	optioncombobox.addItem("/24");
	optioncombobox.addItem("/26");

	JLabel HostnameLabel = new JLabel("Hostname: ");
	JTextField HostnameTextField = new JTextField(10);
	JButton IPButton = new JButton("IP ¡ã");
	
	Image starticon;
	Image stopicon;
	Image seticon;
	Image listicon;
	JButton startButton = null;
	JButton stopButton = null;
	JButton setButton = null;
	JButton listButton = null;
	try {
		starticon = ImageIO.read(new File("Startimages.png"));
		Image changedstartimage = starticon.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
		ImageIcon changedstarticon = new ImageIcon(changedstartimage);
		startButton = new JButton(" START", changedstarticon);
		stopicon = ImageIO.read(new File("Stopimages.png"));
		Image changedstopimage = stopicon.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
		ImageIcon changedstopicon = new ImageIcon(changedstopimage);
		stopButton = new JButton(" Stop" , changedstopicon);
		seticon = ImageIO.read(new File("Setimages.png"));
		Image changedsetimage = seticon.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		ImageIcon changedseticon = new ImageIcon(changedsetimage);
		setButton = new JButton(changedseticon);
		listicon = ImageIO.read(new File("listimages.png"));
		Image changedlistimage = listicon.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		ImageIcon changedlisticon = new ImageIcon(changedlistimage);
		listButton = new JButton(changedlisticon);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	
	
	
	rangeStartLabel.setFont(myFont);
	rangeStartLabel.setPreferredSize(new Dimension(90, 30));
	rangeEndLabel.setFont(myFont);
	rangeEndLabel.setPreferredSize(new Dimension(90, 30));
	HostnameLabel.setFont(myFont);
	HostnameTextField.setPreferredSize(new Dimension(90, 30));
	optioncombobox.setPreferredSize(new Dimension(90, 30));
	IPButton.setPreferredSize(new Dimension(50, 30));
	startButton.setPreferredSize(new Dimension(90, 30));
	stopButton.setPreferredSize(new Dimension(90, 30));
	
	toolbar1.add(rangeStartLabel);
	toolbar1.add(rangeStartTextField);
	toolbar1.add(rangeEndLabel);
	toolbar1.add(rangeEndTextField);
	toolbar1.add(setButton);
	toolbar2.add(HostnameLabel);
	toolbar2.add(HostnameTextField);
	toolbar2.add(IPButton);
	toolbar2.add(optioncombobox);
	toolbar2.add(startButton);
	toolbar2.add(listButton);
	
	//tool bar end
	
	String myIp = null;
	String myHostname = null;
	
	try {
		InetAddress ia = InetAddress.getLocalHost();
		myIp = ia.getHostAddress();
		myHostname = ia.getHostName();
	} catch (Exception e) {
		
	}
	String fixedIp = myIp.substring(0, myIp.lastIndexOf(".")+1);
	rangeStartTextField.setText(fixedIp + "0");
	rangeEndTextField.setText(fixedIp + "254");
	HostnameTextField.setText(myHostname);
	
	JPanel p =new JPanel(new BorderLayout());
	p.add(toolbar1,BorderLayout.NORTH);
	p.add(toolbar2,BorderLayout.SOUTH);
	add(p,BorderLayout.NORTH);
	JScrollPane jsp = new JScrollPane(jt);
	add(jsp);
	setTitle("IP Scanner");
	setSize(700, 700);
	setVisible(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	final JButton sb = startButton;
	final JButton stb = stopButton;
	startButton.addActionListener(new ActionListener() {	
		@Override
		public void actionPerformed(ActionEvent e) {
			
				toolbar2.remove(sb);
				toolbar2.add(stb);
				readyLabel.setText("Please wait...");
				jt.repaint();
				Thread lodingThread = new Thread() {
					@Override
					public void run() {
						int i;
						 for ( i = 0; i <= 100; i++) {
							 final int I=i;

					          try {
					            java.lang.Thread.sleep(100);
					          }
					          catch(Exception e) {
					        	  e.printStackTrace();
					          }
					        }
					}
				};lodingThread.start();
				readyLabel.setText("Ready");
				
				for(int i = 1; i <= 254; i++) {
					final int I = i;
					String myIP = null;
					
						try {
							InetAddress ia = InetAddress.getLocalHost();
							myIP = ia.getHostAddress();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					String ip = myIP.substring(0,myIP.lastIndexOf(".") +1)+I;
					String msg[] = {null, null, null, null, null};
					msg[0] = ip;
					Thread thread = new Thread() {
						@Override
						public void run() {
							
							InputStream is = null;
							BufferedReader br = null;
							try {
								Runtime runtime = Runtime.getRuntime();
								Process pro = runtime.exec("ping -a "+ip);
								is = pro.getInputStream();
								br = new BufferedReader(new InputStreamReader(is));
								String line = null;
						        InetAddress address = InetAddress.getByName(ip);
								boolean reachable = address.isReachable(250);
								
								if(reachable) {
									jt.setValueAt(ip, I-1, 0);
									
									
									while((line = br.readLine()) != null) {
										if (line.indexOf("[") >= 0) {
											Pattern p1 = Pattern.compile("(Ping)(\\s)(.+)(\\s)(\\[)");
											Matcher m1 = p1.matcher(line);
											while(m1.find())
											{
												msg[2]=m1.group(3);
												jt.setValueAt(msg[2], I-1, 2);
											}
										}
										if (line.indexOf("ms") >= 0) {
											Pattern p2 = Pattern.compile("(\\d*+ms)(\\s)(TTL=)(\\d*)");
											Matcher m2 = p2.matcher(line);
											while(m2.find())
											{
												msg[1]=m2.group(1);
												msg[3]=m2.group(4);
												jt.setValueAt(msg[1], I-1, 1);
												jt.setValueAt(msg[3], I-1, 3);
											}
											break;
										}
										final ExecutorService es = Executors .newFixedThreadPool(256);
										final int timeout = 200;
										final List<Future<ScanResult>> futures = new ArrayList<>();
										for(int port = 1 ; port <=600; port++) {
											futures.add(portIsOpen(es,ip,port,timeout));
										}
										es.awaitTermination(200L, TimeUnit.MILLISECONDS);
										int openPorts = 0;
										String openPortsNum = "";
										for(final Future<ScanResult> f : futures) {
											if(f.get().isOpen()) {
												openPorts++;
												openPortsNum += f.get().getport()+",";
											}
											if (!openPortsNum.equals(""))
											msg[4]=	openPortsNum.substring(0,openPortsNum.length()-1);
											jt.setValueAt(msg[4], I-1, 4);
											
											
										}
									}
									if (msg[1]==null)
										jt.setValueAt("[n/a]", I-1, 1);
									if (msg[2]==null)
										jt.setValueAt("[n/a]", I-1, 2);
									if (msg[3]==null)
										jt.setValueAt("[n/s]", I-1, 3);
									if (msg[4]==null)
										jt.setValueAt("[n/s]", I-1, 4);
								}else {
									jt.setValueAt(ip, I-1, 0);
									jt.setValueAt("[n/a]", I-1, 1);
									jt.setValueAt("[n/s]", I-1, 2);
									jt.setValueAt("[n/s]", I-1, 3);
									jt.setValueAt("[n/s]", I-1, 4);
								}										
							}catch(Exception e) {
								e.printStackTrace();
							}
							
						}
						
					};
					thread.start();
					
						if(thread.isAlive()==false) 
						{
							readyLabel.setText("Ready");
							toolbar2.remove(stb);
							toolbar2.add(sb);
						}
						else
						{
							toolbar2.remove(stb);
							toolbar2.add(sb);
						}
						
				}
			}
			
	});
	
	stopButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent se) {
				toolbar2.remove(stb);
				toolbar2.add(sb);
				readyLabel.setText("Ready"); 
				jt.repaint();
			}
	});
}
	
	public Future<ScanResult> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout){
		return es.submit(new Callable<ScanResult>() 
		{
			@Override
			public ScanResult call() 
			{
				try 
				{
						Socket socket = new Socket();
						socket.connect(new InetSocketAddress(ip, port),timeout);
						socket.close();
						return new ScanResult(port, true);					
				}
				catch (Exception e) 
				{
					return new ScanResult(port, false);
				}
			}
		});
	}

	static class ScanResult 
	{
		private int port;
		
		private boolean isOpen;
		
		public ScanResult(int port, boolean isOpen)
		{
			super();
			this.port = port;
			this.isOpen = isOpen;
		}
		
		public int getport() 
		{
			return port;
		}
		
		public void setPort(int port)
		{
			this.port = port;
		}
		
		public boolean isOpen() 
		{
			return isOpen;
		}
		
	}	
	public Object[][] initTable() 
	{
		Object[][] result = new Object[254][5];
		return result;
	}
//	startButton.addActionListener(new ActionListener() {
//		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			new Thread(() -> {
//				Pinging[] pg = new Pinging[255];
//				
//				// add
//				toolbar2.remove(sb);
//				toolbar2.add(stb);
//				for(i=0;i<255;i++)
//				{
//					pg[i] = new Pinging( fixedIp + i);
//					pg[i].start();
//				}
//				for(i=0;i<255;i++)
//				{
//					Object[] msg = pg[i].getMsg();
//					stats[i][0] = msg[0];
//					if(msg[1] != null)  stats[i][1] = msg[1];
//					else stats[i][1] = "[n/a]";
//					if(msg[2] != null)	stats[i][2] = msg[2];
//					else if(msg[1] != null && msg[2] == null) stats[k][2] = "[n/a]";
//					else stats[i][2] = "[n/s]";
//					if (msg[3] != null) stats[i][3] = msg[3];
//					else stats[i][3] = "[n/s]";
//					
//				}
//			
//				
//			
//				//scan value == null ¡æ stats[i][4] = "[n/s] "
//				//scan value != null ¡æ assgin value stats[i][4] = "[n/s] "
//			
//			jt.repaint();
//			
//			
//			}).start();
//			new Thread(() -> {
//				PortScanner[] ps = new PortScanner[255];
//				for(i=0;i<255;i++)
//				{
//					Object[] msg = ps[i].getMsg();
//					ps[i] = new PortScanner(fixedIp + i);
//					ps[i].start();
//					if(stats[i][1]!=null | stats[i][2]!=null | stats[i][3]!=null | stats[4]!=null)
//						stats[i][4]=msg[4];
//						else if(msg[1]!=null | msg[2]!=null | msg[3]!=null | msg[4]!=null)
//							stats[i][4]="[n/s]";
//				}
//			}).start();
//		}
//		
//		});
//	
//	}
	
	
	public static void main(String[] args) {
		PingOutline po = new PingOutline();
	}
}