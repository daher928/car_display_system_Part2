import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Transmitter {
	
	static final String fileName = "MW_samples_original_0001613_20181114_1754.txt";
//	static final String fileName = "short.txt";
	static final String SOH = String.valueOf((char)1);
	static final String androidId = "192.168.43.156";
	static final int port = 9191;
	
	static Socket client;
	static PrintWriter printwriter;
	
	public static void setUpConnection(String Ip, int port) throws UnknownHostException, IOException {
		client = new Socket(Ip, port); // connect to server
		printwriter = new PrintWriter(client.getOutputStream(), true);
	}
	
	public static void send(String message) {
		printwriter.write(message + "\n"); // write the message to output stream
		printwriter.flush();
	}
	
	public static DataSample parseLine(String fullLine) {
		String line = ((String[])fullLine.split(" "))[1];
		String[] lineSamples = line.split(SOH);
		int samplesCount = lineSamples.length;
		int timeStamp;
		String sampleId;
		DataSample dataSample = new DataSample();
		
		for (int i=0; i<samplesCount; i++) {
			String seq = lineSamples[i];
			if(i==0) {
				String ts = seq.substring(0, 6);
				String sc = seq.substring(6,8);
				timeStamp = Integer.parseInt(ts, 16);
				if(samplesCount != Integer.parseInt(sc, 16)) {
					return null;
				}
				dataSample.timeStamp = timeStamp;
				dataSample.samplesCount = samplesCount;
				String sid = seq.substring(9, 13);
				String val = seq.substring(13);
				dataSample.addData(sid, val);
			}else {
				String sid = seq.substring(1, 5);
				String val = seq.substring(5);
				dataSample.addData(sid, val);
			}
		}
		return dataSample;
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		
		try {
			setUpConnection(androidId, port);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		boolean startData = false;
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String line;
			while ((line = br.readLine()) != null) {
				if(line.equals("Start DATA")){
					startData = true;
					continue;
				}
				if(startData) {
					Thread.sleep(100);
					assert(line.contains(SOH));
					DataSample ds = parseLine(line);
					for(int i = 0; i < ds.samplesCount; i++) {
						String sampleId = ds.sampleIds.get(i);
						String sampleVal = ds.samplesData.get(i);
						System.out.println("sampleId = " + sampleId + " sampleValue = " + sampleVal);
						send(sampleId + "#" + sampleVal);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			printwriter.close();
			try {
				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // closing the connection
		}
		printwriter.close();
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // closing the connection
	}

}
