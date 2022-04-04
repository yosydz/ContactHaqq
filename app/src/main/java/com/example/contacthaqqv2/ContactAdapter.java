package com.example.contacthaqqv2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private List<ContactModel> contactList;

    private static ClickListener clickListener;

    public ContactAdapter(Context context, ArrayList<ContactModel> contactList){
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        final ContactModel contact = contactList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvNumber.setText(contact.getNumber());

        holder.tvCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+contact.getNumber()));
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            }
            context.startActivity(intent);
        });

        holder.tvMessage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("sms:"+contact.getNumber()));
            context.startActivity(intent);
        });

        holder.tvWhatsapp.setOnClickListener(v -> {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone="+contact.getNumber()+"&text=";
            sendIntent.setData(Uri.parse(url));
            context.startActivity(sendIntent);
        });

        holder.contactLayout.setOnClickListener(v -> {
            String dataName = holder.tvName.getText().toString();
            String dataNumber = holder.tvNumber.getText().toString();
            String dataInstagram = contact.getInstagram();
            String dataGroup = contact.getGroup();

            Intent intent = new Intent(context, DetailContactActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("cname", dataName);
            bundle.putString("cnumber", dataNumber);
            bundle.putString("cinstagram", dataInstagram);
            bundle.putString("cgroup", dataGroup);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout contactLayout;
        TextView tvName, tvNumber, tvCall, tvMessage, tvWhatsapp, tvDelete;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            contactLayout = itemView.findViewById(R.id.contact_layout);
            tvDelete  = itemView.findViewById(R.id.tv_delete);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvCall = itemView.findViewById(R.id.tv_call);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvWhatsapp  = itemView.findViewById(R.id.tv_whatsapp);

            tvDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), itemView);
        }
    }

    public void setOnItemClickListener(ContactAdapter.ClickListener clickListener) {
        ContactAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
