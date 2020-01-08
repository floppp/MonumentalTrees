package es.floppp.monumentaltreesgva.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.floppp.monumentaltreesgva.R;
import es.floppp.monumentaltreesgva.extras.ItemClickListener;
import es.floppp.monumentaltreesgva.pojos.Tree;

public class TreesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Tree> mTrees;
    private ItemClickListener<Tree> mListener;

    TreesAdapter(ItemClickListener<Tree> listener) {
        this.mTrees = new ArrayList<>();
        this.mListener = listener;
    }

    void setTrees(List<Tree> trees) {
        this.mTrees = trees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(this.mTrees.get(position), this.mListener);
    }

    @Override
    public int getItemCount() {
        return this.mTrees.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView speciesTextView;
    private final TextView townTextView;
    private final TextView ageTextView;
    private final TextView heightTextView;
    private final TextView diameterTextView;

    private ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.speciesTextView = itemView.findViewById(R.id.txv_species);
        this.townTextView = itemView.findViewById(R.id.txv_town);
        this.ageTextView = itemView.findViewById(R.id.txv_age);
        this.heightTextView = itemView.findViewById(R.id.txv_height);
        this.diameterTextView = itemView.findViewById(R.id.txv_diameter);
    }

    void bind(Tree tree, ItemClickListener<Tree> listener) {
        this.speciesTextView.setText(tree.species);
        this.townTextView.setText(tree.town);
        this.ageTextView.setText(tree.age > 0 ? tree.age + " años" : "edad desconocida");
        this.heightTextView.setText("altura: " + tree.height + " m");
        this.diameterTextView.setText("diámetro: " + tree.diameter + " m");

        this.itemView.setOnClickListener(view -> listener.onItemClick(tree));
    }

    static ViewHolder from(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tree_item, parent, false);

        return new ViewHolder(view);
    }
}

