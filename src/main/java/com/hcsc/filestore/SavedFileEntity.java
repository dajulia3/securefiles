package com.hcsc.filestore;

import javax.annotation.Generated;
import javax.persistence.*;
import java.sql.Blob;

@Entity
public class SavedFileEntity {

    public SavedFileEntity(String name, byte[] data){
        this.name = name;
        this.data = data;
    }
    SavedFileEntity(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @Lob
    private byte[] data;

    private String contentType;


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
