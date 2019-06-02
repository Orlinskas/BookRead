package com.orlinskas.bookread.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.R;

public class LibraryBookFragment extends Fragment {

    private ImageView image;
    private TextView title, author, date;
    private Button button;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    public Book book;
    public int countFragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_book_fragment, container, false);

        progressBar = view.findViewById(R.id.library_book_fragment_pb);
        relativeLayout = view.findViewById(R.id.library_book_fragment_rl);
        button = (Button) view.findViewById(R.id.library_book_fragment_btn);
        image = view.findViewById(R.id.library_book_fragment_iv);
        author = view.findViewById(R.id.library_book_fragment_tv_author);
        title = view.findViewById(R.id.library_book_fragment_tv_title);
        date = view.findViewById(R.id.library_book_fragment_tv_date);

        try {
            image.setImageURI(Uri.parse(book.getCoverImage().getAbsolutePath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        author.setText(book.getAuthorName());
        title.setText(book.getBookTitle());
        date.setText(book.getDate());
        progressBar.setVisibility(View.INVISIBLE);
        if(countFragments%2 == 0) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorWHITE));
        }
        else {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorLowGREY));
        }

        button.setAlpha(0.0f);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorMiddleGREY));

            }
        });
        return view;
    }
}
