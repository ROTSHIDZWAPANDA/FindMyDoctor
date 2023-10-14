package com.example.findmydoctor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterSurgery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterSurgery extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private LinearLayout layoutPending;
    private LinearLayout layoutRegister;
    private Button btnRegisterSurgery,btnCancel,btnViewRegisterSurgery;
    private GridView gridViewPdf;
    private final ArrayList<Uri> pdfFiles = new ArrayList<>();
    private PdfGridAdapter pdfGridAdapter;
    private FirebaseDatabase database;
    private DatabaseReference surgeryRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String userId;

    private static final int STORAGE_PERMISSION_CODE = 1;


    private final String apiKey="AIzaSyBNScNA8xZx3WJJQJJyn1mLF2Y92F7FYDg";




    public RegisterSurgery() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterSurgery.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterSurgery newInstance(String param1, String param2) {
        RegisterSurgery fragment = new RegisterSurgery();
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
        View rootView = inflater.inflate(R.layout.fragment_register_surgery, container, false);
        recyclerView = rootView.findViewById(R.id.PendingSurgery);
        Spinner experienceSpinner = rootView.findViewById(R.id.experienceSpinner);
        Spinner languagesSpinner = rootView.findViewById(R.id.language);
        Spinner telemedicineSpinner = rootView.findViewById(R.id.Telemedicine);
        Spinner specialistSpinner = rootView.findViewById(R.id.practioner);







        // Create a list of PendingSurgeryItem objects (replace with your data)
        List<PendingSurgeryItem> itemList = new ArrayList<>();

        // Populate itemList with data (you can add more items)
        itemList.add(new PendingSurgeryItem("Surgery 1", "Dr. John Doe", "Pending"));
        itemList.add(new PendingSurgeryItem("Surgery 2", "Dr. Jane Smith", "Completed"));

        // Initialize and set up your custom adapter
        adapter = new CustomAdapter(itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        layoutPending = rootView.findViewById(R.id.LayoutPending);
        layoutRegister = rootView.findViewById(R.id.LayoutRegister);
        btnRegisterSurgery = rootView.findViewById(R.id.buttonRegisterSurgery);
        btnCancel = rootView.findViewById(R.id.RCancel);
        btnViewRegisterSurgery = rootView.findViewById(R.id.btnRegisterSurg);





       btnViewRegisterSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the Register layout and hide the Pending layout
                layoutRegister.setVisibility(View.VISIBLE);
                layoutPending.setVisibility(View.GONE);
            }
       });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the Pending layout and hide the Register layout
                layoutPending.setVisibility(View.VISIBLE);
                layoutRegister.setVisibility(View.GONE);
            }
        });

        // Load the list of experience ranges from resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.select_experience,
                R.layout.spinner_items_layout
        );
        // Load the list of experience ranges from resources
        ArrayAdapter<CharSequence> adapterLanguage = ArrayAdapter.createFromResource(requireContext(),
                R.array.array_languages,
                R.layout.spinner_items_layout
        );
        // Load the list of experience ranges from resources
        ArrayAdapter<CharSequence> adapterTelemedicine = ArrayAdapter.createFromResource(requireContext(),
                R.array.array_telemedicine,
                R.layout.spinner_items_layout
        );
        // Load the list of experience ranges from resources
        ArrayAdapter<CharSequence> adapterSpecialist = ArrayAdapter.createFromResource(requireContext(),
                R.array.array_specialist,
                R.layout.spinner_items_layout
        );

        // Specify the layout to use when the list appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list appears
        adapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTelemedicine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSpecialist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        experienceSpinner.setAdapter(adapter);
        languagesSpinner.setAdapter(adapterLanguage);
        telemedicineSpinner.setAdapter(adapterTelemedicine);
        specialistSpinner.setAdapter(adapterSpecialist);



        // Optional: Set an OnItemSelectedListener to handle user selections
        experienceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedExperience = parentView.getItemAtPosition(position).toString();
                // Handle the selected experience range here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if needed
            }
        });
        languagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLanguage = parentView.getItemAtPosition(position).toString();
                // Handle the selected experience range here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if needed
            }
        });
        telemedicineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTelemedicine = parentView.getItemAtPosition(position).toString();
                // Handle the selected experience range here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if needed
            }
        });
        specialistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedSpecialist = parentView.getItemAtPosition(position).toString();
                // Handle the selected experience range here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if needed
            }
        });
        //declaring gridview


        gridViewPdf = rootView.findViewById(R.id.gridViewPdf);
        pdfGridAdapter = new PdfGridAdapter(requireContext(), pdfFiles);
        gridViewPdf.setAdapter(pdfGridAdapter);

        Button buttonUploadProof = rootView.findViewById(R.id.buttonUploadProof);
        buttonUploadProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the file picker to select PDFs
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 1);
            }
        });
        //code was here
        // Initialize Firebase Database and Storage references
        database = FirebaseDatabase.getInstance();
        surgeryRef = database.getReference("surgeryNodeName");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        // Initialize Firebase Authentication and get the current user's ID
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            // Handle the case where the user is not authenticated
            // You may want to redirect the user to the login screen or take appropriate action
        }

        // Initialize other UI elements and set up button click listener
        
        // Set up btnRegisterSurgery click listener
        btnRegisterSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch the user's first name and last name, and then save surgery data
                fetchUserNameAndSaveSurgeryData();
            }

            private void fetchUserNameAndSaveSurgeryData() {
                DatabaseReference usersTableRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                usersTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String firstName = dataSnapshot.child("firstName").getValue(String.class);
                            String lastName = dataSnapshot.child("lastName").getValue(String.class);
                            String profilePictureUrl = dataSnapshot.child("profilePictureUrl").getValue(String.class);
                            String userFirstName = firstName;
                            String userLastName = lastName;
                            String userProfilepictureurl = profilePictureUrl;


                            Log.d("SaveSurgery", "First Name: " + userFirstName);
                            Log.d("SaveSurgery", "Last Name: " + userLastName);

                            // Now that you have the user's first name and last name, you can save the surgery data
                            saveSurgeryData(userFirstName, userLastName, userProfilepictureurl);
                        }
                    }

                    private void saveSurgeryData(String userFirstName, String userLastName, String userProfilepictureurl) {
                        String surgeryName = ((EditText) rootView.findViewById(R.id.surgeryName)).getText().toString();
                        String address = ((EditText) rootView.findViewById(R.id.Address)).getText().toString();
                        String experience = ((Spinner) rootView.findViewById(R.id.experienceSpinner)).getSelectedItem().toString();
                        String language = ((Spinner) rootView.findViewById(R.id.language)).getSelectedItem().toString();
                        String specialist = ((Spinner) rootView.findViewById(R.id.practioner)).getSelectedItem().toString();
                        String insuranceAccepted = ((EditText) rootView.findViewById(R.id.Cards)).getText().toString();
                        String telemedicine = ((Spinner) rootView.findViewById(R.id.Telemedicine)).getSelectedItem().toString();
                        String description = ((EditText) rootView.findViewById(R.id.Descption)).getText().toString();
                        double consultationFees = Double.parseDouble(((EditText) rootView.findViewById(R.id.Consultationfees)).getText().toString());

                        Log.d("SaveSurgery", "User ID: " + userId);
                        Log.d("SaveSurgery", "Surgery Name: " + surgeryName);



                        // Create a Surgery object
                        Surgery surgery = new Surgery();
                        surgery.setFirstName(userFirstName);
                        surgery.setLastName(userLastName);
                        surgery.setProfilePictureUrl(userProfilepictureurl);
                        surgery.setSurgeryName(surgeryName);
                        surgery.setAddress(address);
                        surgery.setExperience(experience);
                        surgery.setLanguage(language);
                        surgery.setSpecialist(specialist);
                        surgery.setInsuranceAccepted(insuranceAccepted);
                        surgery.setTelemedicine(telemedicine);
                        surgery.setDescription(description);
                        surgery.setConsultationFees(consultationFees);

                        // Get a reference to the Firebase database node where you want to save the surgery data
                        DatabaseReference userSurgeryRef = surgeryRef
                                .child(userId);


                        // Push the surgery data to Firebase (this will generate a unique ID for the surgery)
                        String surgeryKey = userSurgeryRef.push().getKey();

                        // Now, you have surgeryKey, which can be used to create the "surgeryNodeName" node ...new
                        //surgeryRef.child(surgeryKey).setValue(surgery);

                        // Create a reference to the "surgery_pdf" folder in Firebase Storage
                        StorageReference pdfFolderRef = storageRef.child("surgery_pdf");

                        // Create a reference to the user's folder (UID) inside the "surgery_pdf" folder
                        StorageReference userFolderRef = pdfFolderRef.child(userId);

                        // Upload the selected PDF files to the user's folder
                        // Upload the selected PDF files to the user's folder
                        for (int i = 0; i < pdfFiles.size(); i++) {
                            Uri pdfUri = pdfFiles.get(i);
                            String pdfFileName = "surgery_" + surgeryKey + "_file_" + i + ".pdf";
                            StorageReference pdfStorageRef = userFolderRef.child(pdfFileName);

                            pdfStorageRef.putFile(pdfUri)
                                    .addOnSuccessListener(taskSnapshot -> {
                                        pdfStorageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                            // Store the download URL of the uploaded PDF in the Surgery object
                                            surgery.addPdfDownloadUrl(uri.toString());

                                            // Check if all PDFs have been uploaded and URLs added to the Surgery object
                                            if (surgery.getPdfDownloadUrls().size() == pdfFiles.size()) {
                                                // All PDFs uploaded and URLs added, save the Surgery object to Realtime Database
                                                userSurgeryRef.child(surgeryKey).setValue(surgery);

                                                // Display a success message to the user or perform any other necessary actions
                                                Toast.makeText(requireContext(), "Saved,Thank you.", Toast.LENGTH_SHORT).show();
                                                layoutPending.setVisibility(View.VISIBLE);
                                                layoutRegister.setVisibility(View.GONE);
                                            }
                                        });
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the upload failure
                                        Toast.makeText(requireContext(), "PDF upload failed. Please try again.", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors that may occur during the data retrieval
                        Toast.makeText(requireContext(), "Failed to fetch user data. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });








        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri pdfUri = data.getClipData().getItemAt(i).getUri();
                    String mimeType = requireContext().getContentResolver().getType(pdfUri);

                    if (mimeType != null && mimeType.equals("application/pdf")) {
                        pdfFiles.add(pdfUri);
                    } else {
                        // Handle the case where the selected file is not a PDF
                        // Show a message to the user or log an error.
                    }
                }
                pdfGridAdapter.notifyDataSetChanged();
            } else if (data.getData() != null) {
                Uri pdfUri = data.getData();
                String mimeType = requireContext().getContentResolver().getType(pdfUri);

                if (mimeType != null && mimeType.equals("application/pdf")) {
                    pdfFiles.add(pdfUri);
                } else {
                    // Handle the case where the selected file is not a PDF
                    // Show a message to the user or log an error.
                }
                pdfGridAdapter.notifyDataSetChanged();
            }
        }

    }
    // Check and request permission if needed
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with file operations
            } else {
                // Permission denied, handle this case (e.g., show a message to the user)
            }
        }
    }







}