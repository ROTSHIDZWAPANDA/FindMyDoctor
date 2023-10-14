package com.example.findmydoctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CardView cardView,cardViewAvailablity,appointMent;

    public DoctorHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorHomeFragment newInstance(String param1, String param2) {
        DoctorHomeFragment fragment = new DoctorHomeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_doctor_home, container, false);
        cardView = view.findViewById(R.id.cvRegisterSurgery); // Replace with your actual CardView ID
        cardViewAvailablity = view.findViewById(R.id.cvAvailability);
        appointMent = view.findViewById(R.id.cvdoctorAppointments);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with RegisterSurgeryFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutDoctor, new RegisterSurgery())
                        .addToBackStack(null) // Optional: Add to back stack if you want to navigate back
                        .commit();
            }
        });
        cardViewAvailablity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with RegisterSurgeryFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutDoctor, new DoctorAvailability())
                        .addToBackStack(null) // Optional: Add to back stack if you want to navigate back
                        .commit();
            }
        });
        appointMent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with RegisterSurgeryFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutDoctor, new doctorAppointments())
                        .addToBackStack(null) // Optional: Add to back stack if you want to navigate back
                        .commit();
            }
        });


        return view;
    }
}