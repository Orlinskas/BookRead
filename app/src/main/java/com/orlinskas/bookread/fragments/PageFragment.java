package com.orlinskas.bookread.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.R;

public class PageFragment extends Fragment {

    private int pageNumber;
    public Book book;
    public  String testText;

    public static PageFragment newInstance(int page, String text) {
        PageFragment fragment = new PageFragment();
        Bundle args=new Bundle();
        args.putInt("num", page);
        args.putString("text", text);
        fragment.setArguments(args);
        return fragment;
    }

    public PageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
        testText = getArguments() != null ? getArguments().getString("text") : "test";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.page_fragment, container, false);
        TextView pageHeader = result.findViewById(R.id.page_fragment_tv_display);
        String header = testText;
        pageHeader.setText(header);
        return result;
    }
}