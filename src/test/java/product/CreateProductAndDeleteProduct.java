package product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import Generic_Utility.Excel_Utility;
import Generic_Utility.File_Utility;
import Generic_Utility.Java_Utility;
import Generic_Utility.WebDriver_Utility;
import POM_Repo.CreateProductPage;
import POM_Repo.DeleteProduct;
import POM_Repo.HomePage;
import POM_Repo.LoginPage;
import POM_Repo.PrdLookUpImg;
import POM_Repo.ProductValidatePage;

public class CreateProductAndDeleteProduct {

	public static void main(String[] args) throws Throwable {
		File_Utility flib = new File_Utility();
		String BROWSER = flib.getKeyAndValuePair("browser");
		String URL = flib.getKeyAndValuePair("url");
		String USERNAME = flib.getKeyAndValuePair("username");
		String PASSWORD = flib.getKeyAndValuePair("password");

		WebDriver driver;

		if (BROWSER.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (BROWSER.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else if (BROWSER.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else {
			driver = new ChromeDriver();
		}
		WebDriver_Utility wlib = new WebDriver_Utility();
		wlib.maximizingWindow1(driver);
		wlib.waitForElementsToLoad(driver);

		driver.get(URL);

		LoginPage login = new LoginPage(driver);
		login.loginIntoApp(USERNAME, PASSWORD);

		HomePage home = new HomePage(driver);
		home.clickPrdLink();

		PrdLookUpImg img = new PrdLookUpImg(driver);
		img.clickPrdLoopUp();

		Java_Utility jlib = new Java_Utility();
		int ranNum = jlib.getRandomNum();
		// ----------------------------------------------------------------------------------------------------

		Excel_Utility elib = new Excel_Utility();
		String prdName = elib.readDataFromExcelSheet("Product", 0, 0) + ranNum;

		CreateProductPage page = new CreateProductPage(driver);
		page.prdDetails(prdName);

		ProductValidatePage validate = new ProductValidatePage(driver);
		validate.validateProduct(driver, prdName);
		// ------------------------------------------------------------------------------------------------------

		DeleteProduct delete = new DeleteProduct(driver);
		Thread.sleep(2000);
		delete.ClickPrdLink();
		delete.selectCheckBox(driver, prdName);
		delete.clickDeleteButton();
		Thread.sleep(2000);
		wlib.alertAccept(driver);
		delete.ValidatePrdDeleted(driver, prdName);
		home.logOutApp();

	}

}
