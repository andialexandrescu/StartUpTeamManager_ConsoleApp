package startup.entities;
import java.util.HashSet;
import java.util.Set;

public class Company // singleton design pattern
{
    private static Company instance; // static variable for the single instance I am allowed to create

    private String name;
    private String address;
    private Set<TeamMember> hired_team_members; // hired team members
    private Set<Project> projects;

    private Company(String name, String address) // private constructor to prevent instantiation from outside
    {
        this.name = name;
        this.address = address;
        this.hired_team_members = new HashSet<>();
        this.projects = new HashSet<>();
    }
    public static synchronized Company getInstance(String name, String address) // public static method for global access to instance
    {
        if(instance == null)
        {
            instance = new Company(name, address);
        }
        return instance;
    }
    public static synchronized Company getInstance() // static method to get the existing instance (no parameters)
    {
        if(instance == null)
        {
            throw new IllegalStateException("Company instance has not been initialized, use getInstance(String, String) first");
        }
        return instance;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Set<TeamMember> getTeamMembers()
    {
        return hired_team_members;
    }

    public Set<Project> getProjects()
    {
        return projects;
    }

    public void addTeamMember(TeamMember teamMember)
    {
        if(teamMember == null)
        {
            throw new IllegalArgumentException("Team member cannot be null");
        }
        hired_team_members.add(teamMember);
    }

    public void addProject(Project project)
    {
        if(project == null)
        {
            throw new IllegalArgumentException("Project cannot be null");
        }
        projects.add(project);
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " object, with the following specifications: \n" +
                "name=" + name + ", address=" + address + ", employees=" + hired_team_members + ", projects=" + projects + ")";
    }
}
