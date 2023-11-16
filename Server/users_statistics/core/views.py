from django.shortcuts import render
from collections import OrderedDict
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
        pass
    else:
        users_data = db.child("Users").get()
        users_dict = OrderedDict(users_data.val())
        return render(request, 'index.html', context={"users": users_dict.values()})