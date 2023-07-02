package com.example.Quickr.Service;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QRCodeService {
    byte[] getQRCode(String text, int width, int height) throws WriterException, IOException;
}
