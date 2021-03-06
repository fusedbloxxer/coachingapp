package com.faculty.fusedbloxxer.coachingapp.home.feedbacks;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.faculty.fusedbloxxer.coachingapp.R;
import com.faculty.fusedbloxxer.coachingapp.model.PersonalDevelopmentViewModel;
import com.faculty.fusedbloxxer.coachingapp.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedbackViewHolder extends RecyclerView.ViewHolder {
    private TextView mSessionTextView, mTitleTextView, mUserTextView, mContentTextView, mDateTextView;
    private PersonalDevelopmentViewModel mPersonalDevelopmentViewModel;
    private TextView mFeedbackIdTextView;
    private final Fragment mFragment;
    private RatingBar mRatingbar;
    private CardView mCardView;

    public FeedbackViewHolder(@NonNull View itemView, FeedbackFragment fragment) {
        super(itemView);
        initViews(itemView);
        this.mFragment = fragment;
        mCardView.setOnClickListener(v ->
                Utils.createItemOptions(itemView.getContext(), fragment, (Long) mFeedbackIdTextView.getTag()));
    }

    private void initViews(View itemView) {
        mFeedbackIdTextView = itemView.findViewById(R.id.feedback_verified_text_view);
        mUserTextView = itemView.findViewById(R.id.feedback_client_username_text_view);
        mDateTextView = itemView.findViewById(R.id.feedback_emission_date_feedback);
        mSessionTextView = itemView.findViewById(R.id.feedback_session_text_view);
        mContentTextView = itemView.findViewById(R.id.feedback_content_text_view);
        mTitleTextView = itemView.findViewById(R.id.feedback_title_text_view);
        mRatingbar = itemView.findViewById(R.id.feedback_rating_bar);
        mCardView = itemView.findViewById(R.id.feedback_card_view);
    }

    public void setFeedbackId(Long feedbackId) {
        if (feedbackId != null) {
            mFeedbackIdTextView.setTag(feedbackId);
            mFeedbackIdTextView.setText(String.format("Feedback %d", feedbackId));

            if (mPersonalDevelopmentViewModel == null) {
                mPersonalDevelopmentViewModel = ViewModelProviders.of(mFragment)
                        .get(PersonalDevelopmentViewModel.class);
            }

            setFeedbackObservers(feedbackId);
        }
    }

    private void setFeedbackObservers(Long feedbackId) {
        mPersonalDevelopmentViewModel.getUserWithFeedbackId(feedbackId).observe(mFragment, user -> {
            if (user != null) {
                mUserTextView.setVisibility(View.VISIBLE);
                mUserTextView.setText(String.format("Publicat de catre %s", user.getUsername()));
            } else {
                mUserTextView.setVisibility(View.GONE);
            }
        });
    }

    public void setSessionId(Long sessionId) {
        if (sessionId != null) {
            mSessionTextView.setText(String.format("Id sedinta: %d", sessionId));
        }
    }

    public void setTitle(String title) {
        if (title != null) {
            mTitleTextView.setText(title);
        }
    }

    public void setRating(Float rating) {
        if (rating != null) {
            mRatingbar.setRating(rating);
        }
    }

    public void setContent(String content) {
        if (content != null) {
            mContentTextView.setText(content);
        }
    }

    public void setEmissionDate(Date emissionDate) {
        if (emissionDate != null) {
            mDateTextView.setText(String.format("A fost emis la data %s",
                    new SimpleDateFormat(Utils.DATE_FORMAT, Locale.ENGLISH).format(emissionDate)));
        }
    }
}
