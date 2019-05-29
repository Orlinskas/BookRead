package com.orlinskas.bookread.fragments;

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
import android.widget.TextView;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.R;

public class LibraryBookFragment extends Fragment {

    private ImageView image;
    private TextView title, author, date;
    private Button button;
    public Book book;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_book_fragment, container, false);

        button = (Button) view.findViewById(R.id.button);
        image = view.findViewById(R.id.library_book_fragment_iv);
        author = view.findViewById(R.id.library_book_fragment_tv_author);
        title = view.findViewById(R.id.library_book_fragment_tv_title);
        date = view.findViewById(R.id.library_book_fragment_tv_date);

        author.setText(book.getAuthorName());

        button.setAlpha(0.0f);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });


        return view;
    }
}
