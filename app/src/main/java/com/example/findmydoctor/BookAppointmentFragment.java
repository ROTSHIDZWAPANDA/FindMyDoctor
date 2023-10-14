package com.example.findmydoctor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookAppointmentFragment extends Fragment {


    private EditText dateEditText,problemDescription;
    private DatePicker datePicker;
    private Spinner timeSlotSpinner;
    private DatabaseReference doctorAvailabilityRef;
    private DatabaseReference doctorNodeRef;
    private String currentuserId;
    private String doctorId;
    private String doctorname;
    private String patientName;
    private Double consultationFees;
    private String surgeryId;
    private RadioGroup consultationTypeRadioGroup;
    private RadioButton virtualConsultationRadioButton;
    private RadioButton physicalConsultationRadioButton;
    private String selectedConsultationType;


    public BookAppointmentFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BookAppointmentFragment newInstance(String userId, String surgeryId) {
        BookAppointmentFragment fragment = new BookAppointmentFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("surgeryId", surgeryId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          /*  mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_appointment, container, false);
        Bundle bundle = getArguments();
        String userId = null;
        if (bundle != null) {
            userId = bundle.getString("userId");
            surgeryId = bundle.getString("surgeryId");
            String doctorFirstName = bundle.getString("doctorFirstName");
            String doctorLastName = bundle.getString("doctorLastName");
            consultationFees = bundle.getDouble("consultationFees");
            String telemedicine = bundle.getString("telemedicine");

            // Now you have userId and surgeryId, and you can use them as needed.
            // For example, you can display them in your BookAppointmentFragment's UI.

            TextView doctorNameTextView = view.findViewById(R.id.doctorNameTextView);

            /*TextView userIdTextView = view.findViewById(R.id.userId);
            TextView surgeryIdTextView = view.findViewById(R.id.surgeryId);

            // Set the text for the TextViews
            userIdTextView.setText("User ID: " + userId);
            surgeryIdTextView.setText("Surgery ID: " + surgeryId);*/
            doctorNameTextView.setText("DR " + doctorFirstName + " " + doctorLastName);
            doctorname = "DR " + doctorFirstName + " " + doctorLastName;

        }
        dateEditText = view.findViewById(R.id.dateEditText);
        datePicker = view.findViewById(R.id.datePicker);
        timeSlotSpinner = view.findViewById(R.id.timeSlotSpinner);
        problemDescription = view.findViewById(R.id.problemDescriptionEditText);

        // Set a click listener on the date EditText
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the DatePicker when the EditText is clicked
                showDatePicker();
            }
        });
        consultationTypeRadioGroup = view.findViewById(R.id.consultationTypeRadioGroup);
        virtualConsultationRadioButton = view.findViewById(R.id.virtualConsultationRadioButton);
        physicalConsultationRadioButton = view.findViewById(R.id.physicalConsultationRadioButton);

        consultationTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);

                if (selectedRadioButton == virtualConsultationRadioButton) {
                    // Save "Virtual Consultation" to your database
                    selectedConsultationType = "virtual consultation";
                } else if (selectedRadioButton == physicalConsultationRadioButton) {
                    // Save "Physical Consultation" to your database
                    selectedConsultationType = "physical consultation";
                }
            }
        });
        // Replace "doctorId" with the actual doctor's ID (which is the passed userId)
        /*String userId = getArguments().getString("userId");*/
        doctorId = userId;

        // Build the Firebase database reference for the doctor's availability
        /*doctorAvailabilityRef = FirebaseDatabase.getInstance().getReference()
                .child("availability")
                .child("userId");*/
        DatabaseReference availabilityRef = FirebaseDatabase.getInstance().getReference().child("availability");

// Specify the doctor's unique ID
        /*String doctorId = "d8lNojpbFwaIXK1JLA7Bac1rmuL2";*/


// Get a reference to the doctor's node
        doctorNodeRef = availabilityRef.child(doctorId);
        Log.d("DoctorNodeRef", "Doctor Node Reference: " + doctorNodeRef.toString());

        // Set a listener to handle date selection
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Determine the selected day of the week (0 = Sunday, 1 = Monday, etc.)
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                // Map the day of the week to a string (e.g., "SUNDAY", "MONDAY", etc.)
                String dayOfWeekString = getDayOfWeekString(dayOfWeek);

                Log.d("DateSelected", "Selected Date: " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth); // Log this line
                Log.d("DateSelected", "Day of Week: " + dayOfWeekString);

                // Retrieve and populate the Spinner with time slots for the selected day
                retrieveTimeSlotsFromFirebase(dayOfWeekString);
            }
        });

        Button cancelAppointmentButton = view.findViewById(R.id.CancelAppointmentButton);

        cancelAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a confirmation dialog
                showCancelConfirmationDialog();
            }
        });
        Button bookAppointmentButton = view.findViewById(R.id.bookAppointmentButton);

        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a confirmation dialog
                showBookingConfirmationDialog();
            }
        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        currentuserId = null;
        if (currentUser != null) {
            currentuserId = currentUser.getUid();

            // Now you have the user's ID (userId), proceed to fetch their first name and last name.
        } else {
            // Handle the case where there is no authenticated user
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(currentuserId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists, fetch first name and last name
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);

                    // Now you have the user's first name and last name, update the TextView
                    TextView userNameTextView = view.findViewById(R.id.userNameTextView);
                    userNameTextView.setText("Patient: " + firstName + " " + lastName);
                    patientName = "Patient: " + firstName + " " + lastName;
                } else {
                    // User data does not exist, handle this case
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors here
            }
        });
        setupTimeSlotSpinner();






        return view;
    }

    private void setupTimeSlotSpinner() {
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Select Time");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(adapter);
    }

    private void showBookingConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Book Appointment")
                .setMessage("Are you sure you want to book this appointment?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Yes," perform the booking action here
                        // You can add your booking logic or navigation logic here
                        // For example, if you want to navigate to another fragment, do it here.

                        // If you want to save the appointment data, do it here.
                        saveAppointmentToDatabase();

                        Toast.makeText(getContext(), "Appointment Booked", Toast.LENGTH_SHORT).show();
                        navigateToPatientHomeFragment();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "No," dismiss the dialog
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void showCancelConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Cancel Appointment")
                .setMessage("Are you sure you want to cancel your appointment?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Yes," navigate to PatientHomeFragment

                        navigateToPatientHomeFragment();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "No," dismiss the dialog
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


    private void navigateToPatientHomeFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        PatientHomeFragment patientHomeFragment = new PatientHomeFragment();

        // Replace the current fragment with PatientHomeFragment
        transaction.replace(R.id.frameLayout, patientHomeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private String getDayOfWeekString(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "SUNDAY";
            case Calendar.MONDAY:
                return "MONDAY";
            case Calendar.TUESDAY:
                return "TUESDAY";
            case Calendar.WEDNESDAY:
                return "WEDNESDAY";
            case Calendar.THURSDAY:
                return "THURSDAY";
            case Calendar.FRIDAY:
                return "FRIDAY";
            case Calendar.SATURDAY:
                return "SATURDAY";
            default:
                return ""; // Handle invalid day of the week if needed
        }
    }

    private void retrieveTimeSlotsFromFirebase(String dayOfWeekString) {
        Log.d("TimeSlots", "Retrieving time slots for: " + dayOfWeekString);

        /*DatabaseReference dayAvailabilityRef = doctorNodeRef.child(dayOfWeekString);*/
        Log.d("TimeSlots", "Before adding listener for database");
        doctorNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TimeSlots", "Listener for database triggered");
                List<String> availableTimeSlots = new ArrayList<>();
                // Add "Select Time" as the default item
                availableTimeSlots.add("Select Time");
                boolean slotsFound = false;
                for (DataSnapshot slotSnapshot : dataSnapshot.getChildren()) {
                    // Check if the day of the week matches the selected dayOfWeek
                    String dayOfWeek = slotSnapshot.child("dayOfWeek").getValue(String.class);
                    Log.d("databaseday","checkretrieval : "+ dayOfWeek);
                    if (dayOfWeek != null && dayOfWeek.equals(dayOfWeekString)) {
                        String startTime = slotSnapshot.child("startTime").getValue(String.class);
                        String endTime = slotSnapshot.child("endTime").getValue(String.class);

                        Log.d("TimeSlots", "Day of Week: " + dayOfWeek);
                        Log.d("TimeSlots", "Start Time: " + startTime);
                        Log.d("TimeSlots", "End Time: " + endTime);

                        if (startTime != null && endTime != null) {
                            String formattedTimeSlot = startTime + " - " + endTime;
                            availableTimeSlots.add(formattedTimeSlot);
                            slotsFound = true;
                        }
                    }

                }
                if (!slotsFound) {
                    // No time slots available
                    availableTimeSlots.clear();
                    availableTimeSlots.add("No slots for this day");
                }

                Log.d("TimeSlots", "Number of Time Slots: " + availableTimeSlots.size());
                populateTimeSlotSpinner(availableTimeSlots);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors here
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);

                        // Check if the selected date is in the past
                        Calendar currentCalendar = Calendar.getInstance();
                        if (selectedCalendar.before(currentCalendar)) {
                            // The selected date is in the past, show an error message or prevent setting the date
                            // You can display a Toast message or an AlertDialog here
                            Toast.makeText(requireContext(), "Please select a future date.", Toast.LENGTH_SHORT).show();
                        } else {
                            // The selected date is valid, proceed with setting the date
                            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                            dateEditText.setText(selectedDate);

                            // Determine the selected day of the week (0 = Sunday, 1 = Monday, etc.)
                            int dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);

                            // Map the day of the week to a string (e.g., "SUNDAY", "MONDAY", etc.)
                            String dayOfWeekString = getDayOfWeekString(dayOfWeek);

                            // Retrieve and populate the Spinner with time slots for the selected day
                            retrieveTimeSlotsFromFirebase(dayOfWeekString);
                        }
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }


    private void populateTimeSlotSpinner(List<String> timeSlots) {
        // Create an ArrayAdapter with the updated items
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(adapter);

        // Set the default selection to "Select Time"
        timeSlotSpinner.setSelection(0);

        Log.d("TimeSlots", "Number of Time Slots: " + timeSlots.size());
    }
    private void saveAppointmentToDatabase() {
        // Extract appointment details
        String dbuserId = currentuserId; // Extract this from your Firebase user
        String dbdoctorId = doctorId; // Extract this based on your logic
        String userName = patientName; // Extract this from your user data
        String doctorName = doctorname; // Extract this from the UI
        String surgeryName = surgeryId; // Extract this from your UI or data
        String date = dateEditText.getText().toString(); // Extract from the user-selected date
        String time = timeSlotSpinner.getSelectedItem().toString(); // Extract from the selected time
        String consultationType = selectedConsultationType; // You can set the type
        double consultationFee = consultationFees; // Set the fee
        boolean paidStatus = false; // You can set this to true or false based on payment status
        String dbproblemDescription = problemDescription.getText().toString(); // Extract this from your UI

        Log.d("AppointmentData", "User ID: " + dbuserId);
        Log.d("AppointmentData", "Doctor ID: " + dbdoctorId);
        Log.d("AppointmentData", "User Name: " + userName);
        Log.d("AppointmentData", "Doctor Name: " + doctorName);
        Log.d("AppointmentData", "Surgery Name: " + surgeryName);
        Log.d("AppointmentData", "Date: " + date);
        Log.d("AppointmentData", "Time: " + time);
        Log.d("AppointmentData", "Consultation Type: " + consultationType);
        Log.d("AppointmentData", "Consultation Fee: " + consultationFee);
        Log.d("AppointmentData", "Paid Status: " + paidStatus);
        Log.d("AppointmentData", "Problem Description: " + dbproblemDescription);

        // Create an instance of the Appointment class with the extracted data
        Appointment newAppointment = new Appointment(dbuserId, dbdoctorId, userName, doctorName, surgeryName, date, "pending", time, consultationType, consultationFee, paidStatus, dbproblemDescription);

        // Get a reference to the Firebase database location where you want to save the appointment
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments");

        // Push a new child node and obtain the unique key
        String appointmentKey = appointmentsRef.push().getKey();

        if (appointmentKey != null) {
            // Save the appointment data to the Firebase Realtime Database
            appointmentsRef.child(appointmentKey).setValue(newAppointment);

            // The appointment is now saved with a unique key in the "appointments" section of your database
        } else {
            // Handle the case where obtaining the key failed
        }
    }



}



