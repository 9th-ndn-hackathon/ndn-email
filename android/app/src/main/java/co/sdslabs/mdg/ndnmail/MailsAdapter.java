package co.sdslabs.mdg.ndnmail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MailsAdapter extends RecyclerView.Adapter<MailsAdapter.ViewHolder> {

    private ArrayList<Mail> mails;

    MailsAdapter(ArrayList<Mail> mails) {
        this.mails = mails;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mail_list, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        final Mail mail = mails.get(position);

        holder.content.setText(mail.getContent());
        holder.sender.setText(mail.getSender());
        holder.subject.setText(mail.getSubject());
        holder.dateText.setText(mail.getDate()+"");

    }

    @Override
    public int getItemCount() {
        return mails.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView subject;
        TextView dateText;
        TextView content;
        TextView sender;

        ViewHolder(View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.subject);
            dateText = itemView.findViewById(R.id.date);
            sender = itemView.findViewById(R.id.sender);
            content = itemView.findViewById(R.id.content);
        }
    }
}  