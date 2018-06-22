import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pinging extends Thread {

	private Object[] msg;
	private String ip;
	public Pinging(String ip) {
		this.ip=ip;
		msg = new Object[5];
	}
	
	@Override
	public void run() {
		BufferedReader br = null;
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("ping -a " + ip);
			msg[0] = ip;
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.indexOf("[") >= 0) {
					Pattern p1 = Pattern.compile("(Ping)(\\s)(.+)(\\s)(\\[)");
					Matcher m1 = p1.matcher(line);
					while(m1.find())
					{
						msg[2]=m1.group(3);
					}
				}
				if (line.indexOf("ms") >= 0) {
					Pattern p2 = Pattern.compile("(\\d*+ms)(\\s)(TTL=)(\\d*)");
					Matcher m2 = p2.matcher(line);
					while(m2.find())
					{
						msg[1]=m2.group(1);
						msg[3]=m2.group(4);
					}
					break;
				}
				
				
			} 
			PortScanner Ports = new PortScanner(ip);
			Ports.start();
		} catch (Exception e) {
			
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	public Object[] getMsg() {
		try {
			join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return msg;
	}

	public static void main(String[] args) {
		Pinging[] pg = new Pinging[255];
		String fixedIp = "192.168.1.";
	}

}
