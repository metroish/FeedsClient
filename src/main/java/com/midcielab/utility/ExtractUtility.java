package com.midcielab.utility;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import com.midcielab.model.Item;

public class ExtractUtility {

    private static ExtractUtility instance = new ExtractUtility();
    private XMLInputFactory factory;
    private XMLEventReader eventReader;
    private XMLEvent xmlEvent;
    private List<Item> list = new ArrayList<Item>();
    private Item item;

    private ExtractUtility() {
        this.factory = XMLInputFactory.newInstance();
    }

    public static ExtractUtility getInstance() {
        return instance;
    }

    public List<Item> extract(String input) {
        try {

            this.eventReader = factory
                    .createXMLEventReader(new BufferedInputStream(new ByteArrayInputStream(input.getBytes("UTF-8"))));
            while (eventReader.hasNext()) {
                xmlEvent = eventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if ("item".equalsIgnoreCase(startElement.getName().getLocalPart())) {
                        item = new Item();
                    }
                    if (item != null) {
                        switch (startElement.getName().getLocalPart().toLowerCase()) {
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
                        }
                    }
                }
                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if ("item".equalsIgnoreCase(endElement.getName().getLocalPart())) {
                        list.add(item);
                        item = null;
                    }
                }
            }
            eventReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
