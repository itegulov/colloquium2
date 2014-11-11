package ru.ifmo.md.colloquium2;

public class Candidate implements Comparable<Candidate> {
    private String name;
    private int votes;
    public static int allVotes = 0;
    private Voting voting;
    private long id;

    public Candidate(String name) {
        this.name = name;
        votes = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPercent() {
        if (allVotes == 0) {
            return 0.0D;
        }
        double result = ((double) votes) / allVotes * 100.0D;
        int resultInt = ((int)(result * 100.0D));
        result = ((double)resultInt) / 100.0D;
        return result;
    }

    public String getName() {
        return name;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getVotes() {
        return votes;
    }

    public void vote() {
        votes++;
        allVotes++;
        voting.updateCandidate(this);
        voting.notifyDataSetChanged();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVoting(Voting voting) {
        this.voting = voting;
    }

    @Override
    public int compareTo(Candidate candidate) {
        return -votes + candidate.votes;
    }
}
