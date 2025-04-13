package startup.entities;
import java.time.LocalDate;

public class Comment // reminds me of approving merge requests
{
    private TeamMember author;
    private String content;
    private final LocalDate posted_date;

    public Comment(TeamMember author, String content, LocalDate posted_date)
    {
        this.author = author;
        this.content = content;
        this.posted_date = posted_date;
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
    public void setPostedDate(LocalDate posted_date)
    {
        throw new UnsupportedOperationException("Posted date is a final attribute and cannot be changed");
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " object, with the following specifications: \n" +
                "(author=" + author.getName() + ", content=" + content + ", posted_date=" + posted_date;
    }
}
