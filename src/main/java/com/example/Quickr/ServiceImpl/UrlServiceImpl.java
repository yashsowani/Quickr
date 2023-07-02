package com.example.Quickr.ServiceImpl;

import com.example.Quickr.Entities.Url;
import com.example.Quickr.Repository.UrlRepository;
import com.example.Quickr.Service.BaseConversionService;
import com.example.Quickr.Service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private BaseConversionService baseConversionService;

    @Override
    public String convertToShortUrl(Url url) {
        Url result = urlRepository.save(url);
        return baseConversionService.encode(result.getId());
    }

    @Override
    public String getOriginalUrl(String shortUrl) {
        long id = baseConversionService.decode(shortUrl);
        Url entity = urlRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("There is no URL with " + shortUrl));

        if (entity.getExpiresDate() != null && entity.getExpiresDate().before(new Date())){
            urlRepository.delete(entity);
            throw new EntityNotFoundException("Link expired!");
        }

        return entity.getLongUrl();
    }
}
