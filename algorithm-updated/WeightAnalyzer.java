package com.finalproject_heathbundy.services;

import com.finalproject_heathbundy.models.WeightEntry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WeightAnalyzer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final int LongTermGoal = 5;

    //sort entries chronologically by calendar date
    public static  List<WeightEntry> sortChronologically(List<WeightEntry> entries) {
        List<WeightEntry> sorted = new ArrayList<>(entries);
        sorted.sort(Comparator.comparing(entry -> LocalDate.parse(entry.getDate(), FORMATTER)));
        return sorted;
    }

    //liner regression calculations for projected date the target weight is met
    public TrendResult analyzeTrend(List<WeightEntry> entries, double targetWeight) {

        //making sure there are a minimum of 3 entries before calculating
        if (entries == null || entries.size() <= 2) {
            return TrendResult.insufficientData();
        }

        List<WeightEntry> sortedEntries = sortChronologically(entries);
        LocalDate firstEntryDate = LocalDate.parse(sortedEntries.get(0).getDate(), FORMATTER);

        int n = sortedEntries.size();
        double sumX =  0;
        double sumY = 0;
        double sumXY = 0;
        double sumXSquared = 0;

        //calculating sums
        for (WeightEntry entry : sortedEntries) {
            LocalDate entryDate = LocalDate.parse(entry.getDate(), FORMATTER);
            double x = ChronoUnit.DAYS.between(firstEntryDate, entryDate);
            double y = entry.getWeight();

            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumXSquared += x * x;
        }

        //check for dividing by zero case
        double denominator = (n * sumXSquared) - (sumX * sumX);
        if (denominator == 0) {
            return TrendResult.noTrend();
        }

        double m = ((n * sumXY) - (sumX * sumY)) / denominator;
        double b = (sumY - m * sumX) / n;

        //check for zero slope case
        if (m == 0) {
            return TrendResult.noTrend();
        }

        //solving for projected finish date for the user
        double daysToGoal = (targetWeight - b) / m;
        LocalDate projectedDate = firstEntryDate.plusDays((long) daysToGoal);

        long yearsToGoal = ChronoUnit.YEARS.between(firstEntryDate, projectedDate);

        if (yearsToGoal >= LongTermGoal) {
            return TrendResult.longTermGoal();
        }

        return TrendResult.success(m, projectedDate.format(FORMATTER));
    }
}