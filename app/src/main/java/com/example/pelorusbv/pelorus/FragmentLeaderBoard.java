package com.example.pelorusbv.pelorus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLeaderBoard extends Fragment {


    public FragmentLeaderBoard() {
        // Required empty public constructor
    }

    public static FragmentLeaderBoard newInstance() {
        FragmentLeaderBoard leaderBoardFragment = new FragmentLeaderBoard();
        return leaderBoardFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leader_board, container, false);

    }

}
