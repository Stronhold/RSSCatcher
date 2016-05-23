package es.deusto.view.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.elpoeta.menulateralslide.R;

import es.deusto.model.services.database.Database;
import es.deusto.model.services.database.dao.RSS;
import es.deusto.view.MyActivity;

/**
 * Created by user on 26/08/2014.
 */
public class RSSAddFragment extends Fragment {

    private static final int SELECT_FILE = 0;

    EditText eTextName, eTextUrl;
    Button bSave;
    ImageView img;
    private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.seccion4, container, false);


        img = (ImageView) rootView.findViewById(R.id.imagePlaceholder);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        return rootView;


    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_FILE);
    }


    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        eTextName = (EditText) rootView.findViewById(R.id.tEditNameRss);
        eTextUrl = (EditText) rootView.findViewById(R.id.tEditUrl);
        bSave = (Button) rootView.findViewById(R.id.buttonSave);
        startEvents();
    }

    private void startEvents() {
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RSS rss = new RSS();
                rss.setImageUri(path);
                rss.setName(eTextName.getText().toString());
                rss.setUrl(eTextUrl.getText().toString());
                boolean added = Database.Instance(getActivity()).getsRSS().insertRSS(rss);
                if(!added){
                    Toast.makeText(getActivity(), "There are already three RSS's added",
                            Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getActivity(), "Added RSS",
                            Toast.LENGTH_LONG).show();
                    ((MyActivity)getActivity()).LoadItems();
                    ((MyActivity)getActivity()).notifyAdapter();
                }
                Log.d("Button", "click");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("ENTRO", "AQUI");
        if (requestCode == SELECT_FILE) {
            if (null != data) {
                Uri selectedImage = data.getData();
                path = selectedImage.getPath();
                ImageView imgView = (ImageView) getActivity().findViewById(R.id.imagePlaceholder);
                imgView.setImageURI(selectedImage);
                imgView.setTag(selectedImage);
            }
        }
    }
}
