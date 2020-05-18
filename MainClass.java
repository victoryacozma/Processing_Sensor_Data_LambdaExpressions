import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainClass {

	public static void main(String[] args) throws IOException {
		Path filePath = Paths.get("V:/AN2/Teme_TP/tema5", "Activities.txt");
		List<MonitoredData> activities = new ArrayList();
		FileWriter task1 = new FileWriter("task1.txt");
		FileWriter task2 = new FileWriter("task2.txt");
		FileWriter task3 = new FileWriter("task3.txt");
		FileWriter task4 = new FileWriter("task4.txt");
		FileWriter task5 = new FileWriter("task5.txt");
		FileWriter task6 = new FileWriter("task6.txt");

		// TASK1
		try {
			Stream<String> myLines = Files.lines(filePath);

			// traverse the stream to produce a result
			Consumer<String> readText = (String x) -> {
				String[] splitText = x.split("\\t\\t");
				String[] splitLast = splitText[2].split("\\t");
				activities.add(new MonitoredData(splitText[0], splitText[1], splitLast[0]));
				try {
					task1.write(splitText[0] + " " + splitText[1] + " " + splitLast[0] + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};

			// pentru fiecare linie aplicam consumerul
			myLines.forEach(readText);
			myLines.close();
			task1.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// TASK2
		// vom folosi un hashmap pentru a numara valorile distincte
		HashMap<String, Integer> days = new HashMap<String, Integer>();
		Consumer<MonitoredData> count = (MonitoredData x) -> {
			String[] date = x.getStartTime().split(" ");
			String[] day = date[0].split("-");
			days.put(day[2], 1);
		};

		activities.forEach(count);
		try {
			task2.write(days.size() + " zile distincte");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		task2.close();

		// TASK3
		HashMap<String, Integer> myMap = new HashMap<String, Integer>();

		Consumer<MonitoredData> counter = (MonitoredData x) -> {
			if (myMap.get(x.getActivity()) != null) {
				myMap.put(x.getActivity(), myMap.get(x.getActivity()) + 1);
			} else {
				myMap.put(x.getActivity(), 1);
			}
		};
		activities.forEach(counter);

		Consumer<String> afisare = (String x) -> {
			try {
				task3.write(myMap.get(x) + " aparitii pentru activitatea " + x + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(myMap.get(x) + " aparitii pentru activitatea " + x);
		};
		myMap.keySet().forEach(afisare);
		task3.close();

		// TASK4
		Map<String, Map<String, Long>> mapActivities = new LinkedHashMap<String, Map<String, Long>>();
		mapActivities = activities.stream().collect(Collectors.groupingBy(MonitoredData::getDay,
				Collectors.groupingBy(MonitoredData::getActivity, Collectors.counting())));
		// System.out.println(mapActivities + "\n");
		try {
			task4.write(mapActivities + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		task4.close();

		// TASK5
		HashMap<String, Duration> map = new HashMap<String, Duration>();

		Consumer<MonitoredData> myCount = (MonitoredData x) -> {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime endTime = LocalDateTime.parse(x.getEndTime(), formatter);
			LocalDateTime startTime = LocalDateTime.parse(x.getStartTime(), formatter);
			Duration duration = Duration.between(startTime, endTime);

			if (map.get(x.getActivity()) != null) {
				map.put(x.getActivity(), map.get(x.getActivity()).plus(duration));
			} else {
				map.put(x.getActivity(), duration);
			}
		};
		activities.forEach(myCount);

		BiConsumer<String, Duration> select = (String x, Duration y) -> {
			try {
				task5.write(x + " dureaza " + y.toHours() + " h (" + y.toMinutes() + ") minute\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
		map.forEach(select);
		task5.close();

		// TASK6
		HashMap<String, Integer> map6 = new HashMap<String, Integer>();
		List<String> list = new ArrayList<String>();

		Consumer<MonitoredData> count6 = (MonitoredData x) -> {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime endTime = LocalDateTime.parse(x.getEndTime(), formatter);
			LocalDateTime startTime = LocalDateTime.parse(x.getStartTime(), formatter);
			Duration duration = Duration.between(startTime, endTime);

			if (duration.toMillis() < 5 * 60 * 1000) {

				if (map6.get(x.getActivity()) != null) {
					map6.put(x.getActivity(), map6.get(x.getActivity()) + 1);
				} else {
					map6.put(x.getActivity(), 1);
				}
			}
		};

		Consumer<String> filter = (String x) -> {

			double ninetyPercent = 90 * myMap.get(x) / 100;
			if (map6.get(x) >= ninetyPercent) {
				list.add(x);
			}
		};

		Consumer<String> print = (String x) -> {

			try {
				task6.write(x + " are 90%  din activitai cu durata mai mica de 5 min");
			} catch (IOException e) {
				e.printStackTrace();
			}
		};

		activities.forEach(count6);
		map6.keySet().forEach(filter);
		list.forEach(print);
		task6.close();
	}
}
