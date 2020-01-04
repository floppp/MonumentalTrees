package es.floppp.monumentaltreesgva.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.floppp.monumentaltreesgva.R;
import es.floppp.monumentaltreesgva.databinding.TreeItemBinding;
import es.floppp.monumentaltreesgva.pojos.Tree;

public class TreesAdapter extends RecyclerView.Adapter<TreesAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Tree> mTrees;

    public TreesAdapter(Context context) {
//        this.binding = TreeItemBinding.inflate(LayoutInflater.from(context));
        this.mInflater = LayoutInflater.from(context);
        this.mTrees = new ArrayList<>();
    }

    public void setTrees(List<Tree> trees){
        this.mTrees = trees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.mInflater.inflate(R.layout.tree_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mTrees != null) {
            Tree current = this.mTrees.get(position);
            holder.speciesTextView.setText(current.species);
        } else {
            holder.speciesTextView.setText("No Word");
        }
    }

    @Override
    public int getItemCount() {
        return this.mTrees.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView speciesTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.speciesTextView = itemView.findViewById(R.id.txv_species);
        }
    }
}
