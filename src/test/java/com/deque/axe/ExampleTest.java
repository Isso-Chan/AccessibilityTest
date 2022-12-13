/**
 * Copyright (C) 2015 Deque Systems Inc.,
 *
 * Your use of this Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * This entire copyright notice must appear in every copy of this file you
 * distribute or in any file that contains substantial portions of this source
 * code.
 */

package com.deque.axe;

import static org.junit.Assert.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;


public class ExampleTest {
	@Rule
	public TestName testName = new TestName();

	private WebDriver driver;

	private static final URL scriptUrl = ExampleTest.class.getResource("/axe.min.js");
	JSONArray violations;
	static File f;
	static BufferedWriter bw;

	/**
	 * Instantiate report before all tests start
	 */
	@BeforeClass
	public static void setUp1() {

		f = new File("Accessibility_Test-Report.htm");
		try {
			bw = new BufferedWriter(new FileWriter(f));
			bw.write("<html>");
			bw.write("<body>");
			bw.write("<img height=\"100\" width=\"200\" src = \"https://dynomapper.com/images/Accessibility_Testing2.jpg\">");
			bw.write("<h1>Accessibility Test Report for OAMan");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Instantiate the WebDriver and navigate to the test site
	 */
	@Before
	public void setUp() throws IOException {
		// ChromeDriver needed to test for Shadow DOM testing support
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	/**
	 * Ensure we close the WebDriver after finishing
	 */
	@After
	public void tearDown() throws IOException {
		driver.quit();
	}

	/**
	 * Ensure we close the report  after all tests finished
	 */
	@AfterClass
	public static void tearDown2() throws IOException {
		bw.write("</body>");
		bw.write("</html>");
		bw.close();
	}


	/**
	 * Basic test
	 */
	@Test
	public void testAccessibility(){
//		driver.get("http://localhost:5005");
		driver.get("https://www.ebay.de/");
		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl).analyze();

		violations = responseJSON.getJSONArray("violations");
		prepareReport(testName.getMethodName());

		if (violations.length() == 0) {
			assertTrue("No violations found", true);
		} else {
			AXE.writeResults(testName.getMethodName(), responseJSON);
			assertTrue(AXE.report(violations), false);
		}
	}

	//added later from https://www.youtube.com/watch?v=vKMnOSa8xcg
	/**
	 * This method works separately from other classes, after running, generates a report with "methodName.htm"
	 * @throws IOException
	 */
	@Test
	public void testAccessibility2() {
		driver.get("https://www.ebay.de/");
		JSONObject responseJSON = (new AXE.Builder(driver, scriptUrl)).analyze();
		violations = responseJSON.getJSONArray("violations");
		prepareReport(testName.getMethodName());

		if (violations.length() == 0) {
			Assert.assertTrue("No violations found", true);
		} else {
			AXE.writeResults(testName.getMethodName(), responseJSON);
			assertTrue(AXE.report(violations), false);
		}
//		WebDriverManager.chromedriver().setup();
//		ChromeOptions options = new ChromeOptions();
////		options.addArguments(new String[]{"headless"});
//		options.addArguments(new String[]{"window-size=1200x600"});
//		WebDriver driver = new ChromeDriver(options);
//		driver.get(URL);
//		driver.get("https://www.ebay.de/");
//		File f = new File(testName.getMethodName()+".htm");
//		prepareReport(f);
//		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//		bw.write("<html>");
//		bw.write("<body>");
//		bw.write("<img height=\"100\" width=\"200\" src = \"https://dynomapper.com/images/Accessibility_Testing2.jpg\">");
//		bw.write("<h1>Accessibility Report for " + driver.getCurrentUrl() + "</h1>");
//		JSONObject responseJSON = (new AXE.Builder(driver, scriptUrl)).analyze();
//		violations = responseJSON.getJSONArray("violations");
//prepareReport();
//		for(int i = 0; i < violations.length(); ++i) {
//			bw.write("<table border=\"1\">");
//			JSONObject jsonObject1 = violations.getJSONObject(i);
//			bw.write("<tr> <td> <p> <b>Impact</b> - " + jsonObject1.optString("impact") + "</p> </td> </tr>");
//			bw.write("<tr> <td> <p> <b>Help </b> -  " + jsonObject1.optString("help") + "</p> </td> </tr>");
//			bw.write("<tr> <td> <p> <b> Description</b> -  " + jsonObject1.optString("description") + "</p> </td> </tr>");
//			bw.write("<tr> <td> <p> <b> ID</b> -  " + jsonObject1.optString("id") + "</p> </td> </tr>");
//			bw.write("<tr> <td> <p> <b> Help Url</b> -  " + jsonObject1.optString("helpUrl") + "</p> </td> </tr>");
//			bw.write("</table>");
//			bw.write("</br>");
//		}
//
//		bw.write("</body>");
//		bw.write("</html>");
//		bw.close();
//		Desktop.getDesktop().browse(f.toURI());
//		driver.close();
//		if (violations.length() == 0) {
//			Assert.assertTrue("No violations found", true);
//		} else {
//			AXE.writeResults(testName.getMethodName(), responseJSON);
//			assertTrue(AXE.report(violations), false);
//		}
	}

	public void prepareReport(String testName) {
		try {
			bw.write("<tr> <td> <p> <b>TEST NAME</b> : "+testName+" </p> </td> </tr>");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for(int i = 0; i < violations.length(); ++i) {
			try {
				bw.write("<table border=\"1\">");
				JSONObject jsonObject1 = violations.getJSONObject(i);
				bw.write("<tr> <td> <p> <b>Impact</b> - " + jsonObject1.optString("impact") + "</p> </td> </tr>");
				bw.write("<tr> <td> <p> <b>Help </b> -  " + jsonObject1.optString("help") + "</p> </td> </tr>");
				bw.write("<tr> <td> <p> <b> Description</b> -  " + jsonObject1.optString("description") + "</p> </td> </tr>");
				bw.write("<tr> <td> <p> <b> ID</b> -  " + jsonObject1.optString("id") + "</p> </td> </tr>");
				bw.write("<tr> <td> <p> <b> Help Url</b> -  " + jsonObject1.optString("helpUrl") + "</p> </td> </tr>");
				bw.write("</table>");
				bw.write("</br>");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Test with skip frames
	 */
	@Test
	public void testAccessibilityWithSkipFrames() {
		driver.get("http://localhost:5005");
		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
				.skipFrames()
				.analyze();

		violations = responseJSON.getJSONArray("violations");
		prepareReport(testName.getMethodName());

		if (violations.length() == 0) {
			assertTrue("No violations found", true);
		} else {
			AXE.writeResults(testName.getMethodName(), responseJSON);
			assertTrue(AXE.report(violations), false);
		}
	}

	/**
	 * Test with options
	 */
	@Test
	public void testAccessibilityWithOptions() {
		driver.get("http://localhost:5005");
		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
				.options("{ rules: { 'accesskeys': { enabled: false } } }")
				.analyze();

		violations = responseJSON.getJSONArray("violations");
		prepareReport(testName.getMethodName());

		if (violations.length() == 0) {
			assertTrue("No violations found", true);
		} else {
			AXE.writeResults(testName.getMethodName(), responseJSON);

			assertTrue(AXE.report(violations), false);
		}
	}

	/**
	 * Test a specific selector or selectors
	 */
	@Test
	public void testAccessibilityWithSelector() {
//		driver.get("http://localhost:5005");
		driver.get("https://www.amazon.de/");
		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
				.include("title")
				.include("p")
				.analyze();

		violations = responseJSON.getJSONArray("violations");
		prepareReport(testName.getMethodName());

		if (violations.length() == 0) {
			assertTrue("No violations found", true);
		} else {
			AXE.writeResults(testName.getMethodName(), responseJSON);

			assertTrue(AXE.report(violations), false);
		}
	}

	/**
	 * Test includes and excludes
	 */
	@Test
	public void testAccessibilityWithIncludesAndExcludes() {
//		driver.get("http://localhost:5005/include-exclude.html");
		driver.get("https://www.amazon.de/");
		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
				.include("body")
				.exclude("h1")
				.analyze();

		violations = responseJSON.getJSONArray("violations");
		prepareReport(testName.getMethodName());

		if (violations.length() == 0) {
			assertTrue("No violations found", true);
		} else {
			AXE.writeResults(testName.getMethodName(), responseJSON);
			assertTrue(AXE.report(violations), false);
		}
	}

	/**
	 * Test a WebElement
	 */
	@Test
	public void testAccessibilityWithWebElement() {
//		driver.get("http://localhost:5005");
		driver.get("https://www.amazon.de/");
		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
				.analyze(driver.findElement(By.tagName("p")));

		violations = responseJSON.getJSONArray("violations");
		prepareReport(testName.getMethodName());

		if (violations.length() == 0) {
			assertTrue("No violations found", true);
		} else {
			AXE.writeResults(testName.getMethodName(), responseJSON);
			assertTrue(AXE.report(violations), false);
		}
	}

	/**
	 * Test a page with Shadow DOM violations
	 */
	@Test
	public void testAccessibilityWithShadowElement() {
		driver.get("http://localhost:5005/shadow-error.html");

		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl).analyze();

		violations = responseJSON.getJSONArray("violations");
		prepareReport(testName.getMethodName());

		JSONArray nodes = ((JSONObject)violations.get(0)).getJSONArray("nodes");
		JSONArray target = ((JSONObject)nodes.get(0)).getJSONArray("target");

		if (violations.length() == 1) {
//			assertTrue(AXE.report(violations), true);
			assertEquals(String.valueOf(target), "[[\"#upside-down\",\"ul\"]]");
		} else {
			AXE.writeResults(testName.getMethodName(), responseJSON);
			assertTrue("No violations found", false);

		}
	}

	@Test
	public void testAxeErrorHandling() {
//		driver.get("http://localhost:5005/");
		driver.get("https://www.amazon.de/");
		URL errorScript = ExampleTest.class.getResource("/axe-error.js");
		AXE.Builder builder = new AXE.Builder(driver, errorScript);

		boolean didError = false;

		try {
			builder.analyze();
		} catch (AXE.AxeRuntimeException e) {
			assertEquals(e.getMessage(), "boom!"); // See axe-error.js
			didError = true;
		}

		assertTrue("Did raise axe-core error", didError);
	}

	/**
	 * Test few include
	 */
	@Test
	public void testAccessibilityWithFewInclude() {
//		driver.get("http://localhost:5005/include-exclude.html");
		driver.get("https://www.ebay.de/");
		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
				.include("div")
				.include("p")
				.analyze();

		violations = responseJSON.getJSONArray("violations");
		prepareReport(testName.getMethodName());

		if (violations.length() == 0) {
			assertTrue("No violations found", true);
		} else {
			AXE.writeResults(testName.getMethodName(), responseJSON);
			assertTrue(AXE.report(violations), false);
		}
	}

	/**
	 * Test includes and excludes with violation
	 */
	@Test
	public void testAccessibilityWithIncludesAndExcludesWithViolation() {
//		driver.get("http://localhost:5005/include-exclude.html");
		driver.get("https://www.ebay.de/");
		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
				.include("body")
				.exclude("div")
				.analyze();

		violations = responseJSON.getJSONArray("violations");
		prepareReport(testName.getMethodName());

		JSONArray nodes = ((JSONObject)violations.get(0)).getJSONArray("nodes");
		JSONArray target = ((JSONObject)nodes.get(0)).getJSONArray("target");

		if (violations.length() == 1) {
			assertEquals(String.valueOf(target), "[\"span\"]");
		} else {
			AXE.writeResults(testName.getMethodName(), responseJSON);
			assertTrue("No violations found", false);
		}
	}


}

