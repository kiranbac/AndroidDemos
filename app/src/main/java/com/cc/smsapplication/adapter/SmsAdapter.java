package com.cc.smsapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.smsapplication.R;
import com.cc.smsapplication.activity.InboxMain;
import com.cc.smsapplication.domain.Sms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nxa15932 on 2/21/2017.
 */

public class SmsAdapter  extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder>{
    private List<Sms> smsList ;
    private Context mContext;
    public SmsAdapter(ArrayList<Sms> smsList,Context context) {
        this.smsList = smsList;
        mContext = context;

    }

    @Override
    public SmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        return new SmsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SmsViewHolder holder, int position) {

        Sms sms = smsList.get(position);
        holder.setItem(sms.get_address()+"\n"+sms.get_msg()+"\n\t"+"Type:"+sms.get_folderName(),sms.get_address());
        //holder.name.setText(sms.get_address()+"\n"+sms.get_msg());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public class SmsViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public TextView name;
        String mAddress;
        public SmsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textout);
            name.setOnClickListener(this);
        }

        public void setItem(String num,String address){
            name.setText(num);
            mAddress = address;
        }

        @Override
        public void onClick(View view) {
            if(view instanceof  TextView){
                //Log.e("Position",number+"**"+getPosition());
                ((InboxMain)mContext).openInboxActivity(mAddress);

            }
        }
    }


}
