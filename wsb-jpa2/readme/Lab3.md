Laboratorium III - JPQL

Uwaga! Do wykonania zadan konieczne jest zaimplementowanie architektury warstwowej i testow z Laboratorium II !

Uzupelnij plik data.sql o dane niezbedne do realizacji nastepujacych zapytan:
1. Znajdz pacjentow po nazwisku
2. Znajdz wszystkie wizyty pacjenta po jego ID
3. znajdz pacjentow ktorzy mieli wiecej niz X wizyt (X jest parametrem wejsciowym)
4. Znajdz pacjentow po dodanym przez Ciebie polu - nie wyszukuj wprost po wartosci, uzyj zapytania typu wieksze/mniejsze/pozniej/wczesniej/zawiera, w zaleznosci od wybranego typu zmiennej.

Napisz testy do zapytan w nastepujacej formie:
1. do zapytania nr 1  - test DAO
2. do zapytania nr 2 - test serwisu
3. do zapytania nr 3 - test DAO
4. do zapytania nr 4 - test DAO

W PatientEntity, nad relacja do VisitEntity dodaj adnotacje

@Fetch(FetchMode.SELECT)

a fetchType zmien na EAGER
Uruchom test w ktorym pobierany jest Patient z wieloma wizytami. W logach zaobserwuj, jak wyglada pobieranie dodatkowych encji (ile i jakie sqle).
Nastepnie zmien adnotacje na

@Fetch(FetchMode.JOIN)

i powtorz test i obserwacje. Wnioski zapisz na dole tego pliku i skomituj.

Do wybranej encji dodaj wersjonowanie, oraz napisz test (w DAO) sprawdzajacy rownolegla modyfikacje (OptimisticLock)

----------

Laboratorium 3 - Wnioski dotyczące @Fetch

1. @Fetch(FetchMode.SELECT) z fetch = FetchType.EAGER:
- Pobieranie pacjenta z dwiema wizytami generuje trzy zapytania SQL:
    - Jedno dla PatientEntity: SELECT * FROM patient WHERE id = ?
    - Dwa dla VisitEntity (po jednym dla każdej wizyty): SELECT * FROM visit WHERE patient_id = ?
- Łącznie: 3 zapytania.
- Wady: Większa liczba zapytań może obniżać wydajność przy dużej liczbie wizyt, szczególnie w przypadku problemu N+1.

2. @Fetch(FetchMode.JOIN) z fetch = FetchType.EAGER:
- Pobieranie pacjenta z dwiema wizytami generuje jedno zapytanie SQL z LEFT JOIN:
    - SELECT p.*, v.* FROM patient p LEFT JOIN visit v ON p.id = v.patient_id WHERE p.id = ?
- Łącznie: 1 zapytanie.
- Zalety: Mniejsza liczba zapytań poprawia wydajność, szczególnie dla dużych relacji @OneToMany.

Wnioski:
- FetchMode.JOIN jest bardziej efektywny, ponieważ redukuje liczbę zapytań SQL, co jest kluczowe dla aplikacji z intensywnym dostępem do danych.
- FetchMode.SELECT może być użyteczny w scenariuszach, gdzie wizyty nie są zawsze potrzebne, ale wymaga ostrożnego zarządzania, aby uniknąć problemu N+1.
