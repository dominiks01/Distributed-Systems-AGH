from enum import Enum

class ExtendedEnum(Enum):

    @classmethod
    def list(cls):
        return list(map(lambda c: c.value, cls))

class Voivodeships(ExtendedEnum): 
    DOLNOŚLĄSKIE = "dolnośląskie"
    KUJAWSKO_POMORSKIE = "kujawsko-pomorskie"
    LUBELSKIE="lubelskie"
    LUBUSKIE="lubuskie"
    ŁÓDZKIE="łódzkie"
    MAŁOPOLSKIE="małopolskie"
    MAZOWIECKIE="mazowieckie"
    OPOLSKIE="opolskie"
    PODKARPACKIE="podkarpackie"
    PODLASKIE="podlaskie"
    POMORSKIE="pomorskie"
    ŚLĄSKIE="śląskie"
    ŚWIĘTOKRZYSKIE="świętokrzyskie"
    WARMIŃSKO_MAZURSKIE="warmińsko-mazurskie"
    WIELKOPOLSKIE="wielkopolskie"
    ZACHODNIOPOMORSKIE="zachodniopomorskie"

