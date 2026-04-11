package ru.parovozik.backend.entity;


import jakarta.persistence.*;

@Table(name="Avatars")
@Entity
public class Avatar {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int avatarId;

    @Column
    private String link;

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Avatar() {}

    public Avatar(int avatarId, String link) {
        this.avatarId = avatarId;
        this.link = link;
    }
}
