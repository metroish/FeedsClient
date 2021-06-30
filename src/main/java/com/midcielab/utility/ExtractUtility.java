package com.midcielab.utility;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import com.midcielab.model.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtractUtility {

    private static ExtractUtility instance = new ExtractUtility();
    private static final Logger logger = LogManager.getLogger();
    private XMLInputFactory factory;

    private ExtractUtility() {
        this.factory = XMLInputFactory.newInstance();
    }

    public static ExtractUtility getInstance() {
        return instance;
    }

    public Optional<List<Item>> extract(String input) {
        List<Item> itemLists = new ArrayList<>();
        Item item = new Item();
        try {
            XMLEventReader eventReader = factory.createXMLEventReader(
                    new BufferedInputStream(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
            while (eventReader.hasNext()) {
                XMLEvent xmlEvent = eventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    String startElement = xmlEvent.asStartElement().getName().getLocalPart().toLowerCase();
                    if (Optional.ofNullable(item).isEmpty()) {
                        item = new Item();
                    }
                    switch (startElement) {
                        case "title":
                            item.setTitle(eventReader.nextEvent().asCharacters().getData().trim());
                            break;
                        case "link":
                            item.setLink(eventReader.nextEvent().asCharacters().getData().trim());
                            break;
                        case "description":
                            item.setDescription(eventReader.nextEvent().asCharacters().getData().trim());
                            break;
                        case "pubdate":
                            item.setPubDate(eventReader.nextEvent().asCharacters().getData().trim());
                            break;
                        default:
                    }
                }
                if (xmlEvent.isEndElement()
                        && "item".equalsIgnoreCase(xmlEvent.asEndElement().getName().getLocalPart())) {
                    itemLists.add(item);
                    item = null;
                }
            }
            eventReader.close();
        } catch (Exception e) {
            logger.error("Parsing feed error", e);
        }
        return Optional.ofNullable(itemLists);
    }
}
