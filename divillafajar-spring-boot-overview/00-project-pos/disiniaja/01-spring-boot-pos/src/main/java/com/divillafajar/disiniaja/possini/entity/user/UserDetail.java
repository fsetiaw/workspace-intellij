package com.divillafajar.disiniaja.possini.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_detail")
public class UserDetail {

    // annotate the class as an entity and map to db table
    // define fields
    // annotate the fields with db column names
    // create constructer
    // create getter/setter

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "youtube_channel")
    private String youtubeChannel;

    @Column(name = "instagram")
    private String instagram;

    @Column(name = "tiktok")
    private String tiktok;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
