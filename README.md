# StartUpTeamManager_ConsoleApp
## First Stage GitHub Repository

## Project summary
This project represents a team management system for a tech start-up using Java OOP concepts. It contains two stages, each being represented in a different GitHub repository.
The links for the repositories are:
...

## First stage requirements:
1. System definition
- [x] Create a list based on the chosen theme of at least **10 actions/queries** that can be done within the system and a list of at least **8 object types**.
2. Implementation
- [x] To implement in the Java language an application based on those defined in the first point.
- [ ] The application will contain:
    - [x] simple classes with **private / protected attributes and access methods**
    - [ ] at least **2 different collections** capable of managing the previously defined objects (eg: List, Set, Map, etc.) of which at least one must be **sorted**
    - [x] use **inheritance** to create additional classes and use them within collections
    - [x] at least **one service class** to expose system operations
    - [ ] a **Main class** from which calls to services are made

### Details for the first stage:
## 1. System definition
My project is a team management system for a tech start-up company. The system allows the management of team members, projects, and tasks, providing functionalities to assign team members to projects, manage their roles, and track their work.

### Actions/ Queries:
1. Only one company is meant to be the start-up, developing multiple projects for their portfolio and having employees working on those projects
2. The employees working for that company are either team members or team managers.
    - each team member has multiple roles (developer, tester, etc.) and multiple tech stack knowledge (languages, frameworks, etc.)
    - each team manager is a team member with the additional responsibility of coordinating the project and managing the team members assigned to that project
    - the difference between these entities, besides having different "responsibilities" for my system, is that they have different criteria for computing the salary
3. Each project can have multiple team members assigned to it and one team manager coordinating it
4. Additionally, a project has a start date, an end date, a tech stack along with the required number of people specializing on each mentioned tech stack for completing the project
5. A team member can <b>only</b> be added to work on a project
- if he's eligible, meaning he has at least one tech stack knowledge required for that project and has a remaining slot for working on that specific tech stack
- if he's available, meaning he doesn't have any other project assigned to him in the same time frame
- if there is at least one required tech stack known by the member with no other members assigned on that tech stack having the same roles as that member
    - for example, developer1 might have multiple roles and tech stack knowledge, if there is already a developer representative for each role known by developer1 in that tech stack ( = people working in that tech stack having the same roles as developer1, meaning developer1 can't be part of that tech stack unless he has at least one role not being represented by someone else), that tech stack will not be available
    - the same goes for the rest of tech stack sections needed to be verified for roles with no representatives

6. Each project can be represented by a list of tasks, each task having a description, a deadline, a completion time and an author
7. Each task can have multiple comments, each comment having a description, an author and a posted date, in order to keep track of progress and issues
8. Each comment is connected to a task, to remember the context of that task's steps
9. Each time log, supposed to represent the time spent on a task by a certain team member, will illustrate how many hours were spent each day on that task
10. Each feedback is connected to a project and project stage, meaning you could keep track of each stage
- if the project during that specific stage is approved, it will be sent to the next stage
11. A task, time log and team member are connected to a project, meaning that a task can be assigned to a team member and the time log will be created for that task by that team member
12. Only after the feedback is positive for each stage of the project, the project will be delivered

## 2. Implementation
- Entities:
    - <ins>TeamMember</ins> (extends Employee)
    - <ins>TeamManager</ins> (extends TeamMember)
    - <ins>Company</ins> (simple class, singleton)
    - <ins>Project</ins> (simple class)
    - <ins>Task</ins> (simple class)
    - <ins>TimeLog</ins> (simple class)
    - <ins>Comment</ins> (simple class)
    - <ins>Feedback</ins> (simple class)

- Services:
    - <ins>TeamBuilderInterface</ins> (interface) -> service class?
    - TeamBuilderImpl
        - connection between Project, TeamMember
        - form teams for given project, CRUD operations

    - <ins>ManageProjectInterface</ins> (interface)
    - ManageProjectImpl
        - connection between Project, Task, Comment

    - <ins>DeliverProjectInterface</ins> (interface) - inherits ManageProjectInterface
    - DeliverProjectImpl
        - connection between Project, TimeLog, Feedback

- Abstract classes:
    - Employee

- Enums:
    - Role
    - TechStack
    - ProjectStage

- Other
    - SchedulePair

## Implementation Overview

I structured the project based on some possible queries for this scenario, implementing the logic behind it the following way:

1. **TeamBuilderInterface**: an interface implementing CRUD operations for managing team members working on certain projects
   Details:
- because of polymorphism (methods in an interface that have no body), the implementation of each method can be found in the TeamBuilderImpl class by overriding the method (same name and parameters)

- **Add** team member (create, based on member availability within that project time frame, reserved_time_slot list from TeamMember)
- **Display** team members (read - display all members by using display() method from TeamMember)
- **Assign** member to project
    - by adding member (as function parameter) to `members` variable (which is Set of TeamMember objects) in that current project
        - based on `tech_knowledge` variable (Set of TechStack enum instances) in TeamMember class to have available slots in `tech_requirements` variable in Project (which is a Map of TechStack and Integer)
        - this previous requirement will <b>only</b> be fulfilled (meaning the member will be assigned to that project) if there's are there are no more tech stack representative developers in that project with the same role
          (`earned_roles` variable in TeamMember class which is a Set of Role enum instances)
- **Update/ edit** assigned role in `earned_roles` for a given TeamMember as parameter
    - by removing the old role and adding the new one in the `earned_roles` variable (which is a Set of Role enum instances) in TeamMember class
    - by checking if the new role is already assigned to 3 members in that project (members variable in Project class)
- **Remove** team member (delete)
- **Check** if a member is available (check if the member is already assigned to another project in the same time frame)
    - by checking the `reserved_time_slot` variable (which is a List of SchedulePair objects) in TeamMember class
        - if possible, after adding the member to the project, the `reserved_time_slot` variable will be updated with the new time slot (start and end date) of that project

[//]: # (## Second stage requirements:)

[//]: # ()
[//]: # (1. Extend the Project from the first stage by Implementing Persistence Using a Relational Database and JDBC)

[//]: # (- [ ] Extend the initial project by implementing persistence using a relational database and JDBC.)

[//]: # (- [ ] Develop services that expose **create, read, update, and delete &#40;CRUD&#41;** operations for at least **4 of the defined classes**.)

[//]: # (- [ ] Implement **generic singleton services** for writing to and reading from the database.)

[//]: # ()
[//]: # (2. Implement an Audit Service)

[//]: # (- [ ] Create a service that logs each executed action &#40;as described in Stage I&#41; into a **CSV file**.)

[//]: # (- [ ] The file structure should include:)

[//]: # (  - **action_name**)

[//]: # (  - **timestamp**)