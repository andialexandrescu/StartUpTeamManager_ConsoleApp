package startup.entities;
import java.time.LocalDate;

public class Comment // reminds me of approving merge requests
{
    private TeamMember author;
    private String content;
    private final LocalDate posted_date;
    private Task related_task;

    public Comment(TeamMember author, String content, LocalDate posted_date, Task related_task)
    {
        this.author = author;
        this.content = content;
        this.posted_date = posted_date;
        this.related_task = related_task;
    }

    public TeamMember getAuthor()
    {
        return author;
    }

    public void setAuthor(TeamMember author)
    {
        this.author = author;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public LocalDate getPostedDate() // doesn't have a setter since it's a final attribute
    {
        return posted_date;
    }

    public Task getRelatedTask()
    {
        return related_task;
    }

    public void setRelatedTask(Task related_task)
    {
        this.related_task = related_task;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " object, with the following specifications: \n" +
                "(author=" + author.getName() + ", content=" + content + ", posted_date=" + posted_date + ", related_task=" + related_task.getTitle();
    }
}
