import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class TaskExecutor {
    private final List<MonitoredData> data;

    public TaskExecutor(){
        data = new ArrayList<>();
    }

    public static void main(String[] args) {
        TaskExecutor taskExecutor = new TaskExecutor();
        taskExecutor.doTask1();
        taskExecutor.doTask2();
        taskExecutor.doTask3();
        taskExecutor.doTask4();
        taskExecutor.doTask5();
        taskExecutor.doTask6();
    }

    public void doTask1() {
        InputStream inputStream = getClass().getResourceAsStream("Activity.txt");
        FileOutputStream outputStream;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] aux = line.split("\t\t");
                data.add(new MonitoredData(aux[0],aux[1],aux[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream = new FileOutputStream("Task_1.txt");
            data.forEach(line -> {
                try {
                    outputStream.write((line.toString() + "\n").getBytes());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            outputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public void doTask2() {
        FileOutputStream outputStream;
        int count = (int) data.stream()
                .filter(distinctByKey(MonitoredData::getDistinctDay))
                .count();
        try {
            outputStream = new FileOutputStream("Task_2.txt");
            outputStream.write(("Total number of distinct days: " + count).getBytes());
            outputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void doTask3() {
        HashMap<String, Integer> activityOccurrences = new HashMap<>();
        List<String> activities = data.stream().filter(distinctByKey(MonitoredData::getActivity))
                .map(MonitoredData::getActivity).collect(Collectors.toList());
        FileOutputStream outputStream;
        activities.forEach(activity -> {
            int count = (int) data.stream()
                    .filter(f -> activity.equals(f.getActivity()))
                    .count();
            activityOccurrences.put(activity, count);
        });
        try {
            outputStream = new FileOutputStream("Task_3.txt");
            activityOccurrences.forEach((key,value) -> {
                try {
                    outputStream.write((key + ": " + value + "\n").getBytes());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            outputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void printTask4(HashMap<Integer, HashMap<String, Integer>> activitiesOverDays) {
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream("Task_4.txt");
            activitiesOverDays.forEach((key,value) -> {
                try {
                    outputStream.write(("Day " + key + ":\n").getBytes());
                    value.forEach((key1, value1) -> {
                        try {
                            outputStream.write((key1 + ": " + value1 + "\n").getBytes());
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    });
                    outputStream.write(("\n".getBytes()));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            outputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void doTask4() {
        HashMap<Integer, HashMap<String, Integer>> activitiesOverDays = new HashMap<>();
        List<Integer> daysList = data.stream().filter(distinctByKey(MonitoredData::getDistinctDay))
                                                .map(MonitoredData::getDistinctDay).collect(Collectors.toList());
        List<String> activities = data.stream().filter(distinctByKey(MonitoredData::getActivity))
                                                .map(MonitoredData::getActivity).collect(Collectors.toList());
        daysList.forEach(day -> {
            HashMap<String, Integer> map = new HashMap<>();
            int dayKey = day - data.get(0).getDistinctDay() + 1;
            List<MonitoredData> dataList = data.stream()
                    .filter(f -> day == f.getDistinctDay()).collect(Collectors.toList());

            activities.forEach(activity -> {
                int count = (int) dataList.stream().filter(d -> d.getActivity().equals(activity)).count();
                map.put(activity, count);
            });
            activitiesOverDays.put(dayKey, map);
        });
        printTask4(activitiesOverDays);
    }

    public void doTask5() {
        HashMap<String, Long> taskDuration = new HashMap<>();
        FileOutputStream outputStream;
        List<String> activities = data.stream().filter(distinctByKey(MonitoredData::getActivity))
                .map(MonitoredData::getActivity).collect(Collectors.toList());
        activities.forEach(activity -> {
            long minutes = data.stream().filter(monitoredData -> monitoredData.getActivity().equals(activity))
                                                .map(m -> Duration.between(m.getStartTime(), m.getEndTime()))
                                                .mapToLong(d -> Math.abs(d.toMinutes()))
                                                .sum();
            taskDuration.put(activity, minutes);
        });
        try {
            outputStream = new FileOutputStream("Task_5.txt");
            taskDuration.forEach((key,value) -> {
                try {
                    outputStream.write((key + " duration: " + value + "minutes\n").getBytes());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            outputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void doTask6() {
        FileOutputStream outputStream;
        List<String> result = new ArrayList<>();
        List<String> activities = data.stream().filter(distinctByKey(MonitoredData::getActivity))
                .map(MonitoredData::getActivity).collect(Collectors.toList());
        activities.forEach(activity -> {
            List<MonitoredData> activityData = data.stream().filter(f -> f.getActivity().equals(activity))
                                                    .collect(Collectors.toList());
            int percent90 = activityData.size() * 9 / 10;
            int count = (int) activityData.stream().filter(a -> Math.abs(Duration.between(a.getStartTime(),a.getEndTime())
                                            .toMinutes()) < 5).count();
            if(count >= percent90) result.add(activity);
        });
        try {
            outputStream = new FileOutputStream("Task_6.txt");
            result.forEach(activity -> {
                try {
                    outputStream.write((activity + "\n").getBytes());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            outputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
