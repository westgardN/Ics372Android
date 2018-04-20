package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

public class ReadingListAdapter extends RecyclerView.Adapter<ReadingListAdapter.ViewHolder> {

    private final ReadingsActivity mParentActivity;
    private final List<Reading> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Reading reading = (Reading) view.getTag();

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                //arguments.putString(ReadingDetailFragment.ARG_ITEM_ID, reading.getId());
                arguments.putSerializable(ReadingDetailFragment.ARG_ITEM_ID, reading);
                ReadingDetailFragment fragment = new ReadingDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.reading_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ReadingActivity.class);
                intent.putExtra(ReadingDetailFragment.ARG_ITEM_ID, reading);
                context.startActivity(intent);
            }
        }
    };

    ReadingListAdapter(ReadingsActivity parent,
                                  List<Reading> readings,
                                  boolean twoPane) {
        mValues = readings;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ReadingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reading_list_content, parent, false);
        return new ReadingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReadingListAdapter.ViewHolder holder, int position) {
        holder.mIdView.setText(mValues.get(position).getId());
        // holder.mContentView.setText(mValues.get(position));

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}

