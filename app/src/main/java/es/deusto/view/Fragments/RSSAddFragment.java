package es.deusto.view.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.elpoeta.menulateralslide.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.deusto.model.services.database.Database;
import es.deusto.model.services.database.dao.RSS;
import es.deusto.model.services.rss.task.RSSExistTask;
import es.deusto.view.MyActivity;

/**
 * Created by user on 26/08/2014.
 */
public class RSSAddFragment extends Fragment implements INotifyInternet{

    private static final int SELECT_FILE = 0;

    EditText eTextName, eTextUrl;
    Button bSave;
    ImageView img;
    private String path;
    File f;

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
        eTextUrl.setText("http://feeds.bbci.co.uk/news/rss.xml");
        bSave = (Button) rootView.findViewById(R.id.buttonSave);
        startEvents();
    }

    private void startEvents() {
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean internet = checkInternet();
                if(internet) {
                    RSSExistTask e = new RSSExistTask(RSSAddFragment.this);
                    e.execute(eTextUrl.getText().toString());
                }
                else{
                    Toast.makeText(getActivity(), "There is no internet connection",
                            Toast.LENGTH_LONG);
                }
            }
        });
    }

    private boolean checkInternet() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE) {
            if (null != data) {
                Uri selectedImage = data.getData();
                ImageView imgView = (ImageView) getActivity().findViewById(R.id.imagePlaceholder);
                imgView.setImageURI(selectedImage);
                imgView.setTag(selectedImage);
                Bitmap img;
                try {
                    img = getThumbnail(selectedImage);
                    img = Bitmap.createScaledBitmap(img, 48, 48, true);

                    File f = createImageFile();
                    Intent mediaScanIntent = new Intent (Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

                    Uri url = Uri.fromFile(f);
                    mediaScanIntent.setData(url);
                    getActivity().sendBroadcast(mediaScanIntent);

                    path = f.getAbsolutePath();

                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(f);
                        img.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = getActivity().getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        input = getActivity().getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        path = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void connectivity(boolean exist) {
        if(exist){
            RSS rss = new RSS();
            rss.setImageUri(path);
            rss.setName(eTextName.getText().toString());
            rss.setUrl(eTextUrl.getText().toString());
            boolean added = Database.Instance(getActivity()).getsRSS().insertRSS(rss);
            if (!added) {
                Toast.makeText(getActivity(), "There are already three RSS's added",
                        Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getActivity(), "Added RSS",
                        Toast.LENGTH_LONG).show();
                ((MyActivity) getActivity()).LoadItems();
                ((MyActivity) getActivity()).notifyAdapter();
            }
            Log.d("Button", "click");
        }
        else{
            Toast.makeText(getActivity(), "The RSS does not exist",
                    Toast.LENGTH_LONG).show();
        }
    }
}
