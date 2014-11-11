package ru.ifmo.md.colloquium2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;


public class MainActivity extends ActionBarActivity {
    private Voting voting;
    public static final String RUNNING_KEY = "running";
    public static final String WAS_RUN_KEY = "was_run";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addButton = (Button) findViewById(R.id.addButton);
        Button startButton = (Button) findViewById(R.id.startVotingButton);
        Button stopButton = (Button) findViewById(R.id.stopVotingButton);
        Button restartButton = (Button) findViewById(R.id.restartButton);
        ListView candidatesList = (ListView) findViewById(R.id.candidatesListView);
        final CandidateAdapter candidateAdapter = new CandidateAdapter(getApplicationContext());
        voting = new Voting(candidateAdapter, getApplicationContext());
        voting.openConnection();
        if (savedInstanceState != null) {
            voting.setRunning(savedInstanceState.getBoolean(RUNNING_KEY, false));
            voting.setWasRun(savedInstanceState.getBoolean(WAS_RUN_KEY, false));
            if (!voting.isRunning() && voting.isWasRun()) {
                voting.stop();
            }
        }
        candidatesList.setAdapter(candidateAdapter);
        final EditText editText = (EditText) findViewById(R.id.candidateNameEditText);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                voting.addCandidate(new Candidate(name));
                editText.setText("");
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voting.start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voting.stop();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voting.restart();
            }
        });

        candidatesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (voting.isRunning()) {
                    Candidate candidate = voting.getCandidate(i);
                    candidate.vote();
                }
            }
        });

        candidatesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final PopupMenu popup = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_main, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if(id == R.id.action_delete) {
                            voting.removeCandidate(voting.getCandidate(i));
                            return true;
                        } else if(id == R.id.action_rename) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            final EditText input = new EditText(MainActivity.this);
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            input.setText(voting.getCandidate(i).getName());
                            alert.setView(input);
                            alert.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String name = input.getText().toString();
                                    Candidate candidate = voting.getCandidate(i);
                                    candidate.setName(name);
                                    voting.updateCandidate(candidate);
                                }
                            });
                            alert.setNegativeButton(R.string.cancel_button, null);
                            alert.show();
                            return true;
                        }
                        return false;
                    }
                });
                popup.show();
                return true;
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RUNNING_KEY, voting.isRunning());
        outState.putBoolean(WAS_RUN_KEY, voting.isWasRun());
    }
}