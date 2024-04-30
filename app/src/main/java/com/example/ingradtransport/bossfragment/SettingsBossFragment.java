package com.example.ingradtransport.bossfragment;

import static android.app.Activity.RESULT_OK;
import static com.soundcloud.android.crop.Crop.RESULT_ERROR;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ingradtransport.R;
import com.soundcloud.android.crop.Crop;;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsBossFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int UCROP_REQUEST_CODE = 2;
    private ActivityResultLauncher<Intent> mCropImageActivityResultLauncher;
    private ActivityResultLauncher<Intent> mPickImageActivityResultLauncher;
    private CircleImageView settingsImage;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_boss, container, false);
        settingsImage = view.findViewById(R.id.settings_image);
        //settingsImage.setOnClickListener(v -> openGallery());

        mPickImageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            beginCrop(data.getData());
                        }
                    }
                });

        mCropImageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            handleCrop(result.getResultCode(), data);
                        }
                    }
                });

        settingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crop.pickImage(getActivity());
                //Intent pickImageIntent = Crop.pickImage(getActivity());
                //mPickImageActivityResultLauncher.launch(pickImageIntent);
            }
        });

        return view;
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(mContext.getCacheDir(), "cropped"));
        Intent cropIntent = Crop.of(source, destination).asSquare().getIntent(getActivity());
        mCropImageActivityResultLauncher.launch(cropIntent);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            settingsImage.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(mContext, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.settings_image) {
//            settingsImage.setImageDrawable(null);
//            Crop.pickImage(getActivity());
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
//            beginCrop(data.getData());
//        } else if (requestCode == Crop.REQUEST_CROP) {
//            handleCrop(resultCode, data);
//        }
//    }

//    private void beginCrop(Uri source) {
//        Uri destination = Uri.fromFile(new File(mContext.getCacheDir(), "cropped"));
//        Crop.of(source, destination).asSquare().start(getActivity());
//    }
//
//    private void handleCrop(int resultCode, Intent result) {
//        if (resultCode == Activity.RESULT_OK) {
//            settingsImage.setImageURI(Crop.getOutput(result));
//        } else if (resultCode == Crop.RESULT_ERROR) {
//            Toast.makeText(mContext, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

}