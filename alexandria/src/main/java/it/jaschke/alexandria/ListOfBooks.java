package it.jaschke.alexandria;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import it.jaschke.alexandria.api.BookListAdapter;
import it.jaschke.alexandria.api.Callback;
import it.jaschke.alexandria.data.AlexandriaContract;


public class ListOfBooks extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private BookListAdapter bookListAdapter;
    private ListView bookList;
    private int position = ListView.INVALID_POSITION;
    private EditText searchText;
    // Edit JG: added ImageButton property
    private ImageButton searchButton;

    private final int LOADER_ID = 10;

    public ListOfBooks() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Edit JG: additional null-checks for view-objects; moved actions dealing with the
        // data-model on onViewCreated() so that the view can be displayed faster
        View rootView = inflater.inflate(R.layout.fragment_list_of_books, container, false);
        if (rootView != null) {
            if (rootView.findViewById(R.id.searchText) instanceof EditText) {
                searchText = (EditText) rootView.findViewById(R.id.searchText);
            }

            if (rootView.findViewById(R.id.listOfBooks) instanceof ListView) {
                bookList = (ListView) rootView.findViewById(R.id.listOfBooks);
            }

            if (rootView.findViewById(R.id.searchButton) instanceof ImageButton) {
                searchButton = (ImageButton) rootView.findViewById(R.id.searchButton);
            }
        }

        getActivity().setTitle(R.string.books);
        return rootView;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setup the booklist and its adapter
        Cursor cursor = getActivity().getContentResolver().query(
                AlexandriaContract.BookEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        if (cursor != null) {
            bookListAdapter = new BookListAdapter(getActivity(), cursor, 0);
            if (bookList != null) {
                bookList.setAdapter(bookListAdapter);
                bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Cursor cursor = bookListAdapter.getCursor();
                        if (cursor != null && cursor.moveToPosition(position)) {
                            ((Callback) getActivity())
                                    .onItemSelected(cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry._ID)));
                        }
                    }
                });
            }
        }

        if (searchButton != null) {
            searchButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ListOfBooks.this.restartLoader();
                        }
                    }
            );
        }
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        final String selection = AlexandriaContract.BookEntry.TITLE + " LIKE ? OR " + AlexandriaContract.BookEntry.SUBTITLE + " LIKE ? ";
        String searchString = searchText.getText().toString();

        if (searchString.length() > 0) {
            searchString = "%" + searchString + "%";
            return new CursorLoader(
                    getActivity(),
                    AlexandriaContract.BookEntry.CONTENT_URI,
                    null,
                    selection,
                    new String[]{searchString, searchString},
                    null
            );
        }

        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bookListAdapter.swapCursor(data);
        if (position != ListView.INVALID_POSITION) {
            bookList.smoothScrollToPosition(position);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookListAdapter.swapCursor(null);
    }

    // Edit JG: Set Title onResume instead of onAttach
    //@Override public void onResume() {
    //    super.onResume();
    //    if(getActivity() != null) {
    //        getActivity().setTitle(R.string.books);
    //    }
    //}
}
