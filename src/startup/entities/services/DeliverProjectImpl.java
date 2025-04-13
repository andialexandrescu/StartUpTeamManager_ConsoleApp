package startup.entities.services;

import startup.entities.*;
import startup.entities.enums.ProjectStage;
import startup.entities.enums.Role;
import startup.entities.enums.TechStack;
import startup.entities.pairs.SchedulePair;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static startup.entities.enums.ProjectStage.Initiation;

public class DeliverProjectImpl implements DeliverProjectInterface
{
    @Override
    public void displayDeliveredProject(Project project)
    {
        System.out.println("Displaying delivered project details:");

        System.out.println("\nTime Logs:");
        for(Task task : project.getTasks())
        {
            System.out.println("Task: " + task.getTitle());
            for(TimeLog timeLog : task.getTimeLogs())
            {
                System.out.println("\t- Member " + timeLog.getMember().getName() + ":");
                for(Map.Entry<LocalDate, LocalTime> entry : timeLog.getAllWorkHours().entrySet())
                {
                    System.out.println("\t\tdate: " + entry.getKey() + ", hours: " + entry.getValue());
                }
            }
        }

        System.out.println("\nFeedbacks:");
        for(Feedback feedback : project.getFeedbackList())
        {
            feedback.display();
        }
    }

    @Override
    public void displayTasksWithExceededCompletionTime(Project project)
    {
        List<Task> tasks = project.getTasks();
        System.out.println("Tasks exceeding allocated time:");
        for (Task task : tasks)
        {
            double total_hours_logged = 0.0;

            for(TimeLog timeLog : task.getTimeLogs()) // each time log for the task
            {
                Map<LocalDate, LocalTime> work_hours = timeLog.getAllWorkHours();

                for(LocalTime time : work_hours.values()) // the second value is of my interest
                {
                    double hours = time.getHour() + time.getMinute() / 60.0; // conversion from LocalTime to hours
                    total_hours_logged += hours;
                }
            }

            LocalTime completion_time = task.getCompletionTime(); // conversion from LocalTime to hours
            double completion_hours = completion_time.getHour() + completion_time.getMinute() / 60.0;

            if(total_hours_logged > completion_hours) // compare total hours logged for task with the completion time
            {
                System.out.println("Task: " + task.getTitle());
                System.out.println("- Allocated time: " + completion_hours + " hours");
                System.out.println("- Actual time spent: " + total_hours_logged + " hours");
                System.out.println("- Exceeded by: " + (total_hours_logged - completion_hours) + " hours");
            }
        }
    }

    @Override
    public void addTimeLogToTask(Project project, Task task, TimeLog time_log, LocalDate log_date)
    {
        if(task.getAssignedMembers().contains(time_log.getMember())) // team member assigned to this task
        {
            if(time_log.getAllWorkHours().containsKey(log_date)) // member already logged for this day, contains an available entry to add to
            {
                task.getTimeLogs().add(time_log);
                System.out.println("Time log added successfully for " + time_log.getMember() + " on task " + task.getTitle() + " for date " + log_date);
            } else
            {
                throw new IllegalArgumentException("The TimeLog does not contain an entry for the specified date: " + log_date);
            }
        } else
        {
            throw new IllegalArgumentException("Member " + time_log.getMember().getName() + " is not assigned to this task");
        }
    }

    @Override
    public void addFeedbackToProject(Project project, Feedback feedback)
    {
        if(feedback == null)
        {
            throw new IllegalArgumentException("Feedback cannot be null");
        }
        project.getFeedbackList().add(feedback);
        System.out.println("Feedback for stage " + feedback.getStage() + " added to project " + project.getTitle());
    }

    @Override
    public void deliverProject(Project project)
    {
        List<Feedback> feedback_list = project.getFeedbackList();

        for(Feedback feedback : feedback_list)
        {
            if(!feedback.isPositive())
            {
                throw new IllegalArgumentException("Project cannot be delivered, negative feedback found for stage: " + feedback.getStage());
            }
        }

        System.out.println("All feedback is positive, project " + project.getTitle() + " can be delivered");
        this.displayDeliveredProject(project);
    }

    public static void main(String[] args)
    {
        TeamManager head_manager = new TeamManager("Steve", "steve.professional@inspire.solve.outlook.com", 25000, LocalDate.of(2016, 5, 14));
        head_manager.addRole(Role.DataAnalyst);
        Project product_deduplication = new Project("Product deduplication", head_manager, new SchedulePair(LocalDate.of(2025, 3, 12), LocalDate.of(2025, 9, 27)));
        product_deduplication.addTechRequirement(TechStack.Python, 3);
        product_deduplication.addTechRequirement(TechStack.Cassandra, 2);
        product_deduplication.addTechRequirement(TechStack.ASPDotNET, 2);
        product_deduplication.addTechRequirement(TechStack.CSharp, 1);

        TeamBuilderImpl team_builder = new TeamBuilderImpl();

        TeamMember marius = new TeamMember("Marius", "marius.intern@inspire.intern.outlook.com", 12000, LocalDate.of(2020, 2, 9));
        TeamMember adrian = new TeamMember("Adrian", "adrian.ionescu@inspire.solve.outlook.com", 15000, LocalDate.of(2015, 5, 29));
        marius.addTechKnowledge(TechStack.Cassandra);
        marius.addTechKnowledge(TechStack.ASPDotNET);
        marius.addTechKnowledge(TechStack.Python);
        marius.addTechKnowledge(TechStack.CSharp);
        marius.addRole(Role.FrontEnd);
        adrian.addTechKnowledge(TechStack.Python);
        adrian.addTechKnowledge(TechStack.CSharp); // won't be shown after calling addTeamMember method, even though it's a project tech requirement because it has been fulfilled by the previous candidate
        adrian.addTechKnowledge(TechStack.Java);
        adrian.addTechKnowledge(TechStack.ASPDotNET);
        adrian.addRole(Role.BackEnd);
        adrian.addRole(Role.FullStack);

        team_builder.addTeamMember(product_deduplication, marius);
        team_builder.addTeamMember(product_deduplication, adrian);

        TimeLog time_log1 = new TimeLog(marius);
        TimeLog time_log2 = new TimeLog(adrian);
        time_log1.setWorkHours(LocalDate.of(2025, 3, 12), LocalTime.of(8, 0));
        time_log1.setWorkHours(LocalDate.of(2025, 3, 14), LocalTime.of(6, 0));
        time_log2.setWorkHours(LocalDate.of(2025, 3, 17), LocalTime.of(7, 0));
        time_log2.setWorkHours(LocalDate.of(2025, 3, 18), LocalTime.of(5, 0));

        ManageProjectImpl manage_project = new ManageProjectImpl();
        Task train_model = new Task("Train model", "Train the FastText model using the cleaned dataset in order to find semantic similarities for words in the most relevant columns", LocalTime.of(7, 0, 0));
        Task group_data = new Task("Group data", "Group the data based on the semantic similarities found in the previous step", LocalTime.of(3, 0, 0));
        manage_project.addTeamMemberToTask(product_deduplication, train_model, marius);
        manage_project.addTeamMemberToTask(product_deduplication, group_data, adrian);
        manage_project.addTaskToProject(product_deduplication, train_model);
        manage_project.addTaskToProject(product_deduplication, group_data);

        DeliverProjectImpl deliver_project = new DeliverProjectImpl();
        System.out.println("\n\t------------------Testing addTimeLogToTask:-------------------");
        deliver_project.addTimeLogToTask(product_deduplication, train_model, time_log1, LocalDate.of(2025, 3, 12));
        deliver_project.addTimeLogToTask(product_deduplication, train_model, time_log1, LocalDate.of(2025, 3, 14));
        deliver_project.addTimeLogToTask(product_deduplication, group_data, time_log2, LocalDate.of(2025, 3, 18));

        Feedback feedback1 = new Feedback(ProjectStage.Initiation);
        Feedback feedback2 = new Feedback(ProjectStage.Execution);
        feedback1.setCriteriaScores("no bugs", 9);
        feedback1.setCriteriaScores("on time", 5);
        feedback1.setCriteriaScores("on budget", 8);
        feedback2.setCriteriaScores("explained well", 7);
        feedback2.setCriteriaScores("overall quality", 8);
        feedback1.setApproveStageChoice(Boolean.TRUE);
        feedback2.setApproveStageChoice(Boolean.TRUE);
        System.out.println("\n\t------------------Testing addFeedbackToProject:-------------------");
        deliver_project.addFeedbackToProject(product_deduplication, feedback1);
        deliver_project.addFeedbackToProject(product_deduplication, feedback2);

        System.out.println("\n\t------------------Testing displayTasksWithExceededCompletionTime:-------------------");
        deliver_project.displayTasksWithExceededCompletionTime(product_deduplication);

        System.out.println("\n\t------------------Testing deliverProject:-------------------");
        deliver_project.deliverProject(product_deduplication);
    }
}
