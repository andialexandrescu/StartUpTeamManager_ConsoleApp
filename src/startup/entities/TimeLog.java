package startup.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class TimeLog
{
    private TeamMember member;
    private Task related_task;
    private Map<LocalDate, LocalTime> work_hours;

    public TimeLog(TeamMember member, Task related_task)
    {
        this.member = member;
        this.related_task = related_task;
        this.work_hours = new HashMap<>();
    }

    public TeamMember getMember()
    {
        return member;
    }

    public Task getRelatedTask()
    {
        return related_task;
    }

    public Map<LocalDate, LocalTime> getAllWorkHours()
    {
        return work_hours;
    }

    public void setWorkHours(LocalDate date, LocalTime hours)
    {
        if(hours.getHour() > 10)
        {
            throw new IllegalArgumentException("Cannot log more than 10 hours a day");
        }
        work_hours.put(date, hours);
    }

    public LocalTime getWorkHoursBasedOnDate(LocalDate date)
    {
        return work_hours.get(date);
    }

    public void addWorkHours(LocalDate date, LocalTime hours)
    {
        // already logged hours for the given date, if not, set default to 0 hours
        LocalTime existing_hours = work_hours.containsKey(date) ? work_hours.get(date) : LocalTime.of(0, 0);

        int total_hours = existing_hours.getHour() + hours.getHour();
        int total_minutes = existing_hours.getMinute() + hours.getMinute();
        total_hours += total_minutes / 60;
        total_minutes = total_minutes % 60; // useless

        if(total_hours > 10)
        {
            throw new IllegalArgumentException("Cannot log more than 10 hours a day");
        }
        work_hours.put(date, LocalTime.of(total_hours, total_minutes));
    }

}
