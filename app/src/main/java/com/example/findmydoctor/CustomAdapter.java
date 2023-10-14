package com.example.findmydoctor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.balysv.materialripple.MaterialRippleLayout;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final List<PendingSurgeryItem> itemList;

    public CustomAdapter(List<PendingSurgeryItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_surgery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PendingSurgeryItem item = itemList.get(position);
        holder.surgeryNameTextView.setText(item.getSurgeryName());
        holder.doctorNameTextView.setText(item.getDoctorName());
        holder.statusTextView.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView surgeryNameTextView;
        TextView doctorNameTextView;
        TextView statusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            surgeryNameTextView = itemView.findViewById(R.id.surgeryName);
            doctorNameTextView = itemView.findViewById(R.id.doctorName);
            statusTextView = itemView.findViewById(R.id.Status);
        }
    }
}
