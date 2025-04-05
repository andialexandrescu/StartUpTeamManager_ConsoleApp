package startup.entities;
import startup.entities.enums.ProjectStage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Feedback
{
    private Project project; // the Project needed to be evaluated
    private ProjectStage stage;
    private Map<String, Double> criteria_scores; // criteria and the respective score
    private Boolean approve_stage; // whether the Project at the given stage is progressing as expected (true) or not (false)

    public Feedback(Project project, ProjectStage stage)
    {
        this.project = project;
        this.stage = stage;
        this.criteria_scores = new HashMap<>();
        this.approve_stage = false;
    }
    public Feedback(Project project, ProjectStage stage, Map<String, Double> criteria_scores, Boolean approve_stage)
    {
        this.project = project;
        this.stage = stage;
        this.criteria_scores = new HashMap<>(criteria_scores);
        this.approve_stage = approve_stage;
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public ProjectStage getStage()
    {
        return stage;
    }

    public void setStage(ProjectStage stage)
    {
        this.stage = stage;
    }

    public void setApproveStageChoice(String choice)
    {
        if(Objects.equals("yes", choice != null ? choice.toLowerCase() : null))
        {
            this.approve_stage = true;
        } else if(Objects.equals("no", choice != null ? choice.toLowerCase() : null))
        {
            this.approve_stage = false;
        } else
        {
            throw new IllegalArgumentException("Choice must be 'yes' or 'no'");
        }
    }

    public void addScore(String criteria, double score)
    {
        if(score < 0 || score > 10)
        {
            throw new IllegalArgumentException("Score must be between 0 and 10");
        }
        criteria_scores.put(criteria, score);
    }

    public double computeAverageScore()
    {
        if(criteria_scores.isEmpty())
        {
            throw new IllegalStateException("No scores are available");
        }
        double total = 0;
        for(double score : criteria_scores.values())
        {
            total += score;
        }
        return total / criteria_scores.size();
    }

    public void display()
    {
        System.out.println("Defined in the management system as a " + this);
        System.out.println("Evaluation Report:");
        System.out.println("Project Title: " + project.getTitle());
        System.out.println("Stage: "+ getStage());
        System.out.println("Criteria Scores:");
        for(Map.Entry<String, Double> entry : criteria_scores.entrySet())
        {
            System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Average Score: " + computeAverageScore());
        System.out.println("Approve Stage: " + (approve_stage ? "Yes" : "No"));
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " object, with the following specifications: \n" +
                "(project=" + project.getTitle() + ", stage=" + stage + ", criteria_scores=" + criteria_scores + ", approve_stage=" + approve_stage + ")";
    }

}