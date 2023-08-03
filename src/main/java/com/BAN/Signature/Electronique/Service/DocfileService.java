package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Model.Docfile;
import com.BAN.Signature.Electronique.Repository.DocfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocfileService implements DocfileServiceInter {
    @Autowired
    DocfileRepository pdfFileRepository;

    @Override
    public Docfile Addpdffile(Docfile pdfFile) {
        return pdfFileRepository.save(pdfFile);
    }
}
