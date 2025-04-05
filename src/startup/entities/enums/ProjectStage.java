package startup.entities.enums;

public enum ProjectStage
{
    Initiation,
    Planning,
    Execution,
    Monitoring,
    Closure;

    public static Role getStage(String stage)
    {
        return Role.valueOf(stage);
    }
}
