package startup.entities;
import startup.entities.abstracts.Employee;
import startup.entities.enums.*;
import startup.entities.pairs.SchedulePair;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamMember extends Employee
{
    private static int no_members; // needed this global variable to retain each employee so that I can use this metric when implementing the increaseSalary() method in TeamManager
    private List<SchedulePair> reserved_time_slot; // list of tuples/ pairs of start and end dates for projects which are reserved since the individual will be working on these projects
    private Set<Role> earned_roles;
    private List<Project> assigned_projects; // many-to-many relationship with Project
    // I don't see the reason for replacing List with Set for projects since a member might rework on past projects
    private Set<TechStack> tech_knowledge;
    // I don't want setters for any list of objects, they will all be exclusively assigned in other methods


    // two overloaded constructors
    public TeamMember(String name, String email, double salary, LocalDate employed_date)
    {
        super(name, email, salary, employed_date);
        no_members++;
        this.reserved_time_slot = new ArrayList<>();
        this.earned_roles = new HashSet<>();
        this.assigned_projects = new ArrayList<>();
        this.tech_knowledge = new HashSet<>();
    }
    public TeamMember(String name, String email, double salary, LocalDate employed_date, List<SchedulePair> reserved_time_slot, List<Role> earned_roles, List<Project> assigned_projects, List<TechStack> tech_knowledge)
    {
        super(name, email, salary, employed_date);
        no_members++;
        this.reserved_time_slot = new ArrayList<>(reserved_time_slot); // deep copy instead of shallow
        this.earned_roles = new HashSet<>(earned_roles);
        this.assigned_projects = new ArrayList<>(assigned_projects);
        this.tech_knowledge = new HashSet<>(tech_knowledge);
    }
    public TeamMember(TeamMember other_member) // copy constructor using deep copy
    {
        // name and email are protected, otherwise for name I should've used when concatenating with no_members + 1 String.valueOf(no_members + 1) instead
        super(other_member.name + (no_members + 1), other_member.email, other_member.getSalary(), other_member.getEmployedDate());
        no_members++; // I can't do this beforehand since super() must be the first statement in any type of constructor
        this.reserved_time_slot = new ArrayList<>(other_member.getReservedTimeSlot());
        this.earned_roles = new HashSet<>(other_member.getEarnedRoles());
        this.assigned_projects = new ArrayList<>(other_member.getAssignedProjects());
        this.tech_knowledge = new HashSet<>(other_member.getTechKnowledge());
    }

    public static int getNoMembers()
    {
        return no_members;
    }

    public List<SchedulePair> getReservedTimeSlot()
    {
        return reserved_time_slot;
    }

    public void setReservedTimeSlot(List<SchedulePair> schedule_list)
    {
        reserved_time_slot = new ArrayList<>(schedule_list);
    }

    public Set<Role> getEarnedRoles()
    {
        return earned_roles;
    }

    public void setEarnedRoles(List<Role> earned_roles)
    {
        this.earned_roles = new HashSet<>(earned_roles); // deep copy to avoid external modifications
    }

    public List<Project> getAssignedProjects()
    {
        return assigned_projects;
    }

    public Set<TechStack> getTechKnowledge()
    {
        return tech_knowledge;
    }

    public boolean checkRole(Role role)
    {
        return earned_roles.contains(role);
    }

    public void addRole(Role role)
    {
        if(!earned_roles.add(role)) // performs the addition in the Set variable
        {
            throw new IllegalArgumentException("Role already earned by individual");
        }
    }

    public void removeRole(Role role)
    {
        if(earned_roles.contains(role))
        {
            earned_roles.remove(role);
        } else
        {
            throw new IllegalArgumentException("Role not found in the earned roles list for the current individual");
        }
    }

    public void addTechKnowledge(TechStack techStack)
    {
        if(techStack == null)
        {
            throw new IllegalArgumentException("TechStack cannot be null");
        }
        tech_knowledge.add(techStack);
    }

    public boolean hasTechSkill(TechStack techStack)
    {
        return tech_knowledge.contains(techStack);
    }

    public boolean checkSchedule(SchedulePair current_schedule_pair)
    {
        if(current_schedule_pair.getStartDate().isBefore(current_schedule_pair.getEndDate()))
        {
            for(SchedulePair existingPair : reserved_time_slot)
            {
                if(current_schedule_pair.getStartDate().isBefore(existingPair.getEndDate()) &&
                        current_schedule_pair.getEndDate().isAfter(existingPair.getStartDate()))
                {
                    return false; // overlaps
                }
            }
            return true;
        } else
        {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

    public void addSchedule(SchedulePair current_schedule_pair)
    {
        if(checkSchedule(current_schedule_pair))
        {
            reserved_time_slot.add(current_schedule_pair); // surpasses exceptions + no overlap
        } else
        {
            throw new IllegalArgumentException("Schedule overlaps with an existing reserved time slot, you will be prompted to reschedule");
        }
    }

    @Override
    public void increaseSalary(int percentage) // abstract method in Employee class
    {
        int no_projects = assigned_projects.size();
        double project_percentage = (double) (no_projects * 100) / Project.getNoProjects(); // I want to obtain the exact percentage from this formula: (x/100) * total = no_projects, where x is project_percentage and total is the static variable in Project class (retrieved by using Project.getNoProjects())
        double mean = (percentage + project_percentage) / 2;
        double increaseAmount = (getSalary() * mean / 100);
        setSalary(getSalary() + increaseAmount);
    }

    @Override
    public void display()
    {
        System.out.println();
        System.out.println("Defined in the management system as a " + this + "\n"); // using sout(this)
        System.out.println(name + " team member's details:");
        System.out.println("\tEmail: " + email);
        System.out.println("\tSalary: $" + getSalary());
        System.out.println("\tEmployed Date: " + getEmployedDate());
        System.out.println("\tAssigned Roles: " + getEarnedRoles());
        System.out.println("\tTech knowledge: " + getTechKnowledge());
        System.out.println("\tReserved Time Slots: " + getReservedTimeSlot());
        System.out.println("\tAssigned Projects: ");
        for(Project project : getAssignedProjects()) // each project gets displayed properly
        {
            System.out.println("\t\t" + project.getTitle());
        }
        System.out.println("Number of current team members objects: " + getNoMembers());
    }

    @Override
    public String toString() // sout(obj) where obj is of type TeamMember/ TeamManager will know how to format the output accordingly
    {
        return getClass().getSimpleName() + " object, with the following specifications: \n" +
                "(name=" + name + ", email=" + email + ", salary=" + getSalary() + ", employed_date=" + getEmployedDate() + ", reserved_time_slot=" + reserved_time_slot + ", earned_roles=" + earned_roles + ", assigned_projects=" + assigned_projects + ", tech_knowledge=" + tech_knowledge + ", no_members=" + no_members + ")";
    }
}