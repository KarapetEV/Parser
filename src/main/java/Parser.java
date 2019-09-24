import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser {
    private static List<String> projectLinks = new ArrayList<String>();
    private static String baseUrl = "http://fcpir.ru/participation_in_program/contracts/14.598.11.0098?PAGEN_1=";
    private static int count = 0;

    private static Document getPage(String url) throws IOException {

        Document page = Jsoup.connect(url).timeout(0).get();
        return page;
    }

    private static int getNumberOfPages(Element pagination) {
        Elements aElements = pagination.getElementsByTag("a");
        int pageNum = Integer.parseInt(aElements.get(aElements.size()-2).text());
        return pageNum;
    }

    private static void getLinks(Document page) {
        Element tbody = page.select("tbody").first();
        Elements tr = tbody.select("tr");
        for (Element row: tr) {
            Element td = row.select("td").first();
            String projectPage = "http://fcpir.ru" + td.getElementsByTag("a").attr("href");
            projectLinks.add(projectPage);
        }
    }

    private static void findFiles() throws IOException {
        for (String url: projectLinks) {
            Document page = Jsoup.connect(url).timeout(0).get();
            Elements aElements = page.select("a[class=panel-some-doc preview]");
            count += aElements.size();
        }
    }

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        System.out.println("Поехали!");
        Document page = getPage(baseUrl);
        String url = "";
        Element pagination = page.select("div[class=pagination]").first();
        int num = getNumberOfPages(pagination);
        for (int i = 1; i <= num; i++) {
            url = baseUrl + i;
            page = getPage(url);
            getLinks(page);
            findFiles();
            projectLinks.clear();
            System.out.print("Страница: " + i + " / Файлов: " + count + "\r");
        }
        long stop = System.currentTimeMillis();
        double time = stop - start;
        System.out.println("Количество файлов: " + count);
        getTime(time);
    }

    private static void getTime(double time) {

        int sec = (int) time/1000;
        int msc = (int) time - sec*1000;
        int min = sec / 60;
        sec = sec - min*60;
        System.out.println("Время выполнения: " + min + " минут " + sec + " секунд " + msc + " миллисекунд");
    }
}
