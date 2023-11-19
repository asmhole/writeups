# orakl password checker (100 pts) - web

## Description

I was reading up on password hashes for my new startup, orakl. apparently they're supposed to take a really long time?

## Attachments

[orakl-password-checker](orakl-password-checker)

## Solution

We can decompile the following:

```c
undefined8 main(undefined4 param_1)

{
  int iVar1;
  char local_58 [72];
  ...
  puts(
      "Time for a classic! i\'m not going to GIVE you the flag, instead you give me the flag and I\' ll tell you if its right or wrong!"
      );
  fgets(local_58,0x40,stdin);
  iVar1 = secret_proprietary_super_advanced_password_checker(local_58,param_1);
  if (iVar1 == 0) {
    puts("Hey you got it! nice work!");
  }
  else {
    puts("wrong password! cya");
  }
  ...
  return 0;
}


```
The exact details of `secret_proprietary_super_advanced_password_checker` aren't actually too relevant; by simply trying out a few password guesses and inspecting the response, the premise of the challenge becomes clear. Start with the flag prefix, and for the next additional wrong character in the password, the server responds after N seconds where N is the absolute difference that character's ASCII code and the correct character's ASCII code. The exception to that was when you correctly guessed the next password character, to which the server would indefinitely hang.

In essence, as per the description, we have to conduct a simple timing attack.

By asynchronously guessing the entire character set while logging the responses as they are returned, you could induce the flag character by character. Due to the large degree of variance in response times, which sometimes took up to 45 seconds per character, this couldn't be automated and slowly led to me LOSING MY MARBLES!!!!

Anyways the flag is `flag{i_wouldve_used_argon2_but_i_didnt_want_to_kill_our_infra}`