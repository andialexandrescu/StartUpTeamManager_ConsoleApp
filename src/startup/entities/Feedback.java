package startup.entities;
import startup.entities.enums.ProjectStage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Feedback
{
    private ProjectStage stage;
    private Map<String, Double> criteria_scores; // criteria and the respective score
    private Boolean approve_stage; // whether the Project at the given stage is progressing as expected (true) or not (false)

    public Feedback(ProjectStage stage)
    {
        this.stage = stage;
        this.criteria_scores = new HashMap<>();
        this.approve_stage = false;
    }
    public Feedback(ProjectStage stage, Map<String, Double> criteria_scores, Boolean approve_stage)
    {
        this.stage = stage;
        this.criteria_scores = new HashMap<>(criteria_scores);
        this.approve_stage = approve_stage;
    }

    public ProjectStage getStage()
    {
        return stage;
    }

    public void setStage(ProjectStage stage)
    {
        this.stage = stage;
    }

    public Map<String, Double> getCriteriaScores()
    {
        return criteria_scores;
    }

    public void setApproveStageChoice(Boolean choice)
    {
        this.approve_stage = choice;
    }

    public void setCriteriaScores(String criteria, double score)
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

    public boolean isPositive()
    {
        if(Boolean.TRUE.equals(approve_stage))
        {
            double averageScore = computeAverageScore(); // average score, check if it meets the threshold
            return averageScore >= 7.0;
        }
        return false; // stage unapproved
    }

    public void display()
    {
        System.out.println("Defined in the management system as a " + this);
        System.out.println("Evaluation Report:");
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
                "(stage=" + stage + ", criteria_scores=" + criteria_scores + ", approve_stage=" + approve_stage + ")";
    }

}