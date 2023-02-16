package com.example.csguide;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;

public class CSItemActivity extends AppCompatActivity {

    SharedPref sharedPref;
    TextView nameOfCSItemTv, exerciseCreatorTv, exerciseDescriptionTv, exerciseDetailsTv;
    AlertDialog.Builder builder;
    Button changeOrAddVideo, editDetails, removeDetails, submitDetails, addVideo, deleteVideo;
    EditText exerciseDetailsEt, urlEv;
    TextView noVideoTv;
    Dialog addDialog;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csitem);

        nameOfCSItemTv = findViewById(R.id.nameOfExerciseTv);
        nameOfCSItemTv.setText(DataModel.csItems.get(getIntent().getIntExtra("CSII", 0)).getName());

        /*
        exerciseCreatorTv = findViewById(R.id.exerciseCreatorTv);
        exerciseCreatorTv.setText(" BY " + DataModel.muscles.get(getIntent().getIntExtra("WESEC", 0))
                .getExercisesList().get(getIntent().getIntExtra("ELTOE", 0)).getCreator());

        exerciseDescriptionTv = findViewById(R.id.exerciseDescriptionTv);
        exerciseDescriptionTv.setText(" " + DataModel.muscles.get(getIntent().getIntExtra("WESEC", 0))
                .getExercisesList().get(getIntent().getIntExtra("ELTOE", 0)).getDescriptionOfExercise() + " ");
        if (exerciseDescriptionTv.getText().toString().length() <=3)
        {
            exerciseDescriptionTv.setVisibility(View.GONE);
        }

        exerciseDetailsTv = findViewById(R.id.exerciseDetailsTv);
        exerciseDetailsEt = findViewById(R.id.exerciseDetailsEt);
        exerciseDetailsEt.setVisibility(View.GONE);

        exerciseDetailsTv.setText(DataModel.muscles.get(getIntent().getIntExtra("WESEC", 0))
                .getExercisesList().get(getIntent().getIntExtra("ELTOE", 0)).getDetailsOfExercise());
        exerciseDetailsEt.setText(exerciseDetailsTv.getText());

        submitDetails = findViewById(R.id.submitDetails);
        submitDetails.setVisibility(View.GONE);

        editDetails = findViewById(R.id.editDetails);
        removeDetails = findViewById(R.id.removeDetails);

        noVideoTv = findViewById(R.id.noVideoTv);
        noVideoTv.setVisibility(View.GONE);

        deleteVideo = findViewById(R.id.deleteVideo);

        //Video

        exerciseVideoExample = findViewById(R.id.exerciseVideoExample);
        changeOrAddVideo = findViewById(R.id.addVideo);

        url = DataModel.muscles.get(getIntent().getIntExtra("WESEC", 0))
                .getExercisesList().get(getIntent().getIntExtra("ELTOE", 0)).getUrl();

        changeOrAddVideo.setVisibility(View.GONE);
        if (url == null) {
            exerciseVideoExample.setVisibility(View.GONE);
//            mc.setEnabled(false);
            if (isCreatorOrAdmin())
            {
                changeOrAddVideo.setVisibility(View.VISIBLE);
            }
            noVideoTv.setVisibility(View.VISIBLE);
            deleteVideo.setVisibility(View.GONE);
        } else {
            if (isCreatorOrAdmin()) {
                changeOrAddVideo.setVisibility(View.VISIBLE);
                changeOrAddVideo.setText(" Change video");
            } else {
                deleteVideo.setVisibility(View.GONE);
            }

            mc = (MediaController) findViewById(R.id.mc);
            uriVideo = Uri.parse(url);
            exerciseVideoExample.setVideoURI(uriVideo);
            exerciseVideoExample.setMediaController(new MediaController(this));
            exerciseVideoExample.start();
            exerciseVideoExample.requestFocus();
        }

        //

        if (!sharedPref.GetUsername().equals(DataModel.muscles.get(getIntent().getIntExtra("WESEC", 0))
                .getExercisesList().get(getIntent().getIntExtra("ELTOE", 0)).getCreator()) &&
                !sharedPref.GetUsername().equals("admin")) {
            removeDetails.setVisibility(View.GONE);
            editDetails.setVisibility(View.GONE);
        } else {
            removeDetails.setOnClickListener(this);
            editDetails.setOnClickListener(this);
        }

        //Download video
        downloadVideoIB = findViewById(R.id.downloadVideo);
        downloadVideoIB.setOnClickListener(this);
        if (url == null)
        {
            downloadVideoIB.setVisibility(View.GONE);
        }

        afterDownloadIV = findViewById(R.id.afterDownloadImage);
        afterDownloadIV.setVisibility(View.GONE);
        //

        submitDetails.setOnClickListener(this);
        changeOrAddVideo.setOnClickListener(this);
        deleteVideo.setOnClickListener(this);

        builder = new AlertDialog.Builder(this);
         */
    }
}