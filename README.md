#Project moved to https://gitlab.com/victorpaul/koshots-server

Here is tiny spring server. You can upload image there, get link on it and share it with someone you want.

I use it for my another application https://github.com/victorpaul/koshot-screen-captor 

###To publish shot
POST `/api/shot`
Required Header Authorization

OK 200

    {
        "shotName": "shotname.jpg",
        "publicUrl": "http://....."
    }
    
UNAUTHORIZED 401 

    {
        "message": "error messsage"
    }

###To get shot
GET `/api/shot/{shot_name}`
Required Header Authorization

OK 200

    returns image body

UNAUTHORIZED 401 

    {
        "message": "error messsage"
    }


###To get public url for shot 
GET `/api/shot/{shot_name}/publicurl`
Required Header Authorization

OK 200

    {
        "shotName": "shotname.jpg",
        "publicUrl": "http://....."
    }
    
UNAUTHORIZED 401,404 

    {
        "message": "error messsage"
    }

###To get all my shots
GET `/api/shots`
Required Header Authorization

OK 200

    {
        "shots": [
            {
                    "shotName": "shotname.jpg",
                    "publicUrl": "http://....."
            },
            {
                    "shotName": "shotname.jpg",
                    "publicUrl": "http://....."
            },
            {
                    "shotName": "shotname.jpg",
                    "publicUrl": "http://....."
            }
        ]
    }
    
UNAUTHORIZED 401 

    {
        "message": "error messsage"
    }

###To delete shot
DELETE `/api/shot/{shot_name}`
Required Header Authorization

OK 200

    {
        "message": "Why did ya do this...."
    }
    
UNAUTHORIZED 401,404

    {
        "message": "error messsage"
    }
    
    
##Response 400 could be like this for all requests

    {
        "timestamp": "2018-06-05T07:07:51.584+0000",
        "status": 400,
        "error": "Bad Request",
        "message": "Missing request header 'Authorization' for method parameter of type String",
        "path": "/api/shot/name1"
    }
