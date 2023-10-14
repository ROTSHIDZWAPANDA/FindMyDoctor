package com.example.findmydoctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AvailabilityAdapter extends RecyclerView.Adapter<AvailabilityAdapter.AvailabilityViewHolder> {
    private final Context context;
    private final List<Availability> availabilityList;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();


    public AvailabilityAdapter(Context context, List<Availability> availabilityList) {
        this.context = context;
        this.availabilityList = availabilityList;
    }

    @NonNull
    @Override
    public AvailabilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_availability, parent, false);
        return new AvailabilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailabilityViewHolder holder, int position) {
        Availability availability = availabilityList.get(position);

        // Bind data to your layout views
        holder.dayOfWeekTextView.setText(availability.getDayOfWeek());


        // Set the text for the Slots TextView with the startTime and endTime
        String timeRange = availability.getStartTime() + " to " + availability.getEndTime();
        holder.slotsTextView.setText(timeRange);




        // Add more bindings as needed

        holder.deleteSlotImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    String currentDoctorId = currentUser.getUid();

                    // Iterate through slots under the doctor's UID
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                            .child("availability")
                            .child(currentDoctorId);

                    databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot slotSnapshot : dataSnapshot.getChildren()) {
                                // Match slots based on some criteria (e.g., day and time)
                                Availability slot = slotSnapshot.getValue(Availability.class);
                                if (slot != null && slot.getDayOfWeek().equals(availability.getDayOfWeek())
                                        && slot.getStartTime().equals(availability.getStartTime())
                                        && slot.getEndTime().equals(availability.getEndTime())) {
                                    // Delete the matching slot
                                    slotSnapshot.getRef().removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // Slot deleted successfully
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle the error
                                                }
                                            });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle cancellation
                        }
                    });
                } else {
                    // Handle the case where there is no authenticated user (doctor).
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return availabilityList.size();
    }

    public class AvailabilityViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfWeekTextView;
        TextView slotsTextView; // Add this line
        ImageView deleteSlotImageView; // Add this line



        public AvailabilityViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeek);
            slotsTextView = itemView.findViewById(R.id.Slots);
            deleteSlotImageView = itemView.findViewById(R.id.deleteSlot);

           
        }
    }
}
