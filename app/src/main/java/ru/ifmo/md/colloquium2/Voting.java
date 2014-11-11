package ru.ifmo.md.colloquium2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Voting {
    private boolean running = false;
    private CandidateAdapter candidateAdapter;
    private boolean wasRun = false;
    private DatabaseVotingHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    public Voting(CandidateAdapter candidateAdapter, Context context) {
        this.candidateAdapter = candidateAdapter;
        this.context = context;
    }

    public void openConnection() {
        dbHelper = new DatabaseVotingHelper(context);
        database = dbHelper.getWritableDatabase();
        Cursor c = database.query(DatabaseVotingHelper.CANDIDATES_TABLE_NAME, new String[]{DatabaseVotingHelper.CANDIDATES_ID,
            DatabaseVotingHelper.CANDIDATES_NAME, DatabaseVotingHelper.CANDIDATES_VOTES}, "", null, null, null, null);
        Candidate.allVotes = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Candidate candidate = new Candidate(c.getString(c.getColumnIndex(DatabaseVotingHelper.CANDIDATES_NAME)));
            candidate.setId(c.getLong(c.getColumnIndex(DatabaseVotingHelper.CANDIDATES_ID)));
            candidate.setVotes(c.getInt(c.getColumnIndex(DatabaseVotingHelper.CANDIDATES_VOTES)));
            candidate.setVoting(this);
            Candidate.allVotes += candidate.getVotes();
            candidateAdapter.addCandidate(candidate);
        }
    }

    public void closeConnection() {
        database.close();
    }

    public void start() {
        if (!wasRun) {
            running = true;
            wasRun = true;
        }
    }

    public void stop() {
        running = false;
        candidateAdapter.sort();
    }

    public void setWasRun(boolean wasRun) {
        this.wasRun = wasRun;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void restart() {
        database.delete(DatabaseVotingHelper.CANDIDATES_TABLE_NAME, "", null);
        candidateAdapter.clear();
        running = false;
        wasRun = false;
        Candidate.allVotes = 0;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isWasRun() {
        return wasRun;
    }

    public void addCandidate(Candidate candidate) {
        if (!running && !wasRun) {
            candidateAdapter.addCandidate(candidate);
            candidate.setVoting(this);
            ContentValues values = new ContentValues();
            values.put(DatabaseVotingHelper.CANDIDATES_NAME, candidate.getName());
            values.put(DatabaseVotingHelper.CANDIDATES_VOTES, candidate.getVotes());
            long id = database.insert(DatabaseVotingHelper.CANDIDATES_TABLE_NAME, null, values);
            candidate.setId(id);
        }
    }

    public void removeCandidate(Candidate candidate) {
        if (!running && !wasRun) {
            candidateAdapter.removeCandidate(candidate);
            database.delete(DatabaseVotingHelper.CANDIDATES_TABLE_NAME, DatabaseVotingHelper.CANDIDATES_ID + "=" + candidate.getId(), null);
            candidateAdapter.notifyDataSetChanged();
        }
    }

    public void updateCandidate(Candidate candidate) {
        ContentValues values = new ContentValues();
        values.put(DatabaseVotingHelper.CANDIDATES_NAME, candidate.getName());
        values.put(DatabaseVotingHelper.CANDIDATES_VOTES, candidate.getVotes());
        database.update(DatabaseVotingHelper.CANDIDATES_TABLE_NAME, values, DatabaseVotingHelper.CANDIDATES_ID + "=" + candidate.getId(), null);
        candidateAdapter.notifyDataSetChanged();
    }

    public Candidate getCandidate(int i) {
        return candidateAdapter.getItem(i);
    }

    public void notifyDataSetChanged() {
        candidateAdapter.notifyDataSetChanged();
    }
}
