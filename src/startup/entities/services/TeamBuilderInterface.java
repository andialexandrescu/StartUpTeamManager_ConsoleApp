package startup.entities.services;
import startup.entities.Project;
import startup.entities.TeamMember;
import startup.entities.enums.Role;
import startup.entities.enums.TechStack;
import startup.entities.pairs.SchedulePair;

import java.util.Map;

public interface TeamBuilderInterface
{
    void displayTeamMembers(Project project);
    void addTeamMember(Project project, TeamMember team_member);
    void displayVacantPositions(Project project);
    void updateAssignedRole(Project project, TeamMember team_member, Role prev_role, Role updated_role);
    void removeTeamMember(Project project, TeamMember team_member);
}
