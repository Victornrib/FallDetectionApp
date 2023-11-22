from collections import OrderedDict


class UsersService:

    @classmethod
    def get_users_info_dict(cls, db):
        users = db.child("Users").get()
        users_dict = OrderedDict(users.val()).values()

        em_contacts_count = []

        # ages_dict = cls.get_users_ages_dict(users_dict)
        falls_per_age_dict = cls.get_users_falls_per_age(users_dict)
        genders_dict = cls.get_users_genders_dict(users_dict)

        for user in users_dict:
            em_contacts_count.append(len(user.get("emContacts")) if user.get("emContacts") else 0)
            #diseases = []
            #recordedFalls = []

        users_info_dict = {
            "falls_per_age": falls_per_age_dict,
            "genders": genders_dict,
            "em_contacts_count": em_contacts_count,
        }
        return users_info_dict
    

    @classmethod
    def get_users_falls_per_age(cls, users_dict):
        falls_per_age = []

        for user in users_dict:
            age = user.get("age")
            falls = len(user.get("recordedFalls"))

            for fall_counter in falls_per_age:
                #Caso em que há essa idade presente -> incrementa contador de quedas dessa idade
                if str(age) == fall_counter['name']:
                    fall_counter['value'] += falls
            
            #Caso em não há essa idade presente -> cria contador de quedas para essa idade e a adiciona na lista de contadores
            fall_counter = {'value': falls, 'name': str(age)}
            falls_per_age.append(fall_counter)

        return falls_per_age


    # @classmethod
    # def get_users_ages_dict(cls, users_dict):
    #     ages_dict = {'ages':[], 'ages_count':[]}

    #     for user in users_dict:
    #         age = user.get("age")

    #         if str(age) in ages_dict['ages']:
    #             ages_dict[str(age)] += 1

    #         else:
    #             ages_dict[str(age)] = 1

    #     return ages_dict
    

    @classmethod
    def get_users_genders_dict(cls, users_dict):
        male_count = 0
        female_count = 0
        for user in users_dict:
            gender = user.get("gender")
            if gender == "Male":
                male_count += 1
            else:
                female_count += 1
        return {"Male": male_count, "Female": female_count}