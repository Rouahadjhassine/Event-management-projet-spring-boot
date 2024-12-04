package com.example.eventmanagement.services;

import com.example.eventmanagement.models.EspaceEvenement;
import com.example.eventmanagement.models.Image;
import com.example.eventmanagement.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public void saveImages(MultipartFile[] files, EspaceEvenement espaceEvenement) throws IOException {
        for (MultipartFile file : files) {
            byte[] bytes = file.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(bytes);

            Image image = new Image();
            image.setData(encodedImage);
            image.setEspaceEvenement(espaceEvenement);
            imageRepository.save(image);
        }
    }

    public List<Image> getImagesByEspace(EspaceEvenement espaceEvenement) {
        return imageRepository.findByEspaceEvenement(espaceEvenement);
    }
}
