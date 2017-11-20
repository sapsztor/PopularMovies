package com.dwbi.android.popularmovies;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import com.dwbi.android.popularmovies.model.Movie;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dwbi.android.popularmovies.model.MovieContract.*;

/**
 * Created by PSX on 11/17/2017.
 */

public class TestUtilities {
    /* Nov 17st, 2017 12:30, GMT time */
    static final long DATE_NOMALIZED = 1510918169235L;
    static final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    /**
     * Ensures there is a non empty cursor and validates the cursor's data by checking it against
     * a set of expected values. This method will then close the cursor.
     *
     * @param error          Message when an error occurs
     * @param valueCursor    The Cursor containing the actual values received from an arbitrary query
     * @param expectedValues The values we expect to receive in valueCursor
     */
    static void validateThenCloseCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertNotNull(
                "This cursor is null. Did you make sure to register your ContentProvider in the manifest?",
                valueCursor);

        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    /**
     * This method iterates through a set of expected values and makes various assertions that
     * will pass if our app is functioning properly.
     *
     * @param error          Message when an error occurs
     * @param valueCursor    The Cursor containing the actual values received from an arbitrary query
     * @param expectedValues The values we expect to receive in valueCursor
     */
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);

            /* Test to see if the column is contained within the cursor */
            String columnNotFoundError = "Column '" + columnName + "' not found. " + error;
            assertFalse(columnNotFoundError, index == -1);

            /* Test to see if the expected value equals the actual value (from the Cursor) */
            String expectedValue = entry.getValue().toString();
            String actualValue = valueCursor.getString(index);

            String valuesDontMatchError = "Actual value '" + actualValue
                    + "' did not match the expected value '" + expectedValue + "'. "
                    + error;

            assertEquals(valuesDontMatchError,
                    expectedValue,
                    actualValue);
        }
    }

    /**
     * Used as a convenience method to return a singleton instance of ContentValues to populate
     * our database or insert using our ContentProvider.
     *
     * @return ContentValues that can be inserted into our ContentProvider or weather.db
     */

    /*
    private final String id;
    private final String title;
    private final String posterpath;
    private final String overview;
    private final String vote_average;
    private final String release_date;
     */
    static ContentValues createTestMovieContentValues() {

        Random r = new Random();
        int randomId = r.nextInt(1000)+1;


        ContentValues testMovieValues = new ContentValues();

        testMovieValues.put(MovieEntry.COLUMN_MOVIE_ID, randomId);
        testMovieValues.put(MovieEntry.COLUMN_MOVIE_TITLE, "Random title " + randomId);
        testMovieValues.put(MovieEntry.COLUMN_MOVIE_POSTERPATH, "Random posterpath " + randomId);
        testMovieValues.put(MovieEntry.COLUMN_MOVIE_OVERVIEW, "Random overview " + randomId);
        testMovieValues.put(MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, "Random vote average " + randomId);
        testMovieValues.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, "Random release date " + randomId);

        return testMovieValues;
    }

    /**
     * Used as a convenience method to return a singleton instance of an array of ContentValues to
     * populate our database or insert using our ContentProvider's bulk insert method.
     * <p>
     * It is handy to have utility methods that produce test values because it makes it easy to
     * compare results from ContentProviders and databases to the values you expect to receive.
     * See {@link #validateCurrentRecord(String, Cursor, ContentValues)} and
     * {@link #validateThenCloseCursor(String, Cursor, ContentValues)} for more information on how
     * this verification is performed.
     *
     * @return Array of ContentValues that can be inserted into our ContentProvider or weather.db
     */
    static ContentValues[] createBulkInsertTestWeatherValues() {

        ContentValues[] bulkTestMovieValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
        ArrayList<Movie> testData = new ArrayList<>();

        testData.add(new Movie ("311324",
                "The Great Wall",
                "/p70dq1YxabemdZDm5K6Q8G10wSn.jpg",
                "European mercenaries searching for black powder become embroiled in the defense of the Great Wall of China against a horde of monstrous creatures.",
                "5.8",
                "2016-12-16"));

        testData.add(new Movie ("954",
                "Mission: Impossible",
                "/vmj2PzTLC6xJvshpq8SlaYE3gbd.jpg",
                "When Ethan Hunt, the leader of a crack espionage team whose perilous operation has gone awry with no explanation, discovers that a mole has penetrated the CIA, he's surprised to learn that he's the No. 1 suspect. To clear his name, Hunt now must ferret out the real double agent and, in the process, even the score.",
                "6.7",
                "1996-05-22"));
        testData.add(new Movie ("47933",
                "Independence Day: Resurgence",
                "/5CHJs479xWnm3zMDOl94VkKS7MZ.jpg",
                "We always knew they were coming back. Using recovered alien technology, the nations of Earth have collaborated on an immense defense program to protect the planet. But nothing can prepare us for the aliens’ advanced and unprecedented force. Only the ingenuity of a few brave men and women can bring our world back from the brink of extinction.",
                "4.9",
                "2016-06-22"));
        testData.add(new Movie ("679",
                "Aliens",
                "/nORMXEkYEbzkU5WkMWMgRDJwjSZ.jpg",
                "When Ripley's lifepod is found by a salvage crew over 50 years later, she finds that terra-formers are on the very planet they found the alien species. When the company sends a family of colonists out to investigate her story, all contact is lost with the planet and colonists. They enlist Ripley and the colonial marines to return and search for answers.",
                "7.8",
                "1986-07-18"));
        testData.add(new Movie ("10764",
                "Quantum of Solace",
                "/6mlNx0Jiqhg3D7NlANT9pidsgPJ.jpg",
                "overview",
                "6.1",
                "2008-10-30"));
        testData.add(new Movie ("415214",
                "Operation Mekong",
                "/en7TculN8sgH9v165QZg4y2Q1kW.jpg",
                "Chinese narco-cops take their mission to the Golden Triangle following the Mekong River massacre of innocent fishermen by the region's drug lord.",
                "6.7",
                "2016-09-30"));
        testData.add(new Movie ("14161",
                "2012",
                "/5tIW9nTuxxy8iXSvsKJqFDVZpyg.jpg",
                "Dr. Adrian Helmsley, part of a worldwide geophysical team investigating the effect on the earth of radiation from unprecedented solar storms, learns that the earth's core is heating up. He warns U.S. President Thomas Wilson that the crust of the earth is becoming unstable and that without proper preparations for saving a fraction of the world's population, the entire race is doomed. Meanwhile, writer Jackson Curtis stumbles on the same information. While the world's leaders race to build \"arks\" to escape the impending cataclysm, Curtis struggles to find a way to save his family. Meanwhile, volcanic eruptions and earthquakes of unprecedented strength wreak havoc around the world.",
                "5.6",
                "2009-10-10"));
        testData.add(new Movie ("950",
                "Ice Age: The Meltdown",
                "/isRuztu5Ch7FJdtSBLcG8QSOpEI.jpg",
                "Diego, Manny and Sid return in this sequel to the hit animated movie Ice Age. This time around, the deep freeze is over, and the ice-covered earth is starting to melt, which will destroy the trio's cherished valley. The impending disaster prompts them to reunite and warn all the other beasts about the desperate situation.",
                "6.5",
                "2006-03-23"));
        testData.add(new Movie ("249164",
                "If I Stay",
                "/isUQKy3cdfN62DfIp0dhl4hD5oc.jpg",
                "Based on Gayle Forman's novel of the same name. \"If I Stay\" is the story of the gifted classical musician Mia and her boyfriend, Adam, an up and coming indie-rock star. Torn between two paths in life, her art or her relationship, Mia is forced to make an even starker choice between life and death when she is caught in a fatal car accident with her family one snowy morning in Oregon.",
                "7.3",
                "2014-08-21"));
        testData.add(new Movie ("2502",
                "The Bourne Supremacy",
                "/jXwZgmqOtsqsXuB9oGhocOAegCM.jpg",
                "When a CIA operation to purchase classified Russian documents is blown by a rival agent, who then shows up in the sleepy seaside village where Bourne and Marie have been living. The pair run for their lives and Bourne, who promised retaliation should anyone from his former life attempt contact, is forced to once again take up his life as a trained assassin to survive.",
                "7.2",
                "2004-07-23"));

        int i = 0;
        for (Movie m: testData) {
            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, m.getId());
            movieValues.put(MovieEntry.COLUMN_MOVIE_TITLE, m.getTitle());
            movieValues.put(MovieEntry.COLUMN_MOVIE_POSTERPATH, m.getPosterPath());
            movieValues.put(MovieEntry.COLUMN_MOVIE_OVERVIEW, m.getOverview());
            movieValues.put(MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, m.getVote_Average());
            movieValues.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, m.getRelease_Date());
            bulkTestMovieValues[i] = movieValues;
            i++;
        }
        return bulkTestMovieValues;
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

    /**
     * Students: The functions we provide inside of TestWeatherProvider use TestContentObserver to test
     * the ContentObserver callbacks using the PollingCheck class from the Android Compatibility
     * Test Suite tests.
     * <p>
     * NOTE: This only tests that the onChange function is called; it DOES NOT test that the
     * correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        /**
         * Called when a content change occurs.
         * <p>
         * To ensure correct operation on older versions of the framework that did not provide a
         * Uri argument, applications should also implement this method whenever they implement
         * the {@link #onChange(boolean, Uri)} overload.
         *
         * @param selfChange True if this is a self-change notification.
         */
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        /**
         * Called when a content change occurs. Includes the changed content Uri when available.
         *
         * @param selfChange True if this is a self-change notification.
         * @param uri        The Uri of the changed content, or null if unknown.
         */
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }


    }

    static String getConstantNameByStringValue(Class klass, String value)  {
        for (Field f : klass.getDeclaredFields()) {
            int modifiers = f.getModifiers();
            Class<?> type = f.getType();
            boolean isPublicStaticFinalString = Modifier.isStatic(modifiers)
                    && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)
                    && type.isAssignableFrom(String.class);

            if (isPublicStaticFinalString) {
                String fieldName = f.getName();
                try {
                    String fieldValue = (String) klass.getDeclaredField(fieldName).get(null);
                    if (fieldValue.equals(value)) return fieldName;
                } catch (IllegalAccessException e) {
                    return null;
                } catch (NoSuchFieldException e) {
                    return null;
                }
            }
        }

        return null;
    }

    static String getStaticStringField(Class clazz, String variableName)
            throws NoSuchFieldException, IllegalAccessException {
        Field stringField = clazz.getDeclaredField(variableName);
        stringField.setAccessible(true);
        String value = (String) stringField.get(null);
        return value;
    }

    static Integer getStaticIntegerField(Class clazz, String variableName)
            throws NoSuchFieldException, IllegalAccessException {
        Field intField = clazz.getDeclaredField(variableName);
        intField.setAccessible(true);
        Integer value = (Integer) intField.get(null);
        return value;
    }

    static String studentReadableClassNotFound(ClassNotFoundException e) {
        String message = e.getMessage();
        int indexBeforeSimpleClassName = message.lastIndexOf('.');
        String simpleClassNameThatIsMissing = message.substring(indexBeforeSimpleClassName + 1);
        simpleClassNameThatIsMissing = simpleClassNameThatIsMissing.replaceAll("\\$", ".");
        String fullClassNotFoundReadableMessage = "Couldn't find the class "
                + simpleClassNameThatIsMissing
                + ".\nPlease make sure you've created that class and followed the TODOs.";
        return fullClassNotFoundReadableMessage;
    }

    static String studentReadableNoSuchField(NoSuchFieldException e) {
        String message = e.getMessage();

        Pattern p = Pattern.compile("No field (\\w*) in class L.*/(\\w*\\$?\\w*);");

        Matcher m = p.matcher(message);

        if (m.find()) {
            String missingFieldName = m.group(1);
            String classForField = m.group(2).replaceAll("\\$", ".");
            String fieldNotFoundReadableMessage = "Couldn't find "
                    + missingFieldName + " in class " + classForField + "."
                    + "\nPlease make sure you've declared that field and followed the TODOs.";
            return fieldNotFoundReadableMessage;
        } else {
            return e.getMessage();
        }
    }


}
