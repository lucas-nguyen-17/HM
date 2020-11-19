package com.giangtester;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class App {
    private static final String priceTxt = "#product-price > div > span";
    private static final String imgLink = "//*[@id='main-content']/div[2]/div[2]/div[1]/figure[1]/div/img";

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        String link = "https://www2.hm.com/en_gb/productpage.0878732001.html";
        driver.get(link);
        driver.manage().timeouts().implicitlyWait(Duration.of(5, ChronoUnit.SECONDS));
        String price = driver.findElement(By.cssSelector(priceTxt)).getText().substring(1);
        System.out.println("price = " + price);
        String img = driver.findElement(By.xpath(imgLink)).getAttribute("srcset").split(" ")[0];
        System.out.println("img = " + img);
        driver.quit();
        Product aProduct = new Product(link, price, img);
        List<Product> list = Collections.singletonList(aProduct);
        try {
            Writer writer = new FileWriter("yourfile.csv");
            StatefulBeanToCsv<Product> beanToCsv = new StatefulBeanToCsvBuilder<Product>(writer).build();
            beanToCsv.write(list);
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }
    }

}
