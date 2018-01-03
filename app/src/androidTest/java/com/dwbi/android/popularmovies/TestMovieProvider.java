package com.dwbi.android.popularmovies;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.dwbi.android.popularmovies.model.Movie;
import com.dwbi.android.popularmovies.model.MovieContract;
import com.dwbi.android.popularmovies.model.MovieDbHelper;
import com.dwbi.android.popularmovies.model.MovieProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by PSX on 11/23/2017.
 */



/**
 * <p>
 * In this test suite, we have the following tests:
 * <p>
 *   1) A test to ensure that your ContentProvider has been properly registered in the
 *    AndroidManifest
 * <p>
 *   2) A test to determine if you've implemented the query functionality for your
 *    ContentProvider properly
 * <p>
 *   3) A test to determine if you've implemented the bulkInsert functionality of your
 *    ContentProvider properly
 * <p>
 *   4) A test to determine if you've implemented the delete functionality of your
 *    ContentProvider properly.
 * <p>
 * If any of these tests fail, you should see useful error messages in the testing console's
 * output window.
 * <p>
 * Finally, we have a method annotated with the @Before annotation, which tells the test runner
 * that the {@link #setUp()} method should be called before every method annotated with a @Test
 * annotation. In our setUp method, all we do is delete all records from the database to start our
 * tests with a clean slate each time.
 */
@RunWith(AndroidJUnit4.class)
public class TestMovieProvider {
    private final Context context = InstrumentationRegistry.getTargetContext();

    //----------------------------------------------------------------------------------------------
    @Before
    public void setUp(){
        deleteAllRecordsFromMovieTable();
    }

    //----------------------------------------------------------------------------------------------
    @Test
    public void testProviderRegistry(){
        String packageName = context.getPackageName();
        String movieProviderClassName = MovieProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, movieProviderClassName);

        try {
            PackageManager pm = context.getPackageManager();
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = MovieContract.CONTENT_AUTHORITY;

            /* Make sure that the registered authority matches the authority from the Contract */
            String incorrectAuthority = "Error: MovieProvider registered with authority: " + actualAuthority +
                            " instead of expected authority: " + expectedAuthority;
            assertEquals(incorrectAuthority,
                    actualAuthority,
                    expectedAuthority);

        } catch (PackageManager.NameNotFoundException e) {
            String providerNotRegisteredAtAll = "Error: MovieProvider not registered at " + context.getPackageName();
            fail(providerNotRegisteredAtAll);
        }


    }

    //----------------------------------------------------------------------------------------------
    @Test
    public void testBasicMovieQuery(){
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testMovieValues = TestUtilities.createTestMovieContentValues();

        long movieRowId = db.insert(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                testMovieValues
        );
        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, movieRowId != -1);
        db.close();

        Cursor movieCursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        TestUtilities.validateThenCloseCursor("testBasicMovieQuery", movieCursor, testMovieValues);


    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private void deleteAllRecordsFromMovieTable(){
        MovieDbHelper dbHelper = new MovieDbHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MovieContract.MovieEntry.TABLE_NAME, null, null);
        db.close();
    }
}
