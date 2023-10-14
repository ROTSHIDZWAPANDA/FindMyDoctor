package com.example.findmydoctor;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorAvailability#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorAvailability extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner dayOfWeekSpinner;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private Button addAvailabilityButton;
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference availabilityRef;
    private TextView weekDayTextView,dayTextView,monthTextView;


    public DoctorAvailability() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorAvailability.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorAvailability newInstance(String param1, String param2) {
        DoctorAvailability fragment = new DoctorAvailability();
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
            auth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_availability, container, false);

        dayOfWeekSpinner = view.findViewById(R.id.dayOfWeekSpinner);
        startTimePicker = view.findViewById(R.id.startTimePicker);
        endTimePicker = view.findViewById(R.id.endTimePicker);
        addAvailabilityButton = view.findViewById(R.id.addAvailabilityButton);
        recyclerView = view.findViewById(R.id.availabilityRecyclerView);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        List<Availability> availabilityList = new ArrayList<>();

        ///even this 1

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Months are zero-based
        int currentYear = calendar.get(Calendar.YEAR);

        for (int i = 0; i < 7; i++) { // Assuming you want to display dates for the next 7 days
            // Create a Calendar instance for the next day
            Calendar nextDayCalendar = Calendar.getInstance();
            nextDayCalendar.set(currentYear, currentMonth - 1, currentDay); // Set to the current date
            nextDayCalendar.add(Calendar.DAY_OF_MONTH, i); // Add 'i' days to get the next day

            int nextDayOfWeek = nextDayCalendar.get(Calendar.DAY_OF_WEEK); // 1 for Sunday, 2 for Monday, and so on

            // Calculate the date components (day and month)
            int nextDayOfMonth = nextDayCalendar.get(Calendar.DAY_OF_MONTH);
            int nextMonth = nextDayCalendar.get(Calendar.MONTH) + 1; // Months are zero-based

            // Create a new Availability object and set the calculated date values
            Availability availability = new Availability();
            availability.setCalculatedWeekDay(getWeekDayAbbreviation(nextDayOfWeek));
            availability.setCalculatedDay(String.format("%02d", nextDayOfMonth));
            availability.setCalculatedMonth(getMonthAbbreviation(nextMonth));

            // Add the Availability object to your list
            availabilityList.add(availability);
        }
        //till here





        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

        // Get a reference to the "availability" node in the database with the user's UID as the key
        availabilityRef = database.getReference("availability").child(uid);

        // Set a click listener for the "Add Availability" button
        addAvailabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected day, start time, and end time
                String selectedDay = dayOfWeekSpinner.getSelectedItem().toString();

                int startHour = startTimePicker.getHour();
                int startMinute = startTimePicker.getMinute();
                String selectedStartTime = String.format("%02d:%02d", startHour, startMinute);

                // Get the selected end time from the TimePicker
                int endHour = endTimePicker.getHour();
                int endMinute = endTimePicker.getMinute();
                String selectedEndTime = String.format("%02d:%02d", endHour, endMinute);


                // Create an Availability object (you should have the Availability class defined)
                Availability availability = new Availability(selectedDay, selectedStartTime, selectedEndTime);

                // Push the availability data to the Firebase Realtime Database
                availabilityRef.push().setValue(availability, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            // Data was successfully added
                            // Show a success message to the user
                            Toast.makeText(getContext(), "Added to user", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle the failure here (e.g., show an error message)
                            Toast.makeText(getContext(), "Failed to add availability: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        } else {


        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext()); // Use 'requireContext()' as the context
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(layoutManager);

// Initialize Firebase Firestore and retrieve your availability data
// (You need to implement Firebase Firestore initialization and data retrieval)

         // Populate this list with your Firestore data

        AvailabilityAdapter adapter = new AvailabilityAdapter(requireContext(), availabilityList);

        recyclerView.setAdapter(adapter);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("availability");

        ValueEventListener availabilityListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                availabilityList.clear(); // Clear the list before adding new data

                for (DataSnapshot doctorDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot availabilityDataSnapshot : doctorDataSnapshot.getChildren()) {
                        String dayOfWeek = availabilityDataSnapshot.child("dayOfWeek").getValue(String.class);
                        String startTime = availabilityDataSnapshot.child("startTime").getValue(String.class);
                        String endTime = availabilityDataSnapshot.child("endTime").getValue(String.class);

                        // Create an Availability object and add it to the list
                        Availability availability = new Availability(dayOfWeek, startTime, endTime);
                        availabilityList.add(availability);
                    }
                }

                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
            }
        };

        databaseReference.addValueEventListener(availabilityListener);











        return view;
    }

    ////this 2

    private String getMonthAbbreviation(int month) {
      String[] monthAbbreviations = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        if (month >= 1 && month <= 12) {
            return monthAbbreviations[month - 1]; // Adjust for 0-based array
       }
      return "";
   }
    public static String getWeekDayAbbreviation(int dayOfWeek) {
        String[] weekDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        if (dayOfWeek >= 1 && dayOfWeek <= 7) {
            return weekDays[dayOfWeek - 1];
        }

        return "";
    }

}