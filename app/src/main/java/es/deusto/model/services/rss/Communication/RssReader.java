package es.deusto.model.services.rss.Communication;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import es.deusto.model.services.rss.handler.RssHandler;
import es.deusto.model.services.rss.handler.items.RssItem;

/**
 * Created by Sergio on 13/05/2016.
 */
public class RssReader {

    private String rssUrl;

    public RssReader(String url) {
        rssUrl = url;
    }

    public List<RssItem> getItems() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        //Creates a new RssHandler which will do all the parsing.
        RssHandler handler = new RssHandler();
        //Pass SaxParser the RssHandler that was created.
        saxParser.parse(rssUrl, handler);
        return handler.getRssItemList();
    }
}