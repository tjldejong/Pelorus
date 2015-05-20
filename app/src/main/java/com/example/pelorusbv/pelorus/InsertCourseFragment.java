package com.example.pelorusbv.pelorus;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.maps.android.SphericalUtil;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InsertCourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InsertCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertCourseFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    OnFragmentInteractionListener mListener;

    View view;

    Double latBuoy1;
    Double lngBuoy1;
    Double wind;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InsertCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InsertCourseFragment newInstance(String param1, String param2) {
        InsertCourseFragment fragment = new InsertCourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InsertCourseFragment() {
        // Required empty public constructor
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
        view = inflater.inflate(R.layout.fragment_insert_course, container, false);
        Button insertCourseButton = (Button) view.findViewById(R.id.buttonCreateCourse);
        insertCourseButton.setOnClickListener(this);
        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonCreateCourse:
            if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
                EditText lat = (EditText) this.getView().findViewById(R.id.editTextLat);
                latBuoy1 = Double.parseDouble(lat.getText().toString());
                lngBuoy1 = Double.parseDouble(((EditText) this.getView().findViewById(R.id.editTextLng)).getText().toString());
                wind = Double.parseDouble(((EditText) this.getView().findViewById(R.id.editTextWind)).getText().toString());


                mListener.onCreateCourse(latBuoy1, lngBuoy1, wind);

            }
                break;

        }
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onCreateCourse(double lat, double lng, double wind);
    }



}
