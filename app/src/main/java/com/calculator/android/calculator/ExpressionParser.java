package com.calculator.android.calculator;

/**
 * Created by Braxton on 4/15/2016.
 */
public class ExpressionParser {
    public static final String ERROR_INVALID = "invalid operation";

    private String expression = "";
    private String value;

    boolean hasLeft = false;

    private double left;
    private double right;
    private char operation;

    private int position;

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getValue() {
        return value;
    }

    public void parse() {
        value = "";
        position = 0;
        hasLeft = false;

        if(expression == null || !hasOperator()
                || hasAlphas()) {
            return;
        }

        if(endsWithOperationOrDot()) {
            value = ERROR_INVALID;
            return;
        }

        for(;;) {
            if(!hasLeft) {
                hasLeft = true;
                left = getLeft();
            }
            operation = getOperation();
            right = getRight();

            if(operation == ' ') {
                value = left + "";
                break;
            }
            else if(operation == '+') {
                left = left + right;
            }
            else if(operation == '-') {
                left = left - right;
            }
            else if(operation == 'x') {
                left = left * right;
            }
            else if(operation == '/') {
                left = left / right;
            }
        }
    }

    private boolean hasAlphas() {
        for(int i = 0; i < expression.length(); i++) {
            if(Character.isLetter(expression.charAt(i)) && expression.charAt(i) != 'x') {
                return true;
            }
        }
        return false;
    }

    private boolean endsWithOperationOrDot() {
        char last = expression.charAt(expression.length() - 1);

        return ( last == '.' || isoperator(last));
    }

    private double getRight() {
        String number = "";
        int start = position;
        for(int i = position; i < expression.length(); i++) {
            if(isoperator(expression.charAt(i)) && i != start) {
                break;
            }

            number += expression.charAt(i) + "";
            position++;
        }

        return Double.parseDouble(number.equals("") ? "0" : number);
    }

    private char getOperation() {
        return !(position < expression.length()) ? ' ' : expression.charAt(position++);
    }

    private boolean hasOperator() {
        for(int i = 0; i < expression.length(); i++) {
            if(isoperator(expression.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    public  boolean isoperator(char c) {
        return (c == '+' || c == '-' ||
                c == 'x' || c == '/');
    }

    public double getLeft() {
        String number = "";
        for(int i = 0; i < expression.length(); i++) {
            if(isoperator(expression.charAt(i)) && i != 0) {
                break;
            }

            number += expression.charAt(i) + "";
            position++;
        }
        return Double.parseDouble(number);
    }
}
