package startup.entities.pairs;
import java.time.LocalDate;

public class SchedulePair
{
    private LocalDate start_date;
    private LocalDate end_date;

    public SchedulePair(LocalDate start_date, LocalDate end_date)
    {
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public LocalDate getStartDate()
    {
        return start_date;
    }

    public void setStartDate(LocalDate start_date)
    {
        this.start_date = start_date;
    }

    public LocalDate getEndDate()
    {
        return end_date;
    }

    public void setEndDate(LocalDate end_date)
    {
        this.end_date = end_date;
    }

    @Override
    public String toString()
    {
        return "(start_date=" + start_date + ", end_date=" + end_date + ")";
    }
}