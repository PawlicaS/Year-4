
from tinydb import TinyDB, Query, where

# fp = open('db.json', 'x')
# fp.close()
db = TinyDB('db.json')
user = Query()

db.insert({'int': 1, 'char': 'a'})
db.insert({'int': 1, 'char': 'b'})
char = 'asdfb'
print(db.search(user.char == 'a'))
print(db.get(user.char == 'v'))
result = db.get(user.char == 'b')
print(result.get('int'))
if not db.search(user.char == char):
    print('asdf')
