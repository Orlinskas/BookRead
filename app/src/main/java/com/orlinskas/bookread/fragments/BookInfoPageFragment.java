package com.orlinskas.bookread.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orlinskas.bookread.R;

public class BookInfoPageFragment extends Fragment {
    private ProgressBar pagesProgress;
    private TextView pagesInfo;
    public int pageNumber, pageCount;


    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_info_page_fragment, container, false);

        pagesProgress = view.findViewById(R.id.book_info_page_fragment_pb_pages);
        pagesInfo =  view.findViewById(R.id.book_info_page_fragment_tv_pages_info);

        pagesProgress.setMax(pageCount);
        pagesProgress.setProgress(pageNumber);
        pagesProgress.setAlpha(0.6f);

        pagesInfo.setText(String.format("%d/%d", pageNumber, pageCount));
        pagesInfo.setTextSize(7.0f);
        pagesInfo.setShadowLayer(1.0f, 0.0f, 1.0f, getResources().getColor(R.color.colorPrimaryDark));

        return view;
    }
}
