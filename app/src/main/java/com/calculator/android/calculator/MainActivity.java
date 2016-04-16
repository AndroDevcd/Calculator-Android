package com.calculator.android.calculator;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mExpressionView;
    private TextView mAnswerView;

    private TextView mHistoryView;
    private ImageButton mBackspace;

    private TextView mDotView;
    private TextView mAddView;
    private TextView mSubtractView;
    private TextView mDivideView;
    private TextView mMultiplyView;

    private TextView mZero;
    private TextView mOne;
    private TextView mTwo;
    private TextView mThree;
    private TextView mFour;
    private TextView mFive;
    private TextView mSix;
    private TextView mSeven;
    private TextView mEight;
    private TextView mNine;

    private TextView mEquals;
    private static FrameLayout historyLayout;

    private ExpressionParser parser;

    private final int maxOperations = 6;
    private final int maxNumberChars = 15;

    private String expression = "";

    public static HistoryPreference preference;
    private HistoryPageFragment hist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeCalculator();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backspace) {
            expression = pushback(expression);

            mExpressionView.setText(expression);
            parser.setExpression(expression);
        }
        else if(v.getId() == R.id.dot) {
            char operation = ((TextView)v).getText().toString().charAt(0);
            appendCharToExpression(operation);
            parser.parse();

            String answer = parser.getValue();
            updateAnswer(answer, false);
        }
        else if(v.getId() == R.id.plus || v.getId() == R.id.minus
                || v.getId() == R.id.multiply  || v.getId() == R.id.divide) {

            char operation = ((TextView)v).getText().toString().charAt(0);
            appendCharToExpression(operation);
            parser.parse();

            String answer = parser.getValue();
            updateAnswer(answer, false);
        }
        else if(v.getId() == R.id.history) {
            setHistoryLayoutVisibility(View.VISIBLE);
            hist = new HistoryPageFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.history_layout, hist);
            transaction.commit();

        }
        else if(v.getId() == R.id.equals) {
            if(!mAnswerView.getText().toString().equals("")) {
                String answer = mAnswerView.getText().toString();
                preference.addProblem(expression, answer);

                mExpressionView.setText(answer);
                mAnswerView.setText("");

                expression = answer;
                parser.setExpression(answer);
            }
        }
        else {
            char number = ((TextView)v).getText().toString().charAt(0);
            appendCharToExpression(number);
            parser.parse();

            String answer = parser.getValue();
            updateAnswer(answer, false);
        }

    }

    public static void setHistoryLayoutVisibility(int visibility) {
        historyLayout.setVisibility(visibility);
    }

    private void updateAnswer(String answer, boolean show) {
        if(answer == ExpressionParser.ERROR_INVALID && show) {
            mAnswerView.setText(ExpressionParser.ERROR_INVALID);
        }
        else {
            mAnswerView.setText(answer);
        }
    }

    private void appendCharToExpression(char value) {
        if(parser.isoperator(value)) {
            if(expression.equals("")) {
                return;
            }

            if(operandSize() >= maxOperations) {
                Toast.makeText(MainActivity.this, "Max operations reached (" + maxOperations + ")", Toast.LENGTH_SHORT).show();
            }
            else {
                final char last = expression.equals("") ? ' ' : expression.charAt(expression.length() - 1);

                if(parser.isoperator(last)) {
                    expression = pushback(expression);
                    expression += value + "";
                }
                else if(last == '.') {
                    expression+= "0" + value;
                }
                else {
                    expression += "" + value;
                }
            }
        }
        else {
            if(value == '.') {
                if(!hasDot()) {
                    expression += "" + value;
                }
            }
            else { // number
                if(numberSize() >= maxNumberChars) {
                    Toast.makeText(MainActivity.this, "Max number size reached (" + maxNumberChars + ")", Toast.LENGTH_SHORT).show();
                }
                else {
                    expression += "" + value;
                }
            }
        }

        mExpressionView.setText(expression);
        parser.setExpression(expression);
    }

    private int operandSize() {
        int size = 0;

        for(int i = expression.length() - 1; i > 0; i--) {
            if(parser.isoperator(expression.charAt(i))) {
                size++;
            }
        }
        return size;
    }

    private int numberSize() {
        int size = 0;

        for(int i = expression.length() - 1; i > 0; i--) {
            if(parser.isoperator(expression.charAt(i))) {
                break;
            }

            if(Character.isDigit(expression.charAt(i))) {
                size++;
            }
        }
        return size;
    }

    private boolean hasDot() {
        if(expression.equals(".")) {
            return true;
        }

        for(int i = expression.length() - 1; i > 0; i--) {
            if(parser.isoperator(expression.charAt(i))) {
                break;
            }

            if(expression.charAt(i) == '.') {
                return true;
            }
        }
        return false;
    }

    private String pushback(String s) {
        String str = "";

        for(int i = 0; i < s.length() - 1; i++) {
            str += s.charAt(i) + "";
        }

        return str;
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            //additional code
            if(hist != null) {
                getFragmentManager().beginTransaction().detach(hist).commit();

                setHistoryLayoutVisibility(View.GONE);
                hist = null;
            }
            else {
                super.onBackPressed();
            }
        } else {
            getFragmentManager().popBackStack();
        }

    }

    private void initializeCalculator() {

        mExpressionView = (TextView) findViewById(R.id.expression_view);
        mAnswerView = (TextView) findViewById(R.id.answer_view);

        mHistoryView = (TextView) findViewById(R.id.history);
        mHistoryView.setOnClickListener(this);
        mBackspace = (ImageButton) findViewById(R.id.backspace);
        mBackspace.setOnClickListener(this);

        mDotView = (TextView) findViewById(R.id.dot);
        mDotView.setOnClickListener(this);
        mAddView = (TextView) findViewById(R.id.plus);
        mAddView.setOnClickListener(this);
        mSubtractView = (TextView) findViewById(R.id.minus);
        mSubtractView.setOnClickListener(this);
        mDivideView = (TextView) findViewById(R.id.divide);
        mDivideView.setOnClickListener(this);
        mMultiplyView = (TextView) findViewById(R.id.multiply);
        mMultiplyView.setOnClickListener(this);

        mZero = (TextView) findViewById(R.id.zero);
        mZero.setOnClickListener(this);
        mOne = (TextView) findViewById(R.id.one);
        mOne.setOnClickListener(this);
        mTwo = (TextView) findViewById(R.id.two);
        mTwo.setOnClickListener(this);
        mThree = (TextView) findViewById(R.id.three);
        mThree.setOnClickListener(this);
        mFour = (TextView) findViewById(R.id.four);
        mFour.setOnClickListener(this);
        mFive = (TextView) findViewById(R.id.five);
        mFive.setOnClickListener(this);
        mSix = (TextView) findViewById(R.id.six);
        mSix.setOnClickListener(this);
        mSeven = (TextView) findViewById(R.id.seven);
        mSeven.setOnClickListener(this);
        mEight = (TextView) findViewById(R.id.eight);
        mEight.setOnClickListener(this);
        mNine = (TextView) findViewById(R.id.nine);
        mNine.setOnClickListener(this);

        mEquals = (TextView) findViewById(R.id.equals);
        mEquals.setOnClickListener(this);
        historyLayout = (FrameLayout) findViewById(R.id.history_layout);

        parser = new ExpressionParser();
        preference = new HistoryPreference();
    }
}
