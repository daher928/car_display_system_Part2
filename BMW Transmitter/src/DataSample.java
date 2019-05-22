import java.util.ArrayList;
import java.util.List;

public class DataSample {

	public int timeStamp;
	public int samplesCount;
	public List<String> sampleIds;
	public List<String> samplesData;
	
	public DataSample() {
		sampleIds = new ArrayList<>();
		samplesData = new ArrayList<>();
		
	}
	
	public void addData(String sampleId, String data) {
		this.sampleIds.add(sampleId);
		this.samplesData.add(data);
	}
	
	@Override
	public String toString() {
		return "timeStamp=" + timeStamp + 
				" samplesCount=" + samplesCount + 
				" sampleIds=" + sampleIds + 
				" sampleData=" + samplesData;
		
	}
}
