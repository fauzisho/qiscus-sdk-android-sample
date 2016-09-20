package com.qiscus.sdksample.qiscussamplesdk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zetra. on 9/20/16.
 */

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailVH> {

    private Context context;
    private List<String> emails;
    private OnClickListener clickListener;

    public EmailAdapter(Context context, List<String> emails) {
        this.context = context;
        this.emails = emails;
    }

    @Override
    public EmailVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EmailVH(LayoutInflater.from(context).inflate(R.layout.item_email, parent, false), clickListener);
    }

    @Override
    public void onBindViewHolder(EmailVH holder, int position) {
        holder.bind(emails.get(position));
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class EmailVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView emailText;
        private OnClickListener clickListener;

        public EmailVH(View itemView, OnClickListener clickListener) {
            super(itemView);
            emailText = (TextView) itemView.findViewById(R.id.email);
            this.clickListener = clickListener;

            itemView.setOnClickListener(this);
        }

        public void bind(String email) {
            emailText.setText(email);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(getAdapterPosition());
            }
        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}
