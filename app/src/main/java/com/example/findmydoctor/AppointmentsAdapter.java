package com.example.findmydoctor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private List<Appointment> appointmentsList;
    private Context context;
    private List<String> appointmentIds;

    public AppointmentsAdapter(Context context, List<Appointment> appointmentsList, List<String> appointmentIds) {
        this.appointmentsList = appointmentsList;
        this.context = context;
        this.appointmentIds = appointmentIds;
    }
    public String getAppointmentId(int position) {
        return appointmentIds.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointmentsList.get(position);

        holder.patientName.setText(appointment.getUserName());
        holder.appointmentDate.setText("Date: " + appointment.getDate());
        holder.appointmentTime.setText("Time: " + appointment.getTime());
        holder.consultationtype.setText(appointment.getConsultationType());
        if (appointment.isPaidStatus()) {
            holder.paymentstatus.setText("Payment status: Paid");
        } else {
            holder.paymentstatus.setText("Payment status:Not Paid");
        }
        holder.status.setText("Status: " + appointment.getStatus());
        holder.Problem.setText("Problem: " + appointment.getProblemDescription());

        // Set click listeners for accept, reschedule, and reject buttons
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle accept action
                appointment.setDoctorApproval(true); // Set doctorApproval to true
                appointment.setStatus("approved"); // Set status to approved

                String appointmentId = getAppointmentId(holder.getAdapterPosition());
                Log.d("appointmentid","id: "+ appointmentId);

                // Update the appointment data in the database
                DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments")
                        .child(appointmentId); // Replace "getAppointmentId" with the actual method to get the appointment ID

                // Create a map to update multiple fields
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("doctorApproval", true);
                updateData.put("status", "approved");

                appointmentRef.updateChildren(updateData, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            // Update successful
                            saveChatData(appointmentId, appointment);
                            Log.d("db","successful");
                        } else {
                            // Handle the error
                            Log.d("db","failed");
                        }
                    }
                });

              /*  appointmentRef.child("doctorApproval").setValue(true);
                appointmentRef.child("status").setValue("approved");*/
            }
        });


        holder.rescheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reschedule action for this appointment
            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reject action for this appointment
            }
        });
    }

    private void saveChatData(String appointmentId, Appointment appointment) {
        // Extract necessary data from the appointment object
        String patientUserId = appointment.getUserId();
        String doctorUserId = appointment.getDoctorId();
        String appointmentDate = appointment.getDate();
        String appointmentTime = appointment.getTime();

        // Create a new chat object or data structure based on your database structure
        Chat chat = new Chat();
        chat.setParticipant1(patientUserId);
        chat.setParticipant2(doctorUserId);
        chat.setAppointmentId(appointmentId);


        // Save the chat data to the chats table in the database with a generated chatId
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        DatabaseReference chatRef = chatsRef.push(); // Generates a unique chat ID
        chatRef.setValue(chat);

        // Retrieve the generated chatId if needed
        String generatedChatId = chatRef.getKey();

        // You can also proceed with initializing the chat conversation with a welcome message or other data.
        // Initialize the chat messages structure and other chat-related data as needed.
        // Initialize the chat with a welcome message
        DatabaseReference messagesRef = chatRef.child("messages");
        ChatMessage welcomeMessage = new ChatMessage();
        welcomeMessage.setSender("system"); // You can use a "system" or "admin" user ID for the welcome message
        welcomeMessage.setText("Welcome to the chat! How can we assist you?");
        welcomeMessage.setTimestamp(ServerValue.TIMESTAMP); // Use a server-generaated timestamp

        // Set the welcome message in the chat
        DatabaseReference welcomeMessageRef = messagesRef.push(); // Generates a unique message ID
        welcomeMessageRef.setValue(welcomeMessage);
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView patientName;
        public TextView appointmentDate;
        public TextView appointmentTime;
        public TextView consultationtype;
        public TextView Problem;
        public TextView status;
        public TextView paymentstatus;
        public Button acceptButton;
        public Button rescheduleButton;
        public Button rejectButton;

        public ViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patientName);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            consultationtype = itemView.findViewById(R.id.consultationtype);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rescheduleButton = itemView.findViewById(R.id.rescheduleButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            Problem = itemView.findViewById(R.id.problemDesc);
            status = itemView.findViewById(R.id.appointmentStatus);
            paymentstatus = itemView.findViewById(R.id.paymentstatus);
        }
    }
}
