package startup.entities;
import startup.entities.enums.*;
import startup.entities.pairs.SchedulePair;
import static startup.entities.enums.Role.*;
import java.time.LocalDate;
import java.util.List;

public final class TeamManager extends TeamMember // final because it cant determine the existence of any other subclasses
{
    private static int no_managers = 0;
    private int rank;
    private int no_supervised_members;

    // 4 overloaded constructors
    public TeamManager(String name, String email, double salary, LocalDate employed_date, int experience)
    {
        super(name, email, salary, employed_date);
        constructorHelper(experience);
    }
    public TeamManager(String name, String email, double salary, LocalDate employed_date, List<SchedulePair> reserved_time_slot, List<Role> earned_roles, List<Project> assigned_projects, List<TechStack> tech_knowledge, int experience)
    {
        super(name, email, salary, employed_date, reserved_time_slot, earned_roles, assigned_projects, tech_knowledge);
        constructorHelper(experience);
    }
    public TeamManager(String name, String email, double salary, LocalDate employed_date)
    {
        this(name, email, salary, employed_date, 0); // there is no such thing as implicit parameter value when not specified in Java
        // calling the other constructor to avoid rewriting the section of code where I determine the number of supervised team members
    }
    public TeamManager(String name, String email, double salary, LocalDate employed_date, List<SchedulePair> reserved_time_slot, List<Role> earned_roles, List<Project> assigned_projects, List<TechStack> tech_knowledge)
    {
        this(name, email, salary, employed_date, reserved_time_slot, earned_roles, assigned_projects, tech_knowledge, 0);
    }
    private void constructorHelper(int experience)
    {
        this.addRole(Role.valueOf(String.valueOf(FullStack))); // by default all TeamManagers should be full stack, but not all full stack engineers are team managers
        int aux_no_members = 0;
        for(Project project : getAssignedProjects())
        {
            aux_no_members += project.getMembers().size();
            if(project.getManager() == this)
            {
                aux_no_members -= 1;
            }
        }
        this.no_supervised_members = aux_no_members; // not including the current TeamManager in every project coordonated by him
        no_managers++;
        setRankBasedOnExperience(experience);
    }

    @Override
    public void increaseSalary(int percentage)
    {
        int no_projects = getAssignedProjects().size(); // getter for private attribute from TeamMember class vs. assigned_projects.size();
        double project_percentage = (double) (no_projects * 100) / Project.getNoProjects();

        double supervised_members_percentage = (double) (this.no_supervised_members * 100) / TeamMember.getNoMembers();

        double mean = percentage * 0.5 + project_percentage  * 0.2 + supervised_members_percentage * 0.3;
        double increaseAmount = (getSalary() * mean / 100);
        setSalary(getSalary() + increaseAmount);
    }

    private void setRankBasedOnExperience(int experience) // private method because it relates to the internal logic which shouldn't be visible elsewhere
    {
        // experience is an integer similar to rank however even though an individual can collect plenty of experience, anything above 10 can't increase the rank above 10
        if(experience < 0)
        {
            throw new IllegalArgumentException("Experience cannot be negative");
        } else if (experience == 0)
        {
            this.rank = 1;
        } else
        {
            this.rank = Math.min(experience, 10);
        }
    }

    @Override
    public void display()
    {
        super.display(); // overridden display() method from TeamMember
        System.out.println("Number of supervised members: " + no_supervised_members); // local variable
        System.out.println("Number of existing managers in our system: " + no_managers);
        System.out.println("Rank: " + rank);
    }

    @Override
    public String toString()
    {
        String className = getClass().getSimpleName();
        String superClassName = getClass().getSuperclass().getSimpleName();
        String upcastingMessage = className.equals(superClassName) ? "" : "(upcasting has been done)";
        String superString = super.toString();
        return upcastingMessage + " " + superString.substring(0, superString.length()-1) + ", no_managers=" + no_managers + ")";
        // the second parameter of substring is exclusive
    }
}