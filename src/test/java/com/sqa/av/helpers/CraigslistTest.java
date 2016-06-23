/**
 *   File Name: CraigslistTest.java<br>
 *
 *   Venkatraman, Arunkumar<br>
 *   Java Boot Camp Exercise<br>
 *   Instructor: Jean-francois Nepton<br>
 *   Created: Jun 21, 2016
 *   
 */

package com.sqa.av.helpers;

import java.io.*;

import org.apache.commons.io.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.*;
import org.testng.annotations.*;

/**
 * CraigslistTest //ADDD (description of class)
 * <p>
 * //ADDD (description of core fields)
 * <p>
 * //ADDD (description of core methods)
 * 
 * @author Venkatraman, Arunkumar
 * @version 1.0.0
 * @since 1.0
 *
 */
public class CraigslistTest extends BasicTest {
	private static int testNum = 1;

	@DataProvider
	public static Object[][] getData() {
		return new Object[][] { { "cobol automation player", 0, 0 }, { "java selenium", 5, 50 },
				{ "java junior", 5, 50 }, { "QA engineer", 200, 300 }, { "test developer", 5, 50 },
				{ "automation", 200, 300 }, { "selenium", 5, 50 } };
	}

	/**
	 * @param baseURL
	 */
	public CraigslistTest() {
		super("http://sf.craigslist.com");
	}

	@Test(dataProvider = "getData")
	public void craigslistTest(String keywords, int minExpectedResults, int maxExpectedResults)
			throws InterruptedException {
		// Start back up at the base URL
		getDriver().get(getBaseURL()); // compared to:
										// driver.get("http://mtv.com);
		// Get page element which is located
		WebElement searchField = getDriver().findElement(By.id("query"));
		// clear content inside search field
		searchField.clear();
		// Send input text into search field
		searchField.sendKeys(keywords);
		// Click on submit button
		// No submit present so press enter key
		searchField.sendKeys(Keys.RETURN);
		// OR Submit form by: searchField.submit();

		// Select the option to search in "jobs"
		WebElement categoryArea = getDriver().findElement(By.id("catAbb"));
		// Convert the WebElement to a selectCat object Select
		Select selectCat = new Select(categoryArea);
		// System.out.println("Options:" + selectCat.getOptions());

		// Select the object by visible text
		selectCat.selectByVisibleText("jobs");

		// Capture the text within the element which shows number of results
		String results = getDriver().findElement(By.cssSelector("span.button.pagenum")).getText();
		// System.out.println("Results:" + results);

		int totalResults;
		if (results.equalsIgnoreCase("no results")) {
			// Set to 0 as there are no results
			totalResults = 0;
		}

		// Check if there is a length of more than only one (which is no "of"
		// present scenario)
		else if (results.split("of").length > 1) { // eg., "1 to 100 of 3267"
													// elements "1 to 100 " and
													// " 3267"
			totalResults = Integer.parseInt(results.split("of")[1].trim());
			// System.out.println("Total Results:" + totalResults);

		}

		// eg: "1 to 68". elements: "1 " and " 68"
		else {
			totalResults = Integer.parseInt(results.split("to")[1].trim());
			// System.out.println("Total Results:" + totalResults);
		}

		// Capture a screenshot:
		// File set to the WebDriver instance, caster to a TakeScreenshotto make
		// getScreenshotAs available
		File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		try {
			// Store the screenshot in the folder "screenshots"
			// FileUtils.copyFile(src, new File("screenshots/" + keywords +
			// ".png"));
			FileUtils.copyFile(src, new File("screenshots/"
					+ getClass().getSimpleName().toLowerCase().replace("test", "") + "-" + testNum++ + ".png"));
		} catch (IOException e) {

		}

		// Assert that there is a set amount of result within the min and max
		// range
		Assert.assertTrue(totalResults >= minExpectedResults && totalResults <= maxExpectedResults,
				"Number of results(" + results + ") is not within the range of expected (" + minExpectedResults + " - "
						+ maxExpectedResults + ")");

	}
}
