package com.example.Quickr.Service;

import com.example.Quickr.Entities.Url;

public interface UrlService {

    String convertToShortUrl(Url url);

    String getOriginalUrl(String shortUrl);

}
