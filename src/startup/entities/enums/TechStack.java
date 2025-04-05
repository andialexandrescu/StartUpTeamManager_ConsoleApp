package startup.entities.enums;

public enum TechStack
{
    C,
    CPlusPlus,
    Python,
    PLSQL,
    CSharp,
    LINQ,
    HTML,
    CSS,
    Razor,
    R,
    Assembly,
    Java,
    Oracle,
    ASPDotNET,
    Bootstrap,
    Docker,
    GoogleTest,
    Raylib,
    Bash,
    Cassandra;

    public static TechStack getTech(String tech)
    {
        return TechStack.valueOf(tech);
    }
}
