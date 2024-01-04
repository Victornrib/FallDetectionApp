from django.shortcuts import render
from core.services.users_service import UsersService
from django.shortcuts import redirect
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


def login_page(request):
    #TODO Lembrar de gerar IDs unicos para os usuarios

    if request.method == "POST":
        email = request.POST["email"]
        password = request.POST["password"]
        user = UsersService.login(email, password, db)
        if user != None:
            return redirect(user_page, user_id=user.get("userID"))
        else:
            return render(request, "login_page.html", {'errors': True})
    else:
        return render(request, 'login_page.html')


def user_page(request, user_id):
    user = UsersService.get_user_by_id(db, user_id)
    return render(request, "user_page.html", {
        'user': user
    })


def admin_page(request):
    if request.method == "POST":
        # user = request.POST['user123']
        # result = db.child("user").push({"field": user})
        pass
    else:
        users_info_dict = UsersService.get_users_info_dict(db)
        return render(request, 'admin_page.html', context={
            "users": users_info_dict,
        })
    