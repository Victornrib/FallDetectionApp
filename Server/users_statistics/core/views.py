from django.shortcuts import render
from core.services.users_service import UsersService
import json

import pyrebase

# Firebase configuration
firebaseConfig = {
  "apiKey": "AIzaSyDcatbVwGG_vP0UpoVr4YnpJkJpa6TnrCk",
  "authDomain": "fall-detection-83eed.firebaseapp.com",
  "databaseURL": "https://fall-detection-83eed-default-rtdb.europe-west1.firebasedatabase.app",
  "projectId": "fall-detection-83eed",
  "storageBucket": "fall-detection-83eed.appspot.com",
  "messagingSenderId": "867903176192",
  "appId": "1:867903176192:web:ec7ef5f1d691472c77a2e5",
  "measurementId": "G-VRJFNYDW8E"
}

# Initialize Firebase
firebase = pyrebase.initialize_app(firebaseConfig)
db = firebase.database()

# Create your views here.
def index(request):
    if request.method == "POST":
        # user = request.POST['user123']
        # result = db.child("user").push({"field": user})
        pass
    else:
        users_info_dict = UsersService.get_users_info_dict(db)
        return render(request, 'index.html', context={
            "users": users_info_dict,
        })
    