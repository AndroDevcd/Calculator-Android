package com.calculator.android.calculator;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Braxton on 4/16/2016.
 */
public class HistoryPageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.history_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        initViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {

        final ListView listview = (ListView) getView().findViewById(R.id.history_list);


        HistoryArrayAdapter adapter = new HistoryArrayAdapter(getContext(), MainActivity.preference.getAnswers(),
                MainActivity.preference.getExpressions());
        listview.setAdapter(adapter);

        if(adapter.isEmpty()) {
            TextView noHistoryTextView = (TextView) getView().findViewById(R.id.no_history);
            noHistoryTextView.setVisibility(View.VISIBLE);
        }

    }

    private class HistoryArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;
        private final String[] expressions;

        public HistoryArrayAdapter(Context context, String[] values, String[] expressions) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
            this.expressions = expressions;
        }

        public boolean isEmpty() {
            return values.length == 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.history_list_item, parent, false);
            TextView answer = (TextView) rowView.findViewById(R.id.answer);
            TextView expression = (TextView) rowView.findViewById(R.id.expression);
            answer.setText(values[position]);
            expression.setText(expressions[position]);

            return rowView;
        }
    }

}
