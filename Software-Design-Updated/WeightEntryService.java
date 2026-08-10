package com.finalproject_heathbundy.services;
import com.finalproject_heathbundy.repositories.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class WeightEntryService {

    public String dateValidation (String dateText) {
        if (dateText.isEmpty()){
            return "Date field is empty, please enter a valid date (MM/DD/YYYY)";
        }

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("MM/dd/yyyy")
                .withResolverStyle(ResolverStyle.STRICT);

        LocalDate entryDate;
        try {
            entryDate = LocalDate.parse(dateText, formatter);
        } catch (DateTimeParseException e) {
            return "Invalid Date Format, please enter date in MM/DD/YYYY format";
        }

        if (entryDate.isAfter(LocalDate.now())){
            return "The Date cannot be in the future, double check your entry date";
        }
        return null;
    }

    public String weightValidation (String weightText) {
        if(weightText.isEmpty()){
            return "Entry Weight field is empty, please enter a valid weight";
        }

        double weightEntry = 0;
        try {
            weightEntry = Double.parseDouble(weightText);
        } catch (NumberFormatException e) {
            return "Weight Entry must be a number, please try again";
        }

        if(weightEntry < 50 || weightEntry > 1500){
            return "Entry weight must be between 50 and 1,500";
        }
        return null;
    }
}
