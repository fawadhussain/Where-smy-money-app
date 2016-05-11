package com.example.wsmm.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wsmm.R;
import com.example.wsmm.TabFragment;
import com.example.wsmm.activity.MainActivity;
import com.example.wsmm.adapter.CategoryAdapter;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.model.Category;
import com.example.wsmm.model.CategoryItem;
import com.example.wsmm.util.GeneralUtils;
import com.example.wsmm.util.ImageUtils;
import com.example.wsmm.util.TouchImageView;
import com.tmxlr.lib.driodvalidatorlight.Form;
import com.tmxlr.lib.driodvalidatorlight.helper.RegexTemplate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by abubaker on 13/03/2016.
 */
public class AddTransactionFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private CategoryAdapter categoryAdapter;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private ImageView setImage;
    public static final int TAKE_PHOTO_CODE = 100;
    private String mCurrentPhotoPath;
    private long selectedDate = 0;
    private EditText categoryTitle;
    private EditText amount;
    private String categoryName;
    DBClient db;
    private Realm realm;
    private RealmConfiguration realmConfig;
    private Category editCategory;
    private List<CategoryItem> categoryItemList;



    public AddTransactionFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.add_transaction_fragment;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);

        setImage = (ImageView) parent.findViewById(R.id.set_image);
        categoryTitle = (EditText) parent.findViewById(R.id.et_category_title);
        amount = (EditText) parent.findViewById(R.id.et_add_amount);
        mRecyclerView = (RecyclerView) parent.findViewById(R.id.category_list);
        // realmConfig = new RealmConfiguration.Builder(getActivity()).build();
        // Open the Realm for the UI thread.
        //realm = Realm.getInstance(realmConfig);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        db = new DBClient();
        categoryItemList = db.getCategoryList();
       // categoryList = Arrays.asList(getResources().getStringArray(R.array.categories));
        categoryAdapter = new CategoryAdapter(getActivity(), categoryItemList);
        setImage.setOnClickListener(AddTransactionFragment.this);
        mRecyclerView.setAdapter(categoryAdapter);

        Bundle args = getArguments();

        if (args!=null){

            editCategory = new Category();
            editCategory = (Category) getArguments().getSerializable(PrimaryFragment.DESCRIBABLE_KEY);
            if (editCategory!=null){

                categoryTitle.setText(editCategory.getCategoryTitle());
                amount.setText(editCategory.getPrice());
                ImageUtils.loadImageLocally(getActivity(), 150, parent.findViewById(R.id.image_display_layout).getWidth(), setImage, editCategory.getImagePath());
                mCurrentPhotoPath = editCategory.getImagePath();
                categoryName = editCategory.getCategoryName();
                selectedDate = editCategory.getDate();


            }

        }


        parent.findViewById(R.id.btn_save).setOnClickListener(this);

        setHasOptionsMenu(true);
        parent.findViewById(R.id.btn_select_image).setOnClickListener(AddTransactionFragment.this);
        categoryAdapter.setClickListener(new CategoryAdapter.ClickListener() {
            @Override
            public void onItemClicked(int itemPosition) {

                categoryName = categoryItemList.get(itemPosition).getCategoryItemName();


            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.clear();
        inflater.inflate(R.menu.add_transction, menu);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_select_image:

                dialogOption();
                break;

            case R.id.set_image:

                if (editCategory!=null){

                    ZoomView(editCategory.getImagePath());

                }else {
                    if (getCurrentPhotoPath()!=null){
                        ZoomView(getCurrentPhotoPath());
                    }

                }

                break;

            case R.id.btn_save:
                MainActivity.checkPreviousRecords = false;

                if (categoryName != null) {

                    if (formData().validate()){

                        db = new DBClient();

                        Category categoryObject = new Category();
                        categoryObject.setCategoryName(categoryName);
                        categoryObject.setCategoryTitle(categoryTitle.getText().toString());
                        categoryObject.setPrice(amount.getText().toString());
                        if (getCurrentPhotoPath() != null) {
                            categoryObject.setImagePath(getCurrentPhotoPath());
                        }

                        if (getDate() == 0) {
                            categoryObject.setDate(GeneralUtils.getCurrentSystemDate());
                        } else {
                            categoryObject.setDate(getDate());
                        }

                        if (getDate() == 0){
                            categoryObject.setStringDate(GeneralUtils.getCurrentFormattedDate());
                        }else {
                            categoryObject.setStringDate(GeneralUtils.getFormattedDateString(getDate()));

                        }

                        if (editCategory!=null){

                            categoryObject.setCategoryId(editCategory.getCategoryId());

                            db.updateTransaction(categoryObject, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getActivity(), "Update Successfully ", Toast.LENGTH_SHORT).show();

                                    getHelper().replaceFragment(new TabFragment(),false,"TabFragment");

                                }
                            });



                        }else {

                            db.saveTransaction(categoryObject, new Realm.Transaction.OnSuccess() {

                                @Override
                                public void onSuccess() {

                                    getHelper().replaceFragment(new TabFragment(),false,"TabFragment");

                                }
                            });

                        }



                    }

                }else {

                    Toast.makeText(getActivity(), "Please select category", Toast.LENGTH_SHORT).show();

                }

                break;


        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


    private void dialogOption() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        checkCameraPermission();

                    } else {

                        dispatchTakePictureIntent(getActivity());

                    }

                } else if (items[item].equals("Choose from Library")) {


                    if (Build.VERSION.SDK_INT >= 23) {

                        checkPermissionReadExternalStorage();

                    } else {

                        readFileFromGallery();

                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for Camera Permission
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    dispatchTakePictureIntent(getActivity());


                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;

            case REQUEST_CODE_ASK_PERMISSIONS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    readFileFromGallery();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Permission READ EXTERNAL STORAGE Denied", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkCameraPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        dispatchTakePictureIntent(getActivity());
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissionReadExternalStorage() {
        int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showMessageOKCancel("You need to allow access to Read External Storage",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        readFileFromGallery();

    }


    private void readFileFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == TAKE_PHOTO_CODE) {

                ImageUtils.loadImageLocally(getActivity(), setImage.getWidth() / 2, 200, setImage, getCurrentPhotoPath());

            }

        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        setImage.setImageBitmap(bm);
    }


    private void dispatchTakePictureIntent(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(context);
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

                startActivityForResult(takePictureIntent, AddTransactionFragment.TAKE_PHOTO_CODE);
            }
        }
    }


    private File createImageFile(Context context) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        File appFolder = new File(storageDir, context.getResources().getString(R.string.app_name));
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                appFolder      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;

    }


    public void setChangeDate(long milliSeconed) {
        this.selectedDate = milliSeconed;
    }

    private long getDate() {
        return selectedDate;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
//        realm.close();


    }


    private Form formData() {
        Form form = new Form(getActivity());
        form.check(categoryTitle, RegexTemplate.NOT_EMPTY_PATTERN, "Please add Category title");
        form.check(amount, RegexTemplate.NOT_EMPTY_PATTERN, "Please add amount");
        return form;

    }


    private void ZoomView(String url) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.zoom_dialog);
        TouchImageView view = (TouchImageView) dialog.findViewById(R.id.zoom_image);
      // Picasso.with(getActivity()).load(url).into(view);
        ImageUtils.loadImageLocally(getActivity(),view,url);
        dialog.show();

    }





}
