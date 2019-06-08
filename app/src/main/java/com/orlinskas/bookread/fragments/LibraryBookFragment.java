package com.orlinskas.bookread.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.orlinskas.bookread.helpers.ActivityOpenHelper;
import com.orlinskas.bookread.interfaces.FragmentLibraryBookActions;

public class LibraryBookFragment extends Fragment {

    private ImageView image, delete;
    private TextView title, author, date;
    private Button button;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    public Book book;
    public int countFragments;
    private FragmentLibraryBookActions listenerDelete;
    private FragmentLibraryBookActions listenerOpen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_book_fragment, container, false);

        progressBar = view.findViewById(R.id.library_book_fragment_pb);
        relativeLayout = view.findViewById(R.id.library_book_fragment_rl);
        button = view.findViewById(R.id.library_book_fragment_btn);
        image = view.findViewById(R.id.library_book_fragment_iv);
        author = view.findViewById(R.id.library_book_fragment_tv_author);
        title = view.findViewById(R.id.library_book_fragment_tv_title);
        date = view.findViewById(R.id.library_book_fragment_tv_date);
        delete = view.findViewById(R.id.library_book_fragment_iv_delete);

        if (book.getCoverImage() == null) {
            image.setImageResource(book.getCoverImagePath());
        }else {
            image.setImageURI(Uri.parse(book.getCoverImage().getAbsolutePath()));
        }
        delete.setImageResource(R.drawable.ic_delete);
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
                listenerOpen.openBook(book);
            }
        });
        delete.setAlpha(0.5f);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerDelete.deleteBook(book);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        progressBar.setVisibility(View.INVISIBLE);
        if(countFragments%2 == 0) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorWHITE));
        }
        else {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorLowGREY));
        }
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listenerDelete = (FragmentLibraryBookActions) context;
        listenerOpen = (FragmentLibraryBookActions) context;
    }
}
