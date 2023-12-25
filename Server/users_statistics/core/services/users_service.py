from collections import OrderedDict


class UsersService:

    @classmethod
    def get_users_info_dict(cls, db):
        users = db.child("Users").get()
        users_dict = OrderedDict(users.val()).values()

        falls_per_age_dict = cls.get_users_falls_per_age(users_dict)
        falls_per_sex_dict = cls.get_users_falls_per_sex(users_dict)
        falls_per_movement_disorder_list = cls.get_users_falls_per_movement_disorder(users_dict)

        users_info_dict = {
            "falls_per_age": falls_per_age_dict,
            "falls_per_sex": falls_per_sex_dict,
            "falls_per_movement_disorder": falls_per_movement_disorder_list,
        }
        return users_info_dict
    

    @classmethod
    def get_users_falls_per_age(cls, users_dict):
        sorted_users_dict = sorted(users_dict, key=lambda user: user['age'])

        falls_counter = {}
        
        for user in sorted_users_dict:
            age = user.get("age")

            if user.get("recordedFalls"):
                falls = len(user.get("recordedFalls"))

                if str(age) in falls_counter:
                    falls_counter[str(age)] += falls
                else:
                    falls_counter[str(age)] = falls

        falls_per_age = {'falls': list(falls_counter.values()), 'ages': list(falls_counter.keys())}
        return falls_per_age


    @classmethod
    def get_users_falls_per_sex(cls, users_dict):
        male_count = 0
        female_count = 0
        not_specified_count = 0

        for user in users_dict:
            if user.get("recordedFalls"):
                falls = len(user.get("recordedFalls"))
                sex = user.get("sex")

                if sex == "Male":
                    male_count += falls
                elif sex == "Female":
                    female_count += falls
                else:
                    not_specified_count += falls
                
        falls_counter = [male_count, female_count, not_specified_count]
        listed_sexes = ["Male", "Female", "Not specified"]

        falls_per_sex = {"falls": falls_counter, "sexes": listed_sexes}
        return falls_per_sex
    

    @classmethod
    def get_users_falls_per_movement_disorder(cls, users_dict):
        falls_per_movement_disorder = []

        for user in users_dict:
            if user.get("recordedFalls"):
                falls = len(user.get("recordedFalls"))

                if user.get("movementDisorders"):
                    user_movement_disorders = user.get("movementDisorders")

                    for disorder in user_movement_disorders:
                        found = False

                        for fall_counter in falls_per_movement_disorder:
                            if disorder == fall_counter["name"]:
                                fall_counter["value"] += falls
                                found = True
                                break

                        if not found:
                            falls_per_movement_disorder.append({"name": disorder, "value": falls})

        return falls_per_movement_disorder