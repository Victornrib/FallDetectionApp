from collections import OrderedDict


class UsersService:

    @staticmethod
    def get_users_info_dict(db):
        users = db.child("Users").get()
        users_dict = OrderedDict(users.val()).values()

        ages = []
        em_contacts_count = []
        genders = []

        for user in users_dict:
            ages.append(user.get("age"))
            genders.append(user.get("gender"))
            em_contacts_count.append(len(user.get("emContacts")) if user.get("emContacts") else 0)
            #diseases = []
            #recordedFalls = []

        users_info_dict = {
            "ages": ages,
            "genders": genders,
            "em_contacts_count": em_contacts_count,
        }
        return users_info_dict