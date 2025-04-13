package startup.entities.services;
import startup.entities.Comment;
import startup.entities.Project;
import startup.entities.Task;
import startup.entities.TeamMember;

public interface ManageProjectInterface
{
    void displayReport(Project project);
    void addTaskToProject(Project project, Task task);
    void removeTaskFromProject(Project project, Task task);
    void addCommentToTask(Task task, Comment comment);
    void removeCommentFromTask(Task task, Comment comment);
    void addTeamMemberToTask(Project project, Task task, TeamMember team_member);
    void removeTeamMemberFromTask(Project project, Task task, TeamMember team_member);
    void displayUnassignedTeamMembers(Project project);
}
