package com.example.springapi.models.entities;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity(name = "uploads")
@Table(name = "uploads")
public class Uploadfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "filename", nullable = false)
    private String filename;
    @Column(name = "bucket", nullable = false)
    private String bucket;
    @Column(name = "uploadtime", nullable = false)
    private ZonedDateTime uploadtime;
    @Column(name = "processed", nullable = false)
    private boolean processed;
    @Column(name = "size", nullable = false)
    private Long filesize;
    @Column(name = "uuidtag", nullable = false)
    private String uuid;
    @Column(name = "type", nullable = false)
    private String type;

    public Uploadfiles(String filename, ZonedDateTime uploadtime, boolean processed, Long filesize, String bucket, String uuid, String type) {
        this.filename = filename;
        this.uploadtime = uploadtime;
        this.processed = processed;
        this.filesize = filesize;
        this.bucket = bucket;
        this.uuid = uuid;
        this.type = type;
    }

    protected Uploadfiles(){}

}
