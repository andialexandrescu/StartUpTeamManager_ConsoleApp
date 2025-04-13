import startup.entities.*;
import startup.entities.enums.*;
import startup.entities.services.*;
import startup.entities.pairs.SchedulePair;
import static startup.entities.enums.Role.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("--------Still in progress, see psvm in each service class for testing examples...\n\n");

        Scanner scanner = new Scanner(System.in);
        TeamBuilderImpl team_builder = new TeamBuilderImpl();
        ManageProjectImpl manage_project = new ManageProjectImpl();
        DeliverProjectImpl deliver_project = new DeliverProjectImpl();

        System.out.print("Enter company name: ");
        String name = scanner.nextLine();
        System.out.print("Enter company address: ");
        String address = scanner.nextLine();
        Company company = Company.getInstance(name, address);
        System.out.println(company);

        List<Project> projects = new ArrayList<>();
        List<TeamMember> members = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Map<TeamMember, TimeLog> timelogs = new HashMap<>();
        List<Feedback> feedback_list = new ArrayList<>();

        while(true)
        {
            System.out.println("\nChoose an action:");
            System.out.println("1. Create a Project and add a Team Manager");
            System.out.println("2. Create a Team Member");
            System.out.println("3. TeamBuilder - Add Team Member to Project");
            System.out.println("4. TeamBuilder - Display Team Members");
            System.out.println("5. TeamBuilder - Display Vacant Positions");
            System.out.println("6. Create a Task");
            System.out.println("7. Create a Comment");
            System.out.println("8. ManageProject - Add Comment to Task");
            System.out.println("9. ManageProject - Add Task to Project");
            System.out.println("10. ManageProject - Add Team Member to Task");
            System.out.println("11. ManageProject - Display Report");
            System.out.println("12. ManageProject - Display Unassigned Team Members");
            System.out.println("13. Create a TimeLog");
            System.out.println("14. Create a Feedback");
            System.out.println("15. DeliverProject - Add TimeLog to Task");
            System.out.println("16. DeliverProject - Add Feedback to Project");
            System.out.println("17. DeliverProject - Display Tasks with Exceeded Completion Time");
            System.out.println("14. DeliverProject - Deliver Project");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice)
            {
                case 1:
                    System.out.print("Enter project title: ");
                    String project_title = scanner.nextLine();
                    System.out.print("Enter head manager name: ");
                    String manager_name = scanner.nextLine();
                    System.out.print("Enter head manager email: ");
                    String manager_email = scanner.nextLine();
                    System.out.print("Enter head manager salary: ");
                    double manager_salary = scanner.nextDouble();
                    System.out.print("Enter head manager start date (YYYY-MM-DD): ");
                    String string_date = scanner.next();
                    LocalDate date;
                    try
                    {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
                        date = LocalDate.parse(string_date, formatter);
                    } catch (DateTimeParseException e)
                    {
                        System.out.println("Invalid date format, use YYYY-MM-DD (example 2025-05-12)");
                        break;
                    }
                    scanner.nextLine();

                    TeamManager manager = new TeamManager(manager_name, manager_email, manager_salary, date);
                    Project project = new Project(project_title, manager, new SchedulePair(LocalDate.now(), LocalDate.now().plusMonths(6)));
                    projects.add(project);
                    System.out.println(project);
                    break;

                case 2:
                    System.out.print("Enter team member name: ");
                    String member_name = scanner.nextLine();
                    System.out.print("Enter team member email: ");
                    String member_email = scanner.nextLine();
                    System.out.print("Enter team member salary: ");
                    double member_salary = scanner.nextDouble();
                    System.out.print("Enter team member start date (YYYY-MM-DD): ");
                    String string = scanner.next();
                    LocalDate date1;
                    try
                    {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
                        date1 = LocalDate.parse(string, formatter);
                    } catch (DateTimeParseException e)
                    {
                        System.out.println("Invalid date format, use YYYY-MM-DD (example 2025-05-12)");
                        break;
                    }

                    TeamMember member = new TeamMember(member_name, member_email, member_salary, date1);
                    members.add(member);
                    System.out.println(member);
                    break;

                case 3:
                    if(!projects.isEmpty() && !members.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to add the team member to: ");
                        int project_index = scanner.nextInt()-1; // 0-based index
                        scanner.nextLine();

                        System.out.println("Available Team Members:");
                        for(int i = 0; i < members.size(); i++)
                        {
                            System.out.println((i+1) + ". " + members.get(i).getName());
                        }
                        System.out.print("Enter the team member number to add to the project: ");
                        int member_index = scanner.nextInt()-1;
                        scanner.nextLine();

                        if(project_index >= 0 && project_index < projects.size() && member_index >= 0 && member_index < members.size())
                        {
                            Project selected_project = projects.get(project_index);
                            TeamMember selected_member = members.get(member_index);

                            try
                            {
                                team_builder.addTeamMember(selected_project, selected_member);
                            } catch (IllegalArgumentException e)
                            {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else
                        {
                            System.out.println("Invalid project or team member number");
                        }
                    } else
                    {
                        System.out.println("No projects or team members created yet");
                    }
                    break;

                case 4:
                    if(!projects.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to display details about: ");
                        int project_index = scanner.nextInt()-1; // 0-based index
                        scanner.nextLine();

                        if(project_index >= 0 && project_index < projects.size())
                        {
                            team_builder.displayTeamMembers(projects.get(project_index));
                        } else
                        {
                            System.out.println("Invalid project number");
                        }
                    } else
                    {
                        System.out.println("No projects created yet");
                    }
                    break;
                case 5:
                    if(!projects.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to display details about vacant tech stack positions: ");
                        int project_index = scanner.nextInt()-1; // 0-based index
                        scanner.nextLine();

                        if(project_index >= 0 && project_index < projects.size())
                        {
                            team_builder.displayVacantPositions(projects.get(project_index));
                        } else
                        {
                            System.out.println("Invalid project number");
                        }
                    } else
                    {
                        System.out.println("No projects created yet");
                    }
                    break;
                case 6:
                    System.out.print("Enter task title: ");
                    String task_title = scanner.nextLine();
                    System.out.print("Enter task description: ");
                    String task_description = scanner.nextLine();
                    System.out.print("Enter task completion time (in hours): ");
                    double task_completion_time = 0;
                    try
                    {
                        task_completion_time = scanner.nextDouble();
                        if(task_completion_time <= 0)
                        {
                            System.out.println("Completion time must be a positive number");
                            break;
                        }
                    } catch (InputMismatchException e)
                    {
                        System.out.println("Invalid input, eenter a valid number for completion time");
                        scanner.nextLine(); // clear invalid input
                        break;
                    }
                    int hours = (int) task_completion_time;
                    int minutes = (int) Math.round((task_completion_time - hours) * 60);
                    LocalTime task_completion_time_local = LocalTime.of(hours, minutes);

                    Task task = new Task(task_title, task_description, task_completion_time_local);
                    tasks.add(task);
                    System.out.println(task);
                    break;
                case 7:
                    if(members.isEmpty())
                    {
                        System.out.println("No team members created yet, create a team member first");
                        break;
                    }
                    System.out.println("Available Team Members:");
                    for(int i = 0; i < members.size(); i++)
                    {
                        System.out.println((i+1) + ". " + members.get(i).getName());
                    }
                    System.out.print("Enter the team member number for the comment's author: ");
                    int author_index = scanner.nextInt()-1;
                    scanner.nextLine();

                    if(author_index >= 0 && author_index < members.size())
                    {
                        TeamMember author = members.get(author_index);
                        System.out.print("Enter comment content: ");
                        String comment_content = scanner.nextLine();
                        Comment comment = new Comment(author, comment_content, LocalDate.now());
                        comments.add(comment);
                        System.out.println(comment);
                    } else
                    {
                        System.out.println("Invalid team member number");
                    }
                    break;
                case 8:
                    if(!projects.isEmpty() && !tasks.isEmpty() && !comments.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to select a project: ");
                        int project_index = scanner.nextInt()-1;
                        scanner.nextLine();

                        if(project_index >= 0 && project_index < projects.size())
                        {
                            Project selected_project = projects.get(project_index);

                            System.out.println("Available Tasks:");
                            for(int i = 0; i < tasks.size(); i++)
                            {
                                System.out.println((i+1) + ". " + tasks.get(i).getTitle());
                            }
                            System.out.print("Enter the task number to add a comment to: ");
                            int task_index = scanner.nextInt()-1;
                            scanner.nextLine();

                            if(task_index >= 0 && task_index < tasks.size())
                            {
                                Task selected_task = tasks.get(task_index);

                                System.out.println("Available Comments:");
                                for(int i = 0; i < comments.size(); i++)
                                {
                                    System.out.println((i+1) + ". " + comments.get(i).getContent());
                                }
                                System.out.print("Enter the comment number to add to the task: ");
                                int comment_index = scanner.nextInt()-1;
                                scanner.nextLine();

                                if(comment_index >= 0 && comment_index < comments.size())
                                {
                                    Comment selected_comment = comments.get(comment_index);

                                    try
                                    {
                                        manage_project.addCommentToTask(selected_task, selected_comment);
                                    } catch (IllegalArgumentException e)
                                    {
                                        System.out.println("Error: " + e.getMessage());
                                    }
                                } else
                                {
                                    System.out.println("Invalid comment number");
                                }
                            } else
                            {
                                System.out.println("Invalid task number");
                            }
                        } else
                        {
                            System.out.println("Invalid project number");
                        }
                    } else
                    {
                        System.out.println("No projects, tasks, or comments created yet");
                    }
                    break;

                case 9:
                    if(!projects.isEmpty() && !tasks.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to add a task to: ");
                        int project_index = scanner.nextInt()-1;
                        scanner.nextLine();

                        if(project_index >= 0 && project_index < projects.size())
                        {
                            Project selected_project = projects.get(project_index);

                            System.out.println("Available Tasks:");
                            for(int i = 0; i < tasks.size(); i++)
                            {
                                System.out.println((i+1) + ". " + tasks.get(i).getTitle());
                            }
                            System.out.print("Enter the task number to add to the project: ");
                            int task_index = scanner.nextInt()-1;
                            scanner.nextLine();

                            if(task_index >= 0 && task_index < tasks.size())
                            {
                                Task selected_task = tasks.get(task_index);
                                try
                                {
                                    manage_project.addTaskToProject(selected_project, selected_task);
                                } catch (IllegalArgumentException e)
                                {
                                    System.out.println("Error: " + e.getMessage());
                                }
                            } else
                            {
                                System.out.println("Invalid task number");
                            }
                        } else
                        {
                            System.out.println("nvalid project number");
                        }
                    } else {
                        System.out.println("No projects or tasks created yet");
                    }
                    break;
                case 10:
                    if(!projects.isEmpty() && !tasks.isEmpty() && !members.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to select a project: ");
                        int projectIndex = scanner.nextInt()-1;
                        scanner.nextLine();

                        if(projectIndex >= 0 && projectIndex < projects.size())
                        {
                            Project selected_project = projects.get(projectIndex);

                            System.out.println("Available Tasks:");
                            for(int i = 0; i < tasks.size(); i++)
                            {
                                System.out.println((i+1) + ". " + tasks.get(i).getTitle());
                            }
                            System.out.print("Enter the task number to add a team member to: ");
                            int task_index = scanner.nextInt()-1;
                            scanner.nextLine();

                            if(task_index >= 0 && task_index < tasks.size())
                            {
                                Task selected_task = tasks.get(task_index);

                                System.out.println("Available Team Members:");
                                for(int i = 0; i < members.size(); i++)
                                {
                                    System.out.println((i+1) + ". " + members.get(i).getName());
                                }
                                System.out.print("Enter the team member number to add to the task: ");
                                int member_index = scanner.nextInt()-1;
                                scanner.nextLine();

                                if(member_index >= 0 && member_index < members.size())
                                {
                                    TeamMember selected_member = members.get(member_index);

                                    try
                                    {
                                        manage_project.addTeamMemberToTask(selected_project, selected_task, selected_member);
                                    } catch (IllegalArgumentException e)
                                    {
                                        System.out.println("Error: " + e.getMessage());
                                    }
                                } else
                                {
                                    System.out.println("Invalid team member number");
                                }
                            } else
                            {
                                System.out.println("Invalid task number");
                            }
                        } else
                        {
                            System.out.println("Invalid project number");
                        }
                    } else
                    {
                        System.out.println("No projects, tasks, or team members created yet");
                    }
                    break;

                case 11:
                    if(!projects.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to display the report: ");
                        int project_index = scanner.nextInt()-1;
                        scanner.nextLine();

                        if(project_index >= 0 && project_index < projects.size())
                        {
                            manage_project.displayReport(projects.get(project_index));
                        } else
                        {
                            System.out.println("Invalid project number");
                        }
                    } else {
                        System.out.println("No projects created yet");
                    }
                    break;
                case 12:
                    if(!projects.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to display the report: ");
                        int project_index = scanner.nextInt()-1;
                        scanner.nextLine();

                        if(project_index >= 0 && project_index < projects.size())
                        {
                            manage_project.displayUnassignedTeamMembers(projects.get(project_index));
                        } else
                        {
                            System.out.println("Invalid project number");
                        }
                    } else {
                        System.out.println("No projects created yet");
                    }
                    break;
                case 13:
                    if(!members.isEmpty())
                    {
                        System.out.println("Available Team Members:");
                        for(int i = 0; i < members.size(); i++)
                        {
                            System.out.println((i+1) + ". " + members.get(i).getName());
                        }
                        System.out.print("Enter the team member number to create a TimeLog for: ");
                        int timelog_member_index = scanner.nextInt()-1;
                        scanner.nextLine();

                        if(timelog_member_index >= 0 && timelog_member_index < members.size())
                        {
                            TeamMember selected_member = members.get(timelog_member_index);

                            if(!timelogs.containsKey(selected_member))
                            {
                                TimeLog timelog = new TimeLog(selected_member);
                                timelogs.put(selected_member, timelog);
                                System.out.println(timelog);
                            } else
                            {
                                System.out.println("TimeLog already exists for this member.");
                            }
                        } else
                        {
                            System.out.println("Invalid team member number");
                        }
                    } else
                    {
                        System.out.println("No team members created yet, create a team member first");
                    }
                    break;
                case 14:
                    System.out.println("What stage of project are you in?");
                    System.out.println("1. Initiation");
                    System.out.println("2. Planning");
                    System.out.println("3. Execution");
                    System.out.println("4. Monitoring");
                    System.out.println("Closure (any other key)");
                    int stage_choice = scanner.nextInt();
                    ProjectStage stage;

                    switch (stage_choice)
                    {
                        case 1:
                            stage = ProjectStage.Initiation;
                            break;
                        case 2:
                            stage = ProjectStage.Planning;
                            break;
                        case 3:
                            stage = ProjectStage.Execution;
                            break;
                        case 4:
                            stage = ProjectStage.Monitoring;
                            break;
                        default:
                            stage = ProjectStage.Closure;
                            break;
                    }
                    Feedback feedback = new Feedback(stage);
                    feedback.setApproveStageChoice(Boolean.TRUE);
                    System.out.print("Enter the number of criteria: ");
                    int n = scanner.nextInt();
                    scanner.nextLine();
                    for(int i = 0; i < n; i++)
                    {
                        System.out.print("Enter criteria name: ");
                        String criteria_name = scanner.nextLine();
                        System.out.print("Enter criteria score (0-10): ");
                        double criteria_score = scanner.nextDouble();
                        scanner.nextLine();
                        feedback.setCriteriaScores(criteria_name, criteria_score);
                    }

                    feedback_list.add(feedback);
                    System.out.println(feedback);
                    break;
                case 15:
                    if(!projects.isEmpty() && !tasks.isEmpty() && !timelogs.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to select a project: ");
                        int projectIndex = scanner.nextInt()-1;
                        scanner.nextLine();

                        if(projectIndex >= 0 && projectIndex < projects.size())
                        {
                            Project selected_project = projects.get(projectIndex);

                            System.out.println("Available Tasks:");
                            for(int i = 0; i < tasks.size(); i++)
                            {
                                System.out.println((i+1) + ". " + tasks.get(i).getTitle());
                            }
                            System.out.print("Enter the task number to add a TimeLog to: ");
                            int task_index = scanner.nextInt()-1;
                            scanner.nextLine();

                            if(task_index >= 0 && task_index < tasks.size())
                            {
                                Task selected_task = tasks.get(task_index);

                                System.out.println("Available Team Members:");
                                for(int i = 0; i < selected_task.getAssignedMembers().size(); i++)
                                {
                                    System.out.println((i+1) + ". " + members.get(i).getName());
                                }
                                System.out.print("Enter the team member number to add a TimeLog to: ");
                                int timelog_member_index = scanner.nextInt() - 1;
                                scanner.nextLine();

                                if(timelog_member_index >= 0 && timelog_member_index < members.size())
                                {
                                    List<TeamMember> assigned_members_list = new ArrayList<>(selected_task.getAssignedMembers());
                                    TeamMember selected_member = assigned_members_list.get(timelog_member_index);

                                    TimeLog selected_timelog = timelogs.get(selected_member);
                                    try
                                    {
                                        deliver_project.addTimeLogToTask(selected_project, selected_task, selected_timelog, LocalDate.now());
                                    } catch (IllegalArgumentException e)
                                    {
                                        System.out.println("Error: " + e.getMessage());
                                    }
                                }
                                else
                                {
                                    System.out.println("Invalid member number");
                                }

                            } else
                            {
                                System.out.println("Invalid task number");
                            }
                        } else {
                            System.out.println("Invalid project number");
                        }
                    } else {
                        System.out.println("No projects, tasks, or timeLogs created yet");
                    }
                    break;
                case 16:
                    if(!projects.isEmpty() && !feedback_list.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to select a project: ");
                        int projectIndex = scanner.nextInt()-1;
                        scanner.nextLine();

                        if(projectIndex >= 0 && projectIndex < projects.size())
                        {
                            Project selected_project = projects.get(projectIndex);

                            System.out.println("Available Feedbacks:");
                            for(int i = 0; i < feedback_list.size(); i++)
                            {
                                System.out.println((i+1) + ". " + feedback_list.get(i));
                            }
                            System.out.print("Enter the feedback number to add to the project: ");
                            int feedback_index = scanner.nextInt()-1;
                            scanner.nextLine();

                            if(feedback_index >= 0 && feedback_index < feedback_list.size())
                            {
                                Feedback selected_feedback = feedback_list.get(feedback_index);

                                try
                                {
                                    deliver_project.addFeedbackToProject(selected_project, selected_feedback);
                                } catch (IllegalArgumentException e)
                                {
                                    System.out.println("Error: " + e.getMessage());
                                }
                            } else
                            {
                                System.out.println("Invalid feedback number");
                            }
                        } else
                        {
                            System.out.println("Invalid project number");
                        }
                    } else
                    {
                        System.out.println("No projects, tasks, or timeLogs created yet");
                    }
                    break;
                case 17:
                    if(!projects.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to display tasks with exceeded completion time: ");
                        int project_index = scanner.nextInt() - 1;
                        scanner.nextLine();

                        if(project_index >= 0 && project_index < projects.size())
                        {
                            Project selected_project = projects.get(project_index);
                            deliver_project.displayTasksWithExceededCompletionTime(selected_project);
                        } else
                        {
                            System.out.println("Invalid project number");
                        }
                    } else
                    {
                        System.out.println("No projects created yet");
                    }
                    break;
                case 18:
                    if(!projects.isEmpty())
                    {
                        System.out.println("Available Projects:");
                        for(int i = 0; i < projects.size(); i++)
                        {
                            System.out.println((i+1) + ". " + projects.get(i).getTitle());
                        }
                        System.out.print("Enter the project number to display tasks with exceeded completion time: ");
                        int project_index = scanner.nextInt() - 1;
                        scanner.nextLine();

                        if(project_index >= 0 && project_index < projects.size())
                        {
                            Project selected_project = projects.get(project_index);
                            deliver_project.deliverProject(selected_project);
                        } else
                        {
                            System.out.println("Invalid project number");
                        }
                    } else
                    {
                        System.out.println("No projects created yet");
                    }
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}