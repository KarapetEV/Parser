import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static List<String> projectLinks = new ArrayList<String>();
    private static String baseUrl = "http://fcpir.ru/participation_in_program/contracts/14.598.11.0098?PAGEN_1=";

    private static Document getPage(String url) throws IOException {

        Document page = Jsoup.parse(new URL(url), 3000);
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
            String projectPage = "http:/" + td.getElementsByTag("a").attr("href");
            projectLinks.add(projectPage);
        }
    }

    public static void main(String[] args) throws IOException {
        Document page = getPage(baseUrl);
        String url = "";
        Element pagination = page.select("div[class=pagination]").first();
        for (int i = 1; i <= getNumberOfPages(pagination); i++) {
            url = baseUrl + String.valueOf(i);
            page = getPage(url);
            getLinks(page);
        }

    }
}
