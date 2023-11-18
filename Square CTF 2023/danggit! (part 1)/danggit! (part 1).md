# danggit! (part 1) (150 pts) - misc

## Description

The developer who set up this challenge has left the company. Unfortunately, they forgot to share their code before leaving, and we have no idea how it works. All we could find was an email containing their commit history ¯\\_(ツ)_/¯

```nc 184.72.87.9 8009```

## Attachments

[commit-history.txt](commit-history.txt)
[repo-folder.zip (challenge solve files)](repo-folder.zip)

## Solution

This challenge was truly hellish. Not only did it expect us to recreate the program, it needed us to do so with the perfect amount of whitespace, newlines, variable naming, and function usage to recreate the program's password, which was the tree hash of the commit history.

It started off pretty simple. Set the local git name and email to match the ones in the log.

```sh
git config user.name "CTF Organizer"
git config user.email "ctforganizer@noreply.squarectf.com"
```

The commands I'd be using over and over in this chall were the following:

```sh
export GIT_AUTHOR_DATE="<commit time string>"
export GIT_COMMITER_DATE="<commit time string>"
git commit -m '<commit message>' --time "<commit time string>"
```

First commit. Easy.

```sh
commit 7cababac3f3499755d64cf216e237b6969dcedde
Author: CTF Organizer <ctforganizer@noreply.squarectf.com>
Date:   Wed Nov 1 09:36:47 2023 -0700

    Add hello example from https://go.dev/tour/welcome/1
```

Initialize the git repo (`git init`) and paste the contents of the link into `hello.go`.

The second commit isn't bad either.

```sh
commit 7f29b946f68f2cef5bbaab03cbe8049c64dbc29c
Author: CTF Organizer <ctforganizer@noreply.squarectf.com>
Date:   Wed Nov 1 09:47:39 2023 -0700

    I forgot how to run go code... go mod init ctf
```

Just needed to run `go mod init ctf` with the correct latest version available on November 1st, 1.21.3, and commit, remembering to set the commit time string correctly.

Now, the third commit got a bit more tricky.

```sh
commit 391229073873729b50318bf385494d0b195a9cb1
Author: CTF Organizer <ctforganizer@noreply.squarectf.com>
Date:   Wed Nov 1 10:09:11 2023 -0700

    Rename hello.go to main.go and update the prompt. Almost forgot about go fmt .
```

Renaming is easy. However, what did they mean by "update the prompt"? Well, remember how there's a nc service running for this chall? I had to connect to that to get the initial prompt.

```sh
~ nc 184.72.87.9 8009
Password: 
```

The prompt was `Password: ` with a space. Updated, ran `go fmt .`, and it was good to commit.

The fourth commit asked me to implement the following:

```sh
commit 70d45a6d98f5642cdc5c79921815a760d290abe2
Author: CTF Organizer <ctforganizer@noreply.squarectf.com>
Date:   Wed Nov 1 10:26:51 2023 -0700

    So many ways to read from stdin in go... create a bufio scanner and just call scanner.Scan() I guess? Also, simplify the imports since there are 3 now.
```

So I did, and the fact that they added a newline between the changes for every instruction tripped me up for a bit before figuring it out eventually.

Now, the fifth commit was where I went nearly insane.

```sh
commit 27eeebb5355d8430b5c72757580c7875eba3a6b1
Author: CTF Organizer <ctforganizer@noreply.squarectf.com>
Date:   Wed Nov 1 10:43:07 2023 -0700

    Read the flag file and print it to stdout as a string. Who needs error handling, it's just a ctf!
```

The thing is, go has so many ways to do this, even without considering the naming of both 1) the flag file itself; and 2) the variable name to store the flag file in. This took roughly 4 hours to figure out, and we came close to just giving up several times.

Eventually though, we got it.

The rest of the commits were easy by comparison, and we eventually were able to recover the program password by running `git log --pretty=raw`, which showed the tree hash that we needed to use as the password.

Entering the password into the service, we have the flag given to us `flag{aH_PUsH_1T_gIt_pU$H_1t_0Oh_8aBY_8abY_bA8y_B@by_VpVd6jUm}`

This was by far the most guessy challenge of the CTF.

P.S. the repo folder has been attached incase you would like to take a look. however, 9/10 doctors do not recommend doing so for your own sanity, and the 10th is dead.