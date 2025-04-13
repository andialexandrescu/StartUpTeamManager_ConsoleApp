# StartUpTeamManager_ConsoleApp
## First Stage GitHub Repository

## Project summary
This project represents a team management system for a tech start-up using Java OOP concepts. It contains two stages, each being represented in a different GitHub repository.
The links for the repositories are:
...[yet to add]...

## First stage requirements:
1. System definition
- [x] Create a list based on the chosen theme of at least **10 actions/queries** that can be done within the system and a list of at least **8 object types**.
2. Implementation
- [x] To implement in the Java language an application based on those defined in the first point.
- [x] The application will contain:
    - [x] simple classes with **private / protected attributes and access methods**
    - [x] at least **2 different collections** capable of managing the previously defined objects (eg: List, Set, Map, etc.) of which at least one must be **sorted**
    - [x] use **inheritance** to create additional classes and use them within collections
    - [x] at least **one service class** to expose system operations
    - [x] a **Main class** from which calls to services are made

### Details for the first stage:
## 1. System definition
My project is a team management system for a tech start-up company. The system allows the management of team members, projects, and tasks, providing functionalities to assign team members to projects, manage their roles, and track their work.

### Actions/Queries:
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
6. It is possible to display vacant positions from the tech stack requirements for the project, meaning the positions haven't been filled by any employees
7. Each project can be represented by a list of tasks, each task having a title, a description, a completion time and an author
 - there are at most 7 tasks per project and each task shouldn't contain team members already associated with other tasks's assigned members
8. Each task can have multiple comments, each comment having a description, an author and a posted date, in order to keep track of progress and issues
 - each comment has to have a posted date after the last comment posted for that task
9. Each task can have multiple assigned team members working on it
 - each team member is added to a task only if he is part of the project team and is not part of any other project task
10. It is possible to display team members who are not working on any task
11. Each time log, supposed to represent the time spent on a task by a certain team member, will illustrate how many hours were spent each day on that task
 - each team member can have multiple time logs for each task, but only one time log per day
 - the time log will be added to the task only if the team member is part of the task
13. Each feedback is connected to a project and project stage, meaning you could keep track of each stage
14. Only after the feedback is positive for each stage of the project, the project will be delivered

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

    - <ins>DeliverProjectInterface</ins> (interface)
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