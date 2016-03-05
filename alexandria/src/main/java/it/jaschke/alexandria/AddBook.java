package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import de.gruenewald.android.alexandria.BarcodeScannerActivity;
import de.gruenewald.android.util.Net;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.services.DownloadImage;


public class AddBook extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = AddBook.class.getSimpleName();

    private EditText ean;
    private TextView mErrorText;

    private View rootView;
    private final String EAN_CONTENT = "eanContent";
    private static final String SCAN_FORMAT = "scanFormat";
    private static final String SCAN_CONTENTS = "scanContents";
    private static final int LOADER_ID = 1;

    private String mScanFormat = "Format:";
    private String mScanContents = "Contents:";


    public AddBook() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (ean != null) {
            outState.putString(EAN_CONTENT, ean.getText().toString());
        }
    }

    private void raiseError(String pErrorText) {
        if (mErrorText != null) {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText(pErrorText);
            mErrorText.invalidate();
        }
    }

    private void hideError() {
        if (mErrorText != null) {
            mErrorText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add_book, container, false);

        if (rootView != null) {
            if (rootView.findViewById(R.id.textAddBookError) instanceof TextView) {
                mErrorText = (TextView) rootView.findViewById(R.id.textAddBookError);
            } else {
                mErrorText = null;
            }

            // Edit JG: secured usage of ean
            if (rootView.findViewById(R.id.ean) instanceof EditText) {
                ean = (EditText) rootView.findViewById(R.id.ean);
                ean.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //no need
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //no need
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String ean = s.toString();
                        //catch isbn10 numbers
                        if (ean.length() == 10 && !ean.startsWith("978")) {
                            ean = "978" + ean;
                        }

                        if (ean.length() < 13) {
                            clearFields();
                            return;
                        }

                        // Edit JG: Check for an active internet connection before using the BookService
                        if (Net.isOnline(getActivity())) {
                            // hide the error text since everything should be okay
                            hideError();

                            //Once we have an ISBN, start a book intent
                            Intent bookIntent = new Intent(getActivity(), BookService.class);
                            bookIntent.putExtra(BookService.EAN, ean);
                            bookIntent.setAction(BookService.FETCH_BOOK);
                            getActivity().startService(bookIntent);
                            AddBook.this.restartLoader();
                        } else {
                            // display an error message to the user if there is no internet connection
                            raiseError(getString(R.string.error_nointernet));
                        }
                    }
                });
            } else {
                ean = null;
            }

            // Edit JG: null-check for scan_button
            if (rootView.findViewById(R.id.scan_button) != null) {
                rootView.findViewById(R.id.scan_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myCameraIntent = new Intent(getActivity(), BarcodeScannerActivity.class);
                        startActivityForResult(myCameraIntent, BarcodeScannerActivity.ACTIVITY_REQUEST_CODE_BARCODE);
                    }
                });
            }

            // Edit JG: null-check for save-button
            if (rootView.findViewById(R.id.save_button) != null) {
                rootView.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ean != null) {
                            ean.setText("");
                        }
                    }
                });
            }

            // Edit JG: null-check for delete-button
            if (rootView.findViewById(R.id.delete_button) != null && ean != null) {
                rootView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent bookIntent = new Intent(getActivity(), BookService.class);
                        bookIntent.putExtra(BookService.EAN, ean.getText().toString());
                        bookIntent.setAction(BookService.DELETE_BOOK);
                        getActivity().startService(bookIntent);
                        ean.setText("");
                    }
                });
            }
        }

        if (savedInstanceState != null && ean != null) {
            ean.setText(savedInstanceState.getString(EAN_CONTENT));
            ean.setHint("");
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BarcodeScannerActivity.ACTIVITY_REQUEST_CODE_BARCODE) {
            if (resultCode == BarcodeScannerActivity.ACTIVITY_RESULT_CODE_SUCCESS && data != null) {
                ean.setText(data.getStringExtra(BarcodeScannerActivity.ACTIVITY_RESULT_KEY_BARCODE));
                ean.setHint("");
            } else if (resultCode == BarcodeScannerActivity.ACTIVITY_RESULT_CODE_ERROR_CAMERA) {
                raiseError(getString(R.string.error_barcode_camera));
            } else {
                raiseError(getString(R.string.error_barcode_default));
            }
        }
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (ean.getText().length() == 0) {
            return null;
        }
        String eanStr = ean.getText().toString();
        if (eanStr.length() == 10 && !eanStr.startsWith("978")) {
            eanStr = "978" + eanStr;
        }
        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(eanStr)),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        String bookTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        if (bookTitle != null) {
            ((TextView) rootView.findViewById(R.id.bookTitle)).setText(bookTitle);
        }

        String bookSubTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        if (bookSubTitle != null) {
            ((TextView) rootView.findViewById(R.id.bookSubTitle)).setText(bookSubTitle);
        }

        String authors = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
        if (authors != null) {
            String[] authorsArr = authors.split(",");
            ((TextView) rootView.findViewById(R.id.authors)).setLines(authorsArr.length);
            ((TextView) rootView.findViewById(R.id.authors)).setText(authors.replace(",", "\n"));
        }

        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        if (imgUrl != null) {
            if (Patterns.WEB_URL.matcher(imgUrl).matches()) {
                new DownloadImage((ImageView) rootView.findViewById(R.id.bookCover)).execute(imgUrl);
                rootView.findViewById(R.id.bookCover).setVisibility(View.VISIBLE);
            }
        }

        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        if (categories != null) {
            ((TextView) rootView.findViewById(R.id.categories)).setText(categories);
        }

        rootView.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    private void clearFields() {
        ((TextView) rootView.findViewById(R.id.bookTitle)).setText("");
        ((TextView) rootView.findViewById(R.id.bookSubTitle)).setText("");
        ((TextView) rootView.findViewById(R.id.authors)).setText("");
        ((TextView) rootView.findViewById(R.id.categories)).setText("");
        rootView.findViewById(R.id.bookCover).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.delete_button).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(R.string.scan);
    }
}
