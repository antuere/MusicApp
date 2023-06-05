# Scheduled player
App for launching music on a schedule by server. This is tested app.

At startup, the application loads the schedule from a json file on the server, downloads the necessary files, checks their integrity by md5 sum and displays the playlist schedule on the screen. If an attempt to download one of the tracks fails, a warning icon is displayed next to the corresponding playlist on the playlists screen. The music is played according to the schedule and the specified proportions, which can be changed. For example, you can make a ratio of 2 to 1, then 2 tracks from the first playlist will have one from the second. Also, musical compositions change by cross-fading

[JSON file example](https://drive.google.com/file/d/1mRG6Dlo1oOlgvebtsUHXzpfPY4LxtmBl/view?usp=drive_link).

For tests server develop with Flask and static files.
## Screenshots
<div id="screenshots">
 <img src="https://github.com/antuere/Scheduled-player/assets/98087954/b3028971-9472-44e6-9ed7-b377c4efadb4" width="270" height="600">
 <img src="https://github.com/antuere/Scheduled-player/assets/98087954/ecdbd3d0-da61-4ff9-9a42-8e83e2303b92" width="270" height="600">
</div>

## Libraries and development features
| XML layouts     | Nested RecycleViews |   
| :------: | :------: |
| ExoPlayer      | Constraint layout 
| Room   | Clean architecture with 3 modules |
|  Dagger-hilt     |  Server with Flask on Python   
| Offline mode   | Retrofit         


