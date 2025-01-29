An app made for patient management built as offline first strategy and sync data to server when network is available.

Home Screen consisting of two buttons for new patient to register or existing/returning patients to get their details.

Clicking on Register takes us to patientregistrationscreen where user can enter details and on successful registration user will be redirected to visit details page.

Clicking on Existing user takes us to get details screen where user need to enter their unique id, if unique id exists in db/server user will be automatically redirected to visit details page.

User need to enter all the visit details, once all fields are entered he can save visit details by clicking on the save details button below, which also syncs data to server if network is available,
if network is not available data will be enqueued and synced once the network is available.

On successful saving of visit details user will be redirected to prescription summary screen where user can see all the details of his visit and he can mark his visit as completed by clicking the 
button below.

Mock buttons are provided for printing and sharing details.


Tech Stack:
Jetpack compose,
Room,
WorkManager,
Coroutines,
Flows,
MVVM Architecture,
Hilt and Version Catalogs for easier understanding of dependencies
