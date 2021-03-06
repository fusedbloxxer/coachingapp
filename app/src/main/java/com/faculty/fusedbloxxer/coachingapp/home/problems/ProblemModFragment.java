package com.faculty.fusedbloxxer.coachingapp.home.problems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faculty.fusedbloxxer.coachingapp.R;
import com.faculty.fusedbloxxer.coachingapp.core.ModFragment;
import com.faculty.fusedbloxxer.coachingapp.databinding.ProblemModLayoutBinding;
import com.faculty.fusedbloxxer.coachingapp.model.db.entities.Problem;
import com.faculty.fusedbloxxer.coachingapp.utilities.Utils;

import java.util.Objects;

public class ProblemModFragment extends ModFragment {
    private TextView mIdTextView, mOriginalCoachTextView, mOriginalClientTextView;
    private EditText mTitleEditText, mDescEditText, mStateEditText;
    private Spinner mCoachSpinner, mClientSpinner;
    private Long mProblemId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ProblemModLayoutBinding problemModLayoutBinding = ProblemModLayoutBinding
                .inflate(inflater, container, false);
        problemModLayoutBinding.setFragment(this);
        return problemModLayoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm.getAllCoachUsernames().observe(this, strings ->
                mCoachSpinner.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                        android.R.layout.simple_spinner_dropdown_item, strings)));

        vm.getAllClientUsernames().observe(this, strings ->
                mClientSpinner.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                        android.R.layout.simple_spinner_dropdown_item, strings)));
    }

    @Override
    public void onAccept() {
        if (Utils.checkEditTexts(mTitleEditText, mDescEditText, mStateEditText)) {
            final Problem problem = new Problem(
                    mStateEditText.getText().toString(),
                    mDescEditText.getText().toString(),
                    mTitleEditText.getText().toString(),
                    mCoachSpinner.getSelectedItem().toString(),
                    mClientSpinner.getSelectedItem().toString()
            );

            if (mProblemId != null) {
                problem.setProblemId(mProblemId);
                vm.updateProblems(problem);
            } else {
                vm.insertProblems(problem);
            }

            onCancel();
        }
    }

    @Override
    protected void setArgumentId() {
        if (getArguments() != null) {
            final String problemId = ProblemModFragmentArgs.fromBundle(getArguments()).getProblemId();
            mProblemId = problemId == null ? null : Long.parseLong(problemId);
        }

        if (mProblemId != null) {
            vm.getProblemById(mProblemId).observe(this, problem -> {
                mOriginalClientTextView.setText(String.format("Clientul curent: %s", problem.getClientId()));
                mOriginalCoachTextView.setText(String.format("Coach-ul curent: %s", problem.getCoachId()));
                mDescEditText.setText(problem.getDescription());
                mTitleEditText.setText(problem.getTitle());
                mStateEditText.setText(problem.getState());
                mIdTextView.setText("ID: " + mProblemId);
                mIdTextView.setVisibility(View.VISIBLE);
            });
        }
    }

    @Override
    protected void initViews(View itemView) {
        mOriginalClientTextView = itemView.findViewById(R.id.problem_original_client_text_view);
        mOriginalCoachTextView = itemView.findViewById(R.id.problem_original_coach_text_view);
        mDescEditText = itemView.findViewById(R.id.problem_description_edit_text);
        mTitleEditText = itemView.findViewById(R.id.problem_title_edit_text);
        mStateEditText = itemView.findViewById(R.id.problem_state_edit_text);
        mClientSpinner = itemView.findViewById(R.id.problem_spinner_client);
        mCoachSpinner = itemView.findViewById(R.id.problem_spinner_coach);
        mIdTextView = itemView.findViewById(R.id.problem_id_text_view);
    }
}
