package com.calculator.android.calculator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArrayMap;

import java.util.ArrayList;

/**
 * Created by Braxton on 4/16/2016.
 */
public class HistoryPreference {

    private ArrayList<String> expressions;
    private ArrayList<String> answers;

    public HistoryPreference() {
        expressions= new ArrayList<>(20);
        answers= new ArrayList<>(20);
    }

    public String[] getExpressions() {
        String[] s = new String[expressions.size()];
        for(int i = 0; i < expressions.size(); i++) {
            s[i] = expressions.get(i);
        }
        return s;
    }

    public String[] getAnswers() {
        String[] s = new String[answers.size()];
        for(int i = 0; i < answers.size(); i++) {
            s[i] = answers.get(i);
        }
        return s;
    }

    void addProblem(String expression, String answer) {
        expressions.add(expression);
        answers.add(answer);
    }

}
