package ru.ifmo.md.colloquium2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CandidateAdapter extends BaseAdapter {
    private List<Candidate> candidateList = new ArrayList<Candidate>();
    private LayoutInflater inflater;
    private boolean finished = false;

    public CandidateAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void addCandidate(Candidate candidate) {
        candidateList.add(candidate);
        notifyDataSetChanged();
    }

    public void removeCandidate(Candidate candidate) {
        candidateList.remove(candidate);
    }

    public void sort() {
        finished = true;
        Collections.sort(candidateList);
        notifyDataSetChanged();
    }

    public void clear() {
        finished = false;
        candidateList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return candidateList.size();
    }

    @Override
    public Candidate getItem(int i) {
        return candidateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView;
        if (view != null) {
            newView = view;
        } else {
            newView = inflater.inflate(R.layout.candidate_adapter, viewGroup, false);
        }
        Candidate candidate = candidateList.get(i);
        TextView nameTextView = (TextView) newView.findViewById(R.id.candidateNameTextView);
        TextView voteTextView = (TextView) newView.findViewById(R.id.candidateVotesTextView);
        TextView percentTextView = (TextView) newView.findViewById(R.id.candidatePercentTextView);
        nameTextView.setText(candidate.getName());
        if (finished) {
            voteTextView.setText(" " + Integer.toString(candidate.getVotes()));
            percentTextView.setText(" " + Double.toString(candidate.getPercent()) + "%");
            if (i == 0) {
                newView.setBackgroundColor(Color.RED);
            }
        } else {
            voteTextView.setText("");
            percentTextView.setText("");
            newView.setBackgroundColor(Color.TRANSPARENT);
        }
        return newView;
    }
}
