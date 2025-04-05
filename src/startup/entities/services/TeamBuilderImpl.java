package startup.entities.services;
import startup.entities.Project;
import startup.entities.TeamManager;
import startup.entities.TeamMember;
import startup.entities.enums.Role;
import startup.entities.enums.TechStack;
import startup.entities.pairs.SchedulePair;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TeamBuilderImpl implements TeamBuilderInterface
{
    @Override
    public void displayTeamMembers(Project project)
    {
        if(project == null)
        {
            throw new IllegalArgumentException("Project cannot be null");
        }
        project.display();
        for(TeamMember member : project.getMembers())
        {
            member.display();
        }
    }

    @Override
    public void addTeamMember(Project project, TeamMember team_member)
    {
        System.out.println();
        if(team_member == null || project == null)
        {
            throw new IllegalArgumentException("Team member and project cannot be null");
        }
        if(project.getMembers().contains(team_member))
        {
            throw new IllegalArgumentException("Team member is already part of the project");
        }
//        for(TechStack curr_tech_stack : teamMember.getTechKnowledge()) // previous logic before deciding I want to display the tech stacks the team member has in common with the project's tech requirements
//        {
//            if(!project.getTechRequirements().containsKey(curr_tech_stack))
//            {
//                throw new IllegalArgumentException("Team member does not meet the tech requirements for the project");
//            }
//        }
        Set<TechStack> intersection_tech_knowledge = new HashSet<>(team_member.getTechKnowledge());
        intersection_tech_knowledge.retainAll(project.getTechRequirements().keySet());
        if(intersection_tech_knowledge.isEmpty())
        {
            throw new IllegalArgumentException("Team member does not meet the tech requirements for the project");
        }

        for(TechStack curr_tech_stack : intersection_tech_knowledge)
        {
            int k_required = project.getTechRequirements().get(curr_tech_stack);
            int k_current = project.countMembersWithTechSkill(curr_tech_stack);
            if(k_current >= k_required)
            {
                intersection_tech_knowledge.remove(curr_tech_stack);
            }
        }
        System.out.println("Team member " + team_member.getName() + " has the required tech knowledge, as needed for the " + project.getTitle() + " project:");
        for(TechStack curr_tech_stack : intersection_tech_knowledge)
        {
            System.out.println("\t" + curr_tech_stack);
        }
        // updating
        System.out.println("Available roles to take (from the team member's earned roles) for each tech stack " + team_member.getName() + " has:");
        boolean eligible = false;
        for(TechStack curr_tech_stack : intersection_tech_knowledge)
        {
            System.out.println("\tTech stack " + curr_tech_stack + ":");
            Set<Role> unique_roles = new HashSet<>(); // roles which have not been assigned to any other team member for the current tech stack
            for(Role curr_role : team_member.getEarnedRoles()) // restrict search area only to the roles the team member possesses
            {
                boolean available = true;
                for(TeamMember member : project.getMembers())
                {
                    if(member.hasTechSkill(curr_tech_stack) && member.checkRole(curr_role))
                    {
                        available = false;
                        break;
                    }
                }
                if(available)
                {
                    unique_roles.add(curr_role);
                    eligible = true;
                    // could add a break here if I don't want to see further details, but that's not my intention
                }
//                if(!available)
//                {
//                    System.out.println("\t\tRole " + curr_role + " is already taken by another team member");
//                } else
//                {
//                    System.out.println("\t\tRole " + curr_role + " is available");
//                    unique_roles.add(curr_role);
//                }
            }
            if(!unique_roles.isEmpty())
            {
                System.out.println("\t\tFor current tech stack, team member " + team_member.getName() + " can take one of the following roles: " + unique_roles);
            } else
            {
                System.out.println("\t\tNo available roles for tech stack " + curr_tech_stack);
            }
        }
        if(eligible)
        {
            System.out.println("Team member " + team_member.getName() + " is eligible for the project " + project.getTitle());
            if(team_member.checkSchedule(project.getDeadline()))
            {
                team_member.addSchedule(project.getDeadline());
                project.addMember(team_member);
                System.out.println("Team member is available for the project time frame");
                System.out.println("Team member " + team_member.getName() + " has been added to the project " + project.getTitle());
            } else
            {
                throw new IllegalArgumentException("Team member is not available for the project time frame");
            }
        } else
        {
            System.out.println("No available roles for team member " + team_member.getName() + " in the project " + project.getTitle());
        }
    }

    @Override
    public void displayVacantPositions(Project project)
    {
        if(project == null)
        {
            throw new IllegalArgumentException("Project cannot be null");
        }
        System.out.println();
        Map<TechStack, Integer> vacant_pos = new HashMap<>(); // keeps track of vacant positions for each TechStack key in the tech_requirements HashMap

        for(Map.Entry<TechStack, Integer> entry : project.getTechRequirements().entrySet())
        {
            TechStack curr_tech_stack = entry.getKey();
            int k_total = entry.getValue();

            int k = project.countMembersWithTechSkill(curr_tech_stack);

            int count = k_total - k;
            if(count > 0)
            {
                vacant_pos.put(curr_tech_stack, count);
            }
        }

        if(!vacant_pos.isEmpty())
        {
            System.out.println("Vacant Positions for Project: " + project.getTitle());
            for(Map.Entry<TechStack, Integer> entry : vacant_pos.entrySet())
            {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " positions available");
            }
        } else
        {
            System.out.println("All tech requirements are fulfilled for Project: " + project.getTitle());
        }
    }

    @Override
    public void updateAssignedRole(Project project, TeamMember team_member, Role prev_role, Role updated_role)
    {
        if(team_member == null || prev_role == null || updated_role == null)
        {
            throw new IllegalArgumentException("Team member and roles cannot be null");
        }
        team_member.removeRole(prev_role);
        team_member.addRole(updated_role);
    }

    @Override
    public void removeTeamMember(Project project, TeamMember team_member)
    {
        if(team_member == null)
        {
            throw new IllegalArgumentException("Team member cannot be null");
        }
        project.removeMember(team_member);
    }

    public static void main(String[] args) // acts as a testing environment for basic procedures
    {
        TeamManager head_manager = new TeamManager("Steve", "steve.professional@inspire.solve.outlook.com", 25000, LocalDate.of(2016, 5, 14));
        head_manager.addRole(Role.DataAnalyst);
        Project product_deduplication = new Project("Product deduplication software", head_manager, new SchedulePair(LocalDate.of(2025, 3, 12), LocalDate.of(2025, 9, 27)));
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
        marius.addRole(Role.MLEngineer);
        adrian.addTechKnowledge(TechStack.Python);
        adrian.addTechKnowledge(TechStack.CSharp); // won't be shown after calling addTeamMember method, even though it's a project tech requirement because it has been fulfilled by the previous candidate
        adrian.addTechKnowledge(TechStack.Java);
        adrian.addTechKnowledge(TechStack.ASPDotNET);
        adrian.addRole(Role.BackEnd);
        adrian.addRole(Role.MLEngineer);

        TeamMember lucia = new TeamMember("Lucia", "lucia.intern@inspire.intern.outlook.com", 7000, LocalDate.of(2024, 6, 11));
        lucia.addTechKnowledge(TechStack.Python);
        lucia.addRole(Role.MLEngineer); // this role is already taken

        System.out.println("\n\t---------------Before adding members formation:---------------");
        team_builder.displayVacantPositions(product_deduplication);

        System.out.println("\n\t---------------Adding members logic:---------------");
        team_builder.addTeamMember(product_deduplication, marius);
        team_builder.addTeamMember(product_deduplication, adrian);
        team_builder.addTeamMember(product_deduplication, lucia);

        System.out.println("\n\t---------------Initial member formation:---------------");
        team_builder.displayVacantPositions(product_deduplication);
        //product_deduplication.display();
        team_builder.displayTeamMembers(product_deduplication);

        System.out.println("\nAfter updating the role, here is the new team formation:");
        team_builder.updateAssignedRole(product_deduplication, marius, Role.MLEngineer, Role.FrontEnd);
        team_builder.updateAssignedRole(product_deduplication, adrian, Role.MLEngineer, Role.FullStack);
        team_builder.addTeamMember(product_deduplication, lucia);

        System.out.println("\n\t---------------New member formation:---------------");
        team_builder.displayVacantPositions(product_deduplication);
        team_builder.displayTeamMembers(product_deduplication);

        TeamMember anora = new TeamMember("Anora", "anora.intern@inspire.intern.outlook.com", 13000, LocalDate.of(2020, 2, 9));
        anora.addTechKnowledge(TechStack.Cassandra);
        anora.addRole(Role.BackEnd);

        System.out.println("\n\t---------------Adding a new member:---------------");
        team_builder.addTeamMember(product_deduplication, anora);
        team_builder.displayVacantPositions(product_deduplication);
        team_builder.displayTeamMembers(product_deduplication);

        System.out.println("\n\t---------------Removing a team member:---------------");
        team_builder.removeTeamMember(product_deduplication, lucia);
        team_builder.displayVacantPositions(product_deduplication);
        team_builder.displayTeamMembers(product_deduplication);
    }
}