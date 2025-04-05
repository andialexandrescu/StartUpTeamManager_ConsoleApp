package startup.entities.abstracts;
import java.time.LocalDate;

abstract public class Employee
{
    // name and employed_date never change once objects are instantiated (const/ final)
    private double salary;
    protected final String name;
    protected String email; // I want these attributes to be accessible without using getters in the TeamMember and TeamManager subclasses
    private final LocalDate employed_date; // const: can't be modified after object has been created or in any other subclass

    public Employee(String name, String email, double salary, LocalDate employed_date)
    {
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.employed_date = employed_date;
    }

    // public abstract void read(); // abstract method to read employee details, with each subclass, more attributes need to be introduced
    public abstract void display(); // abstract method to display employee details

    public abstract void increaseSalary(int percentage); // in each Employee subclass increasing the salary will be based either on number of projects that individual worked on (TeamMember) or this previous criteria + number of teams coordonated (TeamManager)

    public LocalDate getEmployedDate()
    {
        return employed_date;
    }

    public double getSalary()
    {
        return salary;
    }

    public void setSalary(double salary)
    {
        this.salary = salary;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }
}
