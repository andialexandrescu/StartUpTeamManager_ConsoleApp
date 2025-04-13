package startup.entities;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Task
{
    private String title;
    private String description;
    private LocalTime completion_time;
    private Set<TeamMember> assigned_members;
    private List<Comment> comments;
    private List<TimeLog> time_logs;

    public Task(String title, String description)
    {
        this.title = title;
        this.description = description;
        this.assigned_members = new HashSet<>();
        this.comments = new ArrayList<>();
        this.time_logs = new ArrayList<>();
    }
    public Task(String title, String description, LocalTime completion_time)
    {
        this.title = title;
        this.description = description;
        this.completion_time = completion_time;
        this.assigned_members = new HashSet<>();
        this.comments = new ArrayList<>();
        this.time_logs = new ArrayList<>();
    }
    public Task(String title, String description, LocalTime completion_time, Set<TeamMember> assigned_members, List<Comment> comments, List<TimeLog> time_logs)
    {
        this.title = title;
        this.description = description;
        this.completion_time = completion_time;
        this.assigned_members = new HashSet<>(assigned_members);
        this.comments = new ArrayList<>(comments);
        this.time_logs = new ArrayList<>(time_logs);
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalTime getCompletionTime()
    {
        return completion_time;
    }

    public void setCompletionTime(LocalTime completionTime)
    {
        this.completion_time = completionTime;
    }

    public Set<TeamMember> getAssignedMembers()
    {
        return assigned_members;
    }

//    public void addAssignedMember(TeamMember member)
//    {
//        assigned_members.add(member);
//    }

    public List<Comment> getComments()
    {
        return comments;
    }

    public List<TimeLog> getTimeLogs()
    {
        return time_logs;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " object, with the following specifications: \n" +
                "(title=" + title + ", description=" + description + ", required completion time=" + completion_time + ", assigned team members for task=" + assigned_members + ", comments=" + comments + ", time logs=" + time_logs + ")";
    }
}