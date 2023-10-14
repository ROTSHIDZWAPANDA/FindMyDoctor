package com.example.findmydoctor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link doctorAppointments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class doctorAppointments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView appointmentsRecyclerView;
    private AppointmentsAdapter appointmentsAdapter;
    private List<Appointment> doctorAppointments;

    public doctorAppointments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment doctorAppointments.
     */
    // TODO: Rename and change types and number of parameters
    public static doctorAppointments newInstance(String param1, String param2) {
        doctorAppointments fragment = new doctorAppointments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_appointments, container, false);
        appointmentsRecyclerView = view.findViewById(R.id.appointmentsRecyclerView);
        doctorAppointments = new ArrayList<>();
        List<String> appointmentIds = new ArrayList<>();
        appointmentsAdapter = new AppointmentsAdapter(getContext(), doctorAppointments, appointmentIds);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        appointmentsRecyclerView.setLayoutManager(layoutManager);
        appointmentsRecyclerView.setAdapter(appointmentsAdapter);

        // Get the currently logged-in user (doctor)
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String currentDoctorId = currentUser.getUid();
            // Now, `currentDoctorId` contains the ID of the logged-in doctor

        // Retrieve and display appointments for the current doctor
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");
        /*String currentDoctorId = "your_current_doctor_id"; // Replace with the actual ID of the logged-in doctor*/

        Query doctorAppointmentsQuery = appointmentsRef.orderByChild("doctorId").equalTo(currentDoctorId);

        doctorAppointmentsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                doctorAppointments.clear(); // Clear existing appointments
                appointmentIds.clear(); // Clear existing appointmentIds

                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {

                    String appointmentId = appointmentSnapshot.getKey();
                    Log.d("appointmentid","id: "+ appointmentId);
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                    doctorAppointments.add(appointment);
                    appointmentIds.add(appointmentId);
                }

                // Notify the adapter that data has changed
                appointmentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any database query errors here.
            }
        });
        } else {
            // Handle the case where there is no logged-in doctor
        }

        return view;
    }
}