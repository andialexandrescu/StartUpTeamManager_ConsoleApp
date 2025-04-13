package startup.entities.services;
import startup.entities.Feedback;
import startup.entities.Project;
import startup.entities.Task;
import startup.entities.TimeLog;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public interface DeliverProjectInterface
{
    void displayTasksWithExceededCompletionTime(Project project);
    void addTimeLogToTask(Project project, Task task, TimeLog time_log, LocalDate log_date);
    void addFeedbackToProject(Project project, Feedback feedback);
    void deliverProject(Project project);
    void displayDeliveredProject(Project project);
}
