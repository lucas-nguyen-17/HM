package com.giangtester;

import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
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

    private static final String DOMAIN = "https://hm.delivery-status.com/uk/en/?orderNo=";
    private static final String LOCATOR_DELIVERY = "#pl-courier-fwd-link > div";
    private static final String LOCATOR_LATEST_DATE = "small";
    private static final String LOCATOR_LATEST_STATUS = "div.pl-checkpoint > div:nth-child(2) > b";
    private static final String LOCATOR_LATEST_ADDITIONAL_INFO = "div.pl-checkpoint > div:nth-child(3)";

    public static void main(String[] args) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.of(5, ChronoUnit.SECONDS));

        List<Tracking> list = new ArrayList<>();
        List<Order> orders = readCsvInput(args[0]);
        for (Order order : orders) {
            driver.get(DOMAIN + order.getId());
            String delivery = driver.findElement(By.cssSelector(LOCATOR_DELIVERY)).getText().split(" ")[1];
            String date = driver.findElements(By.tagName(LOCATOR_LATEST_DATE)).get(0).getText().replaceAll(",", "");
            String status = driver.findElements(By.cssSelector(LOCATOR_LATEST_STATUS)).get(0).getText();
            String additionalInfo = driver.findElements(By.cssSelector(LOCATOR_LATEST_ADDITIONAL_INFO)).get(0).getText();
            String info = status + " " + additionalInfo;
            Tracking aTracking = new Tracking(order.getId(), delivery, date, info);
            list.add(aTracking);
            Thread.sleep(2000);
        }
        driver.quit();
        writeToFile(list, args[1]);
    }

    private static List<Order> readCsvInput(String arg) {
        File file = new File(arg);
        Path path = Paths.get(file.getAbsolutePath());
        List<Order> links = null;
        try (BufferedReader br = Files.newBufferedReader(path, UTF_8)) {
            CsvToBean<Order> csvToBean = new CsvToBeanBuilder<Order>(br)
                    .withType(Order.class)
                    .build();

            links = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }

    private static void writeToFile(List<Tracking> list, String filePath) {
        String fileName = "Tracking_" + LocalDateTime.now().toString().replaceAll(":", "_");

        CustomMappingStrategy<Tracking> mappingStrategy =
                new CustomMappingStrategy<>();
        mappingStrategy.setType(Tracking.class);

        try {
            Writer writer = new FileWriter(filePath + "\\" + fileName + ".csv");
            StatefulBeanToCsv<Tracking> beanToCsv = new StatefulBeanToCsvBuilder<Tracking>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(mappingStrategy)
                    .build();
            beanToCsv.write(list);
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }
    }

}
