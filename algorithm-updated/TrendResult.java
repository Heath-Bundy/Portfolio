package com.finalproject_heathbundy.services;

public class TrendResult {

    private final boolean hasDate;
    private final String message;
    private final String projectedDate;

    private TrendResult(boolean hasDate, String message, String projectedDate) {
        this.hasDate = hasDate;
        this.message = message;
        this.projectedDate = projectedDate;
    }

    public static TrendResult insufficientData() {
        return new TrendResult(false, "Need more weight entries to calculate a Projected Completion Date",null);
    }

    public static TrendResult noTrend() {
        return new TrendResult(false, "Inconsistent Trend Data with the current entries", null);
    }

    public static TrendResult longTermGoal(){
        return new TrendResult(false, "Your projected date to reach your goal is 5 years away. Keep logging entries and working towards your final goal!", null);
    }

    public static TrendResult success(double dailyRate, String projectedDate){
        return new TrendResult(true, null, projectedDate);
    }

    public boolean hasDate() {
    return hasDate;
    }

    public String getMessage() {
        return message;
    }

    public String getProjectedDate() {
        return projectedDate;
    }
}
