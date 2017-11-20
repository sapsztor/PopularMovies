package com.dwbi.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.dwbi.android.popularmovies.model.Movie;
import com.dwbi.android.popularmovies.model.MovieContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import static com.dwbi.android.popularmovies.TestUtilities.getConstantNameByStringValue;
import static com.dwbi.android.popularmovies.TestUtilities.getStaticIntegerField;
import static com.dwbi.android.popularmovies.TestUtilities.getStaticStringField;
import static com.dwbi.android.popularmovies.TestUtilities.studentReadableClassNotFound;
import static com.dwbi.android.popularmovies.TestUtilities.studentReadableNoSuchField;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by PSX on 11/19/2017.
 */

@RunWith(AndroidJUnit4.class)
public class TestMovieDatabase {
    private final Context context = InstrumentationRegistry.getTargetContext();

    private static final String packageName = "com.dwbi.android.popularmovies";
    private static final String dataPackageName = packageName + ".model";


    private Class movieEntryClass;
    private Class movieDbHelperClass;

    private static final String movieContractName = ".MovieContract";
    private static final String movieEntryName = movieContractName + "$MovieEntry";
    private static final String movieDbHelperName = ".MovieDbHelper";

    private static final String databaseNameVariableName = "DATABASE_NAME";
    private static String REFLECTED_DATABASE_NAME;

    private static final String databaseVersionVariableName = "DATABASE_VERSION";
    private static int REFLECTED_DATABASE_VERSION;

    private static final String tableNameVariableName = "TABLE_NAME";
    private static String REFLECTED_TABLE_NAME;

    private static final String columnMovieIdVariableName = "COLUMN_MOVIE_ID";
    static String REFLECTED_COLUMN_MOVIE_ID;

    private static final String columnTitleVariableName = "COLUMN_MOVIE_TITLE";
    static String REFLECTED_COLUMN_MOVIE_TITLE;

    private static final String columnPosterpathVariableName = "COLUMN_MOVIE_POSTERPATH";
    static String REFLECTED_COLUMN_MOVIE_POSTERPATH;

    private static final String columnOverviewVariableName = "COLUMN_MOVIE_OVERVIEW";
    static String REFLECTED_COLUMN_MOVIE_OVERVIEW;

    private static final String columnVoteAvereageVariableName = "COLUMN_MOVIE_VOTE_AVERAGE";
    static String REFLECTED_COLUMN_MOVIE_VOTE_AVERAGE;

    private static final String columnReleaseDateVariableName = "COLUMN_MOVIE_RELEASE_DATE";
    static String REFLECTED_COLUMN_MOVIE_RELEASE_DATE;

    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;

    @Before
    public void before() {
        try {

            movieEntryClass = Class.forName(dataPackageName + movieEntryName);
            if (!BaseColumns.class.isAssignableFrom(movieEntryClass)) {
                String movieEntryDoesNotImplementBaseColumns = "MovieEntry class needs to " +
                        "implement the interface BaseColumns, but does not.";
                fail(movieEntryDoesNotImplementBaseColumns);
            }

            REFLECTED_TABLE_NAME = getStaticStringField(movieEntryClass, tableNameVariableName);
            REFLECTED_COLUMN_MOVIE_ID = getStaticStringField(movieEntryClass, columnMovieIdVariableName );
            REFLECTED_COLUMN_MOVIE_TITLE = getStaticStringField(movieEntryClass, columnTitleVariableName);
            REFLECTED_COLUMN_MOVIE_POSTERPATH = getStaticStringField(movieEntryClass, columnPosterpathVariableName);
            REFLECTED_COLUMN_MOVIE_OVERVIEW = getStaticStringField(movieEntryClass, columnOverviewVariableName);
            REFLECTED_COLUMN_MOVIE_VOTE_AVERAGE = getStaticStringField(movieEntryClass, columnVoteAvereageVariableName);
            REFLECTED_COLUMN_MOVIE_RELEASE_DATE = getStaticStringField(movieEntryClass, columnReleaseDateVariableName);

            movieDbHelperClass = Class.forName(dataPackageName + movieDbHelperName);

            Class movieDbHelperSuperclass = movieDbHelperClass.getSuperclass();

            if (movieDbHelperSuperclass == null || movieDbHelperSuperclass.equals(Object.class)) {
                String noExplicitSuperclass =
                        "MovieDbHelper needs to extend SQLiteOpenHelper, but yours currently doesn't extend a class at all.";
                fail(noExplicitSuperclass);
            } else if (movieDbHelperSuperclass != null) {
                String movieDbHelperSuperclassName = movieDbHelperSuperclass.getSimpleName();
                String doesNotExtendOpenHelper =
                        "MovieDbHelper needs to extend SQLiteOpenHelper but yours extends "
                                + movieDbHelperSuperclassName;

                assertTrue(doesNotExtendOpenHelper,
                        SQLiteOpenHelper.class.isAssignableFrom(movieDbHelperSuperclass));
            }

            REFLECTED_DATABASE_NAME = getStaticStringField(
                    movieDbHelperClass, databaseNameVariableName);

            REFLECTED_DATABASE_VERSION = getStaticIntegerField(
                    movieDbHelperClass, databaseVersionVariableName);

            Constructor movieDbHelperCtor = movieDbHelperClass.getConstructor(Context.class);

            dbHelper = (SQLiteOpenHelper) movieDbHelperCtor.newInstance(context);

            context.deleteDatabase(REFLECTED_DATABASE_NAME);

            Method getWritableDatabase = SQLiteOpenHelper.class.getDeclaredMethod("getWritableDatabase");
            database = (SQLiteDatabase) getWritableDatabase.invoke(dbHelper);

        } catch (ClassNotFoundException e) {
            fail(studentReadableClassNotFound(e));
        } catch (NoSuchFieldException e) {
            fail(studentReadableNoSuchField(e));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        }
    }


    /**
     * This method tests that our database contains all of the tables that we think it should
     * contain.
     */
    @Test
    public void testCreateDb() {
        /*
         * Will contain the name of every table in our database. Even though in our case, we only
         * have only table, in many cases, there are multiple tables. Because of that, we are
         * showing you how to test that a database with multiple tables was created properly.
         */
        final HashSet<String> tableNameHashSet = new HashSet<>();

        /* Here, we add the name of our only table in this particular database */
        tableNameHashSet.add(REFLECTED_TABLE_NAME);
        /* Students, here is where you would add any other table names if you had them */
//        tableNameHashSet.add(MyAwesomeSuperCoolTableName);
//        tableNameHashSet.add(MyOtherCoolTableNameThatContainsOtherCoolData);

        /* We think the database is open, let's verify that here */
        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen());

        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'",
                null);

        /*
         * If tableNameCursor.moveToFirst returns false from this query, it means the database
         * wasn't created properly. In actuality, it means that your database contains no tables.
         */
        String errorInCreatingDatabase =
                "Error: This means that the database has not been created correctly";
        assertTrue(errorInCreatingDatabase,
                tableNameCursor.moveToFirst());

        /*
         * tableNameCursor contains the name of each table in this database. Here, we loop over
         * each table that was ACTUALLY created in the database and remove it from the
         * tableNameHashSet to keep track of the fact that was added. At the end of this loop, we
         * should have removed every table name that we thought we should have in our database.
         * If the tableNameHashSet isn't empty after this loop, there was a table that wasn't
         * created properly.
         */
        do {
            tableNameHashSet.remove(tableNameCursor.getString(0));
        } while (tableNameCursor.moveToNext());

        /* If this fails, it means that your database doesn't contain the expected table(s) */
        assertTrue("Error: Your database was created without the expected tables.",
                tableNameHashSet.isEmpty());

        /* Always close the cursor when you are finished with it */
        tableNameCursor.close();
    }


    @Test
    public void testDatabaseVersionWasIncremented() {
        int expectedDatabaseVersion = 1;
        String databaseVersionShouldBe1 = "Database version should be "
                + expectedDatabaseVersion + " but isn't."
                + "\n Database version: ";

        assertEquals(databaseVersionShouldBe1,
                expectedDatabaseVersion,
                REFLECTED_DATABASE_VERSION);
    }

    /**
     * Tests to ensure that inserts into your database results in automatically incrementing row
     * IDs and that row IDs are not reused.
     * <p>
     * If the INTEGER PRIMARY KEY column is not explicitly given a value, then it will be filled
     * automatically with an unused integer, usually one more than the largest _ID currently in
     * use. This is true regardless of whether or not the AUTOINCREMENT keyword is used.
     * <p>
     * If the AUTOINCREMENT keyword appears after INTEGER PRIMARY KEY, that changes the automatic
     * _ID assignment algorithm to prevent the reuse of _IDs over the lifetime of the database.
     * In other words, the purpose of AUTOINCREMENT is to prevent the reuse of _IDs from previously
     * deleted rows.
     * <p>
     * To test this, we first insert a row into the database and get its _ID. Then, we'll delete
     * that row, change the data that we're going to insert, and insert the changed data into the
     * database again. If AUTOINCREMENT isn't set up properly in the WeatherDbHelper's table
     * create statement, then the _ID of the first insert will be reused. However, if AUTOINCREMENT
     * is setup properly, that older ID will NOT be reused, and the test will pass.
     */
    @Test
    public void testDuplicateDateInsertBehaviorShouldReplace() {

        /* Obtain movie values from TestUtilities */
        ContentValues testMovieValues = TestUtilities.createTestMovieContentValues();
        Log.d("PSX", "testMovieValues.toString() 1-> " + testMovieValues.toString());

        /*
         * Get the original movie ID of the testMovieValues to ensure we use a different
         * movie ID for our next insert.
         */
        //long originalId = testMovieValues.getAsLong(REFLECTED_COLUMN_MOVIE_ID);
        String originalTitle = testMovieValues.getAsString(REFLECTED_COLUMN_MOVIE_TITLE);
        Log.d("PSX", "TestMovieDatabase.testDuplicateDateInsertBehaviorShouldReplace originalTitle-> " + originalTitle);

        /* Insert the ContentValues with old movie ID into database */
        database.insert(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                testMovieValues);

        /*
         * We don't really care what this ID is, just that it is different than the original and
         * that we can use it to verify our "new" movie entry has been made.
         */
        //long newId = originalId + 1;
        String newTitle = originalTitle + "_new";

        testMovieValues.put(REFLECTED_COLUMN_MOVIE_TITLE, newTitle);
        Log.d("PSX", "testMovieValues.toString() 2-> " + testMovieValues.toString());

        /* Insert the ContentValues with new movie ID into database */
        database.insert(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                testMovieValues);

        /* Query for a movie record with our new movie ID */
        Cursor newMovieIdCursor = database.query(
                REFLECTED_TABLE_NAME,
                new String[]{REFLECTED_COLUMN_MOVIE_TITLE},
                null,
                null,
                null,
                null,
                null);

        String recordWithNewIdNotFound = "New record did not overwrite the previous record for the same date.";
        dumpRecord(newMovieIdCursor);
        int curCount = newMovieIdCursor.getCount();
        Log.d("PSX", "TestMovieDatabase.testDuplicateDateInsertBehaviorShouldReplace curCount-> " + curCount);
        assertTrue(recordWithNewIdNotFound, newMovieIdCursor.getCount() == 1);

        /* Always close the cursor after you're done with it */
        newMovieIdCursor.close();
    }

    @Test
    public void testNullColumnConstraints() {
        /* Use a MovieDbHelper to get access to a writable database */

        /* We need a cursor from a movie table query to access the column names */
        Cursor movieTableCursor = database.query(
                REFLECTED_TABLE_NAME,
                /* We don't care about specifications, we just want the column names */
                null, null, null, null, null, null);

        /* Store the column names and close the cursor */
        String[] movieTableColumnNames = movieTableCursor.getColumnNames();
        movieTableCursor.close();

        /* Obtain weather values from TestUtilities and make a copy to avoid altering singleton */
        ContentValues testValues = TestUtilities.createTestMovieContentValues();
        /* Create a copy of the testValues to save as a reference point to restore values */
        ContentValues testValuesReferenceCopy = new ContentValues(testValues);

        for (String columnName : movieTableColumnNames) {

            /* We don't need to verify the _ID column value is not null, the system does */
            if (columnName.equals(MovieContract.MovieEntry._ID)) continue;

            /* Set the value to null */
            testValues.putNull(columnName);

            /* Insert ContentValues into database and get a row ID back */
            long shouldFailRowId = database.insert(
                    REFLECTED_TABLE_NAME,
                    null,
                    testValues);

            String variableName = getConstantNameByStringValue(
                    MovieContract.MovieEntry.class,
                    columnName);

            /* If the insert fails, which it should in this case, database.insert returns -1 */
            String nullRowInsertShouldFail =
                    "Insert should have failed due to a null value for column: '" + columnName + "'"
                            + ", but didn't."
                            + "\n Check that you've added NOT NULL to " + variableName
                            + " in your create table statement in the MovieEntry class."
                            + "\n Row ID: ";
            assertEquals(nullRowInsertShouldFail,
                    -1,
                    shouldFailRowId);

            /* "Restore" the original value in testValues */
            testValues.put(columnName, testValuesReferenceCopy.getAsDouble(columnName));
        }

        /* Close database */
        dbHelper.close();
    }

    /**
     * Tests to ensure that inserts into your database results in automatically
     * incrementing row IDs.
     */
    @Test
    public void testIntegerAutoincrement() {

        /* First, let's ensure we have some values in our table initially */
        testInsertSingleRecordIntoMovieTable();

        /* Obtain weather values from TestUtilities */
        ContentValues testMovieValues = TestUtilities.createTestMovieContentValues();

        /* Get the TMDB ID of the testMovieValues to ensure we use a different row id later */
        //long originalDate = testWeatherValues.getAsLong(REFLECTED_COLUMN_DATE);
        int originalMovieId =  testMovieValues.getAsInteger(REFLECTED_COLUMN_MOVIE_ID);

        /* Insert ContentValues into database and get a row ID back */
        long firstRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testMovieValues);

        /* Delete the row we just inserted to see if the database will reuse the rowID */
        database.delete(
                REFLECTED_TABLE_NAME,
                "_ID == " + firstRowId,
                null);

        /*
         * Now we need to change the date associated with our test content values because the
         * database policy is to replace identical movie id on conflict.
         */
        int secondMovieId = originalMovieId + 1;

        testMovieValues.put(REFLECTED_COLUMN_MOVIE_ID, secondMovieId);

        /* Insert ContentValues into database and get another row ID back */
        long secondRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testMovieValues);

        String sequentialInsertsDoNotAutoIncrementId =
                "IDs were reused and shouldn't be if autoincrement is setup properly.";
        assertNotSame(sequentialInsertsDoNotAutoIncrementId,
                firstRowId, secondRowId);
    }


    /**
     * This method tests the {@link com.dwbi.android.popularmovies.model.MovieDbHelper#onUpgrade(SQLiteDatabase, int, int)} (SQLiteDatabase, int, int)}. The proper
     * behavior for this method in our case is to simply DROP (or delete) the weather table from
     * the database and then have the table recreated.
     */
    @Test
    public void testOnUpgradeBehavesCorrectly() {

        testInsertSingleRecordIntoMovieTable();

        dbHelper.onUpgrade(database, 13, 14);

        /*
         * This Cursor will contain the names of each table in our database and we will use it to
         * make sure that our weather table is still in the database after upgrading.
         */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" + REFLECTED_TABLE_NAME + "'",
                null);

        /*
         * Our database should only contain one table, and so the above query should have one
         * record in the cursor that queried for our table names.
         */
        int expectedTableCount = 1;
        String shouldHaveSingleTable = "There should only be one table returned from this query.";
        assertEquals(shouldHaveSingleTable,
                expectedTableCount,
                tableNameCursor.getCount());

        /* We are done verifying our table names, so we can close this cursor */
        tableNameCursor.close();

        Cursor shouldBeEmptyMovieCursor = database.query(
                REFLECTED_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        int expectedRecordCountAfterUpgrade = 0;
        /* We will finally verify that our weather table is empty after */
        String movieTableShouldBeEmpty = "Weather table should be empty after upgrade, but wasn't."  + "\nNumber of records: ";
        assertEquals(movieTableShouldBeEmpty,
                expectedRecordCountAfterUpgrade,
                shouldBeEmptyMovieCursor.getCount());

        /* Test is over, close the cursor */
        database.close();
    }

    /**
     * This method tests inserting a single record into an empty table from a brand new database.
     * It will fail for the following reasons:
     * <p>
     * 1) Problem creating the database
     * 2) A value of -1 for the ID of a single, inserted record
     * 3) An empty cursor returned from query on the weather table
     * 4) Actual values of weather data not matching the values from TestUtilities
     */
    @Test
    public void testInsertSingleRecordIntoMovieTable() {

        /* Obtain weather values from TestUtilities */
        ContentValues testMovieValues = TestUtilities.createTestMovieContentValues();

        /* Insert ContentValues into database and get a row ID back */
        long weatherRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testMovieValues);

        /* If the insert fails, database.insert returns -1 */
        int valueOfIdIfInsertFails = -1;
        String insertFailed = "Unable to insert into the database";
        assertNotSame(insertFailed,
                valueOfIdIfInsertFails,
                weatherRowId);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor movieCursor = database.query(
                /* Name of table on which to perform the query */
                REFLECTED_TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from weather query";
        assertTrue(emptyQueryError,
                movieCursor.moveToFirst());

        /* Verify that the returned results match the expected results */
        String expectedWeatherDidntMatchActual =
                "Expected weather values didn't match actual values.";
        TestUtilities.validateCurrentRecord(expectedWeatherDidntMatchActual,
                movieCursor,
                testMovieValues);

        /*
         * Since before every method annotated with the @Test annotation, the database is
         * deleted, we can assume in this method that there should only be one record in our
         * Weather table because we inserted it. If there is more than one record, an issue has
         * occurred.
         */
        assertFalse("Error: More than one record returned from weather query",
                movieCursor.moveToNext());

        /* Close cursor */
        movieCursor.close();
    }
    //----------------------------------------------------------------------------------------------
    void dumpRecord(Cursor cur) {
        cur.moveToFirst();
        String[] columns = cur.getColumnNames();
        for(String s : columns){
            Log.d("PSX", "col-> " + s);
        }
        String id = cur.getString(0);
        Log.d("PSX", "cur.tmdb_movie_id-> " + id);
    }
}
