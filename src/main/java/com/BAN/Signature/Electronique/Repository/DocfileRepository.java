package com.BAN.Signature.Electronique.Repository;

import com.BAN.Signature.Electronique.Model.Docfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocfileRepository extends JpaRepository<Docfile,Long> {

}
