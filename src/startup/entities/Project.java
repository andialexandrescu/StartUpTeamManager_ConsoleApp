package startup.entities;
import startup.entities.enums.TechStack;
import startup.entities.pairs.SchedulePair;
import java.time.LocalDate;
import java.util.*;

public class Project
{
    private static int no_projects;
    private String title;
    private Set<TeamMember> members; // many-to-many relationship with TeamMember
    // represent a Set instead of List to make sure there are no duplicates when it comes to people being involved in a certain project
    private TeamManager manager;
    private Map<TechStack, Integer> tech_requirements; // represents the number of team members required for that particular tech stack in the project
    private SchedulePair deadline; // deadline contains both start_date and end_date details for a project

    public Project(String title, TeamManager team_manager, SchedulePair deadline)
    {
        this.title = title;
        no_projects++;
        this.manager = team_manager;
        addDeadline(deadline);
        auxHelper();
    }
    public Project(String title, TeamManager team_manager, SchedulePair deadline, Set<TeamMember> members, HashMap<TechStack, Integer> tech_requirements)
    {
        this(title, team_manager, deadline);
        this.members = new HashSet<>(members); // deep copy
        this.tech_requirements = new HashMap<>(tech_requirements);
    }
    private void auxHelper() // internal logic for constructors
    {
        this.members = new HashSet<>();
        this.tech_requirements = new HashMap<>();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public static int getNoProjects()
    {
        return no_projects;
    }

    public Set<TeamMember> getMembers()
    {
        return members;
    }

    public TeamManager getManager()
    {
        return manager;
    }

    public void setManager(TeamManager manager)
    {
        this.manager = manager;
    }

    public Map<TechStack, Integer> getTechRequirements()
    {
        return tech_requirements;
    }

    public SchedulePair getDeadline()
    {
        return deadline;
    }

    public void addDeadline(SchedulePair deadline)
    {
        if(deadline.getStartDate().isBefore(deadline.getEndDate()))
        {
            this.deadline = deadline;
        } else
        {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

    public void modifyDeadline(String choice, LocalDate date)
    {
        if(deadline == null)
        {
            throw new IllegalStateException("Deadline is not set");
        }

        switch(choice.toLowerCase())
        {
            case "start":
                if(date.isBefore(deadline.getEndDate()))
                {
                    deadline.setStartDate(date);
                } else
                {
                    throw new IllegalArgumentException("New start date must be before end date");
                }
                break;
            case "end":
                if(date.isAfter(deadline.getStartDate()))
                {
                    deadline.setEndDate(date);
                } else
                {
                    throw new IllegalArgumentException("New end date must be after start date");
                }
                break;
            default:
                throw new IllegalArgumentException("Choice must be 'start' or 'end'");
        }
    }

    public void addMember(TeamMember member)
    {
        if(!members.add(member))
        {
            throw new IllegalArgumentException("Member is already part of the project");
        }
    }

    public void removeMember(TeamMember member)
    {
        if(members == null || !members.contains(member))
        {
            throw new IllegalArgumentException("Member is not part of the project");
        }
        members.remove(member);
    }

    public void addTechRequirement(TechStack techStack, int count)
    {
        this.tech_requirements.put(techStack, count);
    }

    public void removeTechRequirement(TechStack techStack)
    {
        this.tech_requirements.remove(techStack);
    }

    public int countMembersWithTechSkill(TechStack techStack)
    {
        int k = 0;
        for(TeamMember member : members)
        {
            if(member.hasTechSkill(techStack))
            {
                k++;
            }
        }
        return k;
    }

    public void display()
    {
        System.out.println();
        System.out.println("Defined in the management system as a " + this + "\n"); // using sout(this)
        System.out.println(getTitle() + " project's details:");
        System.out.println("\tManager: " + manager.getName());
        System.out.println("\tDeadline: " + deadline);
        System.out.println("\tTech Requirements:");
        for(Map.Entry<TechStack, Integer> entry : tech_requirements.entrySet())
        {
            System.out.println("\t\t" + entry.getKey() + ": " + entry.getValue() + " required");
        }
        System.out.println("\tTeam Members:");
        for (TeamMember member : members)
        {
            System.out.println("\t\t" + member.getName() + " (" + member.getEmail() + ")");
        }
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " object, with the following specifications: \n" +
                "(title=" + title + "\nmembers=" + members + "\nmanager=" + manager + "\ntech_requirements=" + tech_requirements + ", deadline" + deadline + ")";
    }
}