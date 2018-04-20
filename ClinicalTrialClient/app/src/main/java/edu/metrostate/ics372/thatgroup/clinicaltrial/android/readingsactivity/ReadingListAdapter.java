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
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.presenter.ReadingsPresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

public class ReadingListAdapter extends RecyclerView.Adapter<ReadingListAdapter.ViewHolder> {

    private final ReadingsActivity parentActivity;
    private final List<Reading> readings;
    private final boolean twoPane;
    private final ReadingsPresenter readingsPresenter;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Reading reading = (Reading) view.getTag();
            readingsPresenter.setActiveReading(reading);
            if (twoPane) {
                Bundle arguments = new Bundle();
                arguments.putSerializable(ReadingDetailFragment.READING_TAG, reading);
                ReadingDetailFragment fragment = new ReadingDetailFragment();
                fragment.setArguments(arguments);
                parentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.reading_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ReadingActivity.class);
                intent.putExtra(ReadingDetailFragment.READING_TAG, reading);
                context.startActivity(intent);
            }
        }
    };

    ReadingListAdapter(ReadingsActivity parentActivity,
                                  List<Reading> readings,
                                  boolean twoPane, ReadingsPresenter readingsPresenter) {
        this.readings = readings;
        this.parentActivity = parentActivity;
        this.twoPane = twoPane;
        this.readingsPresenter = readingsPresenter;
    }

    @Override
    public ReadingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reading_list_content, parent, false);
        return new ReadingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReadingListAdapter.ViewHolder holder, int position) {
        holder.mIdView.setText(readingsPresenter.getReadingId(readings.get(position)));
        holder.mContentView.setText(readingsPresenter.getReadingType(readings.get(position)));
        holder.itemView.setTag(readings.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return readings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.id_text);
            mContentView = view.findViewById(R.id.content);
        }
    }
}

