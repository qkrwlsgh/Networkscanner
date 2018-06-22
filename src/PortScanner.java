
import java.net.*;

import java.util.*;

import java.util.concurrent.*;

public class PortScanner extends Thread {
	private Object[] msg;
	private String ip;
	public PortScanner(String ip)
	{
			this.ip = ip;
			msg = new Object[5];
			msg[4] = null;
	}

	public void run() {
		final ExecutorService es = Executors.newFixedThreadPool(512);
		final int timeout = 20;
		final List<Future<ScanResult>> futures = new ArrayList<>();
		
		//65535,1024
		for(int port  = 1 ; port <= 1024; port ++)
		{
			//for(int port  = 1 ; port <= 80; port ++) {
			futures.add(portIsOpen(es,ip,port,timeout));
		}
		int openPorts = 0;
		String openPortnumber = "";
		try {
			es.awaitTermination(200L, TimeUnit.MILLISECONDS);
	
		
		for(final Future<ScanResult> f : futures )
		{
			if(f.get().isOpen()) 
			{
				openPorts++;
				openPortnumber += f.get().getport()+",";
			}
			
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		msg[4] = openPortnumber;
		System.out.println();
		
		
	}

	
	private static Future<ScanResult> portIsOpen(final ExecutorService es, final String ip, 
			final int port, final int timeout) {
			return es.submit(new Callable<ScanResult>() {
				@Override
				public ScanResult call() {
					try {
						Socket socket = new Socket();
						socket.connect(new InetSocketAddress(ip, port), timeout);
						socket.close();
						return new ScanResult(port,true);
					} catch (Exception e) {
						return new ScanResult(port,false);
					}
				}
			});
		}
	public static class ScanResult {
		private int port;
		
		private boolean isOpen;
		
		public ScanResult(int port, boolean isOpen)
		{
			super();
			this.port = port;
			this.isOpen = isOpen;
		}
		
		public int getport() {
			return port;
		}
		
		public void setPort(int port)
		{
			this.port = port;
		}
		
		public boolean isOpen() {
			return isOpen;
		}
		
		public void setOpen(boolean isOpen)
		{
			this.isOpen = isOpen;
		}
	}
}