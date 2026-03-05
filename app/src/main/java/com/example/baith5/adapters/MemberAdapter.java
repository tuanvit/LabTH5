package com.example.baith5.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.baith5.R;
import com.example.baith5.models.Member;

import java.util.List;
import java.util.Set;

public class MemberAdapter extends ArrayAdapter<Member> {
    private Context context;
    private List<Member> members;
    private Set<String> assignedMemberIds;

    public MemberAdapter(Context context, List<Member> members, Set<String> assignedMemberIds) {
        super(context, R.layout.list_item_member, members);
        this.context = context;
        this.members = members;
        this.assignedMemberIds = assignedMemberIds;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_member, parent, false);
        }

        Member member = members.get(position);
        TextView tvMemberName = convertView.findViewById(R.id.tvMemberName);
        TextView tvMemberId = convertView.findViewById(R.id.tvMemberId);

        tvMemberName.setText(member.getMemName());
        tvMemberId.setText("MSSV: " + member.getMemId());

        // Check if member is already assigned to any group
        boolean isAssigned = assignedMemberIds.contains(member.getMemId());
        
        if (isAssigned) {
            // Disable and change color for assigned members
            convertView.setAlpha(0.5f);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.grey_medium));
            tvMemberName.setTextColor(context.getResources().getColor(R.color.text_secondary));
            tvMemberId.setTextColor(context.getResources().getColor(R.color.text_secondary));
        } else {
            // Enable and normal color for available members
            convertView.setAlpha(1.0f);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
            tvMemberName.setTextColor(context.getResources().getColor(R.color.text_primary));
            tvMemberId.setTextColor(context.getResources().getColor(R.color.text_secondary));
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        Member member = members.get(position);
        return !assignedMemberIds.contains(member.getMemId());
    }
}
