import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MonitoredData {
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String activity;

    public MonitoredData(String startTime, String endTime, String activity) {
        this.startTime = LocalDateTime.parse(startTime, dateFormat);
        this.endTime = LocalDateTime.parse(endTime, dateFormat);
        this.activity = activity;
    }

    public String getActivity() {
        return activity;
    }

    public String getStartTimeAsString() {
        return startTime.format(dateFormat);
    }

    public String getEndTimeAsString() {
        return endTime.format(dateFormat);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getDistinctDay() {
        return startTime.getDayOfYear();
    }

    @Override
    public String toString() {
        return "( " + activity + ", " + startTime.format(dateFormat) + ", " + endTime.format(dateFormat) + " )";
    }

}
