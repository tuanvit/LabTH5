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

public class SimpleMemberAdapter extends ArrayAdapter<Member> {
    private Context context;
    private List<Member> members;

    public SimpleMemberAdapter(Context context, List<Member> members) {
        super(context, R.layout.list_item_member, members);
        this.context = context;
        this.members = members;
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

        // Style mặc định
        convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        tvMemberName.setTextColor(context.getResources().getColor(R.color.text_primary));
        tvMemberId.setTextColor(context.getResources().getColor(R.color.text_secondary));

        return convertView;
    }
}
