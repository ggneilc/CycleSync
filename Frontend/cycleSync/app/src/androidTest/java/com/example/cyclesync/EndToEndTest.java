package com.example.cyclesync;

import androidx.test.espresso.FailureHandler;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import androidx.test.platform.app.InstrumentationRegistry;
import java.util.Random;
import androidx.test.uiautomator.UiDevice;


import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EndToEndTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void createAccountLoginMakeBikeTest() {
        // Generate a unique timestamp
        String randomUUID = UUID.randomUUID().toString();

        // Use the timestamp to create unique IDs
        String uniqueName = "testUser" + randomUUID;
        String uniqueEmail = "testEmail" + randomUUID + "@example.com";
        String uniquePassword = "testPass" + randomUUID;

        // Click on Create Account button on the LoginActivity
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Enter unique values for name, email, and password in CreateAccountActivity
        onView(withId(R.id.editTextNewName)).perform(typeText(uniqueName), closeSoftKeyboard());
        onView(withId(R.id.editTextNewEmail)).perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextNewPassword)).perform(typeText(uniquePassword), closeSoftKeyboard());

        // Click the Create New Account button
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Waits until the editTextEmailLogin is visible
        waitUntilViewIsDisplayed(R.id.editTextEmailLogin);

        onView(withId(R.id.editTextEmailLogin)).perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextPasswordLogin)).perform(typeText(uniquePassword), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        // Waits until the settings option is visible
        waitUntilViewIsDisplayed(R.id.settings);

        onView(withId(R.id.settings)).perform(click());

        // Waits until the inventory is visible
        waitUntilViewIsDisplayed(R.id.inventory);

        onView(withId(R.id.inventory)).perform(click());

        // Waits until the add bike button is visible
        waitUntilViewIsDisplayed(R.id.addBike);

        onView(withId(R.id.addBike)).perform(click());

        String uniqueBikeName = "testBike" + randomUUID;
        String price = "3";
        String rating = "5/5";

        onView(withId(R.id.bikeName)).perform(typeText(uniqueBikeName), closeSoftKeyboard());
        onView(withId(R.id.bikePrice)).perform(typeText(price), closeSoftKeyboard());
        onView(withId(R.id.useCurrentLocation)).perform(click());
        // Wait for address to be fetched
        waitForNonEmptyText(R.id.bikeLocation, 30);
        onView(withId(R.id.bikeRating)).perform(typeText(rating), closeSoftKeyboard());

        // Click add bike button
        onView(withId(R.id.buttonCreateBike)).perform(click());

        // Waits until we're back at settings screen
        waitUntilViewIsDisplayed(R.id.addBike);

        // Find bike from name and click
        onView(withText(uniqueBikeName)).perform(click());

        // Wait for delete button
        waitUntilViewIsDisplayed(R.id.delete_inventory_item);
        // Delete bike
        onView(withId(R.id.delete_inventory_item)).perform(click());
    }
    @Test
    public void createAccountLoginMakeBikeLogoutCreateAccountRentBikeTest() throws InterruptedException {
        // Generate a unique timestamp
        String randomUUID = UUID.randomUUID().toString().substring(0, 5);

        // Use the timestamp to create unique IDs
        String uniqueName = "testUser" + randomUUID;
        String uniqueEmail = "testEmail" + randomUUID + "@example.com";
        String uniquePassword = "testPass" + randomUUID;

        // Click on Create Account button on the LoginActivity
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Enter unique values for name, email, and password in CreateAccountActivity
        onView(withId(R.id.editTextNewName)).perform(typeText(uniqueName), closeSoftKeyboard());
        onView(withId(R.id.editTextNewEmail)).perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextNewPassword)).perform(typeText(uniquePassword), closeSoftKeyboard());

        // Click the Create New Account button
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Waits until the editTextEmailLogin is visible
        waitUntilViewIsDisplayed(R.id.editTextEmailLogin);

        onView(withId(R.id.editTextEmailLogin)).perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextPasswordLogin)).perform(typeText(uniquePassword), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        // Waits until the settings option is visible
        waitUntilViewIsDisplayed(R.id.settings);

        onView(withId(R.id.settings)).perform(click());

        // Waits until the inventory is visible
        waitUntilViewIsDisplayed(R.id.inventory);

        onView(withId(R.id.inventory)).perform(click());

        // Waits until the add bike button is visible
        waitUntilViewIsDisplayed(R.id.addBike);

        onView(withId(R.id.addBike)).perform(click());

        String uniqueBikeName = "testBike" + randomUUID;
        Random random = new Random();
        int uniquePrice = 1000 + random.nextInt(9000); // This ensures the number is between 1000 and 9999, i.e., 4 digits

        // Calculate min and max prices based on the unique price
        int minPrice = uniquePrice - 3;
        int maxPrice = uniquePrice + 3;

        // Convert prices to strings
        String uniquePriceStr = Integer.toString(uniquePrice);
        String minPriceStr = Integer.toString(minPrice);
        String maxPriceStr = Integer.toString(maxPrice);
        String rating = "5/5";

        onView(withId(R.id.bikeName)).perform(typeText(uniqueBikeName), closeSoftKeyboard());
        onView(withId(R.id.bikePrice)).perform(typeText(uniquePriceStr), closeSoftKeyboard());
        onView(withId(R.id.useCurrentLocation)).perform(click());
        // Wait for address to be fetched
        waitForNonEmptyText(R.id.bikeLocation, 30);
        onView(withId(R.id.bikeRating)).perform(typeText(rating), closeSoftKeyboard());

        // Click add bike button
        Thread.sleep(3000);
        onView(withId(R.id.buttonCreateBike)).perform(click());

        // Waits until we're back at settings screen
        waitUntilViewIsDisplayed(R.id.addBike);

        waitUntilViewIsDisplayed(R.id.back_arrow_very_unique);
        onView(withId(R.id.back_arrow_very_unique)).perform(click());

        waitUntilViewIsDisplayed(R.id.back_arrow_very_unique);
        onView(withId(R.id.back_arrow_very_unique)).perform(click());

        scrollDown();
        onView(withId(R.id.logout)).perform(click());

        Thread.sleep(5000);


        String randomUUID2 = UUID.randomUUID().toString().substring(0, 5);

        // Use the timestamp to create unique IDs
        String uniqueName2 = "testUser" + randomUUID2;
        String uniqueEmail2 = "testEmail" + randomUUID2 + "@example.com";
        String uniquePassword2 = "testPass" + randomUUID2;

        // Click on Create Account button on the LoginActivity
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Enter unique values for name, email, and password in CreateAccountActivity
        onView(withId(R.id.editTextNewName)).perform(typeText(uniqueName2), closeSoftKeyboard());
        onView(withId(R.id.editTextNewEmail)).perform(typeText(uniqueEmail2), closeSoftKeyboard());
        onView(withId(R.id.editTextNewPassword)).perform(typeText(uniquePassword2), closeSoftKeyboard());

        Thread.sleep(1000);
        // Click the Create New Account button
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Waits until the editTextEmailLogin is visible
        waitUntilViewIsDisplayed(R.id.editTextEmailLogin);

        onView(withId(R.id.editTextEmailLogin)).perform(typeText(uniqueEmail2), closeSoftKeyboard());
        onView(withId(R.id.editTextPasswordLogin)).perform(typeText(uniquePassword2), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        // click on filter
        waitUntilViewIsDisplayed(R.id.arrow_extra_unique);
        onView(withId(R.id.arrow_extra_unique)).perform(click());

        //search for a unique bike with min max values and both switches pressed
        onView(withId(R.id.priceMin)).perform(typeText(minPriceStr), closeSoftKeyboard());
        onView(withId(R.id.priceMax)).perform(typeText(maxPriceStr), closeSoftKeyboard());

        onView(withId(R.id.filterApply)).perform(click());

        waitUntilViewIsDisplayed(R.id.arrow_extra_unique);
        onView(withId(R.id.arrow_extra_unique)).perform(click());
    }

    @Test
    public void filterTest() throws InterruptedException {
        // Waits until the editTextEmailLogin is visible
        waitUntilViewIsDisplayed(R.id.editTextEmailLogin);

        onView(withId(R.id.editTextEmailLogin)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.editTextPasswordLogin)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        waitUntilViewIsDisplayed(R.id.arrow_extra_unique);
        onView(withId(R.id.arrow_extra_unique)).perform(click());

        onView(withId(R.id.electric)).perform(click());
        onView(withId(R.id.classic)).perform(click());

        String minPriceStr = "0";
        String maxPriceStr = "10";


        onView(withId(R.id.priceMin)).perform(typeText(minPriceStr), closeSoftKeyboard());
        onView(withId(R.id.priceMax)).perform(typeText(maxPriceStr), closeSoftKeyboard());

        onView(withId(R.id.filterApply)).perform(click());

        Thread.sleep(2000);
    }

    @Test
    public void createAccountLogin() {
        // Generate a unique timestamp
        String randomUUID = UUID.randomUUID().toString();

        // Use the timestamp to create unique IDs
        String uniqueName = "testUser" + randomUUID;
        String uniqueEmail = "testEmail" + randomUUID + "@example.com";
        String uniquePassword = "testPass" + randomUUID;

        // Click on Create Account button on the LoginActivity
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Enter unique values for name, email, and password in CreateAccountActivity
        onView(withId(R.id.editTextNewName)).perform(typeText(uniqueName), closeSoftKeyboard());
        onView(withId(R.id.editTextNewEmail)).perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextNewPassword)).perform(typeText(uniquePassword), closeSoftKeyboard());

        // Click the Create New Account button
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Waits until the editTextEmailLogin is visible
        waitUntilViewIsDisplayed(R.id.editTextEmailLogin);

        onView(withId(R.id.editTextEmailLogin)).perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextPasswordLogin)).perform(typeText(uniquePassword), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());


    }

    @Test
    public void rentBikeTest() throws InterruptedException {
        // Waits until the editTextEmailLogin is visible
        waitUntilViewIsDisplayed(R.id.editTextEmailLogin);

        onView(withId(R.id.editTextEmailLogin)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.editTextPasswordLogin)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        // Wait for map to be ready
        Thread.sleep(3000);
        tapCenterOfScreen();
        Thread.sleep(3000);
        // Click rent now button
        onView(withId(R.id.buttonRentNow)).perform(click());

        // Wait start rental button
        waitUntilViewIsDisplayed(R.id.startRental);

        // Select payment method
        onView(withText("Default")).perform(click());

        // Start rental
        scrollDown();
        onView(withId(R.id.startRental)).perform(click());

        // Wait start (stop) rental button
        waitUntilViewIsDisplayed(R.id.buttonRentNow);
        Thread.sleep(3000);

        /* Messages */
        onView(withId(R.id.message_renter)).perform(click());

        // Wait for messages to load
        waitUntilViewIsDisplayed(R.id.messageInput);
        onView(withId(R.id.messageInput)).perform(typeText("test message!"), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.sendButton)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.back_arrow_very_unique)).perform(click());

        waitUntilViewIsDisplayed(R.id.buttonRentNow);

        // Stop Rental
        onView(withText("Stop Rental")).perform(click());

        // Wait for review screen
        waitUntilViewIsDisplayed(R.id.submit_review);

        // Set rating
        onView(withId(R.id.ratingBar)).perform(setRating(5.0f));

        // Fill in review
        onView(withId(R.id.review_text)).perform(typeText("Great bike!"), closeSoftKeyboard());

        onView(withId(R.id.submit_review)).perform(click());

        // Wait start rental button
        waitUntilViewIsDisplayed(R.id.buttonRentNow);

        // Go to settings
        waitUntilViewIsDisplayed(R.id.settings);

        onView(withId(R.id.settings)).perform(click());

        // Waits until the rental history is visible
        waitUntilViewIsDisplayed(R.id.rentalHistory);

        onView(withId(R.id.rentalHistory)).perform(click());

        // Wait until rental history loads
        Thread.sleep(5000);
        onView(withIndex(withId(R.id.rentalHistoryItemLayout), 0)).perform(click());
    }

    @Test
    public void addPaymentMethod() throws InterruptedException {
        // Waits until the editTextEmailLogin is visible
        waitUntilViewIsDisplayed(R.id.editTextEmailLogin);

        onView(withId(R.id.editTextEmailLogin)).perform(typeText("testEmail20231205121026@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextPasswordLogin)).perform(typeText("testPass20231205121026"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        // Go to settings
        waitUntilViewIsDisplayed(R.id.settings);

        onView(withId(R.id.settings)).perform(click());

        // Waits until the payment methods is visible
        waitUntilViewIsDisplayed(R.id.payment_methods);
        onView(withId(R.id.payment_methods)).perform(click());

        // Wait for add payment method button
        waitUntilViewIsDisplayed(R.id.add_payment_method);
        scrollDown();
        onView(withId(R.id.add_payment_method)).perform(click());

        // Wait for save payment method button
        waitUntilViewIsDisplayed(R.id.save_payment_method_button);
        onView(withId(R.id.payment_method_name)).perform(typeText(generateRandomString(10)), closeSoftKeyboard());
        onView(withId(R.id.card_holder_name)).perform(typeText("John Doe"), closeSoftKeyboard());
        onView(withId(R.id.card_number)).perform(typeText("1234123412341234"), closeSoftKeyboard());
        onView(withId(R.id.expiry_date)).perform(typeText("12/27"), closeSoftKeyboard());
        onView(withId(R.id.cvv)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.save_payment_method_button)).perform(click());

        waitUntilViewIsDisplayed(R.id.add_payment_method);
    }

    @Test
    public void renterMessaging() throws InterruptedException {
        // Waits until the editTextEmailLogin is visible
        waitUntilViewIsDisplayed(R.id.editTextEmailLogin);

        onView(withId(R.id.editTextEmailLogin)).perform(typeText("tp"), closeSoftKeyboard());
        onView(withId(R.id.editTextPasswordLogin)).perform(typeText("tp"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        // Go to settings
        waitUntilViewIsDisplayed(R.id.settings);

        onView(withId(R.id.settings)).perform(click());

        // Waits until the payment methods is visible
        waitUntilViewIsDisplayed(R.id.payment_methods);
        scrollDown();
        onView(withId(R.id.become_renter_setting)).perform(click());

        // Wait for add payment method button
        waitUntilViewIsDisplayed(R.id.messages);
        onView(withId(R.id.messages)).perform(click());

        // Wait for message option
        waitUntilViewIsDisplayed(R.id.chat_button);
        onView(withId(R.id.chat_button)).perform(click());

        // Wait for messages to load
        waitUntilViewIsDisplayed(R.id.messageInput);
        onView(withId(R.id.messageInput)).perform(typeText("test message!"), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.sendButton)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.back_arrow_very_unique)).perform(click());
    }

    private static void waitUntilViewIsDisplayed(int viewId) {
        final long startTime = System.currentTimeMillis();
        final long timeout = TimeUnit.SECONDS.toMillis(30); // Timeout of 30 seconds
        do {
            try {
                onView(withId(viewId)).check(matches(isDisplayed()));
                return;
            } catch (NoMatchingViewException | AssertionError e) {
                // Wait a bit before retrying
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            if (System.currentTimeMillis() - startTime >= timeout) {
                throw new AssertionError("View with id " + viewId + " is not displayed after " + timeout + " milliseconds.");
            }
        } while (true);
    }

    private static void waitForNonEmptyText(final int viewId, long timeoutInSeconds) {
        onView(withId(viewId))
                .withFailureHandler(new FailureHandler() {
                    @Override
                    public void handle(Throwable error, Matcher<View> viewMatcher) {
                        // Handle the case where the view is not found. For example
                        // you could throw an exception or log the error.
                    }
                })
                .check(new ViewAssertion() {
                    @Override
                    public void check(View view, NoMatchingViewException noViewFoundException) {
                        long startTime = SystemClock.elapsedRealtime();
                        long endTime = startTime + TimeUnit.SECONDS.toMillis(timeoutInSeconds);
                        do {
                            if (!(view instanceof TextView)) {
                                throw new IllegalArgumentException("The viewId is not of a TextView");
                            }
                            TextView textView = (TextView) view;
                            if (!TextUtils.isEmpty(textView.getText())) {
                                return;  // Text is now non-empty
                            }
                            if (SystemClock.elapsedRealtime() > endTime) {
                                throw new AssertionError("View with id " + viewId + " did not get non-empty text within " + timeoutInSeconds + " seconds.");
                            }
                            SystemClock.sleep(50);  // Wait a bit for the next check
                        } while (true);
                    }
                });
    }

    private void tapCenterOfScreen() {
        // Initialize UiDevice instance
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Get display width and height
        int displayWidth = uiDevice.getDisplayWidth();
        int displayHeight = uiDevice.getDisplayHeight();

        // Calculate center x and y coordinates
        int centerX = displayWidth / 2;
        int centerY = displayHeight / 2;

        // Perform a click at the center of the screen
        uiDevice.click(centerX, centerY);
    }

    public void scrollDown() throws InterruptedException {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Optional: Wait for the app to stabilize (e.g., loading content)
        uiDevice.waitForIdle();

        // Perform a swipe to scroll down
        int displayHeight = uiDevice.getDisplayHeight();
        int displayWidth = uiDevice.getDisplayWidth();

        int startX = displayWidth / 2;
        int startY = (int) (displayHeight * 0.8);  // Start swipe from 80% of the height (bottom of the screen)
        int endX = startX; // Swipe vertically
        int endY = (int) (displayHeight * 0.2);  // End swipe at 20% of the height (top of the screen)

        // Perform the swipe action
        uiDevice.swipe(startX, startY, endX, endY, 10);

        // Optional pause to see the scroll effect
        Thread.sleep(1000);

        // Continue with your other test actions...
    }

    public static ViewAction setRating(final float rating) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RatingBar.class);
            }

            @Override
            public String getDescription() {
                return "Set the rating on a RatingBar";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RatingBar ratingBar = (RatingBar) view;
                ratingBar.setRating(rating);
            }
        };
    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(final View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    public static String generateRandomString(int length) {
        // Generate a random UUID
        String uuid = UUID.randomUUID().toString();

        // Remove hyphens from the UUID to have a continuous string
        String uuidSanitized = uuid.replaceAll("-", "");

        // Ensure the length does not exceed the UUID's character length after hyphen removal
        length = Math.min(length, uuidSanitized.length());

        // Return a substring of the specified length
        return uuidSanitized.substring(0, length);
    }
}
