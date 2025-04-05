package startup.entities.enums;

public enum Role
{
    BackEnd,
    FrontEnd,
    FullStack,
    Design,
    MLEngineer,
    DataAnalyst,
    Tester;

    public static Role getRole(String role)
    {
        return Role.valueOf(role);
    }
}
