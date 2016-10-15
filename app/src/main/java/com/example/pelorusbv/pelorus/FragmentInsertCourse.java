package com.example.pelorusbv.pelorus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.sql.SQLException;

public class FragmentInsertCourse extends Fragment implements View.OnClickListener {
    //Fragment met buttons om course te maken.
    OnFragmentInteractionListener mListener;
    View view;
    String name;
    Double latBuoy1;
    Double lngBuoy1;
    Double wind;
    DataSourceCourses dataSourceCourses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSourceCourses = new DataSourceCourses(getActivity());

        try {
            dataSourceCourses.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_insert_course, container, false);

        Button insertPreviewButton = (Button) view.findViewById(R.id.buttonPreviewCourse);
        insertPreviewButton.setOnClickListener(this);

        Button insertCourseButton = (Button) view.findViewById(R.id.buttonCreateCourse);
        insertCourseButton.setOnClickListener(this);

        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onClick(View view) {

        name = ((EditText) this.getView().findViewById(R.id.editTextName)).getText().toString();
        latBuoy1 = Double.parseDouble(((EditText) this.getView().findViewById(R.id.editTextLat)).getText().toString());
        lngBuoy1 = Double.parseDouble(((EditText) this.getView().findViewById(R.id.editTextLng)).getText().toString());
        wind = Double.parseDouble(((EditText) this.getView().findViewById(R.id.editTextWind)).getText().toString());

        LatLng b1 = new LatLng(latBuoy1,lngBuoy1);
        LatLng b2 = SphericalUtil.computeOffset(b1, 50, wind - 90);
        LatLng b3 = SphericalUtil.computeOffset(b1, 0.1 * 1852, wind);
        LatLng b4 = SphericalUtil.computeOffset(b1, 0.1 * 1852, wind - 180);

        if (mListener != null) {
            switch (view.getId()) {
                case R.id.buttonPreviewCourse:
                    mListener.onPreviewCourse(b1,b2,b3,b4);
                    break;
                case R.id.buttonCreateCourse:
                    dataSourceCourses.CreateCourse(name, b1.latitude, b1.longitude, b2.latitude, b2.longitude, b3.latitude, b4.longitude, b4.latitude, b4.longitude);
                    mListener.onCreateCourse();
                    break;
            }
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

    @Override
    public void onPause() {
        super.onPause();
        dataSourceCourses.close();
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
        void onPreviewCourse(LatLng b1, LatLng b2, LatLng b3, LatLng b4);
        void onCreateCourse();
    }



}
