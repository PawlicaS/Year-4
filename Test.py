import random

i = 1
count = 0

while i < 1000:
    j = random.randint(1, 100)
    if j >= 80:
        count += 1
    i += 1

print(count)