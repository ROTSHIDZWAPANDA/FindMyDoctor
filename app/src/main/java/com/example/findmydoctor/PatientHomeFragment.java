package com.example.findmydoctor;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientHomeFragment extends Fragment implements DoctorAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private DoctorAdapter adapter; // Assuming you have a DoctorAdapter class
    private DatabaseReference databaseReference; // Firebase reference for your data


    public PatientHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientHomeFragment newInstance(String param1, String param2) {
        PatientHomeFragment fragment = new PatientHomeFragment();
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
           // databaseReference = FirebaseDatabase.getInstance().getReference("surgeryNodeName");

        }
        databaseReference = FirebaseDatabase.getInstance().getReference("surgeryNodeName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Use LinearLayoutManager or another layout manager
        adapter = new DoctorAdapter(requireContext(), new ArrayList<>(),this); // Initialize with an empty list

        recyclerView.setAdapter(adapter);

        // Fetch surgery data from Firebase and set it in the adapter
        fetchSurgeryData();

        mAuth = FirebaseAuth.getInstance();



        return view;
    }

    private void fetchSurgeryData() {
        // Assuming you have a DoctorAdapter class with a method to set data
        // You should also create a Surgery class to hold your data structure

        // Add a ValueEventListener to fetch data from Firebase
        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Surgery> surgeryList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot surgerySnapshot : userSnapshot.getChildren()) {
                        Surgery surgery = surgerySnapshot.getValue(Surgery.class);
                        if (surgery != null) {
                            surgeryList.add(surgery);
                        }
                    }

                }
                // Set the fetched data in the adapter
                adapter.setSurgeryList(surgeryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors here
            }
        });*/
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Surgery> surgeryList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey(); // Get the user ID
                    Log.d("UserID", "UserID: " + userId);
                    for (DataSnapshot surgerySnapshot : userSnapshot.getChildren()) {
                        String surgeryId = surgerySnapshot.getKey(); // Get the surgery ID
                        Log.d("SurgeryID", "SurgeryID: " + surgeryId);
                        Surgery surgery = surgerySnapshot.getValue(Surgery.class);
                        if (surgery != null) {
                            surgery.setUserId(userId); // Set the user ID for the surgery
                            surgery.setSurgeryId(surgeryId); // Set the surgery ID
                            surgeryList.add(surgery);
                        }
                    }
                }

                // Set the fetched data in the adapter
                adapter.setSurgeryList(surgeryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors here
            }
        });


    }

    @Override
    public void onItemButtonClick(String userId, String surgeryId, String doctorFirstName, String doctorLastName, Double consultationFees, String telemedicine) {
        Log.d("ItemClicked", "UserId: " + userId);
        Log.d("ItemClicked", "SurgeryId: " + surgeryId);

        // Here, you can perform any action you want with the userId and surgeryId.
        // For example, you can start a new activity, show a dialog, or perform other operations.
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        BookAppointmentFragment bookAppointmentFragment = new BookAppointmentFragment();

        // Pass the data to the BookAppointmentFragment
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("surgeryId", surgeryId);
        bundle.putString("doctorFirstName", doctorFirstName);
        bundle.putString("doctorLastName", doctorLastName);
        bundle.putDouble("consultationFees", consultationFees);
        bundle.putString("telemedicine", telemedicine);
        bookAppointmentFragment.setArguments(bundle);

        transaction.replace(R.id.frameLayout, bookAppointmentFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            // Handle the logout action in this fragment
            performLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performLogout() {
        // Handle the logout action specific to this fragment
        // For example, you can show a dialog or perform additional actions
        try {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // User clicked "Yes," log them out
                        mAuth.signOut();
                        startActivity(new Intent(requireContext(), SignIn.class));
                        getActivity().finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // User clicked "No," dismiss the dialog
                        dialog.dismiss();
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}