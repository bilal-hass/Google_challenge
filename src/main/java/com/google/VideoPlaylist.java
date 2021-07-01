package com.google;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private final String name;
    private final ArrayList<Video> videos;

    public String getName() {
        return name;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void addVideo(Video video) {
        videos.add(video);
    }

    public void removeVideo(Video video) {
        videos.remove(video);
    }

    public void clear() {
        videos.clear();
    }

    public VideoPlaylist(String name) {
        this.name = name;
        this.videos = new ArrayList<>();
    }
}
