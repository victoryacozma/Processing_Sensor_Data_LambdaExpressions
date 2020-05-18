import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class MonitoredData {

	private String startTime;
	private String endTime;
	private String activityLabel;

	MonitoredData(String a, String b, String c) {
		startTime = a;
		endTime = b;
		activityLabel = c;
	}

	void setStartTime(String s) {
		startTime = s;
	}

	void setEndTime(String s) {
		endTime = s;
	}

	void setActivity(String s) {
		activityLabel = s;
	}

	String getStartTime() {
		return startTime;
	}

	String getEndTime() {
		return endTime;
	}

	String getActivity() {
		return activityLabel;
	}

	
	public String getDay() {
		return this.startTime.split(" ")[0];
	}
}
