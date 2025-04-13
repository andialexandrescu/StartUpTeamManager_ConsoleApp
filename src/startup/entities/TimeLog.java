package startup.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class TimeLog
{
    private TeamMember member;
    private Map<LocalDate, LocalTime> work_hours;

    public TimeLog(TeamMember member)
    {
        this.member = member;
        this.work_hours = new HashMap<>();
    }

    public TeamMember getMember()
    {
        return member;
    }

    public Map<LocalDate, LocalTime> getAllWorkHours()
    {
        return work_hours;
    }

    public LocalTime getWorkHoursBasedOnDate(LocalDate date)
    {
        return work_hours.get(date);
    }

    public void setWorkHours(LocalDate date, LocalTime hours)
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

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " object, with the following specifications: \n" +
                "(member=" + member.getName() + ", work_hours=" + work_hours + ")";
    }
}
