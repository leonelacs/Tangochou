package com.leonelacs.tangochou;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TangoAdapter extends RecyclerView.Adapter<TangoAdapter.ViewHolder> {

    private List<TangoItem> tangos;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View searchView;
        TextView tangoWord, tangoDefi;
        public ViewHolder(View view) {
            super(view);
            searchView = view;
            tangoWord = (TextView) view.findViewById(R.id.TWord);
            tangoDefi = (TextView) view.findViewById(R.id.TDefinition);
            //tangoPron = (TextView) view.findViewById(R.id.TPronunciation);
        }
    }

    public TangoAdapter(List<TangoItem> tangoList) {
        tangos = tangoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tango_vector, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                TangoItem tangoBeClicked = tangos.get(position);
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("word", tangoBeClicked.getWord());
                intent.putExtra("definition", tangoBeClicked.getDefinition());
                view.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TangoItem tangoItem = tangos.get(position);
        holder.tangoWord.setText(tangoItem.word);
        holder.tangoDefi.setText(tangoItem.definition);
    }

    @Override
    public int getItemCount() {
        return tangos.size();
    }



}
