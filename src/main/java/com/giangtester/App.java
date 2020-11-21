package com.giangtester;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class App {
    private static final String priceTxt = "#product-price > div > span";
    private static final String imgLink = "//*[@id='main-content']/div[2]/div[2]/div[1]/figure[1]/div/img";

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.of(5, ChronoUnit.SECONDS));

        List<Product> list = new ArrayList<>();
        List<LinkProduct> linkProducts = readCsvInput(args[0]);
        for (LinkProduct aLink : linkProducts) {
            driver.get(aLink.getLink());
            String price = driver.findElement(By.cssSelector(priceTxt)).getText().substring(1);
            String img = driver.findElement(By.xpath(imgLink)).getAttribute("srcset").split(" ")[0];
            Product aProduct = new Product(aLink.getLink(), price, img);
            list.add(aProduct);
        }
        driver.quit();
        writeToFile(list, args[1]);
    }

    private static List<LinkProduct> readCsvInput(String arg) {
        File file = new File(arg);
        Path path = Paths.get(file.getAbsolutePath());
        List<LinkProduct> links = null;
        try (BufferedReader br = Files.newBufferedReader(path, UTF_8)) {
            CsvToBean<LinkProduct> csvToBean = new CsvToBeanBuilder<LinkProduct>(br)
                    .withType(LinkProduct.class)
                    .build();

            links = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }

    private static void writeToFile(List<Product> list, String filePath) {
        String fileName = LocalDateTime.now().toString();
        try {
            Writer writer = new FileWriter(filePath + "\\" + fileName + ".csv");
            StatefulBeanToCsv<Product> beanToCsv = new StatefulBeanToCsvBuilder<Product>(writer).build();
            beanToCsv.write(list);
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }
    }

}
