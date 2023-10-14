package com.example.findmydoctor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PdfGridAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Uri> pdfFiles;

    public PdfGridAdapter(Context context, ArrayList<Uri> pdfFiles) {
        this.context = context;
        this.pdfFiles = pdfFiles;
    }

    @Override
    public int getCount() {
        return pdfFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return pdfFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);
        }

        ImageView pdfImageView = convertView.findViewById(R.id.pdfImageView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        TextView pdfNameTextView = convertView.findViewById(R.id.pdfNameTextView);


        Uri pdfUri = pdfFiles.get(position);
        String pdfName = getFileNameFromUri(pdfUri);
        // You can set the PDF image and handle delete button click here
        // Example:
        pdfImageView.setImageResource(R.drawable.newpdf); // Set a placeholder image
        pdfNameTextView.setText(pdfName);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the delete button click here
                pdfFiles.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    } else {
                        // Handle the case where DISPLAY_NAME is not available
                        // You can use the last segment of the URI as a fallback.
                        result = uri.getLastPathSegment();
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return result;
    }



}
