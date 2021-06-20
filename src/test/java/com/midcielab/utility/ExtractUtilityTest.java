package com.midcielab.utility;

import static org.junit.Assert.assertNotNull;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import com.midcielab.model.Item;

import org.junit.Test;

public class ExtractUtilityTest {
    @Test
    public void testExtract() {
        Optional<HttpResponse<String>> resp = HttpUtility.getInstance()
                .getUrlContent(ConfigUtility.getInsance().getConfig().getFeed().iterator().next().getUrl());
        if (resp.isPresent()) {
            List<Item> itemLists = ExtractUtility.getInstance().extract(resp.get().body().toString());
            System.out.println(itemLists);
            itemLists.forEach(obj -> {
                assertNotNull(obj.getTitle());
                assertNotNull(obj.getDescription());
                assertNotNull(obj.getPubDate());
                assertNotNull(obj.getLink());
            });
        }
    }
}
