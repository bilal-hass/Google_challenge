package com.google;

import java.util.*;

public class VideoPlayer
{

    private final VideoLibrary videoLibrary;
    private Video currentlyPlaying;
    private HashMap<String, VideoPlaylist> playlists;
    private boolean isPaused;
    private final Random random;
    private final HashMap<Video, String> flags;

    public VideoPlayer()
    {
        this.videoLibrary = new VideoLibrary();
        this.playlists = new HashMap<>();
        this.random = new Random();
        this.isPaused = true;
        flags = new HashMap<>();
    }

    public void numberOfVideos()
    {
        System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
    }

    public void showAllVideos()
    {
        List<Video> sortedVideos = videoLibrary.getVideos();
        sortedVideos.sort(Comparator.comparing(Video::getTitle));

        System.out.println("Here's a list of all available videos:");
        for (Video video : sortedVideos) System.out.println(videoDetails(video));
    }

    public void playVideo(String videoId)
    {
        Video video = videoLibrary.getVideo(videoId);
        if (video == null)
        {
            System.out.println("Cannot play video: Video does not exist");
        }
        else if (flags.containsKey(video))
        {
            System.out.println("Cannot play video: Video is currently flagged (reason: " + flags.get(video) + ")");
        }
        else if (currentlyPlaying == null)
        {
            currentlyPlaying = video;
            System.out.println("Playing video: " + video.getTitle());
            isPaused = false;
        }
        else
        {
            System.out.println("Stopping video: " + currentlyPlaying.getTitle());
            System.out.println("Playing video: " + video.getTitle());
            currentlyPlaying = video;
            isPaused = false;
        }
    }

    public void stopVideo()
    {
        try
        {
            System.out.println("Stopping video: " + currentlyPlaying.getTitle());
            currentlyPlaying = null;
        } catch (NullPointerException n)
        {
            System.out.println("Cannot stop video: No video is currently playing");
        }
    }

    public void playRandomVideo()
    {
        List<Video> videos = videoLibrary.getVideos();
        videos.removeAll(flags.keySet());
        if (videos.isEmpty())
        {
            System.out.println("No videos available");
            return;
        }
        Video video = videos.get(random.nextInt(videos.size()));
        playVideo(video.getVideoId());
    }

    public void pauseVideo()
    {
        try
        {
            if (!isPaused)
            {
                System.out.println("Pausing video: " + currentlyPlaying.getTitle());
                isPaused = true;
            }
            else
            {
                System.out.println("Video already paused: " + currentlyPlaying.getTitle());
            }
        } catch (NullPointerException n)
        {
            System.out.println("Cannot pause video: No video is currently playing");
        }
    }

    public void continueVideo()
    {
        try
        {
            if (isPaused)
            {
                System.out.println("Continuing video: " + currentlyPlaying.getTitle());
            }
            else
            {
                System.out.println("Cannot continue video: Video is not paused");
            }
        } catch (NullPointerException n)
        {
            System.out.println("Cannot continue video: No video is currently playing");
        }
    }

    public void showPlaying()
    {
        try
        {
            System.out.println("Currently playing: " + videoDetails(currentlyPlaying) + (isPaused ? " - PAUSED" : ""));
        } catch (NullPointerException n)
        {
            System.out.println("No video is currently playing");
        }
    }

    public void createPlaylist(String playlistName)
    {
        for (String playlist : playlists.keySet())
        {
            if (playlist.equalsIgnoreCase(playlistName))
            {
                System.out.println("Cannot create playlist: A playlist with the same name already exists");
                return;
            }
        }
        // key to lower to avoid case sensitivity when searching, original name in videoplaylist object
        playlists.put(playlistName.toLowerCase(), new VideoPlaylist(playlistName));
        System.out.println("Successfully created new playlist: " + playlistName);
    }

    public void addVideoToPlaylist(String playlistName, String videoId)
    {
        VideoPlaylist videoPlaylist = playlists.get(playlistName.toLowerCase());
        Video video = videoLibrary.getVideo(videoId);

        if (videoPlaylist == null)
        {
            System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
        }
        else if (video == null)
        {
            System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
        }
        else if (flags.containsKey(video))
        {
            System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + flags.get(video) + ")");
        }
        else
        {
            if (videoPlaylist.getVideos().contains(video))
            {
                System.out.println("Cannot add video to " + playlistName + ": Video already added");
            }
            else
            {
                videoPlaylist.addVideo(video);
                System.out.println("Added video to " + playlistName + ": Amazing Cats");
            }
        }
    }

    public void showAllPlaylists()
    {
        if (playlists.isEmpty())
        {
            System.out.println("No playlists exist yet");
        }
        else
        {
            System.out.println("Showing all playlists:");
            SortedSet<String> names = new TreeSet<>(playlists.keySet());
            for (String name : names)
            {
                VideoPlaylist vp = playlists.get(name);
                System.out.println(vp.getName() + " (" + vp.getVideos().size() + "videos)");
            }
        }
    }

    public void showPlaylist(String playlistName)
    {
        VideoPlaylist playlist = playlists.get(playlistName.toLowerCase());
        if (playlist == null)
        {
            System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
            return;
        }
        System.out.println("Showing playlist: " + playlistName);
        if (playlist.getVideos().isEmpty())
        {
            System.out.println("No videos here yet");
        }
        else
        {
            for (Video video : playlist.getVideos())
            {
                System.out.println(videoDetails(video));
            }

        }
    }

    public void removeFromPlaylist(String playlistName, String videoId)
    {
        VideoPlaylist videoPlaylist = playlists.get(playlistName.toLowerCase());
        Video video = videoLibrary.getVideo(videoId);

        if (videoPlaylist == null)
            System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
        else if (video == null)
            System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
        else if (!videoPlaylist.getVideos().contains(video))
            System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
        else
        {
            videoPlaylist.removeVideo(video);
            System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
        }
    }

    public void clearPlaylist(String playlistName)
    {
        try
        {
            VideoPlaylist videoPlaylist = playlists.get(playlistName.toLowerCase());
            videoPlaylist.clear();
            System.out.println("Successfully removed all videos from " + playlistName);
        } catch (NullPointerException n)
        {
            System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
        }
    }

    public void deletePlaylist(String playlistName)
    {
        VideoPlaylist videoPlaylist = playlists.get(playlistName.toLowerCase());
        if (videoPlaylist == null)
        {
            System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
        }
        else
        {
            playlists.remove(videoPlaylist);
            System.out.println("Deleted playlist: " + playlistName);
        }
    }

    public void searchVideos(String searchTerm)
    {
        ArrayList<Video> matches = new ArrayList<>();
        for (Video video : videoLibrary.getVideos())
        {
            if (video.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) && !flags.containsKey(video))
            {
                matches.add(video);
            }
        }
        matches.sort(Comparator.comparing(Video::getTitle));
        if (matches.isEmpty())
        {
            System.out.println("No search results for " + searchTerm);
            return;
        }
        int count = 1;
        System.out.println("Here are the results for " + searchTerm + ":");
        for (Video match : matches)
        {
            System.out.println(
                    count++ + ") " + videoDetails(match)
            );
        }
        System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
        System.out.println("If your answer is not a valid number, we will assume it's a no.");
        try
        {
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            playVideo(matches.get(--choice).getVideoId());
        } catch (InputMismatchException | IndexOutOfBoundsException ignored)
        {
        }
    }

    public void searchVideosWithTag(String videoTag)
    {
        ArrayList<Video> matches = new ArrayList<>();
        for (Video video : videoLibrary.getVideos())
        {
            for (String tag : video.getTags())
            {
                if (tag.equalsIgnoreCase(videoTag) && !flags.containsKey(video)) matches.add(video);
            }
        }
        matches.sort(Comparator.comparing(Video::getTitle));
        if (matches.isEmpty())
        {
            System.out.println("No search results for " + videoTag);
            return;
        }
        int count = 1;
        System.out.println("Here are the results for " + videoTag + ":");
        for (Video match : matches)
        {
            System.out.println(
                    count++ + ") " + videoDetails(match)
            );
        }
        System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
        System.out.println("If your answer is not a valid number, we will assume it's a no.");
        try
        {
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            playVideo(matches.get(--choice).getVideoId());
        } catch (InputMismatchException | IndexOutOfBoundsException ignored)
        {
        }
    }

    public void flagVideo(String videoId)
    {
        Video video = videoLibrary.getVideo(videoId);

        if (video == null)
        {
            System.out.println("Cannot flag video: Video does not exist");
            return;
        }
        if (flags.containsKey(video))
        {
            System.out.println("Cannot flag video: Video is already flagged");
            return;
        }

        flags.put(video, "Not supplied");
        System.out.println(
                "Successfully flagged video: "
                        + video.getTitle() + " (reason: "
                        + flags.get(video) + ")"
        );
    }

    public void flagVideo(String videoId, String reason)
    {
        Video video = videoLibrary.getVideo(videoId);

        if (video == null)
        {
            System.out.println("Cannot flag video: Video does not exist");
            return;
        }
        if (flags.containsKey(video))
        {
            System.out.println("Cannot flag video: Video is already flagged");
            return;
        }

        flags.put(video, reason);
        if (currentlyPlaying == video) stopVideo();
        System.out.println(
                "Successfully flagged video: "
                        + video.getTitle() + " (reason: "
                        + flags.get(video) + ")"
        );
    }

    public void allowVideo(String videoId)
    {
        Video video = videoLibrary.getVideo(videoId);
        if (flags.containsKey(video))
        {
            flags.remove(video);
            System.out.println("Successfully removed flag from video: " + video.getTitle());
        }
        else if (video == null)
        {
            System.out.println("Cannot remove flag from video: Video does not exist");
        }
        else
        {
            System.out.println("Cannot remove flag from video: Video is not flagged");
        }
    }

    public void playPlaylist(String playlistID)
    {
        VideoPlaylist videoPlaylist = playlists.get(playlistID);
        if (videoPlaylist == null)
        {
            System.out.println("Playlist does not exist!");
            return;
        }
        for (Video video : videoPlaylist.getVideos())
        {
            playVideo(video.getVideoId());
        }
    }

    public String videoDetails(Video video)
    {
        return (
                video.getTitle() + " "
                        + "(" + video.getVideoId() + ") "
                        + video.getTags().toString().replaceAll(",", "")
                        + (flags.containsKey(video) ? " - FLAGGED (reason: " + flags.get(video) + ")" : "")
        );
    }
}