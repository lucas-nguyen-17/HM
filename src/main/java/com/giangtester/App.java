package com.giangtester;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
        String img = driver.findElement(By.xpath(imgLink)).getAttribute("srcset").split(" ")[0];
        Product aProduct = new Product(link, price, img);
        driver.quit();
        List<Product> list = Collections.singletonList(aProduct);
        writeToFile(list, args[1]);
    }

    private static void writeToFile(List<Product> list, String filePath) {
        try {
            Writer writer = new FileWriter(filePath + "\\yourfile.csv");
            StatefulBeanToCsv<Product> beanToCsv = new StatefulBeanToCsvBuilder<Product>(writer).build();
            beanToCsv.write(list);
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }
    }

}
