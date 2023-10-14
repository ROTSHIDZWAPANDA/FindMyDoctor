package com.example.findmydoctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {


    public interface OnItemClickListener {
        void onItemButtonClick(String userId, String surgeryId,String doctorFirstName, String doctorLastName, Double consultationFees, String telemedicine);
    }
    private List<Surgery> surgeryList;
    private Context context;
    private OnItemClickListener listener;





    public DoctorAdapter(Context context, List<Surgery> surgeryList, OnItemClickListener listener) {
        this.context = context;
        this.surgeryList = surgeryList;
        this.listener = listener;


    }


    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctors, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Surgery surgery = surgeryList.get(position);

        // Bind data to views
        holder.firstNameLastNameTextView.setText("Dr " + surgery.getFirstName() + " " + surgery.getLastName());
        holder.addressTextView.setText(surgery.getAddress());
        holder.specialistTextView.setText(surgery.getSpecialist());
        holder.experienceTextView.setText(surgery.getExperience());
        holder.insuranceAcceptedTextView.setText(surgery.getInsuranceAccepted());
        holder.telemedicineTextView.setText(surgery.getTelemedicine());
        holder.languageTextView.setText(surgery.getLanguage());
        holder.consultationFeesTextView.setText("R" +String.valueOf(surgery.getConsultationFees()));

        // Load profile picture using Picasso (you need to add Picasso library to your project)
        Picasso.get().load(surgery.getProfilePictureUrl()).into(holder.profileImageView);

        // Handle click events or other interactions here
    }

    @Override
    public int getItemCount() {
        return surgeryList.size();
    }
    public void setSurgeryList(List<Surgery> surgeryList) {
        this.surgeryList = surgeryList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }



    public class DoctorViewHolder extends RecyclerView.ViewHolder {
        //MaterialRippleLayout parentLayout;
        CircleImageView profileImageView;
        TextView firstNameLastNameTextView;
        TextView addressTextView;
        TextView specialistTextView;
        TextView experienceTextView;
        TextView insuranceAcceptedTextView;
        TextView telemedicineTextView;
        TextView languageTextView;
        TextView consultationFeesTextView;
        Button BookAppointment;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            //parentLayout = itemView.findViewById(R.id.parentLayout);
            profileImageView = itemView.findViewById(R.id.image);
            firstNameLastNameTextView = itemView.findViewById(R.id.DoctorFirstNameLastName);
            addressTextView = itemView.findViewById(R.id.DoctorAddress);
            specialistTextView = itemView.findViewById(R.id.DoctorSpecialist);
            experienceTextView = itemView.findViewById(R.id.DoctorExperience);
            insuranceAcceptedTextView = itemView.findViewById(R.id.DoctorinsuarenceAccepted);
            telemedicineTextView = itemView.findViewById(R.id.Doctortelemedicine);
            languageTextView = itemView.findViewById(R.id.Doctorlanguage);
            consultationFeesTextView = itemView.findViewById(R.id.DoctorConsultationFees);
            BookAppointment = itemView.findViewById(R.id.ViewMore);

            // Handle click events or other interactions here
            BookAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Surgery surgery = surgeryList.get(position);
                        String userId = surgery.getUserId();
                        String surgeryId = surgery.getSurgeryId();
                        String doctorFirstName = surgery.getFirstName();
                        String doctorLastName = surgery.getLastName();
                        Double consultationFees = surgery.getConsultationFees();
                        String telemedicine = surgery.getTelemedicine();

                        // Invoke the OnItemClickListener with userId and surgeryId
                        listener.onItemButtonClick(userId, surgeryId , doctorFirstName, doctorLastName, consultationFees, telemedicine);
                    }
                }
            });
           /* BookAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Surgery surgery = surgeryList.get(position);
                        bookAppointmentClickListener.onBookAppointmentClick(surgery);
                    }
                }
            });*/
        }
    }
}

