from django.test import TestCase
from collections import OrderedDict
from core.services.users_service import UsersService
import pyrebase

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


class TestUsersService(TestCase):
    
    def setUp(self):
        self.users_dict = [{'age': 23, 'alertMode': 'SMS', 'birthDate': 'April 21, 2000', 'emContacts': [{'emContactID': 223, 'email': 'victornrib@gmail.com', 'name': 'Victor', 'telephone': '+5521994193386'}], 'email': 'a@b.c', 'movementDisorders': ['Ataxia', 'Cervical dystonia'], 'name': 'Victor', 'password': '1', 'recordedFalls': [{'fallDateTime': {'dayOfMonth': 5, 'dayOfWeek': 'Sunday', 'dayOfYear': 309, 'fallDateTimeString': 'November 5, 2023 - 23:30:57', 'hour': 23, 'minute': 30, 'month': 'November', 'monthValue': 11, 'nano': 257000000, 'second': 57, 'year': 2023}, 'fallId': 54050, 'latLng': {'latitude': -22.9657332, 'longitude': -43.2029417}}, {'fallDateTime': {'dayOfMonth': 15, 'dayOfWeek': 'Wednesday', 'dayOfYear': 319, 'fallDateTimeString': 'November 15, 2023 - 17:24:26', 'hour': 17, 'minute': 24, 'month': 'November', 'monthValue': 11, 'nano': 271000000, 'second': 26, 'year': 2023}, 'fallId': 53798, 'latLng': {'latitude': -22.9656991, 'longitude': -43.2029065}}, {'fallDateTime': {'dayOfMonth': 15, 'dayOfWeek': 'Wednesday', 'dayOfYear': 319, 'fallDateTimeString': 'November 15, 2023 - 17:24:59', 'hour': 17, 'minute': 24, 'month': 'November', 'monthValue': 11, 'nano': 952000000, 'second': 59, 'year': 2023}, 'fallId': 59524, 'latLng': {'latitude': -22.96571646, 'longitude': -43.2030636}}, {'fallDateTime': {'dayOfMonth': 15, 'dayOfWeek': 'Friday', 'dayOfYear': 349, 'fallDateTimeString': 'December 15, 2023 - 3:17:51', 'hour': 3, 'minute': 17, 'month': 'December', 'monthValue': 12, 'nano': 500000000, 'second': 51, 'year': 2023}, 'fallId': 23607, 'latLng': {'latitude': -22.9657384, 'longitude': -43.2029265}}], 'sex': 'Male', 'telephone': '+5521994193386', 'userID': 690},
                           {'age': 50, 'alertMode': 'SMS', 'birthDate': 'August 23, 1973', 'email': 'a@j.k', 'movementDisorders': ['Functional movement disorder', 'Huntington disease'], 'name': 'HJS', 'password': '123', 'recordedFalls': [{'fallDateTime': {'dayOfMonth': 25, 'dayOfWeek': 'Monday', 'dayOfYear': 359, 'fallDateTimeString': 'December 25, 2023 - 3:36:47', 'hour': 3, 'minute': 36, 'month': 'December', 'monthValue': 12, 'nano': 183000000, 'second': 47, 'year': 2023}, 'fallId': 8902, 'latLng': {'latitude': -22.9657525, 'longitude': -43.2029742}}, {'fallDateTime': {'dayOfMonth': 25, 'dayOfWeek': 'Monday', 'dayOfYear': 359, 'fallDateTimeString': 'December 25, 2023 - 3:37:25', 'hour': 3, 'minute': 37, 'month': 'December', 'monthValue': 12, 'nano': 616000000, 'second': 25, 'year': 2023}, 'fallId': 76670, 'latLng': {'latitude': -22.9657525, 'longitude': -43.2029742}}], 'sex': 'Male', 'telephone': '12345678', 'userID': 204},
                           {'age': 16, 'alertMode': 'SMS', 'birthDate': 'December 25, 2007', 'email': 's@r.j', 'movementDisorders': ['Restless legs syndrome', 'Tremor'], 'name': 'JUJ', 'password': '123', 'recordedFalls': [{'fallDateTime': {'dayOfMonth': 25, 'dayOfWeek': 'Monday', 'dayOfYear': 359, 'fallDateTimeString': 'December 25, 2023 - 3:41:18', 'hour': 3, 'minute': 41, 'month': 'December', 'monthValue': 12, 'nano': 841000000, 'second': 18, 'year': 2023}, 'fallId': 18866, 'latLng': {'latitude': -22.9657525, 'longitude': -43.2029742}}, {'fallDateTime': {'dayOfMonth': 25, 'dayOfWeek': 'Monday', 'dayOfYear': 359, 'fallDateTimeString': 'December 25, 2023 - 3:41:26', 'hour': 3, 'minute': 41, 'month': 'December', 'monthValue': 12, 'nano': 336000000, 'second': 26, 'year': 2023}, 'fallId': 54559, 'latLng': {'latitude': -22.9657525, 'longitude': -43.2029742}}, {'fallDateTime': {'dayOfMonth': 25, 'dayOfWeek': 'Monday', 'dayOfYear': 359, 'fallDateTimeString': 'December 25, 2023 - 3:41:45', 'hour': 3, 'minute': 41, 'month': 'December', 'monthValue': 12, 'nano': 298000000, 'second': 45, 'year': 2023}, 'fallId': 93154, 'latLng': {'latitude': -22.9657525, 'longitude': -43.2029742}}], 'sex': 'Female', 'telephone': '24688642', 'userID': 861},
                           {'age': 36, 'alertMode': 'SMS', 'birthDate': 'July 20, 1987', 'email': 't@b.v', 'movementDisorders': ['Ataxia', 'Chorea', 'Parkinson disease', 'Parkinsonism'], 'name': 'TEST', 'password': '123', 'recordedFalls': [{'fallDateTime': {'dayOfMonth': 25, 'dayOfWeek': 'Monday', 'dayOfYear': 359, 'fallDateTimeString': 'December 25, 2023 - 3:20:48', 'hour': 3, 'minute': 20, 'month': 'December', 'monthValue': 12, 'nano': 965000000, 'second': 48, 'year': 2023}, 'fallId': 7955, 'latLng': {'latitude': -22.9657525, 'longitude': -43.2029742}}, {'fallDateTime': {'dayOfMonth': 25, 'dayOfWeek': 'Monday', 'dayOfYear': 359, 'fallDateTimeString': 'December 25, 2023 - 3:20:59', 'hour': 3, 'minute': 20, 'month': 'December', 'monthValue': 12, 'nano': 875000000, 'second': 59, 'year': 2023}, 'fallId': 53554, 'latLng': {'latitude': -22.9657525, 'longitude': -43.2029742}}, {'fallDateTime': {'dayOfMonth': 25, 'dayOfWeek': 'Monday', 'dayOfYear': 359, 'fallDateTimeString': 'December 25, 2023 - 3:21:20', 'hour': 3, 'minute': 21, 'month': 'December', 'monthValue': 12, 'nano': 927000000, 'second': 20, 'year': 2023}, 'fallId': 75326, 'latLng': {'latitude': -22.9657525, 'longitude': -43.2029742}}], 'sex': 'Male', 'telephone': '12345678', 'userID': 392}]

    def test_users_firebase_connection(self):
        firebase = pyrebase.initialize_app(firebaseConfig)
        db = firebase.database()
        users = db.child("Users").get()
        self.assertNotEqual(None, users)
    
    def test_get_users_falls_per_age(self):
        expected = {'falls': [3, 4, 3, 2], 'ages': ['16', '23', '36', '50']}
        observed = UsersService.get_users_falls_per_age(self.users_dict)
        self.assertEqual(expected, observed)

    def test_get_users_falls_per_sex(self):
        expected = {'falls': [9, 3, 0], 'sexes': ['Male', 'Female', 'Not specified']}
        observed = UsersService.get_users_falls_per_sex(self.users_dict)
        self.assertEqual(expected, observed)

    def test_get_users_falls_per_movement_disorder(self):
        expected = [{'name': 'Ataxia', 'value': 7}, {'name': 'Cervical dystonia', 'value': 4}, {'name': 'Functional movement disorder', 'value': 2}, {'name': 'Huntington disease', 'value': 2}, {'name': 'Restless legs syndrome', 'value': 3}, {'name': 'Tremor', 'value': 3}, {'name': 'Chorea', 'value': 3}, {'name': 'Parkinson disease', 'value': 3}, {'name': 'Parkinsonism', 'value': 3}]
        observed = UsersService.get_users_falls_per_movement_disorder(self.users_dict)
        self.assertEqual(expected, observed)
