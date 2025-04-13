package startup.entities.services;
import startup.entities.*;
import startup.entities.enums.Role;
import startup.entities.enums.TechStack;
import startup.entities.pairs.SchedulePair;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageProjectImpl implements ManageProjectInterface
{
    @Override
    public void displayReport(Project project)
    {
        System.out.println();
        if(project == null)
        {
            throw new IllegalArgumentException("Project cannot be null");
        }

        System.out.println("Project Report \uD83D\uDDD0 for: " + project.getTitle());
        System.out.println("⇨⇨⇨----------------------------------------------");

        if(project.getTasks().isEmpty())
        {
            System.out.println("No tasks available for this project~~~");
            return;
        }
        for(Task task : project.getTasks())
        {
            System.out.println("Task Title: " + task.getTitle());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Completion Time: " + task.getCompletionTime() + " hours");
            System.out.println("Comments:");

            if(task.getComments().isEmpty())
            {
                System.out.println("\tNo comments for this task~~~");
            } else
            {
                for(Comment comment : task.getComments())
                {
                    System.out.println("\t- Content: " + comment.getContent());
                    System.out.println("\t  Author: " + comment.getAuthor());
                    System.out.println("\t  Posted Date: " + comment.getPostedDate());
                }
            }
            System.out.println("-----------------------------------------------⇨⇨⇨");
        }
    }

    @Override
    public void addTaskToProject(Project project, Task task)
    {
        System.out.println();
        if(task == null || project == null)
        {
            throw new IllegalArgumentException("Project and task cannot be null");
        }
        if(project.getTasks().size() >= 7)
        {
            throw new IllegalArgumentException("Cannot add more than 7 tasks to the project");
        }

        for(Task other_task : project.getTasks())// check if the task doesn't contain members who work on other tasks already
        {
            for(TeamMember member : other_task.getAssignedMembers())
            {
                if(task.getAssignedMembers().contains(member))
                {
                    throw new IllegalArgumentException("Cannot add task: team member conflict detected");
                }
            }
        }
        project.getTasks().add(task);
        System.out.println("Task " + task.getTitle() + " added successfully");
    }

    @Override
    public void removeTaskFromProject(Project project, Task task)
    {
        System.out.println();
        if(task == null || project == null)
        {
            throw new IllegalArgumentException("Project and task cannot be null");
        }
        if(!project.getTasks().remove(task))
        {
            throw new IllegalArgumentException("Task not found in the project");
        }
        System.out.println("Task " + task.getTitle() + " removed successfully");
    }

    @Override
    public void addCommentToTask(Task task, Comment comment)
    {
        System.out.println();
        if(task == null || comment == null)
        {
            throw new IllegalArgumentException("Task and comment cannot be null");
        }

        List<Comment> comments = task.getComments();
        if(!comments.isEmpty())
        {
            Comment last_comment = comments.getLast();
            LocalDate last_date = last_comment.getPostedDate();
            LocalDate curr_date = comment.getPostedDate();

            if(curr_date.isBefore(last_date)) // allow curr_date to be equal to or after last_date
            {
                throw new IllegalArgumentException("The new comment's posted date must be after the last comment's posted date");
            }
        }
        task.getComments().add(comment);
        System.out.println("Comment '" + comment.getContent() + "' added successfully");
    }

    @Override
    public void removeCommentFromTask(Task task, Comment comment)
    {
        System.out.println();
        if(task == null || comment == null)
        {
            throw new IllegalArgumentException("Task and comment cannot be null");
        }
        if(!task.getComments().remove(comment))
        {
            throw new IllegalArgumentException("Comment not found in the task");
        }
        System.out.println("Comment '" + comment.getContent() + "' removed successfully");
    }

    @Override
    public void addTeamMemberToTask(Project project, Task task, TeamMember team_member)
    {
        System.out.println();
        if(project == null || task == null || team_member == null)
        {
            throw new IllegalArgumentException("Project, task, and team member cannot be null");
        }

        if(!project.getMembers().contains(team_member)) // team member is part of the project team
        {
            throw new IllegalArgumentException("Team member is not part of the project team");
        }

        for(Task other_task : project.getTasks()) // team member is already assigned to another task in the project
        {
            if(other_task.getAssignedMembers().contains(team_member))
            {
                throw new IllegalArgumentException("Team member is already assigned to another task in the project");
            }
        }
        task.getAssignedMembers().add(team_member);
        System.out.println("Team member " + team_member.getName() + " added to the task successfully");
    }

    @Override
    public void removeTeamMemberFromTask(Project project, Task task, TeamMember team_member)
    {
        System.out.println();
        if(project == null || task == null || team_member == null)
        {
            throw new IllegalArgumentException("Project, task, and team member cannot be null");
        }

        if(!project.getMembers().contains(team_member))
        {
            throw new IllegalArgumentException("Team member is not part of the project team");
        }

        if(!task.getAssignedMembers().contains(team_member))
        {
            throw new IllegalArgumentException("Team member is not assigned to this task");
        }
        task.getAssignedMembers().remove(team_member);
        System.out.println("Team member " + team_member.getName() + " removed from the task successfully");
    }

    @Override
    public void displayUnassignedTeamMembers(Project project)
    {
        System.out.println();
        if(project == null)
        {
            throw new IllegalArgumentException("Project cannot be null");
        }

        Set<TeamMember> unassigned_members = new HashSet<>(project.getMembers());
        for(Task task : project.getTasks())
        {
            unassigned_members.removeAll(task.getAssignedMembers()); // remove from the copy
        }

        if(unassigned_members.isEmpty())
        {
            System.out.println("All team members are assigned to tasks");
        } else
        {
            System.out.println("Unassigned Team Members:");
            for(TeamMember member : unassigned_members)
            {
                System.out.println("\t- " + member.getName());
            }
        }
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
        TeamMember lucia = new TeamMember("Lucia", "lucia.intern@inspire.intern.outlook.com", 7000, LocalDate.of(2024, 6, 11));
        TeamMember anora = new TeamMember("Anora", "anora.intern@inspire.intern.outlook.com", 13000, LocalDate.of(2020, 2, 9));
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
        lucia.addTechKnowledge(TechStack.Python);
        lucia.addRole(Role.MLEngineer);
        anora.addTechKnowledge(TechStack.Cassandra);
        anora.addRole(Role.BackEnd);
        team_builder.addTeamMember(product_deduplication, marius);
        team_builder.addTeamMember(product_deduplication, adrian);
        team_builder.addTeamMember(product_deduplication, lucia);
        team_builder.addTeamMember(product_deduplication, anora);
        team_builder.displayTeamMembers(product_deduplication);

        ManageProjectImpl manage_project = new ManageProjectImpl();
        System.out.println("\n\t---------------Before adding tasks or comments to project:---------------");
        manage_project.displayReport(product_deduplication);
        Task clean_string_data = new Task("Clean string data", "Apply normalization and tokenization to the dataset", LocalTime.of(5, 0, 0));
        Task lemmatization = new Task("Stemming/ lemmatization", "Apply stemming or lemmatization to the dataset", LocalTime.of(2, 10, 0));
        Task filter_data = new Task("Filter data", "Remove exact duplicates and irrelevant data from the dataset", LocalTime.of(1, 30, 0));
        Task train_model = new Task("Train model", "Train the FastText model using the cleaned dataset in order to find semantic similarities for words in the most relevant columns", LocalTime.of(7, 0, 0));
        Task group_data = new Task("Group data", "Group the data based on the semantic similarities found in the previous step", LocalTime.of(3, 0, 0));
        Task consolidate_enriched_entities = new Task("Consolidate enriched entities", "Consolidate the validated duplicates into a single entity", LocalTime.of(4, 0, 0));
        Task validate_enriched_entities = new Task("Validate enriched entities", "Validate the enriched entities using a set of rules", LocalTime.of(2, 0, 0));
        Comment comment1_1 = new Comment( anora, "Shouldn't forget replacing trailing spaces with NaNs", LocalDate.of(2025, 3, 12));
        Comment comment2_1 = new Comment( lucia, "Everyone has to make sure we only keep alphanumeric characters and eliminate the rest", LocalDate.of(2025, 3, 15));
        Comment comment3_1 = new Comment( lucia, "Don't tokenize the dataset anymore, since the model will implement word_tokenize", LocalDate.of(2025, 3, 16));
        Comment comment1_2 = new Comment( marius, "Decided to use cosine similarity on `product_name` and tf-idf similarity on `product_title`", LocalDate.of(2025, 3, 12));
        Comment comment2_2 = new Comment( marius, "Discard idea, stick to training a model for the entire dataset", LocalDate.of(2025, 3, 12));


        manage_project.addTeamMemberToTask(product_deduplication, clean_string_data, anora);
        manage_project.addTeamMemberToTask(product_deduplication, clean_string_data, lucia);
        manage_project.addTeamMemberToTask(product_deduplication, filter_data, marius);
        manage_project.addCommentToTask(clean_string_data, comment1_1);
        manage_project.addCommentToTask(clean_string_data, comment2_1);
        manage_project.addCommentToTask(clean_string_data, comment3_1);
        manage_project.addCommentToTask(filter_data, comment1_2);
        manage_project.addCommentToTask(filter_data, comment2_2);

        manage_project.addTaskToProject(product_deduplication, clean_string_data);
        manage_project.addTaskToProject(product_deduplication, filter_data);

        manage_project.addTaskToProject(product_deduplication, lemmatization);
        manage_project.addTaskToProject(product_deduplication, train_model);
        manage_project.addTaskToProject(product_deduplication, group_data);
        manage_project.addTaskToProject(product_deduplication, consolidate_enriched_entities);
        manage_project.addTaskToProject(product_deduplication, validate_enriched_entities);
        System.out.println("\n\t---------------After adding tasks or comments to project:---------------");
        manage_project.displayReport(product_deduplication);
        manage_project.displayUnassignedTeamMembers(product_deduplication);
        manage_project.addTeamMemberToTask(product_deduplication, lemmatization, lucia);
    }
}
