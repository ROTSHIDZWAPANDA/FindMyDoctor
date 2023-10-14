package com.example.findmydoctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientNotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView notificationRecyclerView;

    public PatientNotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientNotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientNotificationFragment newInstance(String param1, String param2) {
        PatientNotificationFragment fragment = new PatientNotificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_patient_notification, container, false);

        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);

        // Set the layout manager (in this case, a vertical LinearLayoutManager)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        notificationRecyclerView.setLayoutManager(layoutManager);

        // Create a list of NotificationData objects
        List<NotificationData> notificationList = new ArrayList<>();
        notificationList.add(new NotificationData("Notification 1", "This is the first notification."));
        notificationList.add(new NotificationData("Notification 2", "This is the second notification."));
// Add more notifications as needed

// Create an instance of the NotificationAdapter and pass the notificationList to it
        NotificationAdapter adapter = new NotificationAdapter(notificationList);

// Set the adapter to your RecyclerView
        notificationRecyclerView.setAdapter(adapter);


        return view;
    }
}