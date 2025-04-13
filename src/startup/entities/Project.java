package startup.entities;
import startup.entities.comparators.TechRequirementsComparator;
import startup.entities.enums.TechStack;
import startup.entities.pairs.SchedulePair;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Project
{
    private static int no_projects;
    private String title;
    private Set<TeamMember> members; // many-to-many relationship with TeamMember
    // represent a Set instead of List to make sure there are no duplicates when it comes to people being involved in a certain project
    private TeamManager manager;
    private Map<TechStack, Integer> tech_requirements; // represents the number of team members required for that particular tech stack in the project
    private SchedulePair deadline; // deadline contains both start_date and end_date details for a project
    private List<Task> tasks; // one-to-many relationship with Task
    private List<Feedback> feedback_list;

    public Project(String title, TeamManager team_manager, SchedulePair deadline)
    {
        validateTitle(title); // logic that doesn't change after the object was created
        this.title = title;
        no_projects++;
        this.manager = team_manager;
        this.setDeadline(deadline);
        auxHelper();
    }
    public Project(String title, TeamManager team_manager, SchedulePair deadline, Set<TeamMember> members, HashMap<TechStack, Integer> tech_requirements, List<Task> tasks, List<Feedback> feedback_list)
    {
        this(title, team_manager, deadline);
        this.members = new HashSet<>(members); // deep copy
        this.tech_requirements = new HashMap<>(tech_requirements);
        this.tasks = new ArrayList<>(tasks);
        this.feedback_list = new ArrayList<>(feedback_list);
    }
    private void auxHelper() // internal logic for constructors
    {
        this.members = new HashSet<>();
        this.tech_requirements = new HashMap<>();
        this.tasks = new ArrayList<>();
        this.feedback_list = new ArrayList<>();
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

    public void setDeadline(SchedulePair deadline)
    {
        if(deadline == null)
        {
            throw new IllegalArgumentException("Deadline cannot be null");
        }

        if(deadline.getStartDate().isBefore(deadline.getEndDate()))
        {
            this.deadline = deadline;
        } else
        {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

    public SchedulePair getDeadline()
    {
        return deadline;
    }

    public List<Task> getTasks()
    {
        return tasks;
    }

    public void setTasks(List<Task> tasks)
    {
        if(tasks == null)
        {
            throw new IllegalArgumentException("Tasks list cannot be null");
        }
        this.tasks = new ArrayList<>(tasks);
    }

    public List<Feedback> getFeedbackList()
    {
        return feedback_list;
    }

    private void validateTitle(String title)
    {
        if(title == null || title.trim().isEmpty()) // null or empty spaces followed by no characters
        {
            throw new IllegalArgumentException("Project title cannot be null or empty");
        }
        if(title.length() <= 5)
        {
            throw new IllegalArgumentException("Project title must be more than 5 characters");
        }
        String[] words = title.trim().split("\\s+"); // just like in Python, split by any type of whitespace
        if(words.length > 3)
        {
            throw new IllegalArgumentException("Project title must contain at most 3 words, otherwise it will be too long");
        }

        Set<String> not_allowed = Set.of("project", "application", "app", "solutions", "software", "web", "mobile", "tool", "platform");
        String aux_title = title.toLowerCase();
        for(String keyword : not_allowed)
        {
            if(aux_title.contains(keyword))
            {
                throw new IllegalArgumentException("Project title cannot contain keyword: " + keyword + " since it has to be an unique name");
            }
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

    // these methods should only be used by the service layer
//    public void addMember(TeamMember member)
//    {
//        if(!members.add(member))
//        {
//            throw new IllegalArgumentException("Member is already part of the project");
//        }
//    }

//    public void removeMember(TeamMember member)
//    {
//        if(members == null || !members.contains(member))
//        {
//            throw new IllegalArgumentException("Member is not part of the project");
//        }
//        members.remove(member);
//    }

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

    public void sortTechRequirements()
    {
        List<Map.Entry<TechStack, Integer>> list = new ArrayList<>(tech_requirements.entrySet()); // entrySet() returns a Set containing all the entries (key-value pairs) from the map, which is then converted into a List
        list.sort(new TechRequirementsComparator()); // sorting the list using a custom comparator

        Map<TechStack, Integer> sorted_map = new LinkedHashMap<>(); // unlike HashMap, LinkedHashMap maintains the order in which entries were inserted, because the entries are sorted in a specific order (by value and then by key), that order needs to be part of the final map
        for(Map.Entry<TechStack, Integer> entry : list)
        {
            sorted_map.put(entry.getKey(), entry.getValue());
        }

        this.tech_requirements = sorted_map;
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
        for(TeamMember member : members)
        {
            System.out.println("\t\t" + member.getName() + " (" + member.getEmail() + ")");
        }
        System.out.println("\tTasks:");
        for(Task tsk : tasks)
        {
            System.out.println("\t\t" + tsk.getTitle() + " (" + tsk.getDescription() + ")");
        }
        System.out.println("\tFeedback:");
        for(Feedback feedback : feedback_list)
        {
            System.out.println("\t\t" + feedback.getStage() + " (" + feedback.getCriteriaScores() + ")");
        }

    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " object, with the following specifications: \n" +
                "(title=" + title + "\nmembers=" + members + "\nmanager=" + manager + "\ntech_requirements=" + tech_requirements + ", deadline" + deadline + "\ntasks" + tasks + "\nfeedback_list=" + feedback_list + ")";
    }
}