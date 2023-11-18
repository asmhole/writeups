# same (50 pts) - forensics

## Description

https://youtu.be/vFyG4m-TSvY

## Attachments

[same.mp4 (downloaded video)](same.mp4)

## Solution

Ever listened to any sort of data transmitted over audio before? It's a pretty annoying sound, and almost immediately given away in this challenge.

The challenge title is `same`, which stands for Specific Area Message Encoding, a technology used to broadcast emergency messages.

Luckily, we can use `minimodem` to decode this for us, using its builtin `SAME` bps setting.

```sh
minimodem -r -f same.flac same
```

(Note that the audio had to be extracted from the video file to be read)

This produces the output

```
### CARRIER 521 @ 2083.3 Hz ###
Hey. Want to hear the most annoying sound in the world?
flag{this.is.the.dumbest.challenge_e5ee7b43}
.
<truncated>

### NOCARRIER ndata=164 confidence=30.375 ampl=0.049 bps=521.12 (0.1% fast) ###
```

giving us the flag, `flag{this.is.the.dumbest.challenge_e5ee7b43}`