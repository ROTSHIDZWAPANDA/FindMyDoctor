package com.example.findmydoctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public chats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chats.
     */
    // TODO: Rename and change types and number of parameters
    public static chats newInstance(String param1, String param2) {
        chats fragment = new chats();
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
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
/*
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        // 1. Get the current user's ID (you need to implement user authentication)
        String currentuserId = null;
        if (currentUser != null) {
            currentuserId = currentUser.getUid();

            // Now you have the user's ID (userId), proceed to fetch their first name and last name.
        } else {
            // Handle the case where there is no authenticated user
        }

// 2. Query the "chats" table
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        Query chatQuery = chatsRef.orderByChild("participant1").equalTo(currentuserId).limitToFirst(10); // You can set a limit to retrieve a specific number of chats

        String finalCurrentuserId = currentuserId;
        chatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    String chatId = chatSnapshot.getKey();
                    String otherParticipantId;
                    final String[] lastMessage = new String[1];

                    // Determine the other participant's ID
                    String participant1 = chatSnapshot.child("participant1").getValue(String.class);
                    String participant2 = chatSnapshot.child("participant2").getValue(String.class);

                    if (participant1.equals(finalCurrentuserId)) {
                        otherParticipantId = participant2;
                    } else {
                        otherParticipantId = participant1;
                    }

                    // 3. Query user information based on otherParticipantId
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    DatabaseReference otherParticipantRef = usersRef.child(otherParticipantId);

                    // You can now fetch the other participant's name and profile picture
                    otherParticipantRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String otherParticipantName = dataSnapshot.getValue(String.class);

                            // Fetch other participant's profile picture
                            // ...

                            // 4. Query the last message in the chat
                            DatabaseReference messagesRef = chatSnapshot.getRef().child("messages");
                            Query lastMessageQuery = messagesRef.orderByKey().limitToLast(1);

                            lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                        lastMessage[0] = messageSnapshot.child("text").getValue(String.class);
                                        // Handle last message
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Handle error
                                }
                            });

                            // Now you have the other participant's name, profile picture, and last message
                            // You can use this data to update your chat list or UI
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });*/


        return view;
    }
}